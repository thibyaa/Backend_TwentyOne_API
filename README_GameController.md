

## @GetMapping - getAllGames

Indicates

Url: localhost:8080/games/3


    @GetMapping
    public ResponseEntity<List<Game>>  getAllGames(){
        List<Game> games = gameService.getAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

## @GetMapping - getGamesById

Indicates


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

Indicates

      @PostMapping
    public ResponseEntity<Reply> createNewGame(@RequestParam Long playerId, @RequestParam String gameType) {
        return gameService.createNewGame(playerId,gameType);
    }
    
## @PostMapping - addPlayerToGame 

    
     @PostMapping( value = "/{gameId}")
    public ResponseEntity<Reply> addPlayerToGame(@PathVariable Long gameId, @RequestParam Long playerId) {
        return gameService.addPlayerToGame(playerId,gameId);
    }

## @DeleteMapping - removePlayerToGame

    
    @DeleteMapping("/{gameId}/{playerId}")
    public ResponseEntity<String> removePlayerFromGame(@PathVariable("gameId") Long gameId, @PathVariable("playerId") Long playerId){
        return gameService.removePlayerFromGame(gameId, playerId);

    }
    
## @DeleteMapping - deleteGame 
   
    
    
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

    
      @PatchMapping(value = "/{gameId}")
    public ResponseEntity<Reply> startNewGame(@PathVariable("gameId") Long gameId) {
        return gameService.startNewGame(gameId);
    }
    
    
## @PutMapping - submitTurn

    
     @PutMapping(value = "/{gameId}")
    public ResponseEntity<Reply> submitTurn (@PathVariable Long gameId,
                                             @RequestParam Long playerId,
                                             @RequestParam int guess) {
        return gameService.submitTurn(gameId, playerId, guess);
    }
