package server;

import client.connection.RemoteClient;
import server.connection.ClientRMI;
import server.connection.ClientSocket;
import server.connection.RemoteConnection;
import server.connection.RemotePlayer;
import server.controller.LobbyList;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Closeable, RemoteConnection {
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final Registry registry;
    private final LobbyList lobbyList;
    private final Config config;

    public Server(Config config) throws IOException {
        this.config = config;
        registry = LocateRegistry.createRegistry(config.RMI_PORT);
        serverSocket = new ServerSocket(config.SOCKET_PORT);
        lobbyList = new LobbyList(config);
    }

    public void start() throws IOException {
        registry.rebind("server.connection", UnicastRemoteObject.exportObject(this, 0));
        new Thread(this::socketListener).start();
    }

    @Override
    public void close() {
        // Close Socket
        try {
            serverSocket.close();
        } catch (IOException e) {
            Main.LOGGER.warning(e::toString);
        }
        threadPool.shutdownNow();
        // Close RMI
        try {
            UnicastRemoteObject.unexportObject(this, true);
            registry.unbind("server.connection");
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (RemoteException | NotBoundException e) {
            Main.LOGGER.warning(e::toString);
        }
    }

    private void socketListener() {
        try (serverSocket) {
            while (!serverSocket.isClosed())
                threadPool.submit(new ClientSocket(lobbyList, serverSocket.accept()));
        } catch (IOException e) {
            Main.LOGGER.info(e::toString);
        }
    }

    public RemotePlayer connectRMI(RemoteClient remoteClient) throws RemoteException {
        return (RemotePlayer) UnicastRemoteObject.exportObject(new ClientRMI(lobbyList, remoteClient), 0);
    }

}
