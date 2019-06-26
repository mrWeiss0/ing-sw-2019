package server.connection;

import server.controller.LobbyList;
import server.controller.Player;
import server.model.board.Board;
import server.model.board.Targettable;

import java.io.Closeable;
import java.util.List;

public abstract class VirtualClient implements Closeable {
    protected final LobbyList lobbyList;
    protected Player player;

    protected VirtualClient(LobbyList lobbyList) {
        this.lobbyList = lobbyList;
    }

    public void setPlayer(Player player) {
        this.player = player;
        player.setClient(this);
        sendMessage("Logged in as " + player.getName());
    }

    public abstract void send(String s);

    public abstract void sendMessage(String s);

    public abstract void sendLobbyList(String[] s);

    public abstract void sendTargets(List<Targettable> targets, Board board);

    @Override
    public abstract void close();
}
