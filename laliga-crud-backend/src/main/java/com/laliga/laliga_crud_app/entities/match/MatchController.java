package com.laliga.laliga_crud_app.entities.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/api/v1/matches")
public class MatchController {
    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping(params = "roundNumber")
    public ResponseEntity<List<MatchReadDto>> getMatchesByRound(@RequestParam int roundNumber) {
        List<MatchReadDto> allMatchesByRound = matchService.getAllMatchesDtoByRound(roundNumber);
        return ResponseEntity.ok(allMatchesByRound);
    }

    @GetMapping(params = "team")
    public ResponseEntity<List<Match>> getMatchesByTeam(@RequestParam String team) {
        List<Match> allMatchesByTeamName = matchService.getAllMatchesByTeamName(team);
        return ResponseEntity.ok(allMatchesByTeamName);
    }
}
