package client;

import connection.rmi.RemoteController;
import connection.rmi.RemoteView;
import connection.socket.VirtualController;
import view.TextView;
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
    private String name;
    private String id = "";
    private RemoteController controller;
    private LineHandler lineHandler;

    public Client(String destIp, int destPort, String name) {
        this.destIp = destIp;
        this.destPort = destPort;
        this.linein = new Scanner(System.in);
        this.name = name;
    }
    public void connectRMI() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        TextView textView = new ViewRMI(this);
        controller = (RemoteController) registry.lookup("connection handler");
        lineHandler = new LineHandler(linein, textView, this);
        controller.notifyConnection((RemoteView) textView,name);
        lineHandler.run();
        controller.logout(id);
    }

    public void connectSocket(){
        try ( Socket socket = new Socket(destIp, destPort)){
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            controller = new VirtualController(this, outputStream);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ViewSocket t = new ViewSocket(inputStream, this);
            Thread thread = new Thread(t);
            thread.start();
            lineHandler = new LineHandler(linein, t, this);
            controller.notifyConnection(t,name);
            lineHandler.run();
            t.stop();
            inputStream.close();
            outputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void saveUuid(String uuid) {
        if (this.id.isEmpty())
            this.id = uuid;
    }


    public String getName() {
        return name;
    }


    public String getID() {
        return this.id;
    }

    public RemoteController getController() {
        return controller;
    }
    public void setController(RemoteController remoteController){
        this.controller=remoteController;
    }

    public static void main(String[] args) {
        Client c = new Client("localhost",9900,"miki1");
        c.connectSocket();
    }
}
