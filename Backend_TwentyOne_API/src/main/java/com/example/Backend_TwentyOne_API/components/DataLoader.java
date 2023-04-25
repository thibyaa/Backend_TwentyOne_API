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

        Player player1 = new Player("Intisar");
        playerRepository.save(player1);
        Player player2 = new Player("Will");
        playerRepository.save(player2);
        Player player3 = new Player("Taliane");
        playerRepository.save(player3);
        Player player4 = new Player("Isabel");
        playerRepository.save(player4);

        Game game1 = new Game(player1, GameType.EASY);
        gameRepository.save(game1);
        Game game2 = new Game(player2, GameType.DIFFICULT);
        gameRepository.save(game2);

        Game game3 = new Game(player3, GameType.MULTIPLAYER);
        game3.addPlayer(player1);
        gameRepository.save(game3);


    }

}
