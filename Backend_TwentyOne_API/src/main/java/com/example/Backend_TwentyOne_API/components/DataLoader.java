package com.example.Backend_TwentyOne_API.components;

import com.example.Backend_TwentyOne_API.models.Game;
import com.example.Backend_TwentyOne_API.models.GameType;
import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.repositories.GameRepository;
import com.example.Backend_TwentyOne_API.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    public DataLoader(){

    }
    @Override
    public void run(ApplicationArguments args) throws Exception{

        Player player1 = new Player("Taliane");
        player1.setGamesLost(2);
        playerRepository.save(player1);
        Player player2 = new Player("Intisar");
        playerRepository.save(player2);
        Player player3 = new Player("Will");
        playerRepository.save(player3);
        Player player4 = new Player("Isabel");
        player4.setGamesLost(3);
        playerRepository.save(player4);


        Game game1 = new Game(player1, GameType.MULTIPLAYER);
        game1.addPlayer(player2);
        game1.addPlayer(player3);
        game1.addPlayer(player4);
        gameRepository.save(game1);


        Game game2 = new Game(player2, GameType.DIFFICULT);
        gameRepository.save(game2);

//        Game game3 = new Game(player3, GameType.MULTIPLAYER);
//        game3.addPlayer(player1);
//        gameRepository.save(game3);
//
//        Game game4 = new Game(player4, GameType.MULTIPLAYER);
//        gameRepository.save(game4);
//
//        Game game5 = new Game(player1, GameType.EASY);
//        game5.setComplete(true);
//        gameRepository.save(game5);


    }

}
