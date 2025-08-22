package com.laliga.laliga_crud_app.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    void deletePlayerByName(String name);
    Optional<Player> findPlayerByName(String name);
}
