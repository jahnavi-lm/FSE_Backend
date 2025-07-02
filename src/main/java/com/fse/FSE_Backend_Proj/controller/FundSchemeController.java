package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.AssignFundManagerDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeResponseDto;
import com.fse.FSE_Backend_Proj.service.FundSchemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fund-schemes")
@RequiredArgsConstructor
public class FundSchemeController {

    private final FundSchemeService fundSchemeService;

    // Create Fund Scheme
    @PostMapping("/create/{amcId}")
    public ResponseEntity<FundSchemeResponseDto> create(@PathVariable String amcId,
                                                        @Valid @RequestBody FundSchemeRequestDto dto) {
        return ResponseEntity.ok(fundSchemeService.create(amcId, dto));
    }

    // Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<FundSchemeResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(fundSchemeService.getById(id));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<FundSchemeResponseDto>> getAll() {
        return ResponseEntity.ok(fundSchemeService.getAll());
    }

    // Get by AMC
    @GetMapping("/amc/{amcId}")
    public ResponseEntity<List<FundSchemeResponseDto>> getByAmc(@PathVariable String amcId) {
        return ResponseEntity.ok(fundSchemeService.getByAmc(amcId));
    }

    // Update Fund Scheme
    @PutMapping("/{id}")
    public ResponseEntity<FundSchemeResponseDto> update(@PathVariable String id,
                                                        @Valid @RequestBody FundSchemeRequestDto dto) {
        return ResponseEntity.ok(fundSchemeService.update(id, dto));
    }

    // Delete Fund Scheme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        fundSchemeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Assign Fund Manager
    @PostMapping("/{schemeId}/assign-manager")
    public ResponseEntity<FundSchemeResponseDto> assignManager(@PathVariable String schemeId,
                                                               @Valid @RequestBody AssignFundManagerDto dto) {
        return ResponseEntity.ok(fundSchemeService.assignManager(schemeId, dto.getManagerId()));
    }
}
