package com.laliga.laliga_crud_app.player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "player_data")
public class Player {
    @Id
    @Column(name = "name")
    private String name;
    private String team;
    private Integer number;
    private String nation;
    private String position;
    private String age;
    private Integer minutesPlayed;
    private Integer goals;
    private Integer assists;
    private Integer penaltiesOnGoal;
    private Integer penaltiesScored;

    public Player() {
    }

    public Player(String name, String team, Integer number, String nation, String position, String age, Integer minutesPlayed, Integer goals, Integer assists, Integer penaltiesOnGoal, Integer penaltiesScored) {
        this.name = name;
        this.team = team;
        this.number = number;
        this.nation = nation;
        this.position = position;
        this.age = age;
        this.minutesPlayed = minutesPlayed;
        this.goals = goals;
        this.assists = assists;
        this.penaltiesOnGoal = penaltiesOnGoal;
        this.penaltiesScored = penaltiesScored;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getMinutesPlayed() {
        return minutesPlayed;
    }

    public void setMinutesPlayed(Integer minutesPlayed) {
        this.minutesPlayed = minutesPlayed;
    }

    public Integer getGoals() {
        return goals;
    }

    public void setGoals(Integer goals) {
        this.goals = goals;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getPenaltiesOnGoal() {
        return penaltiesOnGoal;
    }

    public void setPenaltiesOnGoal(Integer penaltiesOnGoal) {
        this.penaltiesOnGoal = penaltiesOnGoal;
    }

    public Integer getPenaltiesScored() {
        return penaltiesScored;
    }

    public void setPenaltiesScored(Integer penaltiesScored) {
        this.penaltiesScored = penaltiesScored;
    }
}
