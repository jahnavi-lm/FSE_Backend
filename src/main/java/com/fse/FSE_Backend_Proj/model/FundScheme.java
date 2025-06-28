package com.fse.FSE_Backend_Proj.model;

import com.fse.FSE_Backend_Proj.model.enums.FundSchemeStatus;
import com.fse.FSE_Backend_Proj.model.enums.FundSchemeType;
import com.fse.FSE_Backend_Proj.model.enums.RiskLevel;
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
@Table(name = "fund_schemes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Scheme name is required")
    @Size(max = 200)
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Fund type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FundSchemeType type; // ENUM: EQUITY, DEBT, HYBRID, ELSS, INDEX

    @NotBlank(message = "Investment objective is required")
    @Column(columnDefinition = "TEXT")
    private String objective;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal aum;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "current_nav")
    private BigDecimal currentNav;

    @Column(name = "nav_updated_at")
    private LocalDateTime navUpdatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level")
    private RiskLevel riskLevel; // ENUM: LOW, MODERATE, HIGH

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "expense_ratio")
    private BigDecimal expenseRatio;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "exit_load")
    private BigDecimal exitLoad;

    @Min(value = 0)
    @Column(name = "lock_in_period")
    private Integer lockInPeriod; // nullable

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "min_investment", nullable = false)
    private BigDecimal minInvestment;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "min_sip_amount")
    private BigDecimal minSipAmount;

    @NotNull(message = "AMC must be specified")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amc_id", nullable = false)
    private AMC amc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private FundManager manager;

    @NotBlank(message = "Benchmark index is required")
    @Column(name = "benchmark_index")
    private String benchmarkIndex;

    @PastOrPresent(message = "Launch date must be in the past or today")
    @Column(name = "launch_date")
    private LocalDate launchDate;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FundSchemeStatus status; // ENUM: ACTIVE, CLOSED, MERGED

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
