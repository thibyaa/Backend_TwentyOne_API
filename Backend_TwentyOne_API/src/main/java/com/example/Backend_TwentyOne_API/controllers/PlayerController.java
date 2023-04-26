package com.example.Backend_TwentyOne_API.controllers;

import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.models.Reply;
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

    //  Delete player by specifying player id
    //  Create deletePlayer method inside PlayerService class
    //  Create if loop to check if player has been deleted successfully or not
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable("id") Long id) {
        boolean deleted = playerService.deletePlayer(id);
        if (deleted) {
            return new ResponseEntity<>("Player deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
        }
    }


}
