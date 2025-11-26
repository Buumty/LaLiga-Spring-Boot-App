package com.laliga.laliga_crud_app.entities.round;

import com.laliga.laliga_crud_app.entities.match.Match;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;
    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();

    public Round() {}
    public Round(int number, List<Match> matches) {
        this.number = number;
        this.matches = matches;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @Override
    public String toString() {
        return "Round{" +
                "id=" + id +
                ", number=" + number +
                ", matches=" + matches +
                '}';
    }
}
