package server.connection;

import server.controller.LobbyList;
import server.controller.Player;

import java.io.Closeable;

public abstract class VirtualClient implements Closeable {
    protected final LobbyList lobbyList;
    protected Player player;

    protected VirtualClient(LobbyList lobbyList) {
        this.lobbyList = lobbyList;
    }

    public void setPlayer(Player player) {
        this.player = player;
        player.setClient(this);
        send("Logged in as " + player.getName());
    }

    public abstract void send(String s);

    @Override
    public abstract void close();
}
