package view;

import client.Client;
import connection.messages.responses.Response;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;

public class ViewSocket implements Runnable, TextView {
    private ObjectInputStream sin;
    private boolean stop = false;
    private ViewResponseDisplay messageHandler;

    public ViewSocket(ObjectInputStream sin, Client client) {
        this.sin = sin;
        messageHandler= new ViewResponseDisplay(client);
    }

    public void run() {
        while (!stop) {
            try {
                Response received = (Response) sin.readObject();
                received.handle(messageHandler);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void handle(Response m) throws RemoteException {
        m.handle(messageHandler);
    }

}
