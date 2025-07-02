package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.dto.amcDto.AmcRequestDto;
import com.fse.FSE_Backend_Proj.dto.amcDto.AmcResponseDto;
import com.fse.FSE_Backend_Proj.service.AMCService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amcs")
@RequiredArgsConstructor
public class AMCController {

    private final AMCService amcService;

    // CREATE
    @PostMapping("/{userId}")
    public ResponseEntity<AmcResponseDto> createAmc(@PathVariable String userId,
                                                    @Valid @RequestBody AmcRequestDto dto) {
        return ResponseEntity.ok(amcService.createAmc(userId, dto));
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<AmcResponseDto> getAmc(@PathVariable String id) {
        return ResponseEntity.ok(amcService.getAmcById(id));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<AmcResponseDto>> getAllAmcs() {
        return ResponseEntity.ok(amcService.getAllAmcs());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AmcResponseDto> updateAmc(@PathVariable String id,
                                                    @Valid @RequestBody AmcRequestDto dto) {
        return ResponseEntity.ok(amcService.updateAmc(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmc(@PathVariable String id) {
        amcService.deleteAmc(id);
        return ResponseEntity.noContent().build();
    }

}
