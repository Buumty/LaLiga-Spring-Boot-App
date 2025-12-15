package com.laliga.laliga_crud_app.clients.table.records;

import java.util.List;

public record ApiFootballLeague(
        List<List<ApiFootballStanding>> standings
) {
}
