package com.laliga.laliga_crud_app.clients.table;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/table")
public class LaLigaTableController {

    private final ApiFootballStandingsService laLigaStandingsService;

    public LaLigaTableController(ApiFootballStandingsService laLigaStandingsService) {
        this.laLigaStandingsService = laLigaStandingsService;
    }

    @GetMapping
    public List<LeagueTableRowDto> getLaLigaTable() {
        return laLigaStandingsService.getLaLigaTable();
    }
}
