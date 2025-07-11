package com.fse.FSE_Backend_Proj.service.impl;

import com.fse.FSE_Backend_Proj.dto.amcDto.AmcRequestDto;
import com.fse.FSE_Backend_Proj.dto.amcDto.AmcResponseDto;
import com.fse.FSE_Backend_Proj.exception.ResourceNotFoundException;
import com.fse.FSE_Backend_Proj.model.AMC;
import com.fse.FSE_Backend_Proj.model.User;
import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import com.fse.FSE_Backend_Proj.repository.AMCRepository;
import com.fse.FSE_Backend_Proj.repository.UserRepository;
import com.fse.FSE_Backend_Proj.service.impl.AMCServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AMCServiceImplTest {

    @Mock
    private AMCRepository amcRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AMCServiceImpl amcService;

    private User amcUser;
    private AMC amc;
    private AmcRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        amcUser = User.builder()
                .id("user-123")
                .role(UserRole.AMC)
                .build();

        amc = AMC.builder()
                .id("user-123")
                .user(amcUser)
                .name("Axis AMC")
                .registrationNo("REG123")
                .contactEmail("axis@example.com")
                .contactPhone("9999999999")
                .officeAddress("Mumbai")
                .build();

        requestDto = AmcRequestDto.builder()
                .name("Axis AMC")
                .registrationNo("REG123")
                .contactEmail("axis@example.com")
                .contactPhone("9999999999")
                .officeAddress("Mumbai")
                .build();
    }

    @Test
    void testCreateAmc_success() {
        when(userRepository.findById("user-123")).thenReturn(Optional.of(amcUser));
        when(amcRepository.save(any(AMC.class))).thenReturn(amc);

        AmcResponseDto response = amcService.createAmc("user-123", requestDto);

        assertNotNull(response);
        assertEquals("Axis AMC", response.getName());
        assertEquals("REG123", response.getRegistrationNo());
    }

    @Test
    void testCreateAmc_invalidRole_throwsException() {
        amcUser.setRole(UserRole.INVESTOR); // invalid role

        when(userRepository.findById("user-123")).thenReturn(Optional.of(amcUser));

        assertThrows(IllegalArgumentException.class,
                () -> amcService.createAmc("user-123", requestDto));
    }

    @Test
    void testCreateAmc_userNotFound() {
        when(userRepository.findById("user-123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> amcService.createAmc("user-123", requestDto));
    }

    @Test
    void testGetAmcById_success() {
        when(amcRepository.findById("user-123")).thenReturn(Optional.of(amc));

        AmcResponseDto result = amcService.getAmcById("user-123");

        assertNotNull(result);
        assertEquals("Axis AMC", result.getName());
    }

    @Test
    void testGetAmcById_notFound() {
        when(amcRepository.findById("user-123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> amcService.getAmcById("user-123"));
    }

    @Test
    void testGetAllAmcs() {
        when(amcRepository.findAll()).thenReturn(List.of(amc));

        List<AmcResponseDto> result = amcService.getAllAmcs();

        assertEquals(1, result.size());
        assertEquals("Axis AMC", result.get(0).getName());
    }

    @Test
    void testUpdateAmc_success() {
        when(amcRepository.findById("user-123")).thenReturn(Optional.of(amc));
        when(amcRepository.save(any(AMC.class))).thenReturn(amc);

        AmcResponseDto updated = amcService.updateAmc("user-123", requestDto);

        assertNotNull(updated);
        assertEquals("Axis AMC", updated.getName());
    }

    @Test
    void testUpdateAmc_notFound() {
        when(amcRepository.findById("user-123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> amcService.updateAmc("user-123", requestDto));
    }
    @Test
    void testDeleteAmc_success() {
        when(amcRepository.findById("user-123")).thenReturn(Optional.of(amc));

        assertDoesNotThrow(() -> amcService.deleteAmc("user-123"));

        verify(amcRepository, times(1)).delete(amc);
    }

    @Test
    void testDeleteAmc_notFound() {
        when(amcRepository.findById("user-123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> amcService.deleteAmc("user-123"));
    }

}
