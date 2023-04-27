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


    @DeleteMapping("/{gameId}/{playerId}")
    public ResponseEntity<String> removePlayerFromGame(@PathVariable("gameId") Long gameId, @PathVariable("playerId") Long playerId){
        return gameService.removePlayerFromGame(gameId, playerId);

    }


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
    public ResponseEntity<Reply> startNewGame(@PathVariable("gameId") Long gameId) {
        return gameService.startNewGame(gameId);
    }



    @PutMapping(value = "/{gameId}")
    public ResponseEntity<Reply> submitTurn (@PathVariable Long gameId,
                                             @RequestParam Long playerId,
                                             @RequestParam int guess) {
        return gameService.submitTurn(gameId, playerId, guess);
    }

//        get game by gameId
//        get player by playerId
//        check if the game exists
//        check if the player exists
//        check if game is not complete
//        check player who submits guess is player whose turn it is
//        check guess is 1,2,or 3
//        then processTurnMultiplayer(gameId, guess)
//        otherwise processTurnSinglePlayer(gameId, guess)
//
//        Optional<Game> game = gameService.getGameById(gameId);
//        Optional<Player> player = playerService.getPlayerById(playerId);
//        if (!game.isPresent()){
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        } else if(!player.isPresent()){
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        } else if(game.get().getComplete()){
//            Reply reply = gameService.gameAlreadyComplete(gameId);
//            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
//        } else if(game.get().getCurrentPlayerId() != playerId){
//            Reply reply = gameService.wrongPlayer(gameId, playerId);
//            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
//        }else if(!((guess < 4) && (guess >0))){
//            Reply reply = gameService.invalidGuess(gameId);
//            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
//        } else if (game.get().getGameType().equals(GameType.MULTIPLAYER)) {
//            Reply reply = gameService.processTurnMultiplayer(gameId, guess);
//            return new ResponseEntity<>(reply, HttpStatus.OK);
//        } else {
//            Reply reply = gameService.processTurn(gameId, guess);
//            return new ResponseEntity<>(reply, HttpStatus.OK);
//        }
//    }

    // Check

//
}
