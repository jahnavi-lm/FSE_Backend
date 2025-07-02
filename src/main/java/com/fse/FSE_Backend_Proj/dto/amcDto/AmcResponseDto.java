package com.fse.FSE_Backend_Proj.dto.amcDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AmcResponseDto {

    private String id;
    private String name;
    private String registrationNo;
    private String contactEmail;
    private String contactPhone;
    private String officeAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
