package server.connection;

import server.Server;
import server.controller.Player;

import java.io.Closeable;

public abstract class VirtualClient implements Closeable {
    protected final Server server;
    protected Player player;

    protected VirtualClient(Server server) {
        this.server = server;
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
