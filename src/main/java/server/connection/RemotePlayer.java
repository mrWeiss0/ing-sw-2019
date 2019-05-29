package server.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemotePlayer extends Remote {
    void login(String username) throws RemoteException, LoginException;
}
