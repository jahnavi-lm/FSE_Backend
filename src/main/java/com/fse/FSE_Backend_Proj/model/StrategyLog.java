package com.fse.FSE_Backend_Proj.model;

import com.fse.FSE_Backend_Proj.model.enums.TradeAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "strategy_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "Backtest run is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false)
    private BacktestRun run;

    @NotNull(message = "Action must be specified")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeAction action; // BUY or SELL

    @NotBlank(message = "Asset name is required")
    @Column(nullable = false)
    private String asset;

    @NotNull(message = "Units must be specified")
    @DecimalMin(value = "0.0", inclusive = false, message = "Units must be greater than zero")
    @Column(nullable = false, precision = 14, scale = 4)
    private BigDecimal units;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal price;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    // Optional derived field for analytics
    public BigDecimal getTradeValue() {
        return price.multiply(units);
    }
}
