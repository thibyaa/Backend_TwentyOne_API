

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

## @PostMapping - createNewGame

## @PostMapping - addPlayerToGame 

## @DeleteMapping - removePlayerToGame

## @DeleteMapping - deleteGame 

## @PutMapping - submitTurn

MVP 