package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.StatisticRequestDto;
import com.savaleox.hockeyteam.dto.StatisticResponseDto;
import com.savaleox.hockeyteam.mapper.StatisticMapper;
import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.entity.Statistic;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.repository.PlayerRepository;
import com.savaleox.hockeyteam.repository.StatisticRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticServiceTest {

    @Mock
    private StatisticRepository statisticRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private StatisticMapper statisticMapper;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private StatisticService statisticService;

    private Player player;
    private Statistic statistic;
    private StatisticRequestDto requestDto;
    private StatisticResponseDto responseDto;

    @BeforeEach
    void setUp() {
        Team team = new Team();
        team.setId(1L);
        team.setName("Team A");

        player = new Player();
        player.setId(1L);
        player.setName("John");
        player.setSurname("Doe");
        player.setGoals(50);
        player.setAssists(60);
        player.setTeam(team);

        statistic = new Statistic();
        statistic.setId(1L);
        statistic.setSeason(2023);
        statistic.setGoals(20);
        statistic.setAssists(30);
        statistic.setGames(82);
        statistic.setPlayer(player);

        requestDto = new StatisticRequestDto();
        requestDto.setPlayerId(1L);
        requestDto.setSeason(2023);
        requestDto.setGoals(20);
        requestDto.setAssists(30);
        requestDto.setGames(82);

        responseDto = new StatisticResponseDto();
        responseDto.setId(1L);
        responseDto.setPlayerId(1L);
        responseDto.setSeason(2023);
        responseDto.setGoals(20);
        responseDto.setAssists(30);
        responseDto.setGames(82);
    }

    @Test
    void getByPlayer_ShouldReturnList() {
        when(statisticRepository.findByPlayerId(1L)).thenReturn(List.of(statistic));
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        List<StatisticResponseDto> result = statisticService.getByPlayer(1L);

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void getByPlayer_EmptyList() {
        when(statisticRepository.findByPlayerId(99L)).thenReturn(List.of());

        List<StatisticResponseDto> result = statisticService.getByPlayer(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getByPlayerAndSeason_ShouldReturnList() {
        when(statisticRepository.findByPlayerIdAndSeason(1L, 2023))
                .thenReturn(List.of(statistic));
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        List<StatisticResponseDto> result = statisticService.getByPlayerAndSeason(1L, 2023);

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void getByPlayerAndSeason_NoResults() {
        when(statisticRepository.findByPlayerIdAndSeason(1L, 2020)).thenReturn(List.of());

        List<StatisticResponseDto> result = statisticService.getByPlayerAndSeason(1L, 2020);

        assertTrue(result.isEmpty());
    }

    @Test
    void create_ShouldCreateStatisticAndUpdatePlayerTotals() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(statisticMapper.toEntity(requestDto)).thenReturn(statistic);
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        StatisticResponseDto result = statisticService.create(requestDto);

        assertEquals(responseDto, result);
        assertEquals(70, player.getGoals());
        assertEquals(90, player.getAssists());
        verify(playerRepository).save(player);
        verify(playerService).invalidateSearchCache();
    }

    @Test
    void create_PlayerNotFound_ShouldThrowException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> statisticService.create(requestDto));
    }

    @Test
    void create_NegativeGoals_ShouldThrowException() {
        requestDto.setGoals(-1);
        requestDto.setAssists(10);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(statisticMapper.toEntity(requestDto)).thenReturn(statistic);
        when(statisticRepository.save(statistic)).thenReturn(statistic);

        assertThrows(IllegalArgumentException.class, () -> statisticService.create(requestDto));
        assertEquals("Goals and assists cannot be negative",
                assertThrows(IllegalArgumentException.class, () -> statisticService.create(requestDto)).getMessage());
    }

    @Test
    void create_NegativeAssists_ShouldThrowException() {
        requestDto.setGoals(10);
        requestDto.setAssists(-1);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(statisticMapper.toEntity(requestDto)).thenReturn(statistic);
        when(statisticRepository.save(statistic)).thenReturn(statistic);

        assertThrows(IllegalArgumentException.class, () -> statisticService.create(requestDto));
    }

    @Test
    void create_ZeroValues_ShouldSucceed() {
        requestDto.setGoals(0);
        requestDto.setAssists(0);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(statisticMapper.toEntity(requestDto)).thenReturn(statistic);
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        StatisticResponseDto result = statisticService.create(requestDto);

        assertEquals(responseDto, result);
        assertEquals(50, player.getGoals());
        assertEquals(60, player.getAssists());
    }

    @Test
    void createWithoutTransactional_ShouldCreateAndUpdate() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(statisticMapper.toEntity(requestDto)).thenReturn(statistic);
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        StatisticResponseDto result = statisticService.createWithoutTransactional(requestDto);

        assertEquals(responseDto, result);
        assertEquals(70, player.getGoals());
        assertEquals(90, player.getAssists());
        verify(playerRepository).save(player);
        verify(playerService, never()).invalidateSearchCache();
    }

    @Test
    void createWithoutTransactional_PlayerNotFound_ShouldThrowException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> statisticService.createWithoutTransactional(requestDto));
    }

    @Test
    void createWithoutTransactional_BothNegative_ShouldThrowException() {
        requestDto.setGoals(-1);
        requestDto.setAssists(-1);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(statisticMapper.toEntity(requestDto)).thenReturn(statistic);
        when(statisticRepository.save(statistic)).thenReturn(statistic);

        assertThrows(IllegalArgumentException.class,
                () -> statisticService.createWithoutTransactional(requestDto));
    }

    @Test
    void createWithoutTransactional_OneNegative_ShouldNotThrowDueToBug() {

        requestDto.setGoals(-1);
        requestDto.setAssists(10);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(statisticMapper.toEntity(requestDto)).thenReturn(statistic);
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        StatisticResponseDto result = statisticService.createWithoutTransactional(requestDto);

        assertNotNull(result);
    }


    @Test
    void delete_ShouldDeleteAndUpdatePlayerTotals() {
        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        doNothing().when(statisticRepository).delete(statistic);

        statisticService.delete(1L);

        assertEquals(30, player.getGoals());
        assertEquals(30, player.getAssists());
        verify(playerRepository).save(player);
        verify(playerService).invalidateSearchCache();
        verify(statisticRepository).delete(statistic);
    }

    @Test
    void delete_NotFound_ShouldThrowException() {
        when(statisticRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> statisticService.delete(99L));
    }

    @Test
    void update_AllFieldsSamePlayer_ShouldUpdateStatistic() {
        StatisticRequestDto updateDto = new StatisticRequestDto();
        updateDto.setSeason(2024);
        updateDto.setGoals(30);
        updateDto.setAssists(40);
        updateDto.setGames(70);
        updateDto.setPlayerId(1L);

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        StatisticResponseDto result = statisticService.update(1L, updateDto);

        assertEquals(responseDto, result);
        assertEquals(60, player.getGoals());
        assertEquals(70, player.getAssists());
        assertEquals(2024, statistic.getSeason());
        assertEquals(70, statistic.getGames());
    }

    @Test
    void update_NewPlayer_ShouldTransferStats() {
        Player newPlayer = new Player();
        newPlayer.setId(2L);
        newPlayer.setName("Jane");
        newPlayer.setGoals(100);
        newPlayer.setAssists(200);

        StatisticRequestDto updateDto = new StatisticRequestDto();
        updateDto.setPlayerId(2L);
        updateDto.setGoals(30);
        updateDto.setAssists(40);
        updateDto.setSeason(2024);
        updateDto.setGames(70);

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(newPlayer));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        statisticService.update(1L, updateDto);

        assertEquals(30, player.getGoals());
        assertEquals(30, player.getAssists());
        assertEquals(130, newPlayer.getGoals());
        assertEquals(240, newPlayer.getAssists());
        verify(playerRepository, times(2)).save(any(Player.class));
    }

    @Test
    void update_PartialFields_ShouldUpdateOnlyProvided() {
        StatisticRequestDto updateDto = new StatisticRequestDto();
        updateDto.setGoals(25);

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        statisticService.update(1L, updateDto);

        assertEquals(55, player.getGoals());
        assertEquals(60, player.getAssists());
        assertEquals(2023, statistic.getSeason());
        assertEquals(82, statistic.getGames());
    }

    @Test
    void update_NullFields_ShouldKeepExisting() {
        StatisticRequestDto updateDto = new StatisticRequestDto();

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        statisticService.update(1L, updateDto);

        assertEquals(50, player.getGoals());
        assertEquals(60, player.getAssists());
    }

    @Test
    void update_NotFound_ShouldThrowException() {
        when(statisticRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> statisticService.update(99L, requestDto));
    }

    @Test
    void patch_AllFieldsSamePlayer_ShouldUpdateWithDelta() {
        StatisticRequestDto patchDto = new StatisticRequestDto();
        patchDto.setSeason(2024);
        patchDto.setGoals(25);
        patchDto.setAssists(35);
        patchDto.setGames(70);

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        StatisticResponseDto result = statisticService.patch(1L, patchDto);

        assertEquals(responseDto, result);
        assertEquals(55, player.getGoals());
        assertEquals(65, player.getAssists());
        assertEquals(2024, statistic.getSeason());
        assertEquals(70, statistic.getGames());
        verify(playerService).invalidateSearchCache();
    }

    @Test
    void patch_NewPlayer_ShouldTransferWithCorrectDelta() {
        Player newPlayer = new Player();
        newPlayer.setId(2L);
        newPlayer.setName("Jane");
        newPlayer.setGoals(100);
        newPlayer.setAssists(200);

        StatisticRequestDto patchDto = new StatisticRequestDto();
        patchDto.setPlayerId(2L);
        patchDto.setGoals(25);

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(newPlayer));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        statisticService.patch(1L, patchDto);

        assertEquals(30, player.getGoals());
        assertEquals(30, player.getAssists());
        assertEquals(125, newPlayer.getGoals());
        assertEquals(230, newPlayer.getAssists());
    }

    @Test
    void patch_OnlySeason_ShouldNotChangeTotals() {
        StatisticRequestDto patchDto = new StatisticRequestDto();
        patchDto.setSeason(2024);

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        statisticService.patch(1L, patchDto);

        assertEquals(50, player.getGoals());
        assertEquals(60, player.getAssists());
        assertEquals(2024, statistic.getSeason());
    }

    @Test
    void patch_OnlyGames_ShouldNotChangeTotals() {
        StatisticRequestDto patchDto = new StatisticRequestDto();
        patchDto.setGames(50);

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        statisticService.patch(1L, patchDto);

        assertEquals(50, player.getGoals());
        assertEquals(60, player.getAssists());
        assertEquals(50, statistic.getGames());
    }

    @Test
    void patch_NullFields_ShouldNotChangeAnything() {
        StatisticRequestDto patchDto = new StatisticRequestDto();

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        statisticService.patch(1L, patchDto);

        assertEquals(50, player.getGoals());
        assertEquals(60, player.getAssists());
        assertEquals(2023, statistic.getSeason());
        assertEquals(82, statistic.getGames());
    }

    @Test
    void patch_NotFound_ShouldThrowException() {
        when(statisticRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> statisticService.patch(99L, requestDto));
    }

    @Test
    void patch_SamePlayerId_ShouldUseElseBranch() {
        StatisticRequestDto patchDto = new StatisticRequestDto();
        patchDto.setPlayerId(1L);
        patchDto.setGoals(25);

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        statisticService.patch(1L, patchDto);

        assertEquals(55, player.getGoals());
        verify(playerRepository).save(player);
    }

    @Test
    void patch_DecreaseGoals_ShouldDecreaseTotals() {
        StatisticRequestDto patchDto = new StatisticRequestDto();
        patchDto.setGoals(10);

        when(statisticRepository.findById(1L)).thenReturn(Optional.of(statistic));
        when(statisticRepository.save(statistic)).thenReturn(statistic);
        when(statisticMapper.toResponseDto(statistic)).thenReturn(responseDto);

        statisticService.patch(1L, patchDto);

        assertEquals(40, player.getGoals());
        assertEquals(60, player.getAssists());
    }
}