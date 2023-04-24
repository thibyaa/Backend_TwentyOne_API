package com.example.Backend_TwentyOne_API.components;

import com.example.Backend_TwentyOne_API.models.Game;
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
        Game game = new Game(player1);
        gameRepository.save(game);
    }

}
