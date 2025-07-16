package com.fse.FSE_Backend_Proj.dto.fundSchemeDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellStockDto {
    private String fundSchemeId;   // ID of the fund scheme performing the sale
    private Long companyId;        // ID of the company whose stock is being sold
    private Integer stocksToSell;  // Number of stocks to sell
}
