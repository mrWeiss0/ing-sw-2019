package client.model;

import client.view.View;

import java.util.Arrays;
import java.util.function.Consumer;

public class MiniModel {
    private View view;
    private String[] lobbyList;
    private Board board;
    private PowerUp[] powerups;
    private int[] possibleActions;
    private int currentPlayer;
    private int targetType;
    private int[] possibleTargets;
    private int minToSelect;
    private int maxToSelect;

    public MiniModel(View view){
        this.view=view;
    }

    public void setLobbyList(String[] lobbyList) {
        this.lobbyList = lobbyList;
        view.displayLobbyList(lobbyList);
    }

    public String[] getLobbyList() {
        return lobbyList;
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
}
