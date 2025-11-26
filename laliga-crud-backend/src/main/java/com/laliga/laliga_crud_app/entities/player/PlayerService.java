package com.laliga.laliga_crud_app.entities.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Page<PlayerReadDto> getPlayers(
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        List<String> allowedSortFields = List.of("goals", "assists", "minutes", "playerName", "teamName");
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "goals";
        }

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page,size,Sort.by(direction, sortBy));

        return playerRepository.findAll(pageable)
                .map(PlayerMapper::toDto);
    }

    public List<PlayerReadDto> findPlayerByName(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getName().equalsIgnoreCase(searchText))
                .map(PlayerMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PlayerReadDto> findPlayersByTeam(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getTeam().equalsIgnoreCase(searchText))
                .map(PlayerMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<PlayerReadDto> findPlayersByPosition(String searchText) {
        return playerRepository.findAll().stream()
                .filter(player -> player.getPosition().equalsIgnoreCase(searchText))
                .map(PlayerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    @Transactional
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
