package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.PlayerRequestDto;
import com.savaleox.hockeyteam.dto.PlayerResponseDto;
import com.savaleox.hockeyteam.dto.TeamRequestDto;
import com.savaleox.hockeyteam.dto.TeamResponseDto;
import com.savaleox.hockeyteam.exceptions.PartialBulkCreationException;
import com.savaleox.hockeyteam.mapper.PlayerMapper;
import com.savaleox.hockeyteam.mapper.TeamMapper;
import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.model.enums.Position;
import com.savaleox.hockeyteam.repository.PlayerRepository;
import com.savaleox.hockeyteam.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private TeamService teamService;

    private Team team;
    private TeamRequestDto teamRequestDto;
    private TeamResponseDto teamResponseDto;
    private PlayerRequestDto playerRequestDto;
    private PlayerResponseDto playerResponseDto;
    private Player player;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(1L);
        team.setName("Bruins");
        team.setCity("Boston");
        team.setPlayers(new ArrayList<>());

        teamRequestDto = new TeamRequestDto();
        teamRequestDto.setName("Bruins");
        teamRequestDto.setCity("Boston");

        teamResponseDto = new TeamResponseDto();
        teamResponseDto.setId(1L);
        teamResponseDto.setName("Bruins");
        teamResponseDto.setCity("Boston");

        playerRequestDto = new PlayerRequestDto();
        playerRequestDto.setName("John");
        playerRequestDto.setSurname("Doe");
        playerRequestDto.setNumber(10);
        playerRequestDto.setAge(25);
        playerRequestDto.setPosition("FORWARD");
        playerRequestDto.setGoals(20);
        playerRequestDto.setAssists(30);
        playerRequestDto.setTeamId(1L);

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

        playerResponseDto = new PlayerResponseDto();
        playerResponseDto.setId(1L);
        playerResponseDto.setFullName("John");
        playerResponseDto.setNumber(10);
        playerResponseDto.setAge(25);
        playerResponseDto.setPositionName("FORWARD");
        playerResponseDto.setGoals(20);
        playerResponseDto.setAssists(30);
        playerResponseDto.setTeamName("geese");
    }

    @Test
    void getAll_ShouldReturnList() {
        when(teamRepository.findAll()).thenReturn(List.of(team));
        when(teamMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        List<TeamResponseDto> result = teamService.getAll();

        assertEquals(1, result.size());
        assertEquals(teamResponseDto, result.get(0));
    }

    @Test
    void getAll_EmptyList() {
        when(teamRepository.findAll()).thenReturn(List.of());

        List<TeamResponseDto> result = teamService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void getById_WhenExists_ShouldReturnDto() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        TeamResponseDto result = teamService.getById(1L);

        assertEquals(teamResponseDto, result);
    }

    @Test
    void getById_WhenNotFound_ShouldThrowNoSuchElementException() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> teamService.getById(99L));
        assertEquals("Team with id 99 not found", exception.getMessage());
    }

    @Test
    void create_ShouldCreateTeam() {
        when(teamMapper.toEntity(teamRequestDto)).thenReturn(team);
        when(teamRepository.save(team)).thenReturn(team);
        when(teamMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        TeamResponseDto result = teamService.create(teamRequestDto);

        assertEquals(teamResponseDto, result);
        verify(playerService).invalidateSearchCache();
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        doNothing().when(teamRepository).deleteById(1L);

        teamService.delete(1L);

        verify(playerService).invalidateSearchCache();
        verify(teamRepository).deleteById(1L);
    }

    @Test
    void delete_WhenNotFound_ShouldThrowException() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teamService.delete(99L));
    }

    @Test
    void update_ShouldUpdateAllFields() {
        TeamRequestDto updateDto = new TeamRequestDto();
        updateDto.setName("New Name");
        updateDto.setCity("New City");

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(team)).thenReturn(team);
        when(teamMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        TeamResponseDto result = teamService.update(1L, updateDto);

        assertEquals(teamResponseDto, result);
        assertEquals("New Name", team.getName());
        assertEquals("New City", team.getCity());
        verify(playerService).invalidateSearchCache();
    }

    @Test
    void update_WhenNotFound_ShouldThrowException() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teamService.update(99L, teamRequestDto));
    }

    @Test
    void patch_AllFields_ShouldUpdateAll() {
        TeamRequestDto patchDto = new TeamRequestDto();
        patchDto.setName("New Name");
        patchDto.setCity("New City");

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(team)).thenReturn(team);
        when(teamMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        TeamResponseDto result = teamService.patch(1L, patchDto);

        assertEquals(teamResponseDto, result);
        assertEquals("New Name", team.getName());
        assertEquals("New City", team.getCity());
        verify(playerService).invalidateSearchCache();
    }

    @Test
    void patch_OnlyName_ShouldUpdateOnlyName() {
        TeamRequestDto patchDto = new TeamRequestDto();
        patchDto.setName("New Name");

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(team)).thenReturn(team);
        when(teamMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        teamService.patch(1L, patchDto);

        assertEquals("New Name", team.getName());
        assertEquals("Boston", team.getCity());
    }

    @Test
    void patch_OnlyCity_ShouldUpdateOnlyCity() {
        TeamRequestDto patchDto = new TeamRequestDto();
        patchDto.setCity("New City");

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(team)).thenReturn(team);
        when(teamMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        teamService.patch(1L, patchDto);

        assertEquals("Bruins", team.getName());
        assertEquals("New City", team.getCity());
    }

    @Test
    void patch_NullFields_ShouldNotUpdate() {
        TeamRequestDto patchDto = new TeamRequestDto();

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(team)).thenReturn(team);
        when(teamMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        teamService.patch(1L, patchDto);

        assertEquals("Bruins", team.getName());
        assertEquals("Boston", team.getCity());
    }

    @Test
    void patch_WhenNotFound_ShouldThrowException() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teamService.patch(99L, teamRequestDto));
    }

    @Test
    void bulkCreatePlayers_ShouldCreateMultiplePlayers() {
        PlayerRequestDto player2Dto = createValidPlayerDto("Jane", "Smith", 11);
        List<PlayerRequestDto> players = List.of(playerRequestDto, player2Dto);

        Player player2 = createPlayer("Jane", "Smith", 11);
        List<Player> savedPlayers = List.of(player, player2);

        PlayerResponseDto player2Response = createResponseDto("Jane", "Smith", 11);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(playerRequestDto)).thenReturn(player);
        when(playerMapper.toEntity(player2Dto)).thenReturn(player2);
        when(playerRepository.saveAll(anyList())).thenReturn(savedPlayers);
        when(playerMapper.toResponseDto(player)).thenReturn(playerResponseDto);
        when(playerMapper.toResponseDto(player2)).thenReturn(player2Response);

        List<PlayerResponseDto> result = teamService.bulkCreatePlayers(1L, players);

        assertEquals(2, result.size());
        verify(playerService).invalidateSearchCache();
    }

    @Test
    void bulkCreatePlayers_EmptyList_ShouldThrowException() {
        List<PlayerRequestDto> emptyIds = List.of();

        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, emptyIds)
        );
    }

    @Test
    void bulkCreatePlayers_NullList_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, null));
    }

    @Test
    void bulkCreatePlayers_TeamNotFound_ShouldThrowException() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        List<PlayerRequestDto> players = List.of(playerRequestDto);

        assertThrows(NoSuchElementException.class,
                () -> teamService.bulkCreatePlayers(99L, players)
        );
    }

    @Test
    void bulkCreatePlayers_DuplicateNumberWithExisting_ShouldThrowException() {
        team.getPlayers().add(player);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        List<PlayerRequestDto> players = List.of(playerRequestDto);

        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players)
        );
    }

    @Test
    void bulkCreatePlayers_DuplicateNumberInBatch_ShouldThrowException() {
        PlayerRequestDto duplicateNumberDto = createValidPlayerDto("Jane", "Smith", 10);
        List<PlayerRequestDto> players = List.of(playerRequestDto, duplicateNumberDto);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(playerRequestDto)).thenReturn(player);

        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPlayerInput")
    void bulkCreatePlayers_InvalidInput_ShouldThrowException(String name, String surname, int number) {
        PlayerRequestDto invalidDto = createValidPlayerDto(name, surname, number);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(invalidDto);
        assertThrows(IllegalArgumentException.class, () -> teamService.bulkCreatePlayers(1L, players));
    }

    private static Stream<Arguments> provideInvalidPlayerInput() {
        return Stream.of(
                Arguments.of(null, "Smith", 11),
                Arguments.of("", "Smith", 11),
                Arguments.of("John", null, 11),
                Arguments.of("John", "Smith", 0),
                Arguments.of("John", "Smith", 100)
        );
    }

    @Test
    void bulkCreatePlayers_NullNumber_ShouldThrowException() {
        PlayerRequestDto invalidDto = createValidPlayerDto("John", "Smith", null);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(invalidDto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayers_InvalidAgeTooYoung_ShouldThrowException() {
        PlayerRequestDto invalidDto = createValidPlayerDto("John", "Smith", 11);
        invalidDto.setAge(15);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(invalidDto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayers_InvalidAgeTooOld_ShouldThrowException() {
        PlayerRequestDto invalidDto = createValidPlayerDto("John", "Smith", 11);
        invalidDto.setAge(51);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(invalidDto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayers_NullAge_ShouldThrowException() {
        PlayerRequestDto invalidDto = createValidPlayerDto("John", "Smith", 11);
        invalidDto.setAge(null);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(invalidDto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayers_InvalidPosition_ShouldThrowException() {
        PlayerRequestDto invalidDto = createValidPlayerDto("John", "Smith", 11);
        invalidDto.setPosition("INVALID");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(invalidDto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayers_NegativeGoals_ShouldThrowException() {
        PlayerRequestDto invalidDto = createValidPlayerDto("John", "Smith", 11);
        invalidDto.setGoals(-1);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(invalidDto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayers_NullGoals_ShouldThrowException() {
        PlayerRequestDto invalidDto = createValidPlayerDto("John", "Smith", 11);
        invalidDto.setGoals(null);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(invalidDto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayers_NegativeAssists_ShouldThrowException() {
        PlayerRequestDto invalidDto = createValidPlayerDto("John", "Smith", 11);
        invalidDto.setAssists(-1);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(invalidDto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayers_SinglePlayer_ShouldCreate() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(playerRequestDto)).thenReturn(player);
        when(playerRepository.saveAll(anyList())).thenReturn(List.of(player));
        when(playerMapper.toResponseDto(player)).thenReturn(playerResponseDto);

        List<PlayerResponseDto> result = teamService.bulkCreatePlayers(1L, List.of(playerRequestDto));

        assertEquals(1, result.size());
        assertEquals(playerResponseDto, result.get(0));
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_ShouldCreateAllPlayers() {
        PlayerRequestDto player2Dto = createValidPlayerDto("Jane", "Smith", 11);
        List<PlayerRequestDto> players = List.of(playerRequestDto, player2Dto);

        Player player2 = createPlayer("Jane", "Smith", 11);
        PlayerResponseDto player2Response = createResponseDto("Jane", "Smith", 11);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(playerRequestDto)).thenReturn(player);
        when(playerMapper.toEntity(player2Dto)).thenReturn(player2);
        when(playerRepository.save(player)).thenReturn(player);
        when(playerRepository.save(player2)).thenReturn(player2);
        when(playerMapper.toResponseDto(player)).thenReturn(playerResponseDto);
        when(playerMapper.toResponseDto(player2)).thenReturn(player2Response);

        List<PlayerResponseDto> result = teamService.bulkCreatePlayersWithoutTransaction(1L, players);

        assertEquals(2, result.size());
        verify(playerService).invalidateSearchCache();
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_PartialFailure_ShouldThrowException() {
        PlayerRequestDto validDto = createValidPlayerDto("Jane", "Smith", 11);
        PlayerRequestDto invalidDto = createValidPlayerDto(null, "Doe", 10);
        List<PlayerRequestDto> players = List.of(validDto, invalidDto);
        Player validPlayer = createPlayer("Jane", "Smith", 11);
        PlayerResponseDto validResponse = createResponseDto("Jane", "Smith", 11);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(validDto)).thenReturn(validPlayer);
        when(playerRepository.save(validPlayer)).thenReturn(validPlayer);
        when(playerMapper.toResponseDto(validPlayer)).thenReturn(validResponse);
        PartialBulkCreationException exception = assertThrows(PartialBulkCreationException.class,
                () -> teamService.bulkCreatePlayersWithoutTransaction(1L, players));
        assertEquals(1, exception.getSuccessCount());
        assertEquals(1, exception.getFailureCount());
        assertTrue(exception.getFailures().containsKey(1));
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_AllFailed_ShouldThrowException() {
        PlayerRequestDto invalidDto1 = createValidPlayerDto(null, "Smith", 11);
        PlayerRequestDto invalidDto2 = createValidPlayerDto("Jane", null, 12);
        List<PlayerRequestDto> players =

                List.of(invalidDto1, invalidDto2);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        PartialBulkCreationException exception = assertThrows(PartialBulkCreationException.class,
                () -> teamService.bulkCreatePlayersWithoutTransaction(1L, players));
        assertEquals(0, exception.getSuccessCount());
        assertEquals(2, exception.getFailureCount());
        assertEquals(0, exception.getCreatedPlayers());
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_NullList_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayersWithoutTransaction(1L, null));
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_EmptyList_ShouldThrowException() {
        List<PlayerRequestDto> emptyList = List.of();
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayersWithoutTransaction(1L, emptyList));
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_TeamNotFound_ShouldThrowException() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());
        List<PlayerRequestDto> players = List.of(playerRequestDto);
        assertThrows(NoSuchElementException.class,
                () -> teamService.bulkCreatePlayersWithoutTransaction(99L, players));
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_DuplicateWithExisting_ShouldBeCaught() {
        team.getPlayers().add(player);

        PlayerRequestDto newDto = createValidPlayerDto("Jane", "Smith", 11);
        List<PlayerRequestDto> players = List.of(newDto);

        Player newPlayer = createPlayer("Jane", "Smith", 11);
        PlayerResponseDto newResponse = createResponseDto("Jane", "Smith", 11);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(newDto)).thenReturn(newPlayer);
        when(playerRepository.save(newPlayer)).thenReturn(newPlayer);
        when(playerMapper.toResponseDto(newPlayer)).thenReturn(newResponse);

        List<PlayerResponseDto> result = teamService.bulkCreatePlayersWithoutTransaction(1L, players);

        assertEquals(1, result.size());
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_DuplicateInBatch_ShouldBeCaught() {
        PlayerRequestDto dto1 = createValidPlayerDto("John", "Doe", 10);
        PlayerRequestDto dto2 = createValidPlayerDto("Jane", "Smith", 10);
        List<PlayerRequestDto> players = List.of(dto1, dto2);

        Player player1 = createPlayer("John", "Doe", 10);
        PlayerResponseDto response1 = createResponseDto("John", "Doe", 10);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(dto1)).thenReturn(player1);
        when(playerRepository.save(player1)).thenReturn(player1);
        when(playerMapper.toResponseDto(player1)).thenReturn(response1);

        PartialBulkCreationException exception = assertThrows(PartialBulkCreationException.class,
                () -> teamService.bulkCreatePlayersWithoutTransaction(1L, players));

        assertEquals(1, exception.getSuccessCount());
        assertEquals(1, exception.getFailureCount());
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_ExceptionWithoutMessage_ShouldUseClassName() {
        PlayerRequestDto dto = createValidPlayerDto("John", "Doe", 10);
        List<PlayerRequestDto> players = List.of(dto);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(playerMapper.toEntity(dto)).thenThrow(new NullPointerException());

        PartialBulkCreationException exception = assertThrows(PartialBulkCreationException.class,
                () -> teamService.bulkCreatePlayersWithoutTransaction(1L, players));

        assertTrue(exception.getFailures().get(0).contains("NullPointerException"));
    }

    private PlayerRequestDto createValidPlayerDto(String name, String surname, Integer number) {
        PlayerRequestDto dto = new PlayerRequestDto();
        dto.setName(name);
        dto.setSurname(surname);
        dto.setNumber(number);
        dto.setAge(25);
        dto.setPosition("FORWARD");
        dto.setGoals(20);
        dto.setAssists(30);
        dto.setTeamId(1L);
        return dto;
    }

    private Player createPlayer(String name, String surname, Integer number) {
        Player p = new Player();
        p.setId(number.longValue());
        p.setName(name);
        p.setSurname(surname);
        p.setNumber(number);
        p.setAge(25);
        p.setPosition(Position.FORWARD);
        p.setGoals(20);
        p.setAssists(30);
        p.setTeam(team);
        return p;
    }

    private PlayerResponseDto createResponseDto(String name, String surname, Integer number) {
        PlayerResponseDto dto = new PlayerResponseDto();
        dto.setId(number.longValue());
        dto.setFullName(name + surname);
        dto.setNumber(number);
        dto.setAge(25);
        dto.setPositionName("FORWARD");
        dto.setGoals(20);
        dto.setAssists(30);
        dto.setTeamName("geese");
        return dto;
    }

    @Test
    void bulkCreatePlayers_NonEmptyList_ShouldNotThrowOnOptionalCheck() {
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());
        List<PlayerRequestDto> players = List.of(playerRequestDto);
        assertThrows(NoSuchElementException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
        verifyNoInteractions(playerMapper);
    }

    @Test
    void bulkCreatePlayers_NullGoals_ShouldThrowIllegalArgumentException() {
        PlayerRequestDto dto = createValidPlayerDto("John", "Smith", 11);
        dto.setGoals(null);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(dto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayers_NullAssists_ShouldThrowIllegalArgumentException() {
        PlayerRequestDto dto = createValidPlayerDto("John", "Smith", 11);
        dto.setAssists(null);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(dto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_NullGoals_ShouldThrowPartialBulkException() {
        PlayerRequestDto dto = createValidPlayerDto("John", "Doe", 10);
        dto.setGoals(null);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(dto);
        PartialBulkCreationException exception = assertThrows(PartialBulkCreationException.class,
                () -> teamService.bulkCreatePlayersWithoutTransaction(1L, players));
        assertEquals(0, exception.getSuccessCount());
        assertEquals(1, exception.getFailureCount());
        assertTrue(exception.getFailures().get(0).contains("Goals cannot be negative"));
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_NegativeAssists_ShouldThrowPartialBulkException() {
        PlayerRequestDto dto = createValidPlayerDto("John", "Doe", 10);
        dto.setAssists(-5);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(dto);
        PartialBulkCreationException exception = assertThrows(PartialBulkCreationException.class,
                () -> teamService.bulkCreatePlayersWithoutTransaction(1L, players));
        assertEquals(0, exception.getSuccessCount());
        assertEquals(1, exception.getFailureCount());
        assertTrue(exception.getFailures().get(0).contains("Assists cannot be negative"));
    }

    @Test
    void bulkCreatePlayers_BlankSurname_ShouldThrowException() {
        PlayerRequestDto dto = createValidPlayerDto("John", "   ", 11);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(dto);
        assertThrows(IllegalArgumentException.class,
                () -> teamService.bulkCreatePlayers(1L, players));
    }

    @Test
    void bulkCreatePlayersWithoutTransaction_BlankSurname_ShouldThrowPartialBulkException() {
        PlayerRequestDto dto = createValidPlayerDto("John", "", 10);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        List<PlayerRequestDto> players = List.of(dto);
        PartialBulkCreationException exception = assertThrows(PartialBulkCreationException.class,
                () -> teamService.bulkCreatePlayersWithoutTransaction(1L, players));
        assertEquals(0, exception.getSuccessCount());
        assertEquals(1, exception.getFailureCount());
        assertTrue(exception.getFailures().get(0).contains("Player surname is required"));
    }
}