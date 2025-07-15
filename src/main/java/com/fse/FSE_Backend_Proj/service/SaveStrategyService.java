package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.BacktestResultDTO;
import com.fse.FSE_Backend_Proj.dto.StrategyRequest;
import com.fse.FSE_Backend_Proj.dto.TradeDTO;
import com.fse.FSE_Backend_Proj.engine.StrategyEngine;
import com.fse.FSE_Backend_Proj.model.BacktestResult;
import com.fse.FSE_Backend_Proj.model.Candle;
import com.fse.FSE_Backend_Proj.model.Strategy;
import com.fse.FSE_Backend_Proj.repository.BacktestResultRepository;
import com.fse.FSE_Backend_Proj.repository.CandleRepository;
import com.fse.FSE_Backend_Proj.repository.StrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaveStrategyService {

    private final StrategyRepository strategyRepository;
    private final StrategyEngine strategyEngine;
    private final CandleRepository candleRepository;
    private final BacktestResultRepository backtestResultRepository;

    public StrategyRequest saveStrategy(StrategyRequest dto) {
        Strategy strategy = Strategy.builder()
                .id(dto.getId())
                .name(dto.getStrategyName())
                .script(dto.getStrategyScript())
                .symbolList(dto.getSymbolList())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .initialCapital(dto.getInitialCapital())
                .symbol(dto.getSymbol())
                .status(dto.getStatus() != null ? dto.getStatus() : "not started")
                .resultJson(dto.getResultJson())
                .build();

        Strategy saved = strategyRepository.save(strategy);
        return mapToDTO(saved);
    }

    public List<StrategyRequest> getAllStrategies() {
        return strategyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public StrategyRequest getStrategyById(Long id) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        return mapToDTO(strategy);
    }

    public void deleteStrategy(Long id) {
        strategyRepository.deleteById(id);
    }

    public StrategyRequest startSimulation(Long id) {
        Strategy strategy = getStrategy(id);
        strategy.setStatus("running");

        List<String> symbolListToUse = new ArrayList<>();
        for (String s : strategy.getSymbolList()) {
            if (s.equals("NIFTY 50")) {
                symbolListToUse.addAll(List.of(/* NIFTY 50 symbols */));
            } else if (s.equals("NIFTY NEXT 50")) {
                symbolListToUse.addAll(List.of(/* NIFTY NEXT 50 symbols */));
            } else {
                symbolListToUse.add(s);
            }
        }

        HashMap<String, List<Candle>> candleMap = new HashMap<>();
        for (String symbol : symbolListToUse) {
            List<Candle> candles = candleRepository
                    .findBySymbolAndDateBetweenOrderByDateAsc(
                            symbol, strategy.getStartDate(), strategy.getEndDate());
            candleMap.put(symbol, candles);
        }

        strategy.setSymbolList(symbolListToUse);
        Strategy updatedStrategy = strategyRepository.save(strategy);

        BacktestResult newResult = strategyEngine.run(updatedStrategy, candleMap);
        newResult.setStrategy(updatedStrategy);
        newResult.getTrades().forEach(t -> t.setStrategy(updatedStrategy));

        backtestResultRepository.findByStrategyId(updatedStrategy.getId()).ifPresentOrElse(existing -> {
            existing.setInitialEquity(newResult.getInitialEquity());
            existing.setFinalEquity(newResult.getFinalEquity());
            existing.setTotalTrades(newResult.getTotalTrades());
            existing.getTrades().clear();
            existing.getTrades().addAll(newResult.getTrades());
            backtestResultRepository.save(existing);
        }, () -> backtestResultRepository.save(newResult));

        try {
            String summary = String.format(
                    "Strategy completed. Initial: %.2f, Final: %.2f, Trades: %d",
                    newResult.getInitialEquity(),
                    newResult.getFinalEquity(),
                    newResult.getTotalTrades()
            );
            updatedStrategy.setResultJson(summary);
        } catch (Exception e) {
            updatedStrategy.setResultJson("Completed (summary unavailable)");
        }

        updatedStrategy.setStatus("completed");
        strategyRepository.save(updatedStrategy);

        return mapToDTO(updatedStrategy);
    }

    public StrategyRequest stopSimulation(Long id) {
        Strategy strategy = getStrategy(id);
        strategy.setStatus("stopped");
        return mapToDTO(strategyRepository.save(strategy));
    }

    public StrategyRequest completeSimulation(Long id, String resultJson) {
        Strategy strategy = getStrategy(id);
        strategy.setStatus("completed");
        strategy.setResultJson(resultJson);
        return mapToDTO(strategyRepository.save(strategy));
    }

    private Strategy getStrategy(Long id) {
        return strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
    }

    private StrategyRequest mapToDTO(Strategy strategy) {
        return StrategyRequest.builder()
                .id(strategy.getId())
                .strategyName(strategy.getName())
                .strategyScript(strategy.getScript())
                .symbolList(strategy.getSymbolList())
                .startDate(strategy.getStartDate())
                .endDate(strategy.getEndDate())
                .initialCapital(strategy.getInitialCapital())
                .symbol(strategy.getSymbol())
                .status(strategy.getStatus())
                .resultJson(strategy.getResultJson())
                .build();
    }

    public BacktestResultDTO getBacktestResultForStrategy(Long strategyId) {
        BacktestResult result = backtestResultRepository
                .findByStrategyId(strategyId)
                .orElseThrow(() -> new RuntimeException("No result found for strategy ID: " + strategyId));

        return BacktestResultDTO.builder()
                .initialEquity(result.getInitialEquity())
                .finalEquity(result.getFinalEquity())
                .totalTrades(result.getTotalTrades())
                .trades(result.getTrades().stream().map(t -> TradeDTO.builder()
                        .date(t.getDate().toString())
                        .symbol(t.getSymbol())
                        .action(t.getAction())
                        .price(t.getPrice())
                        .quantity(t.getQuantity())
                        .totalCostPrice(t.getTotalCostPrice())
                        .openingBalance(t.getOpeningBalance())
                        .closingBalance(t.getClosingBalance())
                        .nav(t.getNav())
                        .realizedProfit(t.getRealizedProfit())
                        .build()
                ).collect(Collectors.toList()))
                .build();
    }

    public BacktestResult getBacktestResultByStrategyId(Long strategyId) {
        return backtestResultRepository.findByStrategyId(strategyId)
                .orElseThrow(() -> new RuntimeException("No result found for strategy ID: " + strategyId));
    }
}