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
//    private final StrategyRepository strategyRepository;
    private final StrategyEngine strategyEngine;
    private final CandleRepository candleRepository;
    private final BacktestResultRepository backtestResultRepository;



    // Create or update
    public StrategyRequest saveStrategy(StrategyRequest dto) {
        Strategy strategy = Strategy.builder()
                .id(dto.getId()) // needed for update operations
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

    // Get all
    public List<StrategyRequest> getAllStrategies() {
        return strategyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get by id
    public StrategyRequest getStrategyById(Long id) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        return mapToDTO(strategy);
    }

    // Delete
    public void deleteStrategy(Long id) {
        strategyRepository.deleteById(id);
    }

    // Start simulation
//    public StrategyRequest startSimulation(Long id) {
//        Strategy strategy = getStrategy(id);
//        strategy.setStatus("running");
//        return mapToDTO(strategyRepository.save(strategy));
//    }

//    public StrategyRequest startSimulation(Long id) {
//        Strategy strategy = getStrategy(id);
//
//        // ✅ Print to backend terminal
//        System.out.println("🔁 Received Start Simulation for Strategy:");
//        System.out.println("ID: " + strategy.getId());
//        System.out.println("Name: " + strategy.getName());
//        System.out.println("Symbols: " + strategy.getSymbolList());
//        System.out.println("Start Date: " + strategy.getStartDate());
//        System.out.println("End Date: " + strategy.getEndDate());
//        System.out.println("Initial Capital: " + strategy.getInitialCapital());
//        System.out.println("Script: " + strategy.getScript());
//        System.out.println("Params (symbol): " + strategy.getSymbol());
//
//        strategy.setStatus("running");
//        Strategy updated = strategyRepository.save(strategy);
//
//        System.out.println("✅ Status updated to 'running' and saved.");
//
//        return mapToDTO(updated);
//    }
public StrategyRequest startSimulation(Long id) {
    Strategy strategy = getStrategy(id);
    strategy.setStatus("running");

    // ✅ Prepare symbol list
    List<String> symbolListToUse = new ArrayList<>();
    for (String s : strategy.getSymbolList()) {
        if (s.equals("NIFTY 50")) {
            symbolListToUse.addAll(List.of(
                    "HDFCBANK.NS", "ICICIBANK.NS", "RELIANCE.NS", "TCS.NS", "BHARTIARTL.NS", "INFY.NS", "BAJFINANCE.NS",
                    "HINDUNILVR.NS", "ITC.NS", "LT.NS", "HCLTECH.NS", "KOTAKBANK.NS", "ULTRACEMCO.NS", "AXISBANK.NS",
                    "TITAN.NS", "NTPC.NS", "ASIANPAINT.NS", "NESTLEIND.NS", "SBIN.NS", "SUNPHARMA.NS", "MARUTI.NS", "M&M.NS",
                    "JSWSTEEL.NS", "TATAMOTORS.NS", "TATASTEEL.NS", "TECHM.NS", "WIPRO.NS", "ADANIENT.NS", "ADANIPORTS.NS",
                    "COALINDIA.NS", "POWERGRID.NS", "DRREDDY.NS", "CIPLA.NS", "EICHERMOT.NS", "HEROMOTOCO.NS", "HINDALCO.NS",
                    "INDUSINDBK.NS", "SHREECEM.NS", "BPCL.NS", "ONGC.NS", "GRASIM.NS", "IOC.NS", "HDFCLIFE.NS", "SBILIFE.NS",
                    "BAJAJ-AUTO.NS", "BAJAJFINSV.NS", "BRITANNIA.NS", "HDFC.NS", "UPL.NS"
            ));
        } else if (s.equals("NIFTY NEXT 50")) {
            symbolListToUse.addAll(List.of(
                    "BOSCHLTD.NS", "ABB.NS", "APOLLOHOSP.NS", "AMBUJACEM.NS", "ADANIGREEN.NS", "BIOCON.NS", "BEL.NS",
                    "BANKBARODA.NS", "BANDHANBNK.NS", "CANBK.NS", "CHOLAFIN.NS", "COLPAL.NS", "DABUR.NS", "DLF.NS",
                    "GODREJCP.NS", "GAIL.NS", "HAVELLS.NS", "ICICIPRULI.NS", "INDIGO.NS", "LTIM.NS", "LTTS.NS", "L&TFH.NS",
                    "LICI.NS", "MCDOWELL-N.NS", "MFSL.NS", "MUTHOOTFIN.NS", "NAUKRI.NS", "NHPC.NS", "NMDC.NS", "OFSS.NS",
                    "PAGEIND.NS", "PETRONET.NS", "PIDILITIND.NS", "PIIND.NS", "PFC.NS", "RECLTD.NS", "SAIL.NS", "SIEMENS.NS",
                    "SRF.NS", "TORNTPHARM.NS", "TRENT.NS", "TVSMOTOR.NS", "UBL.NS", "VOLTAS.NS", "ZYDUSLIFE.NS",
                    "AUROPHARMA.NS", "ALKEM.NS", "INDUSTOWER.NS", "IOCL.NS", "JINDALSTEL.NS"
            ));
        } else {
            symbolListToUse.add(s);
        }
    }

    // ✅ Load candle data
    HashMap<String, List<Candle>> candleMap = new HashMap<>();
    for (String symbol : symbolListToUse) {
        List<Candle> candles = candleRepository
                .findBySymbolAndDateBetweenOrderByDateAsc(
                        symbol, strategy.getStartDate(), strategy.getEndDate());
        candleMap.put(symbol, candles);
    }

    // ✅ Run strategy
    strategy.setSymbolList(symbolListToUse);
    BacktestResult result = strategyEngine.run(strategy, candleMap);

    // ✅ Save strategy first (to ensure foreign key reference)
    Strategy updatedStrategy = strategyRepository.save(strategy);

    // ✅ Link result + trades to strategy and persist result
    result.setStrategy(updatedStrategy);
    result.getTrades().forEach(trade -> trade.setStrategy(updatedStrategy));
    backtestResultRepository.save(result);

    // ✅ Optionally store a summary in resultJson
    try {
        String summary = String.format(
                "Strategy completed. Initial: %.2f, Final: %.2f, Trades: %d",
                result.getInitialEquity(),
                result.getFinalEquity(),
                result.getTotalTrades()
        );
        updatedStrategy.setResultJson(summary);
    } catch (Exception e) {
        updatedStrategy.setResultJson("Completed (summary unavailable)");
    }

    // ✅ Mark status as completed and save again
    updatedStrategy.setStatus("completed");
    strategyRepository.save(updatedStrategy);

    return mapToDTO(updatedStrategy);
}


    // Stop simulation
    public StrategyRequest stopSimulation(Long id) {
        Strategy strategy = getStrategy(id);
        strategy.setStatus("stopped");
        return mapToDTO(strategyRepository.save(strategy));
    }

    // Complete simulation
    public StrategyRequest completeSimulation(Long id, String resultJson) {
        Strategy strategy = getStrategy(id);
        strategy.setStatus("completed");
        strategy.setResultJson(resultJson);
        return mapToDTO(strategyRepository.save(strategy));
    }

    // Helper to fetch Strategy or throw
    private Strategy getStrategy(Long id) {
        return strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
    }

    // Map entity -> DTO
    private StrategyRequest mapToDTO(Strategy strategy) {
        return StrategyRequest.builder()
                .id(strategy.getId()) // Add this
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
        Strategy strategy = getStrategy(strategyId);
        BacktestResult result = backtestResultRepository
                .findAll() // quick workaround: you can improve with custom query
                .stream()
                .filter(r -> r.getStrategy().getId().equals(strategyId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Result not found"));

        return BacktestResultDTO.builder()
                .initialEquity(result.getInitialEquity())
                .finalEquity(result.getFinalEquity())
                .totalTrades(result.getTotalTrades())
                .trades(result.getTrades().stream().map(t ->
                        TradeDTO.builder()
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
