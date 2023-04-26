package com.example.Backend_TwentyOne_API.services;

import com.example.Backend_TwentyOne_API.models.Game;
import com.example.Backend_TwentyOne_API.models.GameType;
import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.models.Reply;
import com.example.Backend_TwentyOne_API.repositories.GameRepository;
import com.example.Backend_TwentyOne_API.repositories.PlayerRepository;
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

    @Autowired
    PlayerRepository playerRepository;

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
        gameRepository.save(game);

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

        Player player = game.getLeadPlayer();

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
            game.setComplete(true);
            gameRepository.save(game);
            player.setGamesLost(player.getGamesLost()+1);
            playerRepository.save(player);

            return new Reply(game.getCurrentTotal(),game.getComplete(),"Game Over! You lose :(");
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
            game.setComplete(true);
            gameRepository.save(game);
            return new Reply(game.getCurrentTotal(), game.getComplete(), "Game Over! You win :D");
        }
        else {
            return new Reply(game.getCurrentTotal(), false, "Computer played " + computerTurn + "! Your move...");
        }

    }


    public Reply processTurnMultiplayer(Long gameId, int guess) {

        // find the correct game
        Game game = gameRepository.findById(gameId).get();

//        find list of player
        List<Player> playerList = game.getPlayers();

//        find current player
        Long currentPlayerId = game.getCurrentPlayerId();
        Player currentPlayer = playerService.getPlayerById(currentPlayerId).get();



        // Check if game has started
        if(!game.getHasStarted()){
            return new Reply(
                    game.getCurrentTotal(),
                    false,
                    "game has not started"
            );
        }

        // Check game is not already complete
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
//        if not, change current to next player
        if (game.getCurrentTotal()> 20) {
            game.setComplete(true);
            gameRepository.save(game);
            currentPlayer.setGamesLost(currentPlayer.getGamesLost()+1);
            playerRepository.save(currentPlayer);
            return new Reply(
                    game.getCurrentTotal(),
                    game.getComplete(),
                    "Game Over! Player " + game.getCurrentPlayerId() + ", " + playerService.getPlayerNameById(game.getCurrentPlayerId())  +" loses :(");
        }else {
//    increment player turn to the person

            Long nextPlayerId = idOfNextPlayerToGuess(playerList, currentPlayerId);
            game.setCurrentPlayerId(nextPlayerId);
            gameRepository.save(game);
            return new Reply(game.getCurrentTotal(),
                    false,
                    "It is player " + game.getCurrentPlayerId() + "'s turn");

        }
    }


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

    public Long idOfNextPlayerToGuess(List<Player> playerList, Long currentPlayerId){
//        get player by playerId
//        get index of player in list by currentPlayerId
//        increment by 1, looping if needed
//        find nextPlayer by index in list
//        find nexPlayerId from this
        Player currentPlayer = playerService.getPlayerById(currentPlayerId).get();
        int currentPlayerIndex = playerList.indexOf(currentPlayer);
        int nextPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
        Player nextPlayer = playerList.get(nextPlayerIndex);
        Long nextPlayerId = nextPlayer.getId();
        return nextPlayerId;
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

    public Reply addPlayerToGame(Long playerId, Long gameId) {
//        first get the actual player by the playerId
//        then, get the game by the gameId
//        add players to the array list and to the games array list
//        save, then return the game

        Player player = playerService.getPlayerById(playerId).get();
        Game game = getGameById(gameId).get();
        game.addPlayer(player);
        gameRepository.save(game);
        String message = "Player " + player.getId() + ", " + player.getName() + ", has been added to game " + game.getId() + ".";
        return new Reply(game.getCurrentTotal(), game.getComplete(), message);
    }

    public Reply startGameMultiplayerNotEnoughPlayers(Long gameId) {
        Game game = getGameById(gameId).get();
        String playerName = game.getLeadPlayer().getName();
        return new Reply(
                0,
                false,
                 "Only " + playerName + " has joined the game, not enough players to begin"
        );
    }

    public Reply wrongPlayer(Long gameId, Long playerId) {
        Game game = getGameById(gameId).get();
        Long currentPlayerId = game.getCurrentPlayerId();
        int gameState = game.getCurrentTotal();
        return new Reply(
                gameState,
                false,
                "Not your turn, player " + currentPlayerId + " is next."
        );
    }


    public Reply startNewGameMultiplayer(Long gameId) {
        //        get game by id
        Game game = getGameById(gameId).get();

//        start game
        game.setHasStarted(true);
        gameRepository.save(game);

//        flip coin to decide who starts
        int whoToStart = random.nextInt(0,game.getPlayers().size());
        Player player = game.getPlayers().get(whoToStart);
        Long firstPlayerId = player.getId();
        game.setCurrentPlayerId(firstPlayerId);
        gameRepository.save(game);
        String firstPlayerName = player.getName();

//       identify starting player and personalise message
        String message = "Player " + firstPlayerId + ", " + firstPlayerName + ", to start!";

        return new Reply(
                game.getCurrentTotal(),
                false,
                message
        );
    }

    public Reply addPlayerToGameAlreadyContains(Long gameId, Long playerId) {
        Game game = getGameById(gameId).get();
        Player player = playerService.getPlayerById(playerId).get();
        String message = "You're already here, "  + player.getName() + "!";
        return new Reply(game.getCurrentTotal(), game.getComplete(), message);
    }

    public Reply addPlayerToGameAlreadyStarted(Long gameId) {
        Game game = getGameById(gameId).get();
        String message = "Game " + game.getId() + " has already started. Cannot add anymore players";
        return new Reply(game.getCurrentTotal(), game.getComplete(), message);
    }

    public Reply addPlayerToWrongGameType(Long gameId) {
        Game game = getGameById(gameId).get();
        String message = "Game " + game.getId() + " is not multiplayer. Cannot add players";
        return new Reply(game.getCurrentTotal(), game.getComplete(), message);
    }

    public Reply gameAlreadyComplete(Long gameId) {
        Game game = getGameById(gameId).get();
        String message = "Game " + game.getId() + " is finished. Cannot play anymore.";
        return new Reply(game.getCurrentTotal(), game.getComplete(), message);
    }

    public void deleteGame(long gameId) {
        Game game = gameRepository.findById(gameId).get();
        List<Player> playerList = game.getPlayers();
        for(Player player : playerList){
            player.removeGame(game);
            playerRepository.save(player);
        }
        gameRepository.deleteById(gameId);

    }
}
