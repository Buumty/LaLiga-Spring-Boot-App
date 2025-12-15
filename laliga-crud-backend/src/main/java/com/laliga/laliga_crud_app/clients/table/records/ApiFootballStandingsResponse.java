package com.laliga.laliga_crud_app.clients.table.records;

import java.util.List;

public record ApiFootballStandingsResponse(
        List<ApiFootballResponseWrapper> response
) {
}
