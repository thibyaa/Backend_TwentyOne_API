package com.example.Backend_TwentyOne_API.repositories;

import com.example.Backend_TwentyOne_API.models.LoserBoardPlayer;
import com.example.Backend_TwentyOne_API.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query (value = "SELECT players.name, players.id, players.gamesLost FROM players ORDER BY players.gamesLost", nativeQuery = true)
    List<LoserBoardPlayer> getLoserBoard();
}
