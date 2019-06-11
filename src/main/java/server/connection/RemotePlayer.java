package server.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemotePlayer extends Remote {
    void login(String username) throws RemoteException;
    void createLobby(String name) throws RemoteException;
    void joinLobby(String name) throws RemoteException;
    void quitLobby(String name)throws RemoteException;
    void selectPowerUp(int[] selected)throws RemoteException;
    void selectWeapon(int[] selected)throws RemoteException;
    void selectFireMode(int weaponIndex, int[] selectedFireModes)throws RemoteException;
    void selectGrabbable(int index)throws RemoteException;
    void selectTargettable(int[] selected)throws RemoteException;
    void selectColor(int color)throws RemoteException;
    void selectAction(int actionIndex)throws RemoteException;
}
