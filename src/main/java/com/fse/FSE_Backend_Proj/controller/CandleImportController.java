package com.fse.FSE_Backend_Proj.controller;


import com.fse.FSE_Backend_Proj.model.Candle;
import com.fse.FSE_Backend_Proj.repository.CandleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/candles")
@RequiredArgsConstructor
public class CandleImportController {

    private final CandleRepository candleRepository;

    @PostMapping("/import")
    public ResponseEntity<String> importCandles(@RequestBody List<Candle> candles) {
        if (candles == null || candles.isEmpty()) {
            return ResponseEntity.badRequest().body("Candle list is empty");
        }

        candleRepository.saveAll(candles);
        return ResponseEntity.ok("Successfully imported " + candles.size() + " candles.");
    }
}
