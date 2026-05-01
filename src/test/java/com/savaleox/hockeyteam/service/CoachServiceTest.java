package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.CoachRequestDto;
import com.savaleox.hockeyteam.dto.CoachResponseDto;
import com.savaleox.hockeyteam.mapper.CoachMapper;
import com.savaleox.hockeyteam.model.entity.Coach;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.repository.CoachRepository;
import com.savaleox.hockeyteam.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoachServiceTest {

    @Mock
    private CoachRepository coachRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private CoachMapper coachMapper;

    @InjectMocks
    private CoachService coachService;

    private Coach coach;
    private Team team;
    private Team newTeam;
    private CoachRequestDto requestDto;
    private CoachResponseDto responseDto;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(1L);
        team.setName("Team A");

        newTeam = new Team();
        newTeam.setId(2L);
        newTeam.setName("Team B");

        coach = new Coach();
        coach.setId(1L);
        coach.setName("John");
        coach.setSurname("Doe");
        coach.setAge(45);
        coach.setTactic("Offensive");
        coach.setTeam(team);

        requestDto = new CoachRequestDto();
        requestDto.setName("John");
        requestDto.setSurname("Doe");
        requestDto.setAge(45);
        requestDto.setTactic("Offensive");
        requestDto.setTeamId(1L);

        responseDto = new CoachResponseDto();
        responseDto.setId(1L);
        responseDto.setName("John");
        responseDto.setSurname("Doe");
        responseDto.setAge(45);
        responseDto.setTactic("Offensive");
        responseDto.setTeamName("Geese");
    }

    @Test
    void getAll_ShouldReturnList() {
        when(coachRepository.findAll()).thenReturn(List.of(coach));
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        List<CoachResponseDto> result = coachService.getAll();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void getById_WhenExists_ShouldReturnDto() {
        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        CoachResponseDto result = coachService.getById(1L);

        assertEquals(responseDto, result);
    }

    @Test
    void getById_WhenNotFound_ShouldThrowRuntimeException() {
        when(coachRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> coachService.getById(99L));
        assertEquals("Coach not found", exception.getMessage());
    }

    @Test
    void getByTeam_ShouldReturnList() {
        when(coachRepository.findByTeamId(1L)).thenReturn(List.of(coach));
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        List<CoachResponseDto> result = coachService.getByTeam(1L);

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void create_WhenTeamHasNoCoach_ShouldCreate() {
        when(coachRepository.findByTeamId(1L)).thenReturn(List.of());
        when(coachMapper.toEntity(requestDto)).thenReturn(coach);
        when(teamRepository.getById(1L)).thenReturn(team);
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        CoachResponseDto result = coachService.create(requestDto);

        assertEquals(responseDto, result);
        verify(coachRepository).save(coach);
        assertEquals(team, coach.getTeam());
    }

    @Test
    void create_WhenTeamHasCoach_ShouldThrowIllegalStateException() {
        when(coachRepository.findByTeamId(1L)).thenReturn(List.of(coach));

        assertThrows(IllegalStateException.class, () -> coachService.create(requestDto));
    }

    @Test
    void delete_WithTeam_ShouldNullifyTeamCoach() {
        team.setCoach(coach);
        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        doNothing().when(coachRepository).deleteById(1L);

        coachService.delete(1L);

        assertNull(team.getCoach());
        verify(teamRepository).save(team);
        verify(coachRepository).deleteById(1L);
    }

    @Test
    void delete_WithoutTeam_ShouldOnlyDeleteCoach() {
        coach.setTeam(null);
        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        doNothing().when(coachRepository).deleteById(1L);

        coachService.delete(1L);

        verify(teamRepository, never()).save(any());
        verify(coachRepository).deleteById(1L);
    }

    @Test
    void delete_WhenNotFound_ShouldThrowRuntimeException() {
        when(coachRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> coachService.delete(99L));
        assertEquals("Coach not found", exception.getMessage());
    }

    @Test
    void update_WithNewTeam_ShouldUpdateAndReassign() {
        CoachRequestDto updateDto = new CoachRequestDto();
        updateDto.setName("Jane");
        updateDto.setSurname("Smith");
        updateDto.setAge(40);
        updateDto.setTactic("Defensive");
        updateDto.setTeamId(2L);

        team.setCoach(coach);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(newTeam));
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        coachService.update(1L, updateDto);

        assertEquals("Jane", coach.getName());
        assertEquals("Smith", coach.getSurname());
        assertEquals(40, coach.getAge());
        assertEquals("Defensive", coach.getTactic());
        assertEquals(newTeam, coach.getTeam());
        assertNull(team.getCoach());
        verify(teamRepository).save(team);
        verify(teamRepository).save(newTeam);
    }

    @Test
    void update_SameTeam_ShouldNotReassign() {
        CoachRequestDto updateDto = new CoachRequestDto();
        updateDto.setName("Jane");
        updateDto.setSurname("Smith");
        updateDto.setAge(40);
        updateDto.setTactic("Defensive");
        updateDto.setTeamId(1L);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        coachService.update(1L, updateDto);

        verify(teamRepository, never()).save(any());
    }

    @Test
    void update_TeamIdNull_WithExistingTeam_ShouldRemoveTeam() {
        CoachRequestDto updateDto = new CoachRequestDto();
        updateDto.setName("Jane");
        updateDto.setSurname("Smith");
        updateDto.setAge(null);
        updateDto.setTactic(null);
        updateDto.setTeamId(null);

        team.setCoach(coach);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        coachService.update(1L, updateDto);

        assertNull(coach.getTeam());
        assertNull(team.getCoach());
        verify(teamRepository).save(team);
    }

    @Test
    void update_TeamIdNull_WithoutExistingTeam_ShouldNotFail() {
        coach.setTeam(null);

        CoachRequestDto updateDto = new CoachRequestDto();
        updateDto.setName("Jane");
        updateDto.setSurname("Smith");
        updateDto.setAge(null);
        updateDto.setTactic(null);
        updateDto.setTeamId(null);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        coachService.update(1L, updateDto);

        verify(teamRepository, never()).save(any());
    }

    @Test
    void update_WhenNotFound_ShouldThrowException() {
        when(coachRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> coachService.update(99L, requestDto));
    }

    @Test
    void patch_AllFields_ShouldUpdateAll() {
        CoachRequestDto patchDto = new CoachRequestDto();
        patchDto.setName("Jane");
        patchDto.setSurname("Smith");
        patchDto.setAge(40);
        patchDto.setTactic("Defensive");
        patchDto.setTeamId(2L);

        team.setCoach(coach);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(newTeam));
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        coachService.patch(1L, patchDto);

        assertEquals("Jane", coach.getName());
        assertEquals("Smith", coach.getSurname());
        assertEquals(40, coach.getAge());
        assertEquals("Defensive", coach.getTactic());
        assertEquals(newTeam, coach.getTeam());
    }

    @Test
    void patch_OnlyNameAndSurname_ShouldPartialUpdate() {
        CoachRequestDto patchDto = new CoachRequestDto();
        patchDto.setName("Jane");
        patchDto.setSurname("Smith");

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        coachService.patch(1L, patchDto);

        assertEquals("Jane", coach.getName());
        assertEquals("Smith", coach.getSurname());
        assertEquals(45, coach.getAge());
        assertEquals("Offensive", coach.getTactic());
    }

    @Test
    void patch_OnlyTeamId_ShouldReassignTeam() {
        CoachRequestDto patchDto = new CoachRequestDto();
        patchDto.setTeamId(2L);

        team.setCoach(coach);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(newTeam));
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        coachService.patch(1L, patchDto);

        assertEquals(newTeam, coach.getTeam());
        assertNull(team.getCoach());
    }

    @Test
    void patch_TeamIdSameAsCurrent_ShouldNotReassign() {
        CoachRequestDto patchDto = new CoachRequestDto();
        patchDto.setTeamId(1L);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        coachService.patch(1L, patchDto);

        verify(teamRepository, never()).findById(any());
        verify(teamRepository, never()).save(any());
    }

    @Test
    void patch_WhenNotFound_ShouldThrowException() {
        when(coachRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> coachService.patch(99L, requestDto));
    }

    @Test
    void update_CoachWithoutTeam_WithNewTeam_ShouldAssignTeam() {
        coach.setTeam(null);
        CoachRequestDto updateDto = new CoachRequestDto();
        updateDto.setName("Jane");
        updateDto.setSurname("Smith");
        updateDto.setAge(40);
        updateDto.setTactic("Defensive");
        updateDto.setTeamId(2L);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(newTeam));
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        coachService.update(1L, updateDto);

        assertEquals(newTeam, coach.getTeam());
        verify(teamRepository, never()).save(team);
        verify(teamRepository).save(newTeam);
    }

    @Test
    void patch_CoachWithoutTeam_WithNewTeam_ShouldAssignTeam() {
        coach.setTeam(null);
        CoachRequestDto patchDto = new CoachRequestDto();
        patchDto.setTeamId(2L);

        when(coachRepository.findById(1L)).thenReturn(Optional.of(coach));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(newTeam));
        when(coachRepository.save(coach)).thenReturn(coach);
        when(coachMapper.toResponseDto(coach)).thenReturn(responseDto);

        coachService.patch(1L, patchDto);

        assertEquals(newTeam, coach.getTeam());
        verify(teamRepository, never()).save(team);
        verify(teamRepository).save(newTeam);
    }
}