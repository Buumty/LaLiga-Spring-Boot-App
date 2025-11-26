package com.laliga.laliga_crud_app.entities.player;

import org.springframework.lang.NonNull;

public class PlayerMapper {

    public static PlayerReadDto toDto(@NonNull Player player) {
        return new PlayerReadDto(
                player.getName(),
                player.getTeam(),
                player.getGoals(),
                player.getAssists(),
                player.getMinutesPlayed()
        );
    }
}
