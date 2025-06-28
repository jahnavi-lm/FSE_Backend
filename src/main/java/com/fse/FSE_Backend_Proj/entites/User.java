package com.fse.FSE_Backend_Proj.entites;

import com.fse.FSE_Backend_Proj.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "users")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String username;
//    private String fullName;
//    @Column(unique = true, nullable = false)
//    private String email;
//    private String passwordHash;
//    private String role;
//    private String status;
//
//
//}

//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Table(name = "users")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "amc_id")
//    private AMC amc;
//
//    private String name;
//
//    @Column(unique = true, nullable = false)
//    private String email;
//
//    private String password;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt = LocalDateTime.now();
//
//    @Column(name = "kyc_status")
//    private boolean kycStatus;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;
//}

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "amc_id")
    private AMC amc;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "kyc_status")
    private boolean kycStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

