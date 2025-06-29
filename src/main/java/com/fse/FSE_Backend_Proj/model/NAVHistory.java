package com.fse.FSE_Backend_Proj.model;

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
@Table(name = "nav_history", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"scheme_id", "date"})
})

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NAVHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Scheme is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private FundScheme scheme;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "NAV is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "NAV must be greater than 0")
    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal nav;

    @Builder.Default
    @Column(name = "is_manual")
    private Boolean isManual = false; // to indicate whether entered manually or system

    @Column(name = "updated_by")
    private String updatedBy; // user or system name

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }



}
