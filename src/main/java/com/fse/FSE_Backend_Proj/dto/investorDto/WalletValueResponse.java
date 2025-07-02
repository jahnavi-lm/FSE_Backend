package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class WalletValueResponse {
    private BigDecimal walletValue;

    public WalletValueResponse(BigDecimal walletValue) {
        this.walletValue = walletValue;
    }

}
