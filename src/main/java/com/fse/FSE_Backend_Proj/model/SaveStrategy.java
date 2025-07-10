package com.fse.FSE_Backend_Proj.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private Double capitalAllocation;

    private String status;  // not started, running, stopped, completed

    @Column(length = 2000)
    private String parametersJson;  // Store dynamic form values as JSON

    @Column(length = 2000)
    private String resultJson;      // Simulation result

}
