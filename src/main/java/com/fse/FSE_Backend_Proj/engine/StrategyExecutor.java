package com.fse.FSE_Backend_Proj.engine;


import com.fse.FSE_Backend_Proj.model.BacktestResult;
import com.fse.FSE_Backend_Proj.model.Candle;
import com.fse.FSE_Backend_Proj.model.Strategy;

import java.util.HashMap;
import java.util.List;

public interface StrategyExecutor {
    BacktestResult execute(HashMap<String,List<Candle>>CandleMap, Strategy strategy);
}
