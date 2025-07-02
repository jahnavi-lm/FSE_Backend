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
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String employeeCode;

    @Min(0)
    @Column(nullable = false)
    private int experienceYears;

    @NotBlank
    @Column(nullable = false)
    private String qualification;

    @Size(max = 1000)
    @Column(columnDefinition = "TEXT")
    private String bio;
}

