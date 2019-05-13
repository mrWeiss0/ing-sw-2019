package connection.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteConnectionHandler extends Remote {
    RemoteController notifyConnection(RemoteView remoteView, String username) throws RemoteException;
    RemoteController reconnect(String id, String username, RemoteView remoteView) throws RemoteException;
}
