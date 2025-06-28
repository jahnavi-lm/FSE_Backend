package com.fse.FSE_Backend_Proj.model;

import com.fse.FSE_Backend_Proj.model.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "Investor reference is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

    @NotNull(message = "Fund Scheme is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private FundScheme fundScheme;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "txn_type", nullable = false)
    private TransactionType txnType; // ENUM: BUY, REDEEM

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Units must be calculated")
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private BigDecimal units;

    @NotNull(message = "NAV at transaction is required")
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "nav_at_txn", nullable = false)
    private BigDecimal navAtTxn;

    @Column(name = "txn_date", nullable = false, updatable = false)
    private LocalDateTime txnDate;

    @PrePersist
    public void onTransaction() {
        this.txnDate = LocalDateTime.now();
    }
}
