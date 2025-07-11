package com.fse.FSE_Backend_Proj.dto.fundManagerDto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundManagerRequestDto {

    @NotBlank(message = "Employee code is required")
    private String employeeCode;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @Min(value = 0, message = "Experience must be non-negative")
    private int experienceYears;

    @Size(max = 1000, message = "Bio should be within 1000 characters")
    private String bio;

    @NotBlank(message = "User ID is required")
    private String userId;

}
