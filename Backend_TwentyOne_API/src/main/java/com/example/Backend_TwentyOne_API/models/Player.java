package com.example.Backend_TwentyOne_API.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "games_lost")
    private int gamesLost;

    @JsonIgnoreProperties({"players"})
    @ManyToMany(mappedBy = "players")
//    @Transient
    private List<Game> games;

    public Player(String name){
        this.name = name;
        this.games = new ArrayList<>();
        this.gamesLost = 0;
    }

    public Player(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public void incrementGamesLost() {
        this.gamesLost += 1;
    }

    public void removeGame(Game game){
        this.games.remove(game);
    }
}
