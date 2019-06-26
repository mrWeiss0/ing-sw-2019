package client.model;

import client.view.View;

public class MiniModel {
    private View view;
    private String[] lobbyList;

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
