package com.fse.FSE_Backend_Proj.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Strategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;              // e.g. "RSI-Based Strategy"
    private List<String> symbolList;            // Stock symbol like "TCS"
    private String script;            // For user-defined DSL
    private String paramsJson;
    private String symbol;// JSON string for predefined param (optional)

    private Double initialCapital;

    private LocalDate startDate;
    private LocalDate endDate;

    private String status;                // not started, running, stopped, completed
    @Column(length = 20000)
    private String resultJson;            // Simulation result
}
