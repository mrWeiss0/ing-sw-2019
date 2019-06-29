package client.model;

import client.view.View;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MiniModel {
    private View view;
    private String[] lobbyList; // V
    private Board board;
    private PowerUp[] powerups; //player's powerups V
    private int[] possibleActions; //player's possible actions V
    private int currentPlayer; //game's current player V
    private int[] possibleTargets; // possible targets V
    private int minToSelect; //min targets to select V
    private int maxToSelect; //max targets to select V
    private int remainingActions;

    public MiniModel(View view){
        this.view=view;
        this.board = new Board(view);
    }

    public void setLobbyList(String[] lobbyList) {
        this.lobbyList = lobbyList;
        view.displayLobbyList(lobbyList);
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPossibleActions(int [] possibleActions){
        this.possibleActions=possibleActions;
    }

    public void setRemainingActions(int remainingActions){
        this.remainingActions=remainingActions;
    }
    @SuppressWarnings("unchecked")
    public void setPossibleTargets(int[] targets){
        possibleTargets=targets;
        Arrays.stream(targets).forEach(x->(new Consumer[]{
                y->view.displayPossibleRoom((Integer) y, board.getRoom((Integer)y/3)),
                y->view.displayPossibleSquare((Integer) y, board.getSquares()[((Integer) y)/3]),
                y->view.displayPossibleFigure((Integer) y, board.getPlayers()[((Integer) y)/3])
        })[x%3].accept(x));
    }

    public void setMinToSelect(int minToSelect) {
        this.minToSelect = minToSelect;
    }

    public void setMaxToSelect(int maxToSelect) {
        this.maxToSelect = maxToSelect;
    }

    public void setPowerups(int[][] values) {
        this.powerups=(PowerUp[]) Arrays.stream(values).map(x->new PowerUp(x[0],x[1])).toArray();
    }

    public String[] getLobbyList() {
        return lobbyList;
    }

    public Board getBoard() {
        return board;
    }

    public PowerUp[] getPowerups(){
        return powerups;
    }

    public int[] getPossibleTargets(){
        return possibleTargets;
    }

    public int getMinToSelect(){
        return minToSelect;
    }

    public int getMaxToSelect(){
        return maxToSelect;
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public int[] getPossibleActions(){
        return possibleActions;
    }
}
