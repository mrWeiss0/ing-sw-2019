package view;

import client.Client;
import connection.messages.responses.Response;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ViewRMI extends UnicastRemoteObject implements RemoteView, TextView {
    private Client client;
    private ViewResponseDisplay messageHandler;
    public ViewRMI(Client client)throws RemoteException {
        super();
        this.client = client;
        messageHandler = new ViewResponseDisplay(client);
    }

    @Override
    public void handle(Response m) throws RemoteException{
        m.handle(messageHandler);
    }

    @Override
    public void setController(RemoteController remoteController)throws RemoteException{
        client.setController(remoteController);
    }
}
