package client.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteClient extends Remote {
    void send(String s) throws RemoteException;
    void sendLobbyList(String[] s) throws RemoteException;
    void sendTargets(List<Integer> targets) throws RemoteException;
    boolean ping() throws RemoteException;
}
