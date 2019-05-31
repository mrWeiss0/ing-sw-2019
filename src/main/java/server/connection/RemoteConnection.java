package server.connection;

import client.connection.RemoteClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteConnection extends Remote {
    RemotePlayer connect(RemoteClient remoteClient) throws RemoteException;
}
