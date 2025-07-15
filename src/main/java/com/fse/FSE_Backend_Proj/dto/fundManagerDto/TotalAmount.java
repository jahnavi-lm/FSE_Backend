package com.fse.FSE_Backend_Proj.dto.fundManagerDto;

import com.fse.FSE_Backend_Proj.model.Strategy;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@Builder
public class TotalAmount {
   private BigDecimal TotalInvestedAmount;
   private BigDecimal TotalReturn;
   private BigInteger TotalStrategies;
}
