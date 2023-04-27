# PlayerController
This class is responsible for handling HTTP requests related to player data in the Backend_TwentyOne_API application.

## Annotations Used
• @RestController: This annotation is used to indicate that the PlayerController class is a RESTful controller. It combines the functionality of @Controller and @ResponseBody, meaning that all methods in the class will return data in JSON format.

• @RequestMapping: This annotation is used to map HTTP requests to specific handler methods. In this case, the value attribute is set to "/players", so all requests that start with "/players" will be handled by methods in this class.

• @Autowired: This annotation is used to inject an instance of the PlayerService class into this controller. It automatically wires the PlayerService dependency into the PlayerController. This allows the controller to call methods from the service without needing to instantiate it manually.

## Endpoints used
### GET
• @GetMapping: This annotation is used to handle GET requests. The value attribute is set to "/", which means that this method will handle requests to the root URL of "/players". The second @GetMapping annotation handles requests to "/players/{id}", where "{id}" is a placeholder for the player's unique identifier.

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers(){
        List<Player> players = playerService.getAllPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

### POST
• @PostMapping: This annotation is used to handle POST requests. It expects a JSON payload in the request body, which is deserialized into a Player object.

    @PostMapping
    public ResponseEntity<Player> addNewPlayer(@RequestBody Player player){
        Player savedPlayer = playerService.savePlayer(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
    }

### DELETE
• @DeleteMapping: This annotation is used to handle DELETE requests. It expects a player's unique identifier to be passed in the URL.

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable("id") Long id) {
        Optional<Player> player = playerService.getPlayerById(id);
        if (!player.isPresent()) {
            return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
        } else {
            playerService.deletePlayer(id);
            return new ResponseEntity<>("Player deleted successfully", HttpStatus.OK);
        }
    }


## Methods used
• getAllPlayers(): This method returns a list of all players in the database.

• getPlayerById(Long id): This method returns a specific player by their unique identifier. If the player is not found, a 404 NOT FOUND response is returned.

• addNewPlayer(Player player): This method adds a new player to the database and returns a 201 CREATED response.

•deletePlayer(Long id): This method deletes a player from the database by their unique identifier. If the player is not found, a 404 NOT FOUND response is returned.