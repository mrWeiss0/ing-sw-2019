package server.connection;

import server.controller.LobbyList;
import server.controller.Player;
import server.model.PowerUp;
import server.model.board.Board;
import server.model.board.Figure;
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

    public abstract void sendTargets(int min, int max, List<Targettable> targets, Board board);

    public abstract void sendPowerUps(List<PowerUp> powerUps);

    public abstract void sendCurrentPlayer(int currentPlayer);

    public abstract void sendPossibleActions(List<Integer> possibleActions);

    //0:map_type, 1:max_kills
    public abstract void sendGameParams(List<Integer> gameParams);

    public abstract void sendKillTrack(List<Figure> killTrack, List<Boolean> overkills);

    @Override
    public abstract void close();
}
