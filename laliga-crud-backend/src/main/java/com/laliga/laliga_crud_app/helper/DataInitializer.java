package com.laliga.laliga_crud_app.helper;

import com.laliga.laliga_crud_app.entities.match.Match;
import com.laliga.laliga_crud_app.entities.match.MatchRepository;
import com.laliga.laliga_crud_app.entities.round.Round;
import com.laliga.laliga_crud_app.entities.round.RoundRepository;
import com.laliga.laliga_crud_app.entities.team.Team;
import com.laliga.laliga_crud_app.entities.team.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Component
public class DataInitializer implements CommandLineRunner {
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final RoundRepository roundRepository;

    public DataInitializer(TeamRepository teamRepository, MatchRepository matchRepository, RoundRepository roundRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.roundRepository = roundRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (teamRepository.count() ==  0) {
            List<Team> teams = List.of(
                    new Team("Real Madrid"),
                    new Team("FC Barcelona"),
                    new Team("Atlético de Madrid"),
                    new Team("Athletic Club"),
                    new Team("Villarreal CF"),
                    new Team("Real Sociedad"),
                    new Team("Real Betis"),
                    new Team("Valencia CF"),
                    new Team("Girona FC"),
                    new Team("Celta de Vigo"),
                    new Team("Sevilla FC"),
                    new Team("RCD Espanyol"),
                    new Team("CA Osasuna"),
                    new Team("Rayo Vallecano"),
                    new Team("Levante UD"),
                    new Team("RCD Mallorca"),
                    new Team("Elche CF"),
                    new Team("Deportivo Alavés"),
                    new Team("Getafe CF"),
                    new Team("Real Oviedo")
            );

            for (Team t : teams) {
                if (!teamRepository.existsByName(t.getName())) {
                    teamRepository.save(t);
                }
            }
        }
        if (matchRepository.count() == 0) {
            Round round1 = roundRepository.findByNumber(1).orElseGet(() -> roundRepository.save(new Round()));
            Function<String, Team> T = name -> teamRepository.findByName(name)
                    .orElseThrow(() -> new IllegalStateException("Brak drużyny w bazie: " + name));

            List<Match> j1 = List.of(
                    // Pt 15.08.2025
                    new Match(T.apply("Girona FC"),        T.apply("Rayo Vallecano"),
                            LocalDateTime.of(2025, 8, 15, 17, 0), round1),
                    new Match(T.apply("Villarreal CF"),    T.apply("Real Oviedo"),
                            LocalDateTime.of(2025, 8, 15, 19, 30),round1),

                    // Sb 16.08.2025
                    new Match(T.apply("RCD Mallorca"),     T.apply("FC Barcelona"),
                            LocalDateTime.of(2025, 8, 16, 17, 30), round1),
                    new Match(T.apply("Deportivo Alavés"), T.apply("Levante UD"),
                            LocalDateTime.of(2025, 8, 16, 19, 30), round1),
                    new Match(T.apply("Valencia CF"),      T.apply("Real Sociedad"),
                            LocalDateTime.of(2025, 8, 16, 19, 30), round1),

                    // Nd 17.08.2025
                    new Match(T.apply("Celta de Vigo"),    T.apply("Getafe CF"),
                            LocalDateTime.of(2025, 8, 17, 15, 0), round1),
                    new Match(T.apply("Athletic Club"),    T.apply("Sevilla FC"),
                            LocalDateTime.of(2025, 8, 17, 17, 30), round1),
                    new Match(T.apply("RCD Espanyol"),     T.apply("Atlético de Madrid"),
                            LocalDateTime.of(2025, 8, 17, 19, 30), round1),

                    // Pon 18.08.2025
                    new Match(T.apply("Elche CF"),         T.apply("Real Betis"),
                            LocalDateTime.of(2025, 8, 18, 19, 0), round1),

                    // Wt 19.08.2025
                    new Match(T.apply("Real Madrid"),      T.apply("CA Osasuna"),
                            LocalDateTime.of(2025, 8, 19, 19, 0), round1)
            );

            round1.setNumber(1);
            round1.setMatches(j1);

            matchRepository.saveAll(j1);
        }
    }
}
