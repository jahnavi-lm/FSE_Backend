package com.fse.FSE_Backend_Proj.model;

import java.util.List;

public record ImportResult(List<Candle> candles, List<String> failedSymbols) {}
