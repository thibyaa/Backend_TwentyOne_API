package com.example.Backend_TwentyOne_API.controllers;

import com.example.Backend_TwentyOne_API.models.Game;
import com.example.Backend_TwentyOne_API.models.GameType;
import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.models.Reply;
import com.example.Backend_TwentyOne_API.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Backend_TwentyOne_API.services.GameService;

import java.util.List;
import java.util.Optional;
import java.util.Random;


@RestController
@RequestMapping(value = "/games")
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<Game>>  getAllGames(){
        List<Game> games = gameService.getAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id){
        Optional<Game> game = gameService.getGameById(id);
        if (game.isPresent()){
            return new ResponseEntity<>(game.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Reply> createNewGame(@RequestParam Long playerId, @RequestParam String gameType) {
        return gameService.createNewGame(playerId,gameType);
    }

    @PostMapping( value = "/{gameId}")
    public ResponseEntity<Reply> addPlayerToGame(@PathVariable Long gameId, @RequestParam Long playerId) {
        return gameService.addPlayerToGame(playerId,gameId);
    }


//    @PostMapping( value = "/{gameId}")
//    public ResponseEntity<Reply> addPlayerToGame(@PathVariable Long gameId, @RequestParam Long playerId) {
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
//    }


    @DeleteMapping("/{gameId}/{playerId}")
    public ResponseEntity<String> removePlayerFromGame(@PathVariable("gameId") Long gameId, @PathVariable("playerId") Long playerId){
        Optional<Player> player = playerService.getPlayerById(playerId);
        Optional<Game> game = gameService.getGameById(gameId);
        Player leadPlayer;
        if(!game.isPresent()){
            return new ResponseEntity<>("Game not found",HttpStatus.NOT_FOUND);
        } else{
            leadPlayer = game.get().getLeadPlayer();
        }

        if(!player.isPresent()){
            return new ResponseEntity<>("Player not found",HttpStatus.NOT_FOUND);
        } else if (game.get().getHasStarted()) {
            return new ResponseEntity<>("Player cannot be removed from game that has already begun!", HttpStatus.NOT_ACCEPTABLE);
        } else if (!game.get().getPlayers().contains(player.get())){
            return new ResponseEntity<>("Player not in this game", HttpStatus.NOT_ACCEPTABLE);
        } else if (player.get().equals(leadPlayer)){
            return new ResponseEntity<>("Lead player cannot be removed from game",HttpStatus.NOT_ACCEPTABLE);
        } else{
            gameService.removePlayerFromGame(gameId, playerId);
            return new ResponseEntity<>("Player successfully removed from game",HttpStatus.OK);
        }
    }


//  Delete game by specifying game id
//  Create deleteGame method inside GameService class
//  Create if loop to check if game has been deleted successfully or not



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable("id") Long id) {
        Optional<Game> game = gameService.getGameById(id);
        if (!game.isPresent()) {
            return new ResponseEntity<>("Game not found",HttpStatus.NOT_FOUND);
        } else {
            gameService.deleteGame(id);
            return new ResponseEntity<>("Game deleted", HttpStatus.OK);
        }
    }


    //
    @PatchMapping(value = "/{gameId}")
    public ResponseEntity<Reply> startNewGame(@PathVariable Long gameId) {

        // check if game exists
        // Check game has not already started
        // Check game if multiplayer has enough players, if fails a response
        // if passes, than we'll just start the game, depending on whether multiplayer or not

        Optional<Game> game = gameService.getGameById(gameId);
        if (!game.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else if (game.get().getHasStarted()) {
            Reply reply = gameService.startGameAlreadyStarted(gameId);
            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
        } else if (game.get().getGameType().equals(GameType.MULTIPLAYER)
                && game.get().getPlayers().size() < 2) {
            Reply reply = gameService.startGameMultiplayerNotEnoughPlayers(gameId);
            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
        } else if (game.get().getGameType().equals(GameType.MULTIPLAYER)){
            Reply reply = gameService.startNewGameMultiplayer(gameId);
            return new ResponseEntity<>(reply, HttpStatus.ACCEPTED);
        } else{
            Reply reply = gameService.startNewGame(gameId);
            return new ResponseEntity<>(reply, HttpStatus.ACCEPTED);
        }
    }



    @PutMapping(value = "/{gameId}")
    public ResponseEntity<Reply> submitTurn (@PathVariable Long gameId,
                                             @RequestParam Long playerId,
                                             @RequestParam int guess){

//        get game by gameId
//        get player by playerId
//        check if the game exists
//        check if the player exists
//        check if game is not complete
//        check player who submits guess is player whose turn it is
//        check guess is 1,2,or 3
//        then processTurnMultiplayer(gameId, guess)
//        otherwise processTurnSinglePlayer(gameId, guess)

        Optional<Game> game = gameService.getGameById(gameId);
        Optional<Player> player = playerService.getPlayerById(playerId);
        if (!game.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else if(!player.isPresent()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else if(game.get().getComplete()){
            Reply reply = gameService.gameAlreadyComplete(gameId);
            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
        } else if(game.get().getCurrentPlayerId() != playerId){
            Reply reply = gameService.wrongPlayer(gameId, playerId);
            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
        }else if(!((guess < 4) && (guess >0))){
            Reply reply = gameService.invalidGuess(gameId);
            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
        } else if (game.get().getGameType().equals(GameType.MULTIPLAYER)) {
            Reply reply = gameService.processTurnMultiplayer(gameId, guess);
            return new ResponseEntity<>(reply, HttpStatus.OK);
        } else {
            Reply reply = gameService.processTurn(gameId, guess);
            return new ResponseEntity<>(reply, HttpStatus.OK);
        }
    }

    // Check


//
}
