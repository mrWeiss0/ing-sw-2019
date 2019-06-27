package server.connection;

import client.connection.RemoteClient;
import server.Main;
import server.controller.LobbyList;
import server.model.board.Board;
import server.model.board.Targettable;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ClientRMI extends VirtualClient implements RemotePlayer {
    private final RemoteClient remoteClient;
    private final String RMI_ERROR ="RMI send exception";

    public ClientRMI(LobbyList lobbyList, RemoteClient remoteClient) {
        super(lobbyList);
        this.remoteClient = remoteClient;
        sendMessage("Connected");
    }

    @Override
    public void send(String s) {
        try {
            remoteClient.send(s);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendMessage(String s){
        send(s);
    }

    @Override
    public void sendLobbyList(String[] s){
        try {
            remoteClient.sendLobbyList(s);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendTargets(List<Targettable> targets, Board board){
        try {
            remoteClient.sendTargets(targets.stream().map(board::getID).collect(Collectors.toList()));
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void close() {
        player.setOffline();
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            Main.LOGGER.warning(e::toString);
        }
    }

    @Override
    public void login(String username) {
        lobbyList.registerPlayer(username, this);
    }

    @Override
    public void createLobby(String name) {
        try {
            lobbyList.create(name);
        } catch (IllegalStateException e) {
            sendMessage(e.toString());
        }
    }

    @Override
    public void joinLobby(String name) {
        try {
            lobbyList.join(player, name);
        } catch (IllegalStateException | NoSuchElementException e) {
            sendMessage(e.toString());
        }
    }

    @Override
    public void quitLobby(String name) {
        try {
            lobbyList.remove(player, name);
        } catch (IllegalStateException | NoSuchElementException e) {
            sendMessage(e.toString());
        }
    }

    @Override
    public void selectPowerUp(int[] selected) {
        player.selectPowerUp(selected);
    }

    @Override
    public void selectWeapon(int[] selected) throws RemoteException {
        player.selectWeaponToReload(selected);
    }

    @Override
    public void selectFireMode(int weaponIndex, int[] selectedFireModes) throws RemoteException {
        player.selectWeaponFireMode(weaponIndex, selectedFireModes);
    }

    @Override
    public void selectGrabbable(int index) throws RemoteException {
        player.selectGrabbable(index);
    }

    @Override
    public void selectTargettable(int[] selected) throws RemoteException {
        player.selectTargettable(selected);
    }

    @Override
    public void selectColor(int color) throws RemoteException {
        player.selectColor(color);
    }

    @Override
    public void selectAction(int actionIndex) throws RemoteException {
        player.selectAction(actionIndex);
    }

}
