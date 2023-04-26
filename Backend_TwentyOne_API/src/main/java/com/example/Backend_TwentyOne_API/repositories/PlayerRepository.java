package com.example.Backend_TwentyOne_API.repositories;

import com.example.Backend_TwentyOne_API.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByGamesLostIgnoreGames();
}
