package connection.rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;

//TODO AGGIUNGI NUOVI METODI AL CONTROLLER
public interface RemoteController extends Remote {
    void logout(String id) throws RemoteException;

    void sendText(String text, String id) throws RemoteException;
}
