package com.laliga.laliga_crud_app.entities.match;

import com.laliga.laliga_crud_app.entities.round.Round;
import com.laliga.laliga_crud_app.entities.team.Team;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;
    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;
    private LocalDateTime matchDate;
    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    public Match(){}

    public Match(Team homeTeam, Team awayTeam, LocalDateTime matchDate, Round round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchDate = matchDate;
        this.round = round;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                ", matchDate=" + matchDate +
                '}';
    }
}
