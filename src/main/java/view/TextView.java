package view;

import connection.messages.responses.Response;

import java.rmi.RemoteException;

public interface TextView {
    void handle(Response m) throws RemoteException;
}
