package com.fse.FSE_Backend_Proj.dto.fundSchemeDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FundManagerTransactionDto {
    private String fundManagerName;
    private String fundSchemeName;
    private String companyId;
    private String companyName;
    private String transactionType; // BUY or SELL
    private String status; // SUCCESS or FAILED
    private Integer numberOfStocks;
    private BigDecimal pricePerStock;
    private BigDecimal totalValue;
    private LocalDateTime transactionDate;
}
