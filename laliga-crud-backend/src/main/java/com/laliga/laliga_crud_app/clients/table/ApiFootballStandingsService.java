package com.laliga.laliga_crud_app.clients.table;

import com.laliga.laliga_crud_app.clients.table.records.ApiFootballResponseWrapper;
import com.laliga.laliga_crud_app.clients.table.records.ApiFootballStanding;
import com.laliga.laliga_crud_app.clients.table.records.ApiFootballStandingsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.Comparator;
import java.util.List;

@Service
public class ApiFootballStandingsService {
    private final WebClient webClient;
    private final long laLigaSeasonId;
    private final long laLigaLeagueId;

    public ApiFootballStandingsService(WebClient webClient, @Value("${laliga.season}") long laLigaSeasonId,
                                       @Value("${laliga.league-id}") long laLigaLeagueId) {
        this.webClient = webClient;
        this.laLigaSeasonId = laLigaSeasonId;
        this.laLigaLeagueId = laLigaLeagueId;
    }

    public List<LeagueTableRowDto> getLaLigaTable() {
        ApiFootballStandingsResponse response;
        try {
            response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/standings")
                            .queryParam("league", laLigaLeagueId)
                            .queryParam("season", laLigaSeasonId)
                            .build())
                    .retrieve()
                    .bodyToMono(ApiFootballStandingsResponse.class)
                    .block();

        } catch (WebClientRequestException e) {
            System.err.println(e.getMessage());
            throw e;
        }

        if (response == null || response.response() == null || response.response().isEmpty()) {
            return List.of();
        }

        ApiFootballResponseWrapper wrapper = response.response().get(0);
        if (wrapper.league() == null ||
        wrapper.league().standings() == null ||
        wrapper.league().standings().isEmpty()) {
            return List.of();
        }

        List<ApiFootballStanding> standings = wrapper.league().standings().get(0);

        return standings.stream()
                .map(this::mapStandingToDto)
                .sorted(Comparator.comparingInt(LeagueTableRowDto::position))
                .toList();
    }

    private LeagueTableRowDto mapStandingToDto(ApiFootballStanding standingData) {
        int played = standingData.all() != null ? standingData.all().played() : 0;
        int goalsFor = 0;
        int goalsAgainst = 0;

        if (standingData.all() != null && standingData.all().goals() != null) {
            goalsFor = standingData.all().goals().goalsFor();
            goalsAgainst = standingData.all().goals().goalsAgainst();
        }

        int goalDiff = standingData.goalsDiff();

        return new LeagueTableRowDto(
                standingData.rank(),
                standingData.team() != null ? standingData.team().name() : "Unknown",
                null,
                standingData.team() != null ? standingData.team().logo() : null,
                played,
                goalsFor,
                goalsAgainst,
                goalDiff,
                standingData.points()
        );
    }
}
