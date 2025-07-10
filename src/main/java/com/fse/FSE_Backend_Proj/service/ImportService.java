package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.model.Candle;
import com.fse.FSE_Backend_Proj.model.Company;
import com.fse.FSE_Backend_Proj.repository.CandleRepository;
import com.fse.FSE_Backend_Proj.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final YahooFinanceService yahooService;
    private final CandleRepository candleRepo;
    private final CompanyRepository companyRepo;

    public void importIndexData(String indexName) {
        List<String> symbols = yahooService.getSymbolsForIndex(indexName);

        for (String symbol : symbols) {
            if (!companyRepo.existsBySymbol(symbol)) {
                companyRepo.save(Company.builder()
                        .symbol(symbol)
                        .name(symbol)
                        .indexName(indexName)
                        .build());
            }

            List<Candle> candles = yahooService.fetchHistoricalData(symbol);
            candleRepo.saveAll(candles);
        }
    }
}
