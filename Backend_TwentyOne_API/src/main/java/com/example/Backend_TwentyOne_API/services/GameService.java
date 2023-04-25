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
import java.util.Random;

@Service
public class GameService {


Random random = new Random();

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
//        get game by id
        Game game = getGameById(gameId).get();

//        start game
        game.setHasStarted(true);

//        flip coin to decide who starts
        int whoToStart = random.nextInt(0,2);

        String message;

//        if computer to start
        if (whoToStart == 0) {
            int computerTurn;
            if (game.getGameType().equals(GameType.DIFFICULT)){
                computerTurn = computerTurnDifficult(game);
            } else { computerTurn = computerTurnEasy(game);}
            message = "Computer starts. Computer plays " + computerTurn + ". Your turn!";
        }

//        if player to start
        else{
            message = playerService.getPlayerNameByGame(game) + " to start. Your turn.";
        }
        gameRepository.save(game);

        return new Reply(
                game.getCurrentTotal(),
                false,
                message
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

        // increment total by user input
        game.setCurrentTotal(game.getCurrentTotal()+ guess);
        gameRepository.save(game);

        // Check is below 21
        if (game.getCurrentTotal()> 20){
            return new Reply(game.getCurrentTotal(),true,"Game Over! You lose :(");
        }

        // Computer guess
        // Is the guess "easy" or "hard"? (ENUM) DONE
        int computerTurn;
        if (game.getGameType().equals(GameType.DIFFICULT)){
            computerTurn = computerTurnDifficult(game);
        }
        else { computerTurn = computerTurnEasy(game);}
        gameRepository.save(game);

        if (game.getCurrentTotal()>20){
            return new Reply(game.getCurrentTotal(), true, "Game Over! You win :D");
        }
        else {
            return new Reply(game.getCurrentTotal(), false, "Computer played " + computerTurn + "! Your move...");
        }

    }





        // increment the total
        // Check if total is >20. If so , reply "You win! :) "
        // If not, return the current total to the player prompting next input


    public int computerTurnDifficult(Game game) {
        int computerTurn;
        if (game.getCurrentTotal() % 4 != 0) {
            computerTurn = 4 - game.getCurrentTotal() % 4;
        } else {
            computerTurn = random.nextInt(1, 4);
        }
        game.incrementCurrentTotal(computerTurn);
        return computerTurn;
    }

    public int computerTurnEasy(Game game) {
        int computerTurn = random.nextInt(1,4);
        game.incrementCurrentTotal(computerTurn);
        return computerTurn;
    }

    public Reply invalidGuess(Long gameId) {
        Game game = getGameById(gameId).get();
        int currentTotal = game.getCurrentTotal();
        Boolean complete = game.getComplete();
        return new Reply(
                currentTotal,
                complete,
                "Invalid turn, please input either 1, 2, or 3"
        );
    }

    public Game addPlayerToGame(Long playerId, Long gameId) {
//        first get the actual player by the playerId
//        then, get the game by the gameId
//        add players to the array list and to the games array list
//        save, then return the game

        Player player = playerService.getPlayerById(playerId).get();
        Game game = getGameById(gameId).get();
        game.addPlayer(player);
        gameRepository.save(game);
        return game;
    }

    public Reply startGameMultiplayerNotEnoughPlayers(Long gameId) {
        Game game = getGameById(gameId).get();
        String playerName = game.getLeadPlayer().getName();
        return new Reply(
                0,
                false,
                 "Only " + playerName + " has joined the game, not enough players to begin");
    }
}
