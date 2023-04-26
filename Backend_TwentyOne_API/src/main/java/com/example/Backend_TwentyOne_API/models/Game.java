package com.example.Backend_TwentyOne_API.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "has_started")
    private Boolean hasStarted;

    @Column(name = "is_complete")
    private Boolean isComplete;

    @Column(name = "current_total")
    private int currentTotal;

    @Column(name = "current_player_id")
    private Long currentPlayerId;

    @Column(name = "game_type")
    private GameType gameType;


    @ManyToOne
    @JoinColumn(name = "lead_player_id")
    @JsonIgnoreProperties({"games"})
    private Player leadPlayer;

    @JsonIgnoreProperties({"games"})
    @ManyToMany
    @JoinTable(
            name = "games_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> players;


    public Game(Player leadPlayer, GameType gameType){
        this.currentTotal = 0;
        this.currentPlayerId = leadPlayer.getId();
        this.hasStarted = false;
        this.isComplete = false;
        this.leadPlayer = leadPlayer;
        this.players = new ArrayList<>();
        this.players.add(leadPlayer);
        this.gameType = gameType;

    }

    public Game(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(Boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public int getCurrentTotal() {
        return currentTotal;
    }

    public void setCurrentTotal(int currentTotal) {
        this.currentTotal = currentTotal;
    }

    public Long getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(Long currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public Player getLeadPlayer() {
        return leadPlayer;
    }

    public void setLeadPlayer(Player leadPlayer) {
        this.leadPlayer = leadPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }
    public void incrementCurrentTotal(int increment){
        this.currentTotal += increment;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void removePlayer(Player player) {

        this.players.remove(player);
    }
}
