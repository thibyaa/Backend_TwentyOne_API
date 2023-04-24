package models;

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

    @ManyToOne
    @Column(name = "lead_player_id")
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


    public Game(Player leadPlayer){
        this.currentTotal = 0;
        this.hasStarted = false;
        this.isComplete = false;
        this.leadPlayer = leadPlayer;
        this.players = new ArrayList<>();
        this.players.add(leadPlayer);
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
}
