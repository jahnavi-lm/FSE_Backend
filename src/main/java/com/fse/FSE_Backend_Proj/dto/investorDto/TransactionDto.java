package com.fse.FSE_Backend_Proj.dto.investorDto;

import com.fse.FSE_Backend_Proj.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionDto {
    private String transactionId;
    private TransactionType type;           // ← use TransactionType if you're passing enum
    private String schemeId;
    private BigDecimal navAtTransaction;
    private BigDecimal units;
    private BigDecimal amount;
    private LocalDateTime txnTime;
}
