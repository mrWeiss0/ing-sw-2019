package controller;

import connection.rmi.LobbyListRMI;
import connection.socket.ClientHandlerSocket;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Closeable {
    private ServerSocket serverSocket;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private LobbyList lobbyList;

    public Server() throws IOException {
        lobbyList = new LobbyList(10);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("connection handler", new LobbyListRMI(lobbyList));
        serverSocket = new ServerSocket(9900);
        new Thread(this::socketListener).start();
    }

    private void socketListener() {
        try {
            while (!serverSocket.isClosed())
                threadPool.submit(new ClientHandlerSocket(serverSocket.accept(), lobbyList));
        } catch (IOException e) {
            close();
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException ignore) {
        }
        threadPool.shutdown();
    }
}
