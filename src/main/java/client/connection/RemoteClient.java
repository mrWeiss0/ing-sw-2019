package client.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteClient extends Remote {
    void send(String s) throws RemoteException;
    void sendLobbyList(String[] s) throws RemoteException;
    void sendTargets(int min, int max, List<Integer> targets) throws RemoteException;
    void sendPowerUps(List<Integer[]> powerUps) throws RemoteException;
    void sendCurrentPlayer(int currentPlayer) throws RemoteException;
    void sendPossibleActions(List<Integer> possibleActions) throws RemoteException;
    void sendGameParams(List<Integer> gameParams) throws RemoteException;
    void sendKillTrack(int[] killTrack, boolean[] overkills)throws RemoteException;
    boolean ping() throws RemoteException;


}
