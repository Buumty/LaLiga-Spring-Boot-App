package com.laliga.laliga_crud_app.entities.match;

import com.laliga.laliga_crud_app.entities.round.Round;
import com.laliga.laliga_crud_app.entities.team.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;

    @Test
    void givenMatchesFromRound_whenGetAllMatchesDtoByRound_shouldReturnDtosOnlyFromGivenRound() {
        // given

        Round round1 = new Round();
        round1.setNumber(1);

        Round round2 = new Round();
        round2.setNumber(2);

        Match match1 = new Match();
        match1.setId(1L);
        match1.setRound(round1);

        Match match2 = new Match();
        match2.setId(2L);
        match2.setRound(round2);

        Match match3 = new Match();
        match3.setId(3L);
        match3.setRound(round1);

        List<Match> matchesFromRepo = List.of(match1, match2, match3);

        when(matchRepository.findAll()).thenReturn(matchesFromRepo);

        // when

        MatchReadDto dto1 = new MatchReadDto(
                1L,
                "Barcelona",
                "Real Madrid",
                null
        );
        MatchReadDto dto3 = new MatchReadDto(
                3L,
                "Atletico",
                "Sevilla",
                null
        );

        // then

        try (MockedStatic<MatchMapper> mockedStatic = mockStatic(MatchMapper.class)){
            mockedStatic.when(() -> MatchMapper.toDto(match1)).thenReturn(dto1);
            mockedStatic.when(() -> MatchMapper.toDto(match3)).thenReturn(dto3);

            List<MatchReadDto> result = matchService.getAllMatchesDtoByRound(1);

            assertThat(result).containsExactly(dto1,dto3);
            verify(matchRepository).findAll();
            mockedStatic.verify(() -> MatchMapper.toDto(match1));
            mockedStatic.verify(() -> MatchMapper.toDto(match3));
            verifyNoMoreInteractions(matchRepository);
        }
    }
    @Test
    void givenNoMatches_whenGetAllMatchesDtoByRound_thenReturnEmptyList() {
        // given
        when(matchRepository.findAll()).thenReturn(List.of());

        // when
        List<MatchReadDto> result = matchService.getAllMatchesDtoByRound(1);

        // then
        assertThat(result).isEmpty();
        verify(matchRepository).findAll();
        verifyNoMoreInteractions(matchRepository);
    }

    @Test
    void givenMatchesFromDifferentRound_whenGetAllMatchesDtoByRound_thenReturnEmptyList() {
        // given

        Round round1 = new Round();
        round1.setNumber(1);

        Match match1 = new Match();
        match1.setId(1L);
        match1.setRound(round1);

        Match match2 = new Match();
        match2.setId(2L);
        match2.setRound(round1);

        Match match3 = new Match();
        match3.setId(3L);
        match3.setRound(round1);

        when(matchRepository.findAll()).thenReturn(List.of(match1, match2, match3));

        // when

        List<MatchReadDto> result = matchService.getAllMatchesDtoByRound(2);

        // then

        assertThat(result).isEmpty();
        verify(matchRepository).findAll();
        verifyNoMoreInteractions(matchRepository);
    }

    @Test
    void givenMatchesFromDifferentRounds_whenGetAllMatchesByRound_thenReturnMatchesFromGivenRound() {
        // given

        Round round1 = new Round();
        round1.setNumber(1);

        Round round2 = new Round();
        round2.setNumber(2);

        Match match1 = new Match();
        match1.setId(1L);
        match1.setRound(round1);

        Match match2 = new Match();
        match2.setId(2L);
        match2.setRound(round2);

        Match match3 = new Match();
        match3.setId(3L);
        match3.setRound(round2);

        List<Match> matchesFromRepo = List.of(match1, match2, match3);

        when(matchRepository.findAll()).thenReturn(matchesFromRepo);

        // when

        MatchReadDto dto1 = new MatchReadDto(
                1L,
                "Barcelona",
                "Real Madrid",
                null
        );
        MatchReadDto dto2 = new MatchReadDto(
                2L,
                "Espanyol",
                "Real Betis",
                null
        );
        MatchReadDto dto3 = new MatchReadDto(
                3L,
                "Atletico",
                "Sevilla",
                null
        );

        // then

        try (MockedStatic<MatchMapper> mockedStatic = mockStatic(MatchMapper.class)){
            mockedStatic.when(() -> MatchMapper.toDto(match1)).thenReturn(dto1);
            mockedStatic.when(() -> MatchMapper.toDto(match2)).thenReturn(dto2);
            mockedStatic.when(() -> MatchMapper.toDto(match3)).thenReturn(dto3);

            List<MatchReadDto> result = matchService.getAllMatchesDtoByRound(2);

            assertThat(result).containsExactly(dto2,dto3);
            verify(matchRepository).findAll();
            mockedStatic.verify(() -> MatchMapper.toDto(match2));
            mockedStatic.verify(() -> MatchMapper.toDto(match3));
            mockedStatic.verifyNoMoreInteractions();
        }
    }
    @Test
    void givenMatchesWithTeamAsHomeAndAway_whenGetAllMatchesByTeamName_thenReturnBothMatches()
    {
        // given
        Team barca = new Team();
        barca.setName("Barcelona");

        Team real = new Team();
        real.setName("Real Madrid");

        Team girona = new Team();
        girona.setName("Girona");

        Match match1 = new Match(); // Barcelona u siebie
        match1.setId(1L);
        match1.setHomeTeam(barca);
        match1.setAwayTeam(real);

        Match match2 = new Match(); // Barcelona na wyjeździe
        match2.setId(2L);
        match2.setHomeTeam(girona);
        match2.setAwayTeam(barca);

        Match match3 = new Match(); // brak Barcelony
        match3.setId(3L);
        match3.setHomeTeam(girona);
        match3.setAwayTeam(real);

        when(matchRepository.findAll()).thenReturn(List.of(match1, match2, match3));

        // when
        List<Match> result = matchService.getAllMatchesByTeamName("Barcelona");

        // then
        assertThat(result)
                .hasSize(2)
                .containsExactly(match1, match2); // kolejność powinna być taka jak w streamie

        verify(matchRepository).findAll();
        verifyNoMoreInteractions(matchRepository);
    }
    @Test
    void givenMatchesWithoutGivenTeam_whenGetAllMatchesByTeamName_thenReturnEmptyList() {
        // given

        Team real = new Team();
        real.setName("Real Madrid");

        Team girona = new Team();
        girona.setName("Girona");

        Match match1 = new Match();
        match1.setId(2L);
        match1.setHomeTeam(real);
        match1.setAwayTeam(girona);

        Match match2 = new Match();
        match2.setId(3L);
        match2.setHomeTeam(girona);
        match2.setAwayTeam(real);

        when(matchRepository.findAll()).thenReturn(List.of(match1,match2));

        // when
        List<Match> result = matchService.getAllMatchesByTeamName("Barcelona");

        // then
        assertThat(result).isEmpty();
        verify(matchRepository).findAll();
    }
    @Test
    void givenNoMatches_whenGetAllMatchesByTeamName_thenReturnEmptyList() {
        // given

        when(matchRepository.findAll()).thenReturn(List.of());

        // when
        List<Match> result = matchService.getAllMatchesByTeamName("Barcelona");

        // then
        assertThat(result).isEmpty();
        verify(matchRepository).findAll();
    }
}
