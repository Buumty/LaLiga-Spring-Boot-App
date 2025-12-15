package com.laliga.laliga_crud_app.clients.table.records;

public record ApiFootballStanding(
        int rank,
        ApiFootballTeam team,
        int points,
        int goalsDiff,
        ApiFootballAllStats all
) {
}
