package com.example.Backend_TwentyOne_API.controllers;

import com.example.Backend_TwentyOne_API.models.LoserBoardPlayer;
import com.example.Backend_TwentyOne_API.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/loserBoard")

public class LoserBoardController {

    @Autowired
    PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<LoserBoardPlayer>> getLoserBoard(){
         List<LoserBoardPlayer> players = playerService.getLoserBoard();
         return new ResponseEntity<>(players, HttpStatus.OK);
    }

}
