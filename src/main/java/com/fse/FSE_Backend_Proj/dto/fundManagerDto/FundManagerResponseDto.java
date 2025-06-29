package com.fse.FSE_Backend_Proj.dto.fundManagerDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FundManagerResponseDto {
    private String id;
    private String employeeCode;
    private String qualification;
    private int experienceYears;
    private String bio;
    private String amcId;
}
