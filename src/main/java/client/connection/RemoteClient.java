package client.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteClient extends Remote {
    void send(String s) throws RemoteException;

    void sendLobbyList(String[] s) throws RemoteException;

    void sendTargets(int min, int max, List<Integer> targets) throws RemoteException;

    void sendPowerUps(List<int[]> powerUps) throws RemoteException;

    void sendCurrentPlayer(int currentPlayer) throws RemoteException;

    void sendPossibleActions(int actionSetID) throws RemoteException;

    void sendGameParams(List<Integer> gameParams) throws RemoteException;

    void sendKillTrack(int[] killTrack, boolean[] overkills) throws RemoteException;

    void sendSquares(int[][] coordinates, int[] rooms, boolean[] spawn) throws RemoteException;

    void sendSquareContent(int squareID, int tileID, int[] weapons, int[][] pcost) throws RemoteException;

    void sendPlayers(int[] avatars, String[] names) throws RemoteException;

    void sendPlayerDamages(int id, int[] damages) throws RemoteException;

    void sendPlayerMarks(int id, int[] marks) throws RemoteException;

    void sendPlayerLocation(int id, int[] coords) throws RemoteException;

    void sendPlayerPoints(int id, int points) throws RemoteException;

    void sendPlayerNPowerUps(int id, int nPowerUps) throws RemoteException;

    void sendPlayerDeaths(int id, int deaths) throws RemoteException;

    void sendPlayerAmmo(int id, int[] ammo) throws RemoteException;

    void sendPlayerWeapons(int id, int[] weaponIDs, String[] names, int[][] lcost, boolean[] charges) throws RemoteException;

    void sendRemainingActions(int remainingActions) throws RemoteException;

    void sendGameState(int value) throws RemoteException;

    void sendChatMessage(String name, String msg) throws RemoteException;

    void sendCountDown(int remaining) throws RemoteException;

    void sendPlayerID(int id) throws RemoteException;

    void sendLeaderBoard(int[] points) throws RemoteException;

    void sendNKills(int[] kills) throws RemoteException;

    boolean ping() throws RemoteException;


}
