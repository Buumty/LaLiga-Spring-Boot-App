package com.laliga.laliga_crud_app.entities.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/api/v1/teams")
public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<Team>> getTeams() {
        List<Team> allTeams = teamService.findAllTeams();
        return ResponseEntity.ok(allTeams);
    }
    @GetMapping(params = "team")
    public ResponseEntity<Team> getTeamByName(@RequestParam String name) {
        Team teamByName = teamService.findTeamByName(name);
        return ResponseEntity.ok(teamByName);
    }
}
