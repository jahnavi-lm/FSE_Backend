package com.fse.FSE_Backend_Proj.dto.investorDto;

import com.fse.FSE_Backend_Proj.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor // Optional, but helpful
public class TransactionDto {
    private String transactionId;
    private TransactionType type;
    private String schemeId;
    private String schemeName; // <-- Add this field
    private BigDecimal navAtTransaction;
    private BigDecimal units;
    private BigDecimal amount;
    private LocalDateTime txnTime;
}
