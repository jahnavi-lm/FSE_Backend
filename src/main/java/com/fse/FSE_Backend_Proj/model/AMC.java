package com.fse.FSE_Backend_Proj.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "amcs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AMC {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "AMC name is required")
    @Size(min = 2, max = 150, message = "AMC name must be between 2 and 150 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Registration number is required")
    @Size(min = 5, max = 50)
    @Column(name = "registration_no", nullable = false, unique = true)
    private String registrationNo;

    @NotBlank(message = "AMC email is required")
    @Email(message = "Invalid email format")
    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact phone must be 10 digits")
    @Column(name = "contact_phone")
    private String contactPhone;

    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 300)
    @Column(name = "office_address", columnDefinition = "TEXT")
    private String officeAddress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
