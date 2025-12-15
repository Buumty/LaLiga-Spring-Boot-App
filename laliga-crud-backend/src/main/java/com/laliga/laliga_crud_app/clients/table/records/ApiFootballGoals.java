package com.laliga.laliga_crud_app.clients.table.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiFootballGoals(
        @JsonProperty("for") int goalsFor,
        @JsonProperty("against") int goalsAgainst
) {
}
