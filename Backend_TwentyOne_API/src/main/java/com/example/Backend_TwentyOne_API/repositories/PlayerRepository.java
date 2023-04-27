package com.example.Backend_TwentyOne_API.repositories;

import com.example.Backend_TwentyOne_API.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
