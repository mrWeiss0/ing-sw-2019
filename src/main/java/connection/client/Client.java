package connection.client;

import connection.rmi.RemoteConnectionHandler;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;
import connection.socket.VirtualController;
import view.ViewRMI;
import view.ViewSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    private String destIp;
    private int destPort;
    private Scanner linein;
    private String name = "";
    private String id = "";
    private RemoteController controller;
    private LineHandler lineHandler;

    public Client(String destIp, int destPort) {
        this.destIp = destIp;
        this.destPort = destPort;
        this.linein = new Scanner(System.in);
    }

    public static void main(String[] args) throws NotBoundException, IOException {
        Client c = new Client("localhost", 9900);
        c.connectRMI();
    }

    public void connectRMI() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        RemoteView textView = new ViewRMI(this);
        RemoteConnectionHandler connectionHandler = (RemoteConnectionHandler) registry.lookup("connection handler");
        controller = connectionHandler.notifyConnection(textView, name);
        lineHandler = new LineHandler(linein, textView, this);
        lineHandler.run();
        controller.logout(id);
    }

    public void connectSocket() throws IOException {
        try (Socket socket = new Socket(destIp, destPort)) {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            controller = new VirtualController(this, outputStream);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ViewSocket t = new ViewSocket(inputStream, this);
            Thread thread = new Thread(t);
            thread.start();
            lineHandler = new LineHandler(linein, t, this);
            ((VirtualController) controller).notifyConnection(null, name);
            lineHandler.run();
            t.stop();
            inputStream.close();
            outputStream.close();
        }
    }

    public void saveUuid(String uuid) {
        if (this.id.isEmpty())
            this.id = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return this.id;
    }

    public RemoteController getController() {
        return controller;
    }

    public void setController(RemoteController remoteController) {
        this.controller = remoteController;
    }

}
