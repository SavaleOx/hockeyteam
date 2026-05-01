package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.AchievementRequestDto;
import com.savaleox.hockeyteam.dto.AchievementResponseDto;
import com.savaleox.hockeyteam.mapper.AchievementMapper;
import com.savaleox.hockeyteam.model.entity.Achievement;
import com.savaleox.hockeyteam.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private AchievementService achievementService;

    private Achievement achievement;
    private AchievementRequestDto requestDto;
    private AchievementResponseDto responseDto;

    @BeforeEach
    void setUp() {
        achievement = new Achievement();
        achievement.setId(1L);
        achievement.setName("Stanley Cup");
        achievement.setDescription("Win the cup");

        requestDto = new AchievementRequestDto();
        requestDto.setName("Stanley Cup");
        requestDto.setDescription("Win the cup");

        responseDto = new AchievementResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Stanley Cup");
        responseDto.setDescription("Win the cup");
    }

    @Test
    void getAll_ShouldReturnList() {
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        when(achievementMapper.toResponseDto(achievement)).thenReturn(responseDto);

        List<AchievementResponseDto> result = achievementService.getAll();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
        verify(achievementRepository).findAll();
    }

    @Test
    void getAll_EmptyList() {
        when(achievementRepository.findAll()).thenReturn(List.of());

        List<AchievementResponseDto> result = achievementService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void getById_WhenExists_ShouldReturnDto() {
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
        when(achievementMapper.toResponseDto(achievement)).thenReturn(responseDto);

        AchievementResponseDto result = achievementService.getById(1L);

        assertEquals(responseDto, result);
    }

    @Test
    void getById_WhenNotFound_ShouldThrowNoSuchElementException() {
        when(achievementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> achievementService.getById(99L));
    }

    @Test
    void getByName_WhenExists_ShouldReturnDto() {
        when(achievementRepository.findByName("Stanley Cup")).thenReturn(achievement);
        when(achievementMapper.toResponseDto(achievement)).thenReturn(responseDto);

        AchievementResponseDto result = achievementService.getByName("Stanley Cup");

        assertEquals(responseDto, result);
    }

    @Test
    void getByName_WhenNull_ShouldReturnNull() {
        when(achievementRepository.findByName("NonExistent")).thenReturn(null);

        AchievementResponseDto result = achievementService.getByName("NonExistent");

        assertNull(result);
    }

    @Test
    void create_ShouldSaveAndReturnDto() {
        when(achievementMapper.toEntity(requestDto)).thenReturn(achievement);
        when(achievementRepository.save(achievement)).thenReturn(achievement);
        when(achievementMapper.toResponseDto(achievement)).thenReturn(responseDto);

        AchievementResponseDto result = achievementService.create(requestDto);

        assertEquals(responseDto, result);
        verify(achievementRepository).save(achievement);
    }

    @Test
    void delete_ShouldCallRepository() {
        doNothing().when(achievementRepository).deleteById(1L);

        achievementService.delete(1L);

        verify(achievementRepository).deleteById(1L);
    }

    @Test
    void update_WhenExists_ShouldUpdateAllFields() {
        AchievementRequestDto updateDto = new AchievementRequestDto();
        updateDto.setName("New Name");
        updateDto.setDescription("New Description");

        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);
        when(achievementMapper.toResponseDto(achievement)).thenReturn(responseDto);

        AchievementResponseDto result = achievementService.update(1L, updateDto);

        assertEquals(responseDto, result);
        assertEquals("New Name", achievement.getName());
        assertEquals("New Description", achievement.getDescription());
    }

    @Test
    void update_WhenNotFound_ShouldThrowException() {
        when(achievementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> achievementService.update(99L, requestDto));
    }

    @Test
    void patch_OnlyName_ShouldUpdateOnlyName() {
        AchievementRequestDto patchDto = new AchievementRequestDto();
        patchDto.setName("New Name");

        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
        when(achievementRepository.save(achievement)).thenReturn(achievement);
        when(achievementMapper.toResponseDto(achievement)).thenReturn(responseDto);

        achievementService.patch(1L, patchDto);

        assertEquals("New Name", achievement.getName());
        assertEquals("Win the cup", achievement.getDescription());
    }

    @Test
    void patch_OnlyDescription_ShouldUpdateOnlyDescription() {
        AchievementRequestDto patchDto = new AchievementRequestDto();
        patchDto.setDescription("New Description");

        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
        when(achievementRepository.save(achievement)).thenReturn(achievement);
        when(achievementMapper.toResponseDto(achievement)).thenReturn(responseDto);

        achievementService.patch(1L, patchDto);

        assertEquals("Stanley Cup", achievement.getName());
        assertEquals("New Description", achievement.getDescription());
    }

    @Test
    void patch_BothFields_ShouldUpdateBoth() {
        AchievementRequestDto patchDto = new AchievementRequestDto();
        patchDto.setName("New Name");
        patchDto.setDescription("New Description");

        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
        when(achievementRepository.save(achievement)).thenReturn(achievement);
        when(achievementMapper.toResponseDto(achievement)).thenReturn(responseDto);

        achievementService.patch(1L, patchDto);

        assertEquals("New Name", achievement.getName());
        assertEquals("New Description", achievement.getDescription());
    }

    @Test
    void patch_NullFields_ShouldNotUpdate() {
        AchievementRequestDto patchDto = new AchievementRequestDto();

        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
        when(achievementRepository.save(achievement)).thenReturn(achievement);
        when(achievementMapper.toResponseDto(achievement)).thenReturn(responseDto);

        achievementService.patch(1L, patchDto);

        assertEquals("Stanley Cup", achievement.getName());
        assertEquals("Win the cup", achievement.getDescription());
    }

    @Test
    void patch_WhenNotFound_ShouldThrowException() {
        when(achievementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> achievementService.patch(99L, requestDto));
    }
}