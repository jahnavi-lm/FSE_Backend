package com.fse.FSE_Backend_Proj.engine;


import com.fse.FSE_Backend_Proj.model.BacktestResult;
import com.fse.FSE_Backend_Proj.model.Candle;
import com.fse.FSE_Backend_Proj.model.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StrategyEngine {

    @Autowired
    private Map<String, StrategyExecutor> strategyExecutors;

    public BacktestResult run(Strategy strategy, HashMap<String,List<Candle>>candleMap) {
        StrategyExecutor executor;
        if (strategy.getScript() != null && !strategy.getScript().isBlank()) {
            executor = strategyExecutors.get("dslStrategyExecutor"); // bean name
        } else {
            executor = strategyExecutors.get(strategy.getName()); // predefined name
        }
        if (executor == null) {
            throw new IllegalArgumentException("Unknown strategy: " + strategy.getName());
        }
        return executor.execute(candleMap, strategy);
    }
}
