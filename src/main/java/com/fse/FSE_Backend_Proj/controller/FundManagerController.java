package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerResponseDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeResponseDto;
import com.fse.FSE_Backend_Proj.service.FundManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fundManagers")
@RequiredArgsConstructor
public class FundManagerController {

    private final FundManagerService fundManagerService;

    @PostMapping("/create")
    public ResponseEntity<FundManagerResponseDto> create(
            @Valid @RequestBody FundManagerRequestDto dto) {
        System.out.println("inside the create ctrl block");
        return ResponseEntity.ok(fundManagerService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundManagerResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(fundManagerService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<FundManagerResponseDto>> getAll() {
        return ResponseEntity.ok(fundManagerService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FundManagerResponseDto> update(@PathVariable String id,
                                                         @Valid @RequestBody FundManagerRequestDto dto) {
        return ResponseEntity.ok(fundManagerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        fundManagerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/schemes")
    public ResponseEntity<List<FundSchemeResponseDto>> getSchemesByFundManager(@PathVariable String id) {
        return ResponseEntity.ok(fundManagerService.getSchemesByFundManagerId(id));
    }

}
