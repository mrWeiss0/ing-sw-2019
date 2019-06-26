package client.connection;

import client.Client;
import server.connection.RemoteConnection;
import server.connection.RemotePlayer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
        }catch(NullPointerException e){
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
    public void quitLobby(String name) {
        try {
            player.quitLobby(name);
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


    //TODO SERVER->CLIENT
    @Override
    public void send(String s) throws RemoteException {
        controller.print(s);
    }

    @Override
    public void sendLobbyList(String[] s) throws RemoteException{
        controller.setLobbyList(s);
    }

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    @Override
    public void sendTargets(List<Integer> targets) throws RemoteException {
        //TODO
    }
}
