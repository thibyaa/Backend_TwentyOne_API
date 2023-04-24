package models;

public class Reply {
    private int gameState;

    private Boolean complete;

    private String message;

    public Reply(int gameState, Boolean complete, String message){
        this.gameState = gameState;
        this.complete = complete;
        this.message = message;
    }

    public Reply(){

    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
