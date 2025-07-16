package com.fse.FSE_Backend_Proj.engine.dsl;

import com.fse.FSE_Backend_Proj.engine.StrategyExecutor;
import com.fse.FSE_Backend_Proj.indicator.IndicatorService;
import com.fse.FSE_Backend_Proj.model.*;
import com.fse.FSE_Backend_Proj.parser.ScriptParserService;
import com.fse.FSE_Backend_Proj.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

@Component("dslStrategyExecutor")
public class DSLStrategyExecutor implements StrategyExecutor {

    @Autowired
    private ScriptParserService parser;

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public BacktestResult execute(HashMap<String, List<Candle>> candleMap, Strategy strategy) {
        List<CompositeRule> rules = parser.parse(strategy.getScript());

        Map<String, Map<Integer, Map<String, Double>>> indicatorCachePerSymbol = new ConcurrentHashMap<>();
        Map<String, Boolean> holding = new ConcurrentHashMap<>();
        Map<String, Double> buyPrice = new ConcurrentHashMap<>();
        Map<String, Integer> quantity = new ConcurrentHashMap<>();
        List<Trade> trades = Collections.synchronizedList(new ArrayList<>());


        double initialCapital = 500000;
        if(strategy.getInitialCapital() != 0){
            initialCapital = strategy.getInitialCapital();
        }
        final double[] capital = {initialCapital};

        int totalDays = candleMap.values().stream().findFirst().map(List::size).orElse(0);

        for (int day = 0; day < totalDays; day++) {
            List<String> buySymbolsToday = Collections.synchronizedList(new ArrayList<>());
            Map<String, Candle> candleOfDay = new ConcurrentHashMap<>();

            ExecutorService executor = Executors.newFixedThreadPool(candleMap.size());
            List<Future<?>> futures = new ArrayList<>();

            for (Map.Entry<String, List<Candle>> entry : candleMap.entrySet()) {
                String symbol = entry.getKey();
                List<Candle> candles = entry.getValue();
                if (day >= candles.size()) continue;
                Candle candle = candles.get(day);
                candleOfDay.put(symbol, candle);

                final int finalDay = day;

                futures.add(executor.submit(() -> {
                    boolean alreadyBought = false;
                    for (CompositeRule rule : rules) {
                        boolean ruleMatched = false;

                        if (rule.getConditionGroups() != null && !rule.getConditionGroups().isEmpty()) {
                            for (ConditionGroup group : rule.getConditionGroups()) {
                                boolean groupMatched = "AND".equalsIgnoreCase(group.getOperator());

                                for (Condition c : group.getConditions()) {
                                    double leftVal = getValue(candles, c.getLeft(), c.getLeftArg(), finalDay, getCache(indicatorCachePerSymbol, symbol));
                                    double rightVal = getValue(candles, c.getRight(), c.getRightArg(), finalDay, getCache(indicatorCachePerSymbol, symbol));
                                    boolean result = evaluate(leftVal, rightVal, c.getOperator());

                                    if ("AND".equalsIgnoreCase(group.getOperator())) {
                                        groupMatched &= result;
                                        if (!groupMatched) break;
                                    } else if ("OR".equalsIgnoreCase(group.getOperator())) {
                                        groupMatched |= result;
                                        if (groupMatched) break;
                                    }
                                }

                                ruleMatched |= groupMatched;
                            }
                        }
                        if (rule.getConditions() != null && !rule.getConditions().isEmpty()) {
                            ruleMatched = true;
                            for (Condition c : rule.getConditions()) {
                                double leftVal = getValue(candles, c.getLeft(), c.getLeftArg(), finalDay, getCache(indicatorCachePerSymbol, symbol));
                                double rightVal = getValue(candles, c.getRight(), c.getRightArg(), finalDay, getCache(indicatorCachePerSymbol, symbol));
                                boolean result = evaluate(leftVal, rightVal, c.getOperator());
                                ruleMatched &= result;
                                if (!ruleMatched) break;
                            }
                        }

                        if (ruleMatched) {
                            synchronized (holding) {
                                if ("BUY".equalsIgnoreCase(rule.getAction()) &&
                                        !holding.getOrDefault(symbol, false) &&
                                        !buySymbolsToday.contains(symbol) &&
                                        !alreadyBought) {
                                    buySymbolsToday.add(symbol);
                                    alreadyBought = true;
                                    break;
                                }

                                if ("SELL".equalsIgnoreCase(rule.getAction()) && holding.getOrDefault(symbol, false)) {
                                    double sell = candle.getClose();
                                    int qty = quantity.get(symbol);
                                    double cost = qty * buyPrice.get(symbol);
                                    double proceeds = qty * sell;
                                    double profit = proceeds - cost;

                                    synchronized (capital) {
                                        double openingBalance = capital[0];
                                        capital[0] += proceeds;

                                        trades.add(Trade.builder()
                                                .date(candle.getDate())
                                                .symbol(symbol)
                                                .action("SELL")
                                                .price(sell)
                                                .quantity(qty)
                                                .totalCostPrice(cost)
                                                .openingBalance(openingBalance)
                                                .closingBalance(capital[0])
                                                .nav(qty * sell)
                                                .realizedProfit(profit)
                                                .build());
                                    }

                                    holding.put(symbol, false);
                                    break;
                                }
                            }
                        }
                    }
                }));
            }

            for (Future<?> f : futures) {
                try {
                    f.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();

            if (!buySymbolsToday.isEmpty()) {
                double totalWeight = 0.0;
                Map<String, Double> weights = new HashMap<>();

                for (String symbol : buySymbolsToday) {
                    Optional<Company> companyOpt = companyRepository.findBySymbol(symbol);
                    if (companyOpt.isPresent()) {
                        Company company = companyOpt.get();
                        BigDecimal totalCapitalBD = company.getTotalCapital();
                        Double riskFactor = company.getRiskFactor() != null ? company.getRiskFactor() : 0.0;
                        double weight = totalCapitalBD != null ? totalCapitalBD.doubleValue() * (1 - riskFactor) : 0.0;
                        weights.put(symbol, weight);
                        totalWeight += weight;
                    }
                }

                for (String symbol : buySymbolsToday) {
                    Candle candle = candleOfDay.get(symbol);
                    double price = candle.getClose();
                    double weight = weights.getOrDefault(symbol, 0.0);
                    double allocatedCapital = (totalWeight > 0) ? capital[0] * (weight / totalWeight) : 0;
                    int qty = (int) (allocatedCapital / price);
                    if (qty <= 0) continue;
                    double cost = qty * price;

                    synchronized (capital) {
                        double openingBalance = capital[0];
                        capital[0] -= cost;

                        trades.add(Trade.builder()
                                .date(candle.getDate())
                                .symbol(symbol)
                                .action("BUY")
                                .price(price)
                                .quantity(qty)
                                .totalCostPrice(cost)
                                .openingBalance(openingBalance)
                                .closingBalance(capital[0])
                                .nav(qty * price)
                                .realizedProfit(0.0)
                                .build());
                    }

                    holding.put(symbol, true);
                    buyPrice.put(symbol, price);
                    quantity.put(symbol, qty);
                }
            }
        }

        for (Map.Entry<String, List<Candle>> entry : candleMap.entrySet()) {
            String symbol = entry.getKey();
            if (holding.getOrDefault(symbol, false)) {
                List<Candle> candles = entry.getValue();
                if (candles.isEmpty()) continue;
                Candle last = candles.get(candles.size() - 1);

                System.out.println("Selling remaining shares of " + symbol + " on final day " + last.getDate());

                double sell = last.getClose();
                int qty = quantity.getOrDefault(symbol, 0);
                double cost = qty * buyPrice.get(symbol);
                double proceeds = qty * sell;
                double profit = proceeds - cost;

                synchronized (capital) {
                    double openingBalance = capital[0];
                    capital[0] += proceeds;
                    trades.add(Trade.builder()
                            .date(last.getDate())
                            .symbol(symbol)
                            .action("SELL")
                            .price(sell)
                            .quantity(qty)
                            .totalCostPrice(cost)
                            .openingBalance(openingBalance)
                            .closingBalance(capital[0])
                            .nav(qty * sell)
                            .realizedProfit(profit)
                            .build());
                }

                holding.put(symbol, false);
            }
        }

        return BacktestResult.builder()
                .initialEquity(initialCapital)
                .finalEquity(capital[0])
                .totalTrades(trades.size())
                .trades(trades)
                .strategy(strategy)
                .build();
    }

    private Map<Integer, Map<String, Double>> getCache(Map<String, Map<Integer, Map<String, Double>>> cacheMap, String symbol) {
        return cacheMap.computeIfAbsent(symbol, k -> new ConcurrentHashMap<>());
    }

    private double getValue(List<Candle> candles, String type, Integer arg, int index, Map<Integer, Map<String, Double>> cache) {
        cache.putIfAbsent(index, new ConcurrentHashMap<>());

        if (!type.equalsIgnoreCase("SMA") && !type.equalsIgnoreCase("RSI") &&
                !type.equalsIgnoreCase("VALUE") && !type.equalsIgnoreCase("CLOSE") &&
                !type.equalsIgnoreCase("PERC_CLOSE") && !type.equalsIgnoreCase("PERC_OPEN")) {
            arg = Integer.parseInt(type);
            type = "VALUE";
        }

        final Integer finalArg = arg;

        return switch (type.toUpperCase()) {
            case "SMA" -> cache.get(index).computeIfAbsent("SMA" + finalArg,
                    k -> indicatorService.calculateSMA(candles, finalArg).getOrDefault(index, 0.0));
            case "RSI" -> cache.get(index).computeIfAbsent("RSI" + finalArg,
                    k -> indicatorService.calculateRSI(candles, finalArg).getOrDefault(index, 0.0));
            case "VALUE" -> arg;
            case "CLOSE" -> candles.get(index).getClose();
            case "PERC_CLOSE" -> {
                if (index == 0) yield 0.0;
                double todayClose = candles.get(index).getClose();
                double prevClose = candles.get(index - 1).getClose();
                yield ((todayClose - prevClose) / prevClose) * 100;
            }
            case "PERC_OPEN" -> {
                if (index == 0) yield 0.0;
                double todayOpen = candles.get(index).getOpen();
                double prevOpen = candles.get(index - 1).getOpen();
                yield ((todayOpen - prevOpen) / prevOpen) * 100;
            }
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    private boolean evaluate(double left, double right, String op) {
        return switch (op) {
            case ">" -> left > right;
            case "<" -> left < right;
            case ">=" -> left >= right;
            case "<=" -> left <= right;
            case "==" -> left == right;
            case "!=" -> left != right;
            default -> false;
        };
    }
}
