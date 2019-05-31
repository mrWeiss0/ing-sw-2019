package server;

import client.connection.RemoteClient;
import server.connection.*;
import server.controller.Player;
import server.model.Game;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Closeable {
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final Registry registry;
    private final RemoteConnection serverRMI = this::rmiListener;
    private final ArrayDeque<Game.Builder> lobbyList = new ArrayDeque<>();
    private final Map<String, Player> players = new HashMap<>();

    public Server() throws IOException {
        registry = LocateRegistry.createRegistry(1099);
        serverSocket = new ServerSocket(9900);
    }

    public void start() throws IOException {
        registry.rebind("server.connection", UnicastRemoteObject.exportObject(serverRMI, 0));
        new Thread(this::socketListener).start();
    }

    public void registerPlayer(String username, VirtualClient client) {
        Player player = players.get(username);
        if (player == null) {
            player = new Player(username);
            players.put(username, player);
            Game.Builder game = getJoinableGame();
            game.addPlayer(player);
        } else if (player.isOnline()) {
            client.send("Username " + username + " already taken");
            return;
        } else player.setOnline();
        client.setPlayer(player);
    }

    private Game.Builder getJoinableGame() {
        Game.Builder game = lobbyList.peek();
        if (game == null) {
            game = new Game.Builder();
            lobbyList.add(game);
        }
        return game;
    }

    @Override
    public void close() {
        for (Player p : players.values())
            p.getClient().close();
        // Close Socket
        try {
            serverSocket.close();
        } catch (IOException e) {
            Main.LOGGER.warning(e::toString);
        }
        threadPool.shutdownNow();
        // Close RMI
        try {
            UnicastRemoteObject.unexportObject(serverRMI, true);
            registry.unbind("server.connection");
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (RemoteException | NotBoundException e) {
            Main.LOGGER.warning(e::toString);
        }
    }

    private void socketListener() {
        try (serverSocket) {
            while (!serverSocket.isClosed())
                threadPool.submit(new ClientSocket(this, serverSocket.accept()));
        } catch (IOException e) {
            Main.LOGGER.info(e::toString);
        }
    }

    private RemotePlayer rmiListener(RemoteClient remoteClient) throws RemoteException {
        return (RemotePlayer) UnicastRemoteObject.exportObject(new ClientRMI(this, remoteClient), 0);
    }
}
