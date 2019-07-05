package client.model;

import client.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MiniModel {
    private final View view;
    private final Board board;
    private final List<String[]> chat = new ArrayList<>();
    private String[] lobbyList = new String[]{};
    private PowerUp[] powerups;
    private int[] possibleActions;
    private int actionSetID;
    private int currentPlayer;
    private int[] possibleTargets;
    private int minToSelect;
    private int maxToSelect;
    private int remainingActions;
    private int remainingTime;
    private GameState state = GameState.NOT_CONNECTED;
    private int playerID;

    public MiniModel(View view) {
        this.view = view;
        this.board = new Board(view);
    }

    public String[] getLobbyList() {
        return lobbyList;
    }

    public void setLobbyList(String[] lobbyList) {
        this.lobbyList = lobbyList;
        view.displayLobbyList(lobbyList);
    }

    public Board getBoard() {
        return board;
    }

    public PowerUp[] getPowerups() {
        return powerups;
    }

    public void setPowerups(int[][] values) {
        this.powerups = Arrays.stream(values).map(x -> new PowerUp(x[0], x[1])).toArray(PowerUp[]::new);
        view.displayPowerUps(powerups);
    }

    public int[] getPossibleTargets() {
        return possibleTargets;
    }

    @SuppressWarnings("unchecked")
    public void setPossibleTargets(int[] targets) {
        possibleTargets = targets;
        Arrays.stream(targets).forEach(x -> (new Consumer[]{
                y -> view.displayPossibleRoom((Integer) y, board.getRoom((Integer) y / 3)),
                y -> view.displayPossibleSquare((Integer) y, board.getSquares()[((Integer) y) / 3]),
                y -> view.displayPossibleFigure((Integer) y, board.getPlayers()[((Integer) y) / 3])
        })[x % 3].accept(x));
    }

    public int getMinToSelect() {
        return minToSelect;
    }

    public void setMinToSelect(int minToSelect) {
        this.minToSelect = minToSelect;
        view.displayMinToSelect(minToSelect);
    }

    public int getMaxToSelect() {
        return maxToSelect;
    }

    public void setMaxToSelect(int maxToSelect) {
        this.maxToSelect = maxToSelect;
        view.displayMaxToSelect(maxToSelect);
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        view.displayCurrentPlayer(currentPlayer);
    }

    public int getActionSetID() { return actionSetID; }

    public int[] getPossibleActions() {
        return possibleActions;
    }

    public void setPossibleActions(int actionSetID) {
        this.actionSetID = actionSetID;
        if(actionSetID==0 || actionSetID==1)
            this.possibleActions=new int[]{0,1,2};
        else if(actionSetID==2)
            this.possibleActions=new int[]{0,1};
        else
            this.possibleActions = new int[]{-1};
        view.displayPossibleActions(possibleActions);
    }

    public int getRemainingActions() {
        return remainingActions;
    }

    public void setRemainingActions(int remainingActions) {
        this.remainingActions = remainingActions;
        view.displayRemainingActions(remainingActions);
    }

    public void setGameState(int value) {
        state = GameState.values()[value];
        view.displayGameState(state);
    }

    public void addChatMessage(String user, String msg) {
        chat.add(new String[]{user, msg});
        view.displayChat(chat);
    }

    public void setRemainingTime(int remaining) {
        remainingTime = remaining;
        view.displayRemainingTime(remaining);
    }

    public List<String[]> getChat() {
        return chat;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int id) {
        this.playerID = id;
    }

    public GameState getState() { return state; }
}
