package com.fse.FSE_Backend_Proj.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "investors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investor {

    @Id
    private String id; // FK from User

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "kyc_status", nullable = false)
    private boolean kycStatus;

    @Column(name = "kyc_doc_url")
    private String kycDocUrl;

    private LocalDate dob;

    @Column(name = "pan_number", nullable = false, unique = true)
    private String panNumber;

    private String address;

    @Column(name = "guardian_name")
    private String guardianName;

    private String occupation;

    @Column(name = "annual_income")
    private BigDecimal annualIncome;

    @Column(name = "nominee_name")
    private String nomineeName;

    @Column(name = "bank_account_no", nullable = false)
    private String bankAccountNo;

    @Column(name = "ifsc_code", nullable = false)
    private String ifscCode;

    @Column(name = "wallet_balance")
    private double walletBalance;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (walletBalance == 0.0) {
            walletBalance = 2000000.0;
        }
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
