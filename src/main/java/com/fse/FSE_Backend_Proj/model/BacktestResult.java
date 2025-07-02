package com.fse.FSE_Backend_Proj.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "backtest_results",
        uniqueConstraints = @UniqueConstraint(columnNames = {"run_id", "date"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BacktestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "Backtest run is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false)
    private BacktestRun run;

    @NotNull(message = "Result date is required")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "Simulated NAV is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "NAV must be positive")
    @Column(name = "simulated_nav", nullable = false, precision = 12, scale = 4)
    private BigDecimal simulatedNav;

    @NotNull(message = "Portfolio value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Portfolio value must be positive")
    @Column(name = "portfolio_value", nullable = false, precision = 14, scale = 2)
    private BigDecimal portfolioValue;

    @DecimalMin(value = "-1.0", inclusive = true)
    @DecimalMax(value = "1.0", inclusive = true)
    @Column(name = "daily_return")
    private BigDecimal dailyReturn;

    // Optional enhancements (if you need these for analytics):
    // @Column(name = "cumulative_return")
    // private BigDecimal cumulativeReturn;

    // @Column(name = "drawdown")
    // private BigDecimal drawdown;
}
