package com.example.Backend_TwentyOne_API.services;

import com.example.Backend_TwentyOne_API.models.Game;
import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.models.Reply;
import com.example.Backend_TwentyOne_API.repositories.GameRepository;
import com.example.Backend_TwentyOne_API.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

//    @Autowired
//    GameService gameService;

    @Autowired
    GameRepository gameRepository;

    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Long playerId) {
        return playerRepository.findById(playerId);
    }

    public String getPlayerNameByGame(Game game) {
        return getPlayerById(game.getCurrentPlayerId()).get().getName();
    }

    public Player savePlayer(Player player) {
        playerRepository.save(player);
        return player;
    }

    public String getPlayerNameById(Long playerId){
        Player player = getPlayerById(playerId).get();
        return player.getName();
    }

    public List<Player> getLoserBoard() {
        return playerRepository.findByGamesLostIgnoreGames();
    }

}
