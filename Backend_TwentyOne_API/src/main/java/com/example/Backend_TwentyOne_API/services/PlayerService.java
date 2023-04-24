package com.example.Backend_TwentyOne_API.services;

import com.example.Backend_TwentyOne_API.models.Player;
import com.example.Backend_TwentyOne_API.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Long playerId) {
        return playerRepository.findById(playerId);
    }
}
