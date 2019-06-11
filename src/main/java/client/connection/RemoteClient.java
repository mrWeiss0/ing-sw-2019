package client.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClient extends Remote {
    void send(String s) throws RemoteException;
    boolean ping() throws RemoteException;
}
