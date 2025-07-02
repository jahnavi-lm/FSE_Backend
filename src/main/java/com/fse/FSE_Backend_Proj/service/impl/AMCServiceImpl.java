package com.fse.FSE_Backend_Proj.service.impl;

import com.fse.FSE_Backend_Proj.dto.amcDto.AmcRequestDto;
import com.fse.FSE_Backend_Proj.dto.amcDto.AmcResponseDto;
import com.fse.FSE_Backend_Proj.exception.ResourceNotFoundException;
import com.fse.FSE_Backend_Proj.model.AMC;
import com.fse.FSE_Backend_Proj.model.User;
import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import com.fse.FSE_Backend_Proj.repository.AMCRepository;
import com.fse.FSE_Backend_Proj.repository.UserRepository;
import com.fse.FSE_Backend_Proj.service.AMCService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AMCServiceImpl implements AMCService {

    private final AMCRepository amcRepository;
    private final UserRepository userRepository;

    @Override
    public AmcResponseDto createAmc(String userId, AmcRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (!UserRole.AMC.equals(user.getRole())) {
            throw new IllegalArgumentException("User must have AMC role to register an AMC.");
        }

        AMC amc = AMC.builder()
                .id(user.getId())
                .user(user)
                .name(dto.getName())
                .registrationNo(dto.getRegistrationNo())
                .contactEmail(dto.getContactEmail())
                .contactPhone(dto.getContactPhone())
                .officeAddress(dto.getOfficeAddress())
                .build();

        amc = amcRepository.save(amc);
        return mapToDto(amc);
    }

    @Override
    public AmcResponseDto getAmcById(String id) {
        AMC amc = amcRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AMC not found with ID: " + id));
        return mapToDto(amc);
    }

    @Override
    public List<AmcResponseDto> getAllAmcs() {
        return amcRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AmcResponseDto updateAmc(String id, AmcRequestDto dto) {
        AMC amc = amcRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AMC not found with ID: " + id));

        amc.setName(dto.getName());
        amc.setRegistrationNo(dto.getRegistrationNo());
        amc.setContactEmail(dto.getContactEmail());
        amc.setContactPhone(dto.getContactPhone());
        amc.setOfficeAddress(dto.getOfficeAddress());

        return mapToDto(amcRepository.save(amc));
    }

    @Override
    public void deleteAmc(String id) {
        AMC amc = amcRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AMC not found with ID: " + id));
        amcRepository.delete(amc);
    }

    private AmcResponseDto mapToDto(AMC amc) {
        return AmcResponseDto.builder()
                .id(amc.getId())
                .name(amc.getName())
                .registrationNo(amc.getRegistrationNo())
                .contactEmail(amc.getContactEmail())
                .contactPhone(amc.getContactPhone())
                .officeAddress(amc.getOfficeAddress())
                .createdAt(amc.getCreatedAt())
                .updatedAt(amc.getUpdatedAt())
                .build();
    }
}
