package com.laliga.laliga_crud_app.entities.player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {
    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void givenValidSortField_whenGetPlayers_thenUseGivenSortField() {
        // given

        Player p1 = new Player();
        Player p2 = new Player();
        Page<Player> pageEntities = new PageImpl<>(List.of(p1,p2));

        when(playerRepository.findAll(any(Pageable.class))).thenReturn(pageEntities);

        PlayerReadDto dto1 = new PlayerReadDto("name1", "teamName1", 1, 2, 3);
        PlayerReadDto dto2 = new PlayerReadDto("name2", "teamName2", 3, 2, 1);

        try (MockedStatic<PlayerMapper> mockedStatic = mockStatic(PlayerMapper.class)){
            mockedStatic.when(() -> PlayerMapper.toDto(p1)).thenReturn(dto1);
            mockedStatic.when(() -> PlayerMapper.toDto(p2)).thenReturn(dto2);

            // when

            Page<PlayerReadDto> result = playerService.getPlayers(0,10,"goals", "asc");

            // then

            assertThat(result.getContent()).containsExactly(dto1,dto2);

            ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
            verify(playerRepository).findAll(captor.capture());

            Pageable value = captor.getValue();
            assertThat(value.getSort().getOrderFor("goals").getDirection()).isEqualTo(Sort.Direction.ASC);
        }

    }
    @Test
    void givenInvalidSortField_whenGetPlayers_thenFallbackToGoals() {
        // given

        when(playerRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        // when

        playerService.getPlayers(0, 10, "wrong", "asc");

        // then

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(playerRepository).findAll(captor.capture());

        Pageable pageable = captor.getValue();
        Sort.Order order = pageable.getSort().getOrderFor("goals");

        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.ASC);
    }
    @Test
    void givenSortDirDesc_whenGetPlayers_thenUseDescendingOrder() {
        // given

        when(playerRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        // when

        playerService.getPlayers(0, 10, "assists", "desc");

        // then

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(playerRepository).findAll(captor.capture());

        Pageable pageable = captor.getValue();
        Sort.Order order = pageable.getSort().getOrderFor("assists");

        assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);
    }
    @Test
    void givenUnknownSortDir_whenGetPlayers_thenFallbackToAscending() {
        // given
        when(playerRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        // when
        playerService.getPlayers(0, 10, "assists", "wrong");

        // then
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(playerRepository).findAll(captor.capture());

        Pageable pageable = captor.getValue();
        Sort.Order order = pageable.getSort().getOrderFor("assists");

        assertThat(order.getDirection()).isEqualTo(Sort.Direction.ASC);
    }
    @Test
    void givenPlayersWithDifferentNames_whenFindPlayerByName_thenReturnMatchingDtosIgnoringCase() {
        // given
        Player p1 = new Player();
        p1.setName("Robert Lewandowski");

        Player p2 = new Player();
        p2.setName("Pedri");

        Player p3 = new Player();
        p3.setName("Robert Lewandowski");

        when(playerRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        PlayerReadDto dto1 = new PlayerReadDto(
                "Robert Lewandowski",
                "Barcelona",
                20,
                5,
                1800
        );

        PlayerReadDto dto3 = new PlayerReadDto(
                "Robert Lewandowskii",
                "Barcelona",
                10,
                3,
                900
        );

        // when + then
        try (MockedStatic<PlayerMapper> mockedStatic = mockStatic(PlayerMapper.class)) {
            mockedStatic.when(() -> PlayerMapper.toDto(p1)).thenReturn(dto1);
            mockedStatic.when(() -> PlayerMapper.toDto(p3)).thenReturn(dto3);

            List<PlayerReadDto> result = playerService.findPlayerByName("Robert LEWANDOWSKI");

            assertThat(result).containsExactly(dto1, dto3);

            verify(playerRepository).findAll();
            verifyNoMoreInteractions(playerRepository);

            mockedStatic.verify(() -> PlayerMapper.toDto(p1));
            mockedStatic.verify(() -> PlayerMapper.toDto(p3));
            mockedStatic.verifyNoMoreInteractions();
        }
    }
    @Test
    void givenNoMatchingName_whenFindPlayerByName_thenReturnEmptyList() {
        // given
        Player p1 = new Player();
        p1.setName("Pedri");

        Player p2 = new Player();
        p2.setName("Gavi");

        when(playerRepository.findAll()).thenReturn(List.of(p1, p2));

        // when
        List<PlayerReadDto> result = playerService.findPlayerByName("Lewandowski");

        // then
        assertThat(result).isEmpty();
        verify(playerRepository).findAll();
        verifyNoMoreInteractions(playerRepository);
        // nie mockujemy PlayerMapper, bo map() nie będzie wywołane (brak matchy)
    }
    @Test
    void givenPlayersFromDifferentTeams_whenFindPlayersByTeam_thenReturnMatchingDtosIgnoringCase() {
        // given
        Player p1 = new Player();
        p1.setName("Lewandowski");
        p1.setTeam("Barcelona");

        Player p2 = new Player();
        p2.setName("Bellingham");
        p2.setTeam("Real Madrid");

        Player p3 = new Player();
        p3.setName("Pedri");
        p3.setTeam("barcelona"); // inna wielkość liter

        when(playerRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        PlayerReadDto dto1 = new PlayerReadDto(
                "Lewandowski",
                "Barcelona",
                20,
                5,
                1800
        );

        PlayerReadDto dto3 = new PlayerReadDto(
                "Pedri",
                "Barcelona",
                3,
                7,
                1500
        );

        try (MockedStatic<PlayerMapper> mockedStatic = mockStatic(PlayerMapper.class)) {
            mockedStatic.when(() -> PlayerMapper.toDto(p1)).thenReturn(dto1);
            mockedStatic.when(() -> PlayerMapper.toDto(p3)).thenReturn(dto3);

            // when
            List<PlayerReadDto> result = playerService.findPlayersByTeam("BARCELONA");

            // then
            assertThat(result).containsExactly(dto1, dto3);

            verify(playerRepository).findAll();
            verifyNoMoreInteractions(playerRepository);

            mockedStatic.verify(() -> PlayerMapper.toDto(p1));
            mockedStatic.verify(() -> PlayerMapper.toDto(p3));
            mockedStatic.verifyNoMoreInteractions();
        }
    }
    @Test
    void givenNoPlayersFromGivenTeam_whenFindPlayersByTeam_thenReturnEmptyList() {
        // given
        Player p1 = new Player();
        p1.setName("Lewandowski");
        p1.setTeam("Barcelona");

        Player p2 = new Player();
        p2.setName("Bellingham");
        p2.setTeam("Real Madrid");

        when(playerRepository.findAll()).thenReturn(List.of(p1, p2));

        // when
        List<PlayerReadDto> result = playerService.findPlayersByTeam("Girona");

        // then
        assertThat(result).isEmpty();
        verify(playerRepository).findAll();
        verifyNoMoreInteractions(playerRepository);
    }
    @Test
    void givenPlayer_whenAddPlayer_thenSaveAndReturnPlayer() {
        // given
        Player player = new Player();
        player.setName("Lewandowski");
        player.setAge("35");
        player.setTeam("Barcelona");
        player.setNumber(9);
        player.setNation("Poland");

        when(playerRepository.save(player)).thenReturn(player);

        // when
        Player result = playerService.addPlayer(player);

        // then
        assertThat(result).isEqualTo(player);

        verify(playerRepository).save(player);
        verifyNoMoreInteractions(playerRepository);
    }
    @Test
    void givenExistingPlayer_whenUpdatePlayer_thenUpdateFieldsAndSave() {
        // given
        Player existing = new Player();
        existing.setName("Lewandowski");
        existing.setAge("34");
        existing.setTeam("Bayern");
        existing.setNumber(9);
        existing.setNation("Poland");

        Player updated = new Player();
        updated.setName("Lewandowski"); // klucz do wyszukania
        updated.setAge("35");
        updated.setTeam("Barcelona");
        updated.setNumber(9);
        updated.setNation("Poland");

        when(playerRepository.findPlayerByName(updated.getName()))
                .thenReturn(Optional.of(existing));
        // save zwraca to, co dostał
        when(playerRepository.save(any(Player.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Player result = playerService.updatePlayer(updated);

        // then
        assertThat(result).isNotNull();
        // ważne: metoda zwraca istniejącego gracza po aktualizacji
        assertThat(result).isSameAs(existing);

        assertThat(result.getName()).isEqualTo(updated.getName());
        assertThat(result.getAge()).isEqualTo(updated.getAge());
        assertThat(result.getTeam()).isEqualTo(updated.getTeam());
        assertThat(result.getNumber()).isEqualTo(updated.getNumber());
        assertThat(result.getNation()).isEqualTo(updated.getNation());

        verify(playerRepository).findPlayerByName("Lewandowski");
        verify(playerRepository).save(existing);
        verifyNoMoreInteractions(playerRepository);
    }
    @Test
    void givenNonExistingPlayer_whenUpdatePlayer_thenReturnNullAndDoNotSave() {
        // given
        Player updated = new Player();
        updated.setName("Lewandowski");

        when(playerRepository.findPlayerByName(updated.getName()))
                .thenReturn(Optional.empty());

        // when
        Player result = playerService.updatePlayer(updated);

        // then
        assertThat(result).isNull();

        verify(playerRepository).findPlayerByName("Lewandowski");
        verify(playerRepository, never()).save(any(Player.class));
        verifyNoMoreInteractions(playerRepository);
    }
    @Test
    void givenPlayerName_whenDeletePlayerByName_thenRepositoryDeleteCalled() {
        // given
        String name = "Lewandowski";

        // when
        playerService.deletePlayerByName(name);

        // then
        verify(playerRepository).deletePlayerByName(name);
        verifyNoMoreInteractions(playerRepository);
    }
}
