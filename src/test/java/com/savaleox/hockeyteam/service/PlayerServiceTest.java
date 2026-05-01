package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.cache.SearchCacheKey;
import com.savaleox.hockeyteam.cache.SearchCacheManager;
import com.savaleox.hockeyteam.dto.AchievementResponseDto;
import com.savaleox.hockeyteam.dto.PlayerRequestDto;
import com.savaleox.hockeyteam.dto.PlayerResponseDto;
import com.savaleox.hockeyteam.dto.PlayerSearchCriteria;
import com.savaleox.hockeyteam.exceptions.ResourceNotFoundException;
import com.savaleox.hockeyteam.mapper.AchievementMapper;
import com.savaleox.hockeyteam.mapper.PlayerMapper;
import com.savaleox.hockeyteam.model.entity.Achievement;
import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.model.enums.Position;
import com.savaleox.hockeyteam.repository.AchievementRepository;
import com.savaleox.hockeyteam.repository.PlayerRepository;
import com.savaleox.hockeyteam.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private SearchCacheManager cacheManager;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private PlayerService playerService;

    private Player player;
    private Team team;
    private PlayerRequestDto requestDto;
    private PlayerResponseDto responseDto;
    private Achievement achievement;
    private AchievementResponseDto achievementResponseDto;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(1L);
        team.setName("Team A");

        player = new Player();
        player.setId(1L);
        player.setName("John");
        player.setSurname("Doe");
        player.setNumber(10);
        player.setAge(25);
        player.setPosition(Position.FORWARD);
        player.setGoals(20);
        player.setAssists(30);
        player.setTeam(team);
        player.setAchievements(new HashSet<>());

        requestDto = new PlayerRequestDto();
        requestDto.setName("John");
        requestDto.setSurname("Doe");
        requestDto.setNumber(10);
        requestDto.setAge(25);
        requestDto.setPosition("FORWARD");
        requestDto.setGoals(20);
        requestDto.setAssists(30);
        requestDto.setTeamId(1L);

        responseDto = new PlayerResponseDto();
        responseDto.setId(1L);
        responseDto.setFullName("John Doe");
        responseDto.setNumber(10);
        responseDto.setAge(25);
        responseDto.setPositionName("FORWARD");
        responseDto.setGoals(20);
        responseDto.setAssists(30);
        responseDto.setTeamName("geese");

        achievement = new Achievement();
        achievement.setId(1L);
        achievement.setName("MVP");
        achievement.setDescription("Most Valuable Player");
        achievement.setPlayers(new HashSet<>());

        achievementResponseDto = new AchievementResponseDto();
        achievementResponseDto.setId(1L);
        achievementResponseDto.setName("MVP");
        achievementResponseDto.setDescription("Most Valuable Player");
    }

    @Test
    void getAll_ShouldReturnList() {
        when(playerRepository.findAll()).thenReturn(List.of(player));
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        List<PlayerResponseDto> result = playerService.getAll();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }

    @Test
    void getAll_EmptyList() {
        when(playerRepository.findAll()).thenReturn(List.of());

        List<PlayerResponseDto> result = playerService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void getByTeam_ShouldReturnList() {
        when(playerRepository.findByTeamId(1L)).thenReturn(List.of(player));
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        List<PlayerResponseDto> result = playerService.getByTeam(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getByTeam_EmptyList() {
        when(playerRepository.findByTeamId(99L)).thenReturn(List.of());

        List<PlayerResponseDto> result = playerService.getByTeam(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getByPosition_ValidPosition_ShouldReturnList() {
        when(playerRepository.findByPosition(Position.FORWARD)).thenReturn(List.of(player));
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        List<PlayerResponseDto> result = playerService.getByPosition("FORWARD");

        assertEquals(1, result.size());
    }

    @Test
    void getByPosition_Goalkeeper_ShouldReturnList() {
        when(playerRepository.findByPosition(Position.GOALKEEPER)).thenReturn(List.of());

        List<PlayerResponseDto> result = playerService.getByPosition("GOALKEEPER");

        assertTrue(result.isEmpty());
    }

    @Test
    void getByPosition_Defender_ShouldReturnList() {
        when(playerRepository.findByPosition(Position.DEFENDER)).thenReturn(List.of());

        List<PlayerResponseDto> result = playerService.getByPosition("DEFENDER");

        assertTrue(result.isEmpty());
    }

    @Test
    void getByPosition_InvalidPosition_ShouldReturnEmptyList() {
        List<PlayerResponseDto> result = playerService.getByPosition("INVALID");

        assertTrue(result.isEmpty());
        verify(playerRepository, never()).findByPosition(any());
    }

    @Test
    void getByTeamAndPosition_ValidInput_ShouldReturnList() {
        when(playerRepository.findByTeamIdAndPosition(1L, Position.FORWARD))
                .thenReturn(List.of(player));
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        List<PlayerResponseDto> result = playerService.getByTeamAndPosition(1L, "FORWARD");

        assertEquals(1, result.size());
    }

    @Test
    void getByTeamAndPosition_InvalidPosition_ShouldReturnEmptyList() {
        List<PlayerResponseDto> result = playerService.getByTeamAndPosition(1L, "INVALID");

        assertTrue(result.isEmpty());
        verify(playerRepository, never()).findByTeamIdAndPosition(anyLong(), any());
    }

    @Test
    void getByMinGoals_ShouldReturnList() {
        when(playerRepository.findByMinGoals(10)).thenReturn(List.of(player));
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        List<PlayerResponseDto> result = playerService.getByMinGoals(10);

        assertEquals(1, result.size());
    }

    @Test
    void getByMinGoals_ZeroGoals_ShouldReturnAll() {
        when(playerRepository.findByMinGoals(0)).thenReturn(List.of(player));
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        List<PlayerResponseDto> result = playerService.getByMinGoals(0);

        assertEquals(1, result.size());
    }

    @Test
    void getById_WhenExists_ShouldReturnDto() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        PlayerResponseDto result = playerService.getById(1L);

        assertEquals(responseDto, result);
    }

    @Test
    void getById_WhenNotFound_ShouldThrowResourceNotFoundException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.getById(99L));
    }

    @Test
    void create_WithAllFields_ShouldCreatePlayer() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(requestDto)).thenReturn(player);
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        PlayerResponseDto result = playerService.create(requestDto);

        assertEquals(responseDto, result);
        assertEquals(team, player.getTeam());
        assertEquals(Position.FORWARD, player.getPosition());
        assertEquals(20, player.getGoals());
        assertEquals(30, player.getAssists());
        verify(cacheManager).invalidateAll();
    }

    @Test
    void create_WithoutPosition_ShouldNotSetPosition() {
        requestDto.setPosition(null);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(requestDto)).thenReturn(player);
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        playerService.create(requestDto);

        verify(playerRepository).save(player);
    }

    @Test
    void create_TeamNotFound_ShouldThrowException() {
        requestDto.setTeamId(99L);
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.create(requestDto));
    }

    @Test
    void delete_ShouldDeleteAndInvalidateCache() {
        doNothing().when(playerRepository).deleteById(1L);

        playerService.delete(1L);

        verify(playerRepository).deleteById(1L);
        verify(cacheManager).invalidateAll();
    }

    @Test
    void update_AllFields_ShouldUpdatePlayer() {
        PlayerRequestDto updateDto = new PlayerRequestDto();
        updateDto.setName("Jane");
        updateDto.setSurname("Smith");
        updateDto.setNumber(20);
        updateDto.setAge(30);
        updateDto.setGoals(40);
        updateDto.setAssists(50);
        updateDto.setTeamId(2L);
        updateDto.setPosition("DEFENDER");

        Team newTeam = new Team();
        newTeam.setId(2L);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(newTeam));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        playerService.update(1L, updateDto);

        assertEquals("Jane", player.getName());
        assertEquals("Smith", player.getSurname());
        assertEquals(20, player.getNumber());
        assertEquals(30, player.getAge());
        assertEquals(40, player.getGoals());
        assertEquals(50, player.getAssists());
        assertEquals(Position.DEFENDER, player.getPosition());
        assertEquals(newTeam, player.getTeam());
        verify(cacheManager).invalidateAll();
    }

    @Test
    void update_WithoutTeamId_ShouldNotChangeTeam() {
        PlayerRequestDto updateDto = new PlayerRequestDto();
        updateDto.setName("Jane");
        updateDto.setSurname("Smith");
        updateDto.setNumber(20);
        updateDto.setAge(30);
        updateDto.setGoals(40);
        updateDto.setAssists(50);
        updateDto.setTeamId(null);
        updateDto.setPosition("DEFENDER");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        playerService.update(1L, updateDto);

        assertEquals(team, player.getTeam());
    }

    @Test
    void update_TeamNotFound_ShouldThrowException() {
        PlayerRequestDto updateDto = new PlayerRequestDto();
        updateDto.setTeamId(99L);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playerService.update(1L, updateDto));
    }

    @Test
    void update_PlayerNotFound_ShouldThrowException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playerService.update(99L, requestDto));
    }

    @Test
    void update_NullPosition_ShouldNotSetPosition() {
        PlayerRequestDto updateDto = new PlayerRequestDto();
        updateDto.setName("Jane");
        updateDto.setSurname("Smith");
        updateDto.setNumber(20);
        updateDto.setAge(30);
        updateDto.setGoals(40);
        updateDto.setAssists(50);
        updateDto.setTeamId(null);
        updateDto.setPosition(null);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        playerService.update(1L, updateDto);

        assertEquals(Position.FORWARD, player.getPosition());
    }

    @Test
    void patch_AllFields_ShouldUpdateAll() {
        PlayerRequestDto patchDto = new PlayerRequestDto();
        patchDto.setName("Jane");
        patchDto.setSurname("Smith");
        patchDto.setNumber(20);
        patchDto.setAge(30);
        patchDto.setGoals(40);
        patchDto.setAssists(50);
        patchDto.setTeamId(2L);
        patchDto.setPosition("DEFENDER");

        Team newTeam = new Team();
        newTeam.setId(2L);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(2L)).thenReturn(Optional.of(newTeam));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        playerService.patch(1L, patchDto);

        assertEquals("Jane", player.getName());
        assertEquals("Smith", player.getSurname());
        assertEquals(20, player.getNumber());
        assertEquals(30, player.getAge());
        assertEquals(40, player.getGoals());
        assertEquals(50, player.getAssists());
        assertEquals(Position.DEFENDER, player.getPosition());
        assertEquals(newTeam, player.getTeam());
        verify(cacheManager).invalidateAll();
    }

    @Test
    void patch_OnlyName_ShouldUpdateOnlyName() {
        PlayerRequestDto patchDto = new PlayerRequestDto();
        patchDto.setName("Jane");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        playerService.patch(1L, patchDto);

        assertEquals("Jane", player.getName());
        assertEquals("Doe", player.getSurname());
        assertEquals(10, player.getNumber());
        assertEquals(25, player.getAge());
        assertEquals(20, player.getGoals());
        assertEquals(30, player.getAssists());
    }

    @Test
    void patch_NullTeamId_ShouldNotChangeTeam() {
        PlayerRequestDto patchDto = new PlayerRequestDto();
        patchDto.setName("Jane");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        playerService.patch(1L, patchDto);

        assertEquals(team, player.getTeam());
    }

    @Test
    void patch_TeamNotFound_ShouldThrowException() {
        PlayerRequestDto patchDto = new PlayerRequestDto();
        patchDto.setTeamId(99L);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playerService.patch(1L, patchDto));
    }

    @Test
    void patch_PlayerNotFound_ShouldThrowException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playerService.patch(99L, requestDto));
    }

    @Test
    void searchPlayersJPQL_CacheMiss_ShouldQueryAndCache() {
        PlayerSearchCriteria criteria = new PlayerSearchCriteria();
        Pageable pageable = PageRequest.of(0, 10);
        SearchCacheKey key = new SearchCacheKey(criteria, pageable);
        Page<Player> playerPage = new PageImpl<>(List.of(player));
        Page<PlayerResponseDto> dtoPage = new PageImpl<>(List.of(responseDto));

        when(cacheManager.get(key)).thenReturn(null);
        when(playerRepository.searchWithFiltersJPQL(criteria, pageable)).thenReturn(playerPage);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);
        doNothing().when(cacheManager).put(key, dtoPage);

        Page<PlayerResponseDto> result = playerService.searchPlayersJPQL(criteria, pageable);

        assertEquals(1, result.getTotalElements());
        verify(cacheManager).put(any(), any());
    }

    @Test
    void searchPlayersJPQL_CacheHit_ShouldReturnFromCache() {
        PlayerSearchCriteria criteria = new PlayerSearchCriteria();
        Pageable pageable = PageRequest.of(0, 10);
        SearchCacheKey key = new SearchCacheKey(criteria, pageable);
        Page<PlayerResponseDto> cachedPage = new PageImpl<>(List.of(responseDto));

        doReturn(cachedPage).when(cacheManager).get(key);

        Page<PlayerResponseDto> result = playerService.searchPlayersJPQL(criteria, pageable);

        assertEquals(cachedPage, result);
        verify(playerRepository, never()).searchWithFiltersJPQL(any(), any());
    }

    @Test
    void searchPlayersNative_CacheMiss_ShouldQueryAndCache() {
        PlayerSearchCriteria criteria = new PlayerSearchCriteria();
        Pageable pageable = PageRequest.of(0, 10);
        SearchCacheKey key = new SearchCacheKey(criteria, pageable);
        Page<Player> playerPage = new PageImpl<>(List.of(player));
        Page<PlayerResponseDto> dtoPage = new PageImpl<>(List.of(responseDto));

        when(cacheManager.get(key)).thenReturn(null);
        when(playerRepository.searchWithFiltersNative(criteria, pageable)).thenReturn(playerPage);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);
        doNothing().when(cacheManager).put(key, dtoPage);

        Page<PlayerResponseDto> result = playerService.searchPlayersNative(criteria, pageable);

        assertEquals(1, result.getTotalElements());
        verify(cacheManager).put(any(), any());
    }

    @Test
    void searchPlayersNative_CacheHit_ShouldReturnFromCache() {
        PlayerSearchCriteria criteria = new PlayerSearchCriteria();
        Pageable pageable = PageRequest.of(0, 10);
        SearchCacheKey key = new SearchCacheKey(criteria, pageable);

        Page<PlayerResponseDto> cachedPage = new PageImpl<>(List.of(responseDto));

        doReturn(cachedPage).when(cacheManager).get(key);

        Page<PlayerResponseDto> result = playerService.searchPlayersNative(criteria, pageable);

        assertEquals(cachedPage, result);
        verify(playerRepository, never()).searchWithFiltersNative(any(), any());
    }

    @Test
    void addAchievement_ShouldAddAchievement() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        PlayerResponseDto result = playerService.addAchievement(1L, 1L);

        assertEquals(responseDto, result);
        assertTrue(player.getAchievements().contains(achievement));
        assertTrue(achievement.getPlayers().contains(player));
        verify(cacheManager).invalidateAll();
    }

    @Test
    void addAchievement_PlayerNotFound_ShouldThrowException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> playerService.addAchievement(99L, 1L));
    }

    @Test
    void addAchievement_AchievementNotFound_ShouldThrowException() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(achievementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> playerService.addAchievement(1L, 99L));
    }

    @Test
    void addAchievement_AlreadyHasAchievement_ShouldThrowException() {
        player.getAchievements().add(achievement);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));

        assertThrows(IllegalStateException.class,
                () -> playerService.addAchievement(1L, 1L));
    }

    @Test
    void removeAchievement_ShouldRemoveAchievement() {
        player.getAchievements().add(achievement);
        achievement.getPlayers().add(player);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        PlayerResponseDto result = playerService.removeAchievement(1L, 1L);

        assertEquals(responseDto, result);
        assertFalse(player.getAchievements().contains(achievement));
        assertFalse(achievement.getPlayers().contains(player));
        verify(cacheManager).invalidateAll();
    }

    @Test
    void removeAchievement_PlayerNotFound_ShouldThrowException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> playerService.removeAchievement(99L, 1L));
    }

    @Test
    void removeAchievement_AchievementNotFound_ShouldThrowException() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(achievementRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> playerService.removeAchievement(1L, 99L));
    }

    @Test
    void removeAchievement_DoesNotHaveAchievement_ShouldThrowException() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));

        assertThrows(IllegalStateException.class,
                () -> playerService.removeAchievement(1L, 1L));
    }

    @Test
    void getPlayerAchievements_ShouldReturnList() {
        player.getAchievements().add(achievement);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(achievementMapper.toResponseDto(achievement)).thenReturn(achievementResponseDto);

        List<AchievementResponseDto> result = playerService.getPlayerAchievements(1L);

        assertEquals(1, result.size());
        assertEquals(achievementResponseDto, result.get(0));
    }

    @Test
    void getPlayerAchievements_PlayerNotFound_ShouldThrowException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> playerService.getPlayerAchievements(99L));
    }

    @Test
    void getPlayerAchievements_NoAchievements_ShouldReturnEmptyList() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        List<AchievementResponseDto> result = playerService.getPlayerAchievements(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void setAchievements_ShouldReplaceAllAchievements() {
        Achievement oldAchievement = new Achievement();
        oldAchievement.setId(2L);
        oldAchievement.setName("Old");
        oldAchievement.getPlayers().add(player);
        player.getAchievements().add(oldAchievement);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(achievementRepository.findAllById(List.of(1L))).thenReturn(List.of(achievement));
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        PlayerResponseDto result = playerService.setAchievements(1L, List.of(1L));

        assertEquals(responseDto, result);
        assertEquals(1, player.getAchievements().size());
        assertTrue(player.getAchievements().contains(achievement));
        assertFalse(oldAchievement.getPlayers().contains(player));
        assertTrue(achievement.getPlayers().contains(player));
        verify(cacheManager).invalidateAll();
    }

    @Test
    void setAchievements_SomeNotFound_ShouldThrowException() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(achievementRepository.findAllById(List.of(1L, 99L))).thenReturn(List.of(achievement));
        List<Long> achievementIds = List.of(1L, 99L);

        assertThrows(IllegalArgumentException.class,
                () -> playerService.setAchievements(1L, achievementIds)
        );
    }

    @Test
    void setAchievements_PlayerNotFound_ShouldThrowException() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        List<Long> achievementIds = List.of(1L);

        assertThrows(ResourceNotFoundException.class,
                () -> playerService.setAchievements(99L, achievementIds)
        );
    }

    @Test
    void setAchievements_EmptyList_ShouldClearAllAchievements() {
        player.getAchievements().add(achievement);
        achievement.getPlayers().add(player);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(achievementRepository.findAllById(List.of())).thenReturn(List.of());
        when(playerRepository.save(player)).thenReturn(player);
        when(playerMapper.toResponseDto(player)).thenReturn(responseDto);

        PlayerResponseDto result = playerService.setAchievements(1L, List.of());

        assertEquals(responseDto, result);
        assertTrue(player.getAchievements().isEmpty());
        assertFalse(achievement.getPlayers().contains(player));
    }
}