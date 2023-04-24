package com.example.Backend_TwentyOne_API.repositories;

import com.example.Backend_TwentyOne_API.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
