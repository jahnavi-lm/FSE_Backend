package com.fse.FSE_Backend_Proj.repository;


import com.fse.FSE_Backend_Proj.model.Candle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CandleRepository extends JpaRepository<Candle, Long> {

    List<Candle> findBySymbolAndDateBetweenOrderByDateAsc(
            String symbol,
            LocalDate startDate,
            LocalDate endDate
    );
}
