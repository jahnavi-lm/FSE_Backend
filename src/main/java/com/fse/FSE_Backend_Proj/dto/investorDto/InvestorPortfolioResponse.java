package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class InvestorPortfolioResponse {
    private String investorId;
    private List<HoldingDto> portfolio;
}
