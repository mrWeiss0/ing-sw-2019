package server;

import connection.socket.ClientHandlerSocket;
import controller.Controller;
import controller.LobbyList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private boolean isRunning = true;
    private ExecutorService pool;
    private LobbyList lobbyList;

    public Server(int portSocket, int portRMI) throws IOException {
        pool = Executors.newCachedThreadPool();
        System.out.println(">>Socket: Server Started on Port: " + portSocket);
        lobbyList = new LobbyList();
        Controller firstController = new Controller();
        lobbyList.addController(firstController);

        serverSocket = new ServerSocket(portSocket);

        Registry registry = LocateRegistry.createRegistry(portRMI);

        registry.rebind("connection handler", lobbyList);
        System.out.println(">>RMI: RMI handler exposed");

    }

    public void run() {
        Socket socket;
        while (isRunning) {
            try {
                socket = serverSocket.accept();
                System.out.println(">>Socket: Connection by: " + socket.getRemoteSocketAddress());
                pool.submit(new ClientHandlerSocket(socket, lobbyList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        this.isRunning = false;
        serverSocket.close();
        pool.shutdown();
    }


    public static void main(String[] args) throws IOException {
        Server server = new Server(9900, 1099);
        Scanner sin = new Scanner(System.in);
        Thread thread = new Thread(server);
        thread.start();
        String in = "";
        while (!in.startsWith("quit")) {
            in = sin.nextLine();
        }
        try {
            server.close();
        } catch (IOException ignored) {
        }
        thread.interrupt();
    }
}
