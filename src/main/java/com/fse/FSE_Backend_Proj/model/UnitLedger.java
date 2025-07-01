package com.fse.FSE_Backend_Proj.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "unit_ledger",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "investor_id", "scheme_id" })
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "Investor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

    @NotNull(message = "Fund scheme is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private FundScheme fundScheme;

    @NotNull(message = "Units held must be present")
    @DecimalMin(value = "0.0", inclusive = true, message = "Units must be non-negative")
    @Column(name = "units_held", nullable = false)
    private BigDecimal unitsHeld;

    @NotNull(message = "Average NAV is required")
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "avg_nav", nullable = false)
    private BigDecimal avgNav;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }



}
