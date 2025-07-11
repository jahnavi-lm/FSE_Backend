package com.fse.FSE_Backend_Proj.dto.amcDto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmcRequestDto {

    @NotBlank(message = "AMC name is required")
    @Size(min = 2, max = 150, message = "AMC name must be between 2 and 150 characters")
    private String name;

    @NotBlank(message = "Registration number is required")
    @Size(min = 5, max = 50, message = "Registration number must be between 5 and 50 characters")
    private String registrationNo;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact phone must be 10 digits")
    private String contactPhone;

    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 300, message = "Address must be between 10 and 300 characters")
    private String officeAddress;
}
