package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerResponseDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.TotalAmount;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.CompanyInvestmentDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeResponseDto;
import com.fse.FSE_Backend_Proj.model.FundScheme;
import com.fse.FSE_Backend_Proj.repository.FundManagerRepository;
import com.fse.FSE_Backend_Proj.service.FundManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fundManagers")
@RequiredArgsConstructor
public class FundManagerController {

    private final FundManagerService fundManagerService;
    private final FundManagerRepository fundManagerRepository;

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

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> checkFundManagerExists(@PathVariable UUID id){
        boolean exists = fundManagerRepository.existsById(String.valueOf(id));
        return ResponseEntity.ok(exists);

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

    @GetMapping("/schemes/{id}")
    public ResponseEntity<List<FundSchemeResponseDto>> getSchemesByFundManager(@PathVariable String id) {
        return ResponseEntity.ok(fundManagerService.getSchemesByFundManagerId(id));
    }

    @PutMapping("/scheme/{id}")
    public ResponseEntity<FundSchemeResponseDto> updateSchemeById(
            @PathVariable String id,
            @Valid @RequestBody FundSchemeResponseDto dto) {
        return ResponseEntity.ok(fundManagerService.UpdateSchemeById(id, dto));
    }

    @GetMapping("/{id}/totalamount")
    public ResponseEntity<TotalAmount>getTotalAmount(@PathVariable String id){
        return ResponseEntity.ok(fundManagerService.getTotalAmount(id));
    }

    @PutMapping("/buy/{id}")
    public ResponseEntity<CompanyInvestmentDto>buyStocks(@PathVariable String id,
                                                         @RequestBody CompanyInvestmentDto dto){
        return ResponseEntity.ok(fundManagerService.buyStocks(id,dto));
    }



}
