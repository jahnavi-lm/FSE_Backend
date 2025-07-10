package com.fse.FSE_Backend_Proj.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String action;
    private double price;

    private String symbol; // 👈 Add this line
    private int quantity;
    private double totalCostPrice;
    private double openingBalance;
    private double closingBalance;
    private double nav;
    private double realizedProfit;

    @ManyToOne
    private Strategy strategy;
}
