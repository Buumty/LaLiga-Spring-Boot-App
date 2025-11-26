package com.laliga.laliga_crud_app.entities.team;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    void givenTeamsInRepository_whenFindAllTeams_thenReturnAllTeams() {
        // given
        Team barca = new Team();
        barca.setName("Barcelona");

        Team real = new Team();
        real.setName("Real Madrid");

        when(teamRepository.findAll()).thenReturn(List.of(barca, real));

        // when
        List<Team> result = teamService.findAllTeams();

        // then
        assertThat(result)
                .hasSize(2)
                .containsExactly(barca, real);

        verify(teamRepository).findAll();
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    void givenExistingTeam_whenFindTeamByName_thenReturnTeam() {
        // given
        String name = "Barcelona";
        Team barca = new Team();
        barca.setName(name);

        when(teamRepository.findByName(name)).thenReturn(Optional.of(barca));

        // when
        Team result = teamService.findTeamByName(name);

        // then
        assertThat(result).isSameAs(barca);

        verify(teamRepository).findByName(name);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    void givenNonExistingTeam_whenFindTeamByName_thenThrowEntityNotFoundException() {
        // given
        String name = "Girona";
        when(teamRepository.findByName(name)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> teamService.findTeamByName(name))
                .isInstanceOf(EntityNotFoundException.class);

        verify(teamRepository).findByName(name);
        verifyNoMoreInteractions(teamRepository);
    }
}
