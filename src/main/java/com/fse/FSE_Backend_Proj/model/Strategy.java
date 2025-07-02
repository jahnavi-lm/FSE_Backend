package com.fse.FSE_Backend_Proj.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "strategies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Strategy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Strategy name is required")
    @Size(max = 150, message = "Name cannot exceed 150 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 2000)
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Strategy parameters must be provided")
    @Lob
    @Column(name = "parameters", nullable = false, columnDefinition = "JSONB")
    private String parameters; // store JSON config like { "rsi_period": 14 }

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
