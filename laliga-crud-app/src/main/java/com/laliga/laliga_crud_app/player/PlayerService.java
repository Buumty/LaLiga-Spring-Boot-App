package com.laliga.laliga_crud_app.player;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> findPlayerByName(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getName().equalsIgnoreCase(searchText))
                .collect(Collectors.toList());
    }

    public List<Player> findPlayersByTeam(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getTeam().equalsIgnoreCase(searchText))
                .collect(Collectors.toList());
    }

    public List<Player> findPlayersByPosition(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getPosition().equalsIgnoreCase(searchText))
                .collect(Collectors.toList());
    }

    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player updatePlayer(Player updatedPlayer) {
        Optional<Player> existingPlayer = playerRepository.findPlayerByName(updatedPlayer.getName());
        if (existingPlayer.isPresent()) {
            Player playerToUpdate = existingPlayer.get();
            playerToUpdate.setName(updatedPlayer.getName());
            playerToUpdate.setAge(updatedPlayer.getAge());
            playerToUpdate.setTeam(updatedPlayer.getTeam());
            playerToUpdate.setNumber(updatedPlayer.getNumber());
            playerToUpdate.setNation(updatedPlayer.getNation());

            playerRepository.save(playerToUpdate);
            return playerToUpdate;
        }
        return null;
    }

    @Transactional
    public void deletePlayerByName(String name) {
        playerRepository.deletePlayerByName(name);
    }
}
