package com.laliga.laliga_crud_app.entities.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/api/v1/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<Page<PlayerReadDto>> getPlayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "goals") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return new ResponseEntity<>(playerService.getPlayers(page,size,sortBy,sortDir), HttpStatus.OK);
    }

    @GetMapping(params = "team")
    public ResponseEntity<List<PlayerReadDto>> getPlayersByTeam(@RequestParam String team) {
        return new ResponseEntity<List<PlayerReadDto>>(playerService.findPlayersByTeam(team), HttpStatus.OK);
    }
    @GetMapping(params = "name")
    public ResponseEntity<List<PlayerReadDto>> getPlayersByName(@RequestParam String name) {
        return new ResponseEntity<List<PlayerReadDto>>(playerService.findPlayerByName(name), HttpStatus.OK);
    }
    @GetMapping(params = "position")
    public ResponseEntity<List<PlayerReadDto>> getPlayersByPosition(@RequestParam String position) {
        return new ResponseEntity<List<PlayerReadDto>>(playerService.findPlayersByPosition(position), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        Player addedPlayer = playerService.addPlayer(player);

        return new ResponseEntity<>(addedPlayer, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player) {
        Player updatedPlayer = playerService.updatePlayer(player);

        if (updatedPlayer != null) {
            return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{playerName}")
    public ResponseEntity<String> deletePlayer(@PathVariable String playerName) {
        playerService.deletePlayerByName(playerName);

        return new ResponseEntity<>("Player deleted successfully", HttpStatus.OK);
    }
}
