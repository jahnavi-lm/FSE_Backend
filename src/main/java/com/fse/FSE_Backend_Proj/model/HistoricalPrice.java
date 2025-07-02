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
@Table(name = "historical_prices",
        uniqueConstraints = @UniqueConstraint(columnNames = {"asset", "date"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricalPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Asset symbol is required")
    @Column(nullable = false)
    private String asset; // e.g. RELIANCE, TCS

//    @NotBlank(message = "Company name is required")
//    @Column(name = "company_name", nullable = false)
//    private String companyName;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private LocalDate date;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "open_price")
    private BigDecimal openPrice;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "high_price")
    private BigDecimal highPrice;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "low_price")
    private BigDecimal lowPrice;

    @NotNull(message = "Close price is required")
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "close_price", nullable = false)
    private BigDecimal closePrice;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "adj_close_price")
    private BigDecimal adjClosePrice;

    @PositiveOrZero(message = "Volume must be zero or positive")
    @Column(name = "volume")
    private Long volume;
}
