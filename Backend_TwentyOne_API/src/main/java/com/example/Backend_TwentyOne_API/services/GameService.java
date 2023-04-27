package com.example.Backend_TwentyOne_API.services;

import com.example.Backend_TwentyOne_API.models.Game;
import com.example.Backend_TwentyOne_API.models.GameType;
import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.models.Reply;
import com.example.Backend_TwentyOne_API.repositories.GameRepository;
import com.example.Backend_TwentyOne_API.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    public ResponseEntity<Reply> createNewGame(long playerId, String gameType) {
//        Get player by playerId
//        Check if player exists
//        Check if gameType is valid
//        If passes, create new game

        Optional<Player> player = playerService.getPlayerById(playerId);
        ResponseEntity<Reply> responseEntity;
        if(!player.isPresent()){
             responseEntity = new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        else if (gameType.equalsIgnoreCase("Easy")) {
            GameType newGameType = GameType.EASY;
            responseEntity = createGameByType(newGameType, player);
        } else if (gameType.equalsIgnoreCase("difficult")) {
            GameType newGameType = GameType.DIFFICULT;
            responseEntity = createGameByType(newGameType, player);
        } else if (gameType.equalsIgnoreCase("multiplayer")) {
            GameType newGameType = GameType.MULTIPLAYER;
            responseEntity = createGameByType(newGameType, player);
        } else {
            responseEntity  = new  ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        return responseEntity;

    }




//        check player exists before creating game
//        else create game depending on gameType
//        Optional<Player> player = playerService.getPlayerById(playerId);
//        if(!player.isPresent()){
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//        else {
//            if (gameType.equalsIgnoreCase("Easy")) {
//                GameType newGameType = GameType.EASY;
//                Reply reply = gameService.createNewGame(playerId, newGameType);
//                return new ResponseEntity<>(reply, HttpStatus.CREATED);
//            } else if (gameType.equalsIgnoreCase("difficult")) {
//                GameType newGameType = GameType.DIFFICULT;
//                Reply reply = gameService.createNewGame(playerId, newGameType);
//                return new ResponseEntity<>(reply, HttpStatus.CREATED);
//            } else if (gameType.equalsIgnoreCase("multiplayer")) {
//                GameType newGameType = GameType.MULTIPLAYER;
//                Reply reply = gameService.createNewGame(playerId, newGameType);
//                return new ResponseEntity<>(reply, HttpStatus.CREATED);
//            } else {
//                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//            }
//        }


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

    public ResponseEntity<Reply> addPlayerToGame(Long playerId, Long gameId) {

        // Get the player by the playerId,
        Optional<Player> player = playerService.getPlayerById(playerId);

        // get game by gameId,
        Optional<Game> game = getGameById(gameId);

//        create empty responseEntity
        ResponseEntity<Reply> responseEntity;

        // Check the player exists
        // check game exists
        if(!player.isPresent()){
            responseEntity = new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        } else if(!game.isPresent()){
            responseEntity = new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        // Check the players not already in the game
        else if (game.get().getPlayers().contains(player.get())){
            Reply reply = addPlayerToGameAlreadyContains(gameId, playerId);
            responseEntity = new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
        }
        // Check the game hasn't started
        else if(game.get().getHasStarted()){
            Reply reply = addPlayerToGameAlreadyStarted(gameId);
            responseEntity = new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
        }
        // Check if game is multiplayer
        else if (!game.get().getGameType().equals(GameType.MULTIPLAYER)){
            Reply reply = addPlayerToWrongGameType(gameId);
            responseEntity = new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
        }
        // Start game
        else{
            Game actualGame = game.get();
            actualGame.addPlayer(player.get());
            gameRepository.save(actualGame);
            String message = "Player " + player.get().getId() + ", " + player.get().getName() + ", has been added to game " + actualGame.getId() + ".";
            Reply reply = new Reply(actualGame.getCurrentTotal(), actualGame.getComplete(), message);
            responseEntity = new ResponseEntity<>(reply, HttpStatus.OK);
        }
        //  return response entity
        return responseEntity;
    }

    // Get the game by the GameId,
    // get player by playerId,
    // Check the game exists
    // Check the player exists
    // Check the players not already in the game
    // Check the game hasn't started
    // Check if game is not multiplayer

//        Optional <Game> game = gameService.getGameById(gameId);
//        Optional <Player> player = playerService.getPlayerById(playerId);
//        if (!game.isPresent()){
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        } else if (!player.isPresent()) {
//            return  new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        } else if (game.get().getPlayers().contains(player.get())) {
//            Reply reply = gameService.addPlayerToGameAlreadyContains(gameId, playerId);
//            return  new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
//        } else if (game.get().getHasStarted()) {
//            Reply reply = gameService.addPlayerToGameAlreadyStarted(gameId);
//            return  new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
//        } else if(!game.get().getGameType().equals(GameType.MULTIPLAYER)){
//            Reply reply = gameService.addPlayerToWrongGameType(gameId);
//            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
//        }
//        else {
//            Reply reply = gameService.addPlayerToGame(playerId, gameId);
//            return new ResponseEntity<>(reply, HttpStatus.ACCEPTED);
//        }
//





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

    public void removePlayerFromGame(Long gameId, Long playerId) {
        Player player = playerService.getPlayerById(playerId).get();
        Game game = getGameById(gameId).get();
        game.removePlayer(player);
        gameRepository.save(game);
    }

    public ResponseEntity<Reply> createGameByType(GameType gameType, Optional<Player> player){
        Game game = new Game(player.get(), gameType);
        Reply reply = new Reply(0,
                false,
                "Create new game with id " + game.getId() + " with lead player " + player.get().getName());
        return new ResponseEntity<>(reply, HttpStatus.CREATED);
    }

}
