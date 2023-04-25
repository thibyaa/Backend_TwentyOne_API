package com.example.Backend_TwentyOne_API.controllers;

import com.example.Backend_TwentyOne_API.models.Game;
import com.example.Backend_TwentyOne_API.models.GameType;
import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.models.Reply;
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
    public ResponseEntity<Reply> createNewGame(@RequestParam Long playerId, GameType gameType){
        Reply reply = gameService.createNewGame(playerId, gameType);
        return new ResponseEntity<>(reply, HttpStatus.CREATED);
    }


    //
    @PatchMapping(value = "/{gameId}")
    public ResponseEntity<Reply> startNewGame(@PathVariable Long gameId) {

        // Check game has not already started
        // Check game if multiplayer has enough players, if fails a response
        // if passes, than we'll just start the game
        // check if game exists
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
        } else {
            Reply reply = gameService.startNewGame(gameId);
            return new ResponseEntity<>(reply, HttpStatus.ACCEPTED);
        }
    }



//        if(game.isPresent() && game.get().getHasStarted()==false) {
//            Reply reply = gameService.startNewGame(gameId);
//            return new ResponseEntity<>(reply, HttpStatus.ACCEPTED);
//        } else if (game.isPresent() && game.get().getHasStarted()==true){
//            Reply reply = gameService.startGameAlreadyStarted(gameId);
//            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }


    @PutMapping(value = "/{gameId}")
    public ResponseEntity<Reply> submitTurn (@PathVariable Long gameId,@RequestParam int guess){
        if( (guess < 4) && (guess >0)) {
            Reply reply = gameService.processTurn(gameId, guess);
            return new ResponseEntity<>(reply, HttpStatus.OK);
        } else{
            Reply reply = gameService.invalidGuess(gameId);
            return new ResponseEntity<>(reply, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping( value = "/{gameId}")
    public ResponseEntity<Game> addPlayerToGame(@PathVariable Long gameId, @RequestParam Long playerId){
        Game savedGame = gameService.addPlayerToGame(playerId, gameId);
        return new ResponseEntity<>(savedGame, HttpStatus.ACCEPTED);
    }

//
}
