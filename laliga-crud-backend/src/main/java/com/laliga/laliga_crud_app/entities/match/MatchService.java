package com.laliga.laliga_crud_app.entities.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MatchService {
    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<MatchReadDto> getAllMatchesDtoByRound(int roundNumber) {
        return matchRepository.findAll().stream()
                .filter(m -> m.getRound().getNumber() == roundNumber)
                .map(MatchMapper::toDto)
                .toList();
    }

    public List<Match> getAllMatchesByTeamName(String teamName) {
        return matchRepository.findAll().stream()
                .filter(m -> m.getHomeTeam().getName().equals(teamName) || m.getAwayTeam().getName().equals(teamName))
                .toList();
    }
}
