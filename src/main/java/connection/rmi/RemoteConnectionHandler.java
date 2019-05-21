package connection.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteConnectionHandler extends Remote {
    void notifyConnection(RemoteView remoteView, String username) throws RemoteException;
}
