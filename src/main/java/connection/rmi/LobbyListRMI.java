package connection.rmi;

import connection.server.VirtualView;
import controller.LobbyList;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LobbyListRMI extends UnicastRemoteObject implements RemoteConnectionHandler, Serializable {
    private LobbyList lobbyList;
    public LobbyListRMI(LobbyList lobbyList) throws RemoteException {
        super();
        this.lobbyList=lobbyList;
    }

    @Override
    public void notifyConnection(RemoteView remoteView, String username) throws RemoteException {
        VirtualView virtualView= new VirtualViewRMI(remoteView);
        lobbyList.notifyConnection(virtualView, username);
    }
}
