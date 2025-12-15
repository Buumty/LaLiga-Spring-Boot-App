package com.laliga.laliga_crud_app.clients.table;

public record LeagueTableRowDto(
        int position,
        String teamName,
        String shortCode,
        String logoUrl,
        int played,
        int goalsFor,
        int goalsAgainst,
        int goalsDiff,
        int points
) {
}
