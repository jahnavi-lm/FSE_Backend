package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RedeemResponse {
    private String message;
    private double amountCredited;
    private double navAtRedemption;
    private LocalDateTime txnTime;


}
