package com.example.Backend_TwentyOne_API.services;

import com.example.Backend_TwentyOne_API.models.Game;
import com.example.Backend_TwentyOne_API.models.GameType;
import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.models.Reply;
import com.example.Backend_TwentyOne_API.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {


    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerService playerService;

    public GameService(){

    }
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }


    public Reply createNewGame(long playerId, GameType gameType) {
        Player player = playerService.getPlayerById(playerId).get();
        Game game = new Game(player, gameType);
        gameRepository.save(game);
        return new Reply(
                0,
                false,
                "Create new game with id " + game.getId() + " with lead player " + player.getName()
        );
    }

    public Reply startNewGame(Long gameId) {
        Game game = getGameById(gameId).get();
        game.setHasStarted(true);
        gameRepository.save(game);
        return new Reply(
                0,
                false,
                "Game with id " + game.getId() + " has started"
        );
    }

    public Reply startGameAlreadyStarted(Long gameId){
        Game game = getGameById(gameId).get();
        int currentTotal = game.getCurrentTotal();
        Boolean complete = game.getComplete();
        return new Reply(
                currentTotal,
                complete,
                "Game with id " + game.getId() + " has already been started"
        );
    }

    public Reply processTurn (Long gameId, int guess){

        // find the correct game
        Game game = gameRepository.findById(gameId).get();

        // Check if game has started
        if(!game.getHasStarted()){
            return new Reply(
                    game.getCurrentTotal(),
                    false,
                    "game has not started"
            );
        }

        // Check game is already complete
        if (game.getComplete()){
            return new Reply(
                    game.getCurrentTotal(),
                    true,
                    "game is already complete"
            );
        }

        // increment total
        game.setCurrentTotal(game.getCurrentTotal()+ guess);
        gameRepository.save(game);

        // Check is below 21
        if (game.getCurrentTotal()> 20){
            return new Reply(game.getCurrentTotal(),true,"Game Over! You lose :(");
        }

       // Computer guess
        // Is the guess "easy" or "hard"? (ENUM)
        // increment the total
        // Check if total is >20. If so , reply "You win! :) "
        // If not, return the current total to the player prompting next input

    }
}
