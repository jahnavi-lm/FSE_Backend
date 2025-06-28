package com.fse.FSE_Backend_Proj.model;

import com.fse.FSE_Backend_Proj.model.enums.BacktestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "backtest_runs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BacktestRun {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "Fund manager is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private FundManager manager;

    @NotNull(message = "Scheme is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private FundScheme fundScheme;

    @NotNull(message = "Strategy is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id", nullable = false)
    private Strategy strategy;

    @NotNull(message = "Start date is required")
    @PastOrPresent
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull(message = "Initial capital must be specified")
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "initial_capital", nullable = false)
    private BigDecimal initialCapital;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BacktestStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = BacktestStatus.RUNNING;
    }
}
