package connection;

import client.RemoteClient;
import controller.Player;
import model.Game;

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

public class Server implements Closeable, Runnable {
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final Registry registry;
    private final RemoteConnection serverRMI = this::rmiListener;
    private final ArrayDeque<Game.Builder> lobbyList = new ArrayDeque<>();
    private final Map<String, Player> players = new HashMap<>();

    public Server() throws IOException {
        registry = LocateRegistry.createRegistry(1099);
        registry.rebind("connection", UnicastRemoteObject.exportObject(serverRMI, 0));
        serverSocket = new ServerSocket(9900);
    }

    public Player registerPlayer(String username) throws LoginException {
        Player player = players.get(username);
        if (player == null) {
            player = new Player(username);
            players.put(username, player);
            Game.Builder game = getJoinableGame();
            game.addPlayer(player);
        } else if (player.isOnline()) throw new LoginException("Username already taken");
        else player.setOnline();
        return player;
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
    public void run() {
        new Thread(this::socketListener).start();
    }

    @Override
    public void close() {
        for (Player p : players.values())
            p.getClient().close();
        // Close Socket
        try {
            serverSocket.close();
        } catch (IOException e) {
            Main.logger.warning(e::toString);
        }
        threadPool.shutdownNow();
        // Close RMI
        try {
            UnicastRemoteObject.unexportObject(serverRMI, true);
            registry.unbind("connection");
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (RemoteException | NotBoundException e) {
            Main.logger.warning(e::toString);
        }
    }

    private void socketListener() {
        try {
            while (!serverSocket.isClosed()) {
                threadPool.submit(new ClientSocket(this, serverSocket.accept()));
            }
        } catch (IOException e) {
            close();
        }
    }

    private RemotePlayer rmiListener(RemoteClient remoteView) throws RemoteException {
        return (RemotePlayer) UnicastRemoteObject.exportObject(new ClientRMI(this, remoteView), 0);
    }
}
