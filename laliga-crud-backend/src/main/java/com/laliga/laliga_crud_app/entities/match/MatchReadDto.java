package com.laliga.laliga_crud_app.entities.match;

import java.time.LocalDateTime;

public record MatchReadDto(
        Long id,
        String homeTeamName,
        String awayTeamName,
        LocalDateTime matchDate
) {
}
