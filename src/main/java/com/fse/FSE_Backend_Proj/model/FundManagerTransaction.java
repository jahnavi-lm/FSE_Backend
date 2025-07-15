package com.fse.FSE_Backend_Proj.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fund_manager_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundManagerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund_manager_id", nullable = false)
    private FundManager fundManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund_scheme_id", nullable = false)
    private FundScheme fundScheme;

    @Column(name = "fund_scheme_name", nullable = false)
    private String fundSchemeName;

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType; // BUY or SELL

    @Column(name = "status", nullable = false)
    private String status; // SUCCESS or FAILED

    @Column(name = "number_of_stocks", nullable = false)
    private Integer numberOfStocks;

    @Column(name = "price_per_stock", nullable = false)
    private BigDecimal pricePerStock;

    @Column(name = "total_value", nullable = false)
    private BigDecimal totalValue;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @PrePersist
    public void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }
}
