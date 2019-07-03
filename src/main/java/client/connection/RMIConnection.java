package client.connection;

import client.Client;
import server.connection.RemoteConnection;
import server.connection.RemotePlayer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

public class RMIConnection implements Connection, RemoteClient {
    private static final String ERRORSTRING = "Error, you are not connected, use connect";
    private final Client controller;
    private Registry registry;
    private RemoteConnection remote;
    private RemotePlayer player;

    public RMIConnection(Client controller) {
        this.controller = controller;
    }

    @Override
    public void connect(String host, int port) throws Exception {
        registry = LocateRegistry.getRegistry(host, port);
        remote = (RemoteConnection) registry.lookup("server.connection");
        UnicastRemoteObject.exportObject(this, 0);
        player = remote.connectRMI(this);
    }

    @Override
    public void login(String username) {
        try {
            player.login(username);
        } catch (RemoteException e) {
            controller.print("RemoteException");
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void createLobby(String name) {
        try {
            player.createLobby(name);
        } catch (RemoteException e) {
            controller.print(e.toString());
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void joinLobby(String name) {
        try {
            player.joinLobby(name);
        } catch (RemoteException e) {
            controller.print(e.toString());
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void quitLobby() {
        try {
            player.quitLobby();
        } catch (RemoteException e) {
            controller.print(e.toString());
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void selectPowerUp(int[] selected) {
        try {
            player.selectPowerUp(selected);
        } catch (RemoteException e) {
            controller.print(e.toString());
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void selectWeapon(int[] selected) {
        try {
            player.selectWeapon(selected);
        } catch (RemoteException e) {
            controller.print(e.toString());
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void selectFireMode(int weaponIndex, int[] selectedFireModes) {
        try {
            player.selectFireMode(weaponIndex, selectedFireModes);
        } catch (RemoteException e) {
            controller.print(e.toString());
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void selectGrabbable(int index) {
        try {
            player.selectGrabbable(index);
        } catch (RemoteException e) {
            controller.print(e.toString());
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void selectTargettable(int[] selected) {
        try {
            player.selectTargettable(selected);
        } catch (RemoteException e) {
            controller.print(e.toString());
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void selectColor(int color) {
        try {
            player.selectColor(color);
        } catch (RemoteException e) {
            controller.print(e.toString());
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void selectAction(int actionIndex) {
        try {
            player.selectAction(actionIndex);
        } catch (RemoteException e) {
            controller.print(e.toString());
        } catch (NullPointerException e) {
            controller.print(ERRORSTRING);
        }
    }

    @Override
    public void sendChat(String msg) {
        try {
            player.chatMessage(msg);
        } catch (RemoteException e) {
            controller.print(e.getMessage());
        }
    }

    @Override
    public void reconnect() {
        try {
            player.reconnect();
        } catch (RemoteException e) {
            controller.print(e.getMessage());
        }
    }

    //TODO SERVER->CLIENT
    @Override
    public void send(String s) throws RemoteException {
        controller.print(s);
    }

    @Override
    public void sendLobbyList(String[] s) throws RemoteException {
        controller.setLobbyList(s);
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    @Override
    public void sendTargets(int min, int max, List<Integer> targets) throws RemoteException {
        controller.setPossibleTargets(min, max, targets.stream().mapToInt(Integer::intValue).toArray());
    }

    @Override
    public void sendPowerUps(List<Integer[]> powerUps) {
        controller.setPowerUps((int[][]) powerUps.stream().map(x -> Arrays.stream(x).mapToInt(Integer::intValue).toArray()).toArray());
    }

    @Override
    public void sendCurrentPlayer(int currentPlayer) {
        controller.setCurrentPlayer(currentPlayer);
    }

    @Override
    public void sendPossibleActions(int actionSetID) {
        controller.setPossibleActions(actionSetID);
    }

    @Override
    public void sendGameParams(List<Integer> gameParams) {
        controller.setGameParams(gameParams.stream().mapToInt(Integer::intValue).toArray());
    }

    @Override
    public void sendKillTrack(int[] killtrack, boolean[] overkills) {
        controller.setKillTrack(killtrack, overkills);
    }

    @Override
    public void sendSquares(int[][] coordinates, int[] rooms, boolean[] spawn) {
        controller.setSquares(coordinates, rooms, spawn);
    }

    @Override
    public void sendSquareContent(int squareID, int tileID, int[] weapons) {
        controller.setSquareContent(squareID, tileID, weapons);
    }

    @Override
    public void sendPlayers(int[] avatars, String[] names) {
        controller.setPlayers(avatars, names);
    }

    @Override
    public void sendPlayerDamages(int id, int[] damages) {
        controller.setPlayerDamages(id, damages);
    }

    @Override
    public void sendPlayerMarks(int id, int[] marks) {
        controller.setPlayerMarks(id, marks);
    }

    @Override
    public void sendPlayerLocation(int id, int[] coords) {
        controller.setPlayerLocation(id, coords);
    }

    @Override
    public void sendPlayerPoints(int id, int points) {
        controller.setPlayerPoints(id, points);
    }

    @Override
    public void sendPlayerDeaths(int id, int deaths) {
        controller.setPlayerDeaths(id, deaths);
    }

    @Override
    public void sendPlayerAmmo(int id, int[] ammo) {
        controller.setPlayerAmmo(id, ammo);
    }

    @Override
    public void sendPlayerNPowerUps(int id, int nPowerUps) {
        controller.setPlayerNPowerUps(id, nPowerUps);
    }

    @Override
    public void sendPlayerWeapons(int id, int[] weaponsIDs, boolean[] charges) {
        controller.setPlayerWeapons(id, weaponsIDs, charges);
    }

    @Override
    public void sendRemainingActions(int remaining) {
        controller.setRemainingActions(remaining);
    }

    @Override
    public void sendGameState(int value) {
        controller.setGameState(value);
    }

    @Override
    public void sendChatMessage(String name, String msg) {
        controller.addChatMessage(name, msg);
    }

    @Override
    public void sendCountDown(int remaining) {
        controller.setRemainingTime(remaining);
    }
}
