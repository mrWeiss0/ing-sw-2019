package connection.rmi;

import connection.messages.responses.Response;
import view.TextView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteView extends Remote, TextView {
    void handle(Response m) throws RemoteException;

    void setController(RemoteController remoteController) throws RemoteException;
}
