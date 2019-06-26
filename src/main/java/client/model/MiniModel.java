package client.model;

import client.view.View;

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
}
