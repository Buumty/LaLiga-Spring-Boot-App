package com.laliga.laliga_crud_app.entities.team;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class TeamService {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }
    public Team findTeamByName(String name) {
        return teamRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }
}
