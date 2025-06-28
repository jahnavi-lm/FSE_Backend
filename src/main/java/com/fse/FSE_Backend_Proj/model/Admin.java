package com.fse.FSE_Backend_Proj.model;//package com.fse.FSE_Backend_Proj.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//import lombok.*;
//
//@Entity
//@Table(name = "admins")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Admin {
//
//    @Id
//    @Column(name = "id")
//    private Long id; // FK to users.id
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "id") // Primary key and FK to users
//    private User user;
//
//    @NotBlank(message = "Designation is required")
//    @Size(min = 3, max = 100, message = "Designation must be between 3 and 100 characters")
//    @Column(name = "designation", nullable = false)
//    private String designation;
//
//    @Size(max = 1000, message = "Notes should be under 1000 characters")
//    @Column(name = "notes", columnDefinition = "TEXT")
//    private String notes;
//
//    @Column(name = "super_admin", nullable = false)
//    private boolean superAdmin;
//}
