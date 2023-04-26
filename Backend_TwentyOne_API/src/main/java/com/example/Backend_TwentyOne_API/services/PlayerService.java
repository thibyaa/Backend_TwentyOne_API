package com.example.Backend_TwentyOne_API.services;

import com.example.Backend_TwentyOne_API.models.Game;
import com.example.Backend_TwentyOne_API.models.LoserBoardPlayer;
import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.models.Reply;
import com.example.Backend_TwentyOne_API.repositories.GameRepository;
import com.example.Backend_TwentyOne_API.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // getAllPlayers
    // loopThrough list of players - for each one create DTO, which
    // name, loserboard playerslost
    // adds dto to a list, preset a list
    // return list ordered by gamesLost

    public List<LoserBoardPlayer> getLoserBoard() {
        List<Player> playerList = playerRepository.findAll(Sort.by(Sort.Direction.DESC, "gamesLost"));
        List<LoserBoardPlayer> loserBoardPlayerList = new ArrayList<>();
        for(Player player : playerList){
            LoserBoardPlayer loserBoardPlayer = new LoserBoardPlayer(
                    player.getName(),
                    player.getId(),
                    player.getGamesLost()
            );
            loserBoardPlayerList.add(loserBoardPlayer);
        }
        return loserBoardPlayerList;
    }

//    public boolean deletePlayer(Long playerId) {
//        Optional<Game> game = gameRepository.findById(playerId);
//        if (game.isPresent()) {
//            gameRepository.deleteById(playerId);
//            return true;
//        } else {
//            return false;
//        }
//    }
    public void deletePlayer(long playerId) {
    Player player = playerRepository.findById(playerId).get();
    List<Game> gameList = player.getGames();
    for(Game game : gameList){
        game.removePlayer(player);
        //if no more players, delete game
        if (game.getPlayers().size()==0){
            gameRepository.delete(game);
        }
        else { gameRepository.save(game);}
    }
    playerRepository.deleteById(playerId);
}
//    public List<Player> getLoserBoard(){
//        return playerRepository.findAll(Sort.by(Sort.Direction.DESC, "gamesLost"));
//    }

}
