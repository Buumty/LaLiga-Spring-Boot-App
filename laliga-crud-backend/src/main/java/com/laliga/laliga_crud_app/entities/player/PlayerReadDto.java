package com.laliga.laliga_crud_app.entities.player;

public record PlayerReadDto(
        String playerName,
        String teamName,
        int goals,
        int assists,
        int minutes) { }
