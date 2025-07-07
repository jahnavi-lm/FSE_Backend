package com.fse.FSE_Backend_Proj.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "company_investments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyInvestment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // Auto-generated primary key

    @NotBlank(message = "Company ID is required")
    @Column(name = "company_id", nullable = false)
    private String companyId; // Manual input from request

    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotNull(message = "Invested amount is required")
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "invested_amount", nullable = false)
    private BigDecimal investedAmount;

    @NotNull(message = "Number of stocks is required")
    @Min(value = 1, message = "Number of stocks must be at least 1")
    @Column(name = "number_of_stocks", nullable = false)
    private Integer numberOfStocks;

    @NotNull(message = "Investment date is required")
    @Column(name = "investment_date", nullable = false)
    private LocalDate investmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund_scheme_id", nullable = false)
    private FundScheme fundScheme;
}
