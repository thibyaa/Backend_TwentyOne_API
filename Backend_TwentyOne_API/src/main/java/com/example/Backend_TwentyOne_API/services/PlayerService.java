package com.example.Backend_TwentyOne_API.services;

import com.example.Backend_TwentyOne_API.models.Game;
import com.example.Backend_TwentyOne_API.models.LoserBoardPlayer;
import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.repositories.GameRepository;
import com.example.Backend_TwentyOne_API.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

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

//      method to return table of all players, in order of games lost (ignoring the players.games data)
    public List<LoserBoardPlayer> getLoserBoard() {

//        presorts list of players by games lost
        List<Player> playerList = playerRepository.findAll(Sort.by(Sort.Direction.DESC, "gamesLost"));

//        creates list of LoserBoardPlayer DTO (all player info except the details of the games they have)
        List<LoserBoardPlayer> loserBoardPlayerList = new ArrayList<>();

//        loops through players, creating LoserBoardPlayer DTOs from them, and adds to list
        for(Player player : playerList){
            LoserBoardPlayer loserBoardPlayer = new LoserBoardPlayer(
                    player.getName(),
                    player.getId(),
                    player.getGamesLost()
            );
            loserBoardPlayerList.add(loserBoardPlayer);
        }
//        returns list
        return loserBoardPlayerList;
    }

//    method to delete player
    public void deletePlayer(long playerId) {
    Player player = playerRepository.findById(playerId).get();
    List<Game> gameList = player.getGames();


//    cycle through games in players game list, remove player from them before can delete layer
    for(Game game : gameList){
        game.removePlayer(player);
//      if no more players left on game, delete game as well
        if (game.getPlayers().size()==0){
            gameRepository.delete(game);
        }
        else { gameRepository.save(game);}
    }
    playerRepository.deleteById(playerId);
}

}
