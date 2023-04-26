package com.example.Backend_TwentyOne_API.controllers;

import com.example.Backend_TwentyOne_API.models.Game;
import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.models.Reply;
import com.example.Backend_TwentyOne_API.services.GameService;
import com.example.Backend_TwentyOne_API.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/players")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers(){
        List<Player> players = playerService.getAllPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }
//
//    @GetMapping(value = "/loserBoard")
//    public ResponseEntity<List<Player>> getLoserBoard(){
//        List<Player> players = playerService.getLoserBoard();
//        return new ResponseEntity<>(players, HttpStatus.OK);
//    }



    @GetMapping(value = "/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id){
        Optional<Player> player = playerService.getPlayerById(id);
        if (player.isPresent()){
            return new ResponseEntity<>(player.get(), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Player> addNewPlayer(@RequestBody Player player){
        Player savedPlayer = playerService.savePlayer(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable("id") Long id) {
        Optional<Player> player = playerService.getPlayerById(id);
        if (!player.isPresent()) {
            return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
        } else {
            playerService.deletePlayer(id);
            return new ResponseEntity<>("Player deleted successfully", HttpStatus.OK);
        }
    }
    //  Delete player by specifying player id
    //  Create deletePlayer method inside PlayerService class
    //  Create if loop to check if player has been deleted successfully or not
//

// removing each player from the game

    // loop through every player in the game
    // remove the game from the list of players
    // delete players gameList



        //

}
