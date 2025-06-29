package com.fse.FSE_Backend_Proj.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "fund_managers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundManager {

    @Id
    private String id; // FK to users.id

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @NotBlank(message = "Employee code is required")
    @Column(name = "employee_code", nullable = false, unique = true)
    private String employeeCode;

    @NotNull(message = "AMC must be assigned")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amc_id", nullable = false)
    private AMC amc;

    @Min(value = 0, message = "Experience must be non-negative")
    @Column(name = "experience_years", nullable = false)
    private int experienceYears;

    @NotBlank(message = "Qualification is required")
    @Column(nullable = false)
    private String qualification;

    @Size(max = 1000, message = "Bio should be within 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String bio;


}
