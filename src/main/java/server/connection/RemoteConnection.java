package server.connection;

import client.RemoteClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteConnection extends Remote {
    RemotePlayer login(RemoteClient remoteView) throws RemoteException;
}
