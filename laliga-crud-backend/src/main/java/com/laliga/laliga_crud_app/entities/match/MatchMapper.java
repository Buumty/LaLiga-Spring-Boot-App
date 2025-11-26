package com.laliga.laliga_crud_app.entities.match;

import org.springframework.lang.NonNull;

public class MatchMapper {
    public static MatchReadDto toDto(@NonNull Match match) {
        return new MatchReadDto(
                match.getId(),
                match.getHomeTeam().getName(),
                match.getAwayTeam().getName(),
                match.getMatchDate()
        );
    }
}
