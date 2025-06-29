package com.fse.FSE_Backend_Proj.dto.fundSchemeDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignFundManagerDto {
    @NotBlank(message = "Manager ID is required")
    private String managerId;
}
