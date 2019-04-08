package model;

public class Board {

    private List<AbstractSquare> squares;
    private List<SquareSpawn> spawnpoints;

    public void initBoard(JSONObject BoardFile){

    } //TODO json library needed and how to create the board using JSON

    public void refillBoard(Game game){
        for(AbstractSquare current : squares){
            current.accept(game);
        }
    }

}
