
## @GetMapping - getAllGames


To retreive the list of all possible games. 

url: /games

    @GetMapping
    public ResponseEntity<List<Game>>  getAllGames(){
        List<Game> games = gameService.getAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

## @GetMapping - getGamesById

url: /games/{id}

To retrieve a single game by id. If game is not found, returns http 404 exception not found. 

    @GetMapping(value = "/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id){
        Optional<Game> game = gameService.getGameById(id);
        if (game.isPresent()){
            return new ResponseEntity<>(game.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
## @PostMapping - createNewGame

url: GET POST /games/{id}

To create a new game.

      @PostMapping
    public ResponseEntity<Reply> createNewGame(@RequestParam Long playerId, @RequestParam String gameType) {
        return gameService.createNewGame(playerId,gameType);
    }
    
## @PostMapping - addPlayerToGame 

To add the player to a game. Add a player to a game by passing player Id and game Id in the path.
    
     @PostMapping( value = "/{gameId}")
    public ResponseEntity<Reply> addPlayerToGame(@PathVariable Long gameId, @RequestParam Long playerId) {
        return gameService.addPlayerToGame(playerId,gameId);
    }

## @DeleteMapping - removePlayerToGame

To remove the player to a game. By passing player Id and game Id in the path.


    
    @DeleteMapping("/{gameId}/{playerId}")
    public ResponseEntity<String> removePlayerFromGame(@PathVariable("gameId") Long gameId, @PathVariable("playerId") Long playerId){
        return gameService.removePlayerFromGame(gameId, playerId);

    }
    
## @DeleteMapping - deleteGame 
   
To delete a game. This is done by passing game Id in the path.
    
    
     @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable("id") Long id) {
        Optional<Game> game = gameService.getGameById(id);
        if (!game.isPresent()) {
            return new ResponseEntity<>("Game not found",HttpStatus.NOT_FOUND);
        } else {
            gameService.deleteGame(id);
            return new ResponseEntity<>("Game deleted", HttpStatus.OK);
        }
    }
    
    
## @PatchMapping - startNewGame

 To add a new game. This is done by adding game Id in the path.

    
      @PatchMapping(value = "/{gameId}")
    public ResponseEntity<Reply> startNewGame(@PathVariable("gameId") Long gameId) {
        return gameService.startNewGame(gameId);
    }
    
    
## @PutMapping - submitTurn

To submit turn to a running game. This is done by passing gameId, playerId and int guess.

    
     @PutMapping(value = "/{gameId}")
    public ResponseEntity<Reply> submitTurn (@PathVariable Long gameId,
                                             @RequestParam Long playerId,
                                             @RequestParam int guess) {
        return gameService.submitTurn(gameId, playerId, guess);
    }
