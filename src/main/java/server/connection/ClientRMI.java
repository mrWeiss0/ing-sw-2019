package server.connection;

import client.connection.RemoteClient;
import client.model.GameState;
import server.Main;
import server.controller.LobbyList;
import server.controller.Player;
import server.model.AmmoCube;
import server.model.AmmoTile;
import server.model.PowerUp;
import server.model.board.*;
import server.model.weapon.Weapon;

import java.io.FileNotFoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ClientRMI extends VirtualClient implements RemotePlayer {
    private final RemoteClient remoteClient;
    private final String RMI_ERROR = "RMI send exception";

    public ClientRMI(LobbyList lobbyList, RemoteClient remoteClient) {
        super(lobbyList);
        this.remoteClient = remoteClient;
        sendGameState(GameState.NOT_LOGGED_IN.ordinal());
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
    public void sendMessage(String s) {
        send(s);
    }

    @Override
    public void sendLobbyList(String[] s) {
        try {
            remoteClient.sendLobbyList(s);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendTargets(int min, int max, List<Targettable> targets, Board board) {
        try {
            remoteClient.sendTargets(min, max, targets.stream().map(board::getID).collect(Collectors.toList()));
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendCurrentPlayer(int currentPlayer) {
        try {
            remoteClient.sendCurrentPlayer(currentPlayer);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPossibleActions(int actionSetID) {
        try {
            remoteClient.sendPossibleActions(actionSetID);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPowerUps(List<PowerUp> powerUps) {
        try {
            remoteClient.sendPowerUps(powerUps.stream()
                    .map(x -> new Integer[]{x.getType().ordinal(), x.getColor()})
                    .collect(Collectors.toList()));
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendGameParams(List<Integer> gameParams) {
        try {
            remoteClient.sendGameParams(gameParams);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendKillTrack(List<Figure> killTrack, List<Boolean> overkills) {
        Boolean[] wrapper = overkills.toArray(Boolean[]::new);
        boolean[] ok = new boolean[wrapper.length];
        for (int i = 0; i < wrapper.length; i++)
            ok[i] = wrapper[i];
        try {
            remoteClient.sendKillTrack(killTrack.stream()
                            .mapToInt(x -> player.getGame().getGame().getBoard().getFigures().indexOf(x)).toArray()
                    , ok);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendSquares(List<AbstractSquare> squares) {
        int[][] coordinates = squares.stream().map(AbstractSquare::getCoordinates).toArray(int[][]::new);
        int[] rooms = squares.stream().mapToInt(x -> player.getGame().getGame().getBoard().getRooms().indexOf(x.getRoom())).toArray();
        Boolean[] wrapper = squares.stream().map(x -> x instanceof SpawnSquare).toArray(Boolean[]::new);
        boolean[] spawn = new boolean[wrapper.length];
        for (int i = 0; i < wrapper.length; i++)
            spawn[i] = wrapper[i];
        try {
            remoteClient.sendSquares(coordinates, rooms, spawn);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendSquareContent(AbstractSquare square) {
        int id = player.getGame().getGame().getBoard().getSquares().indexOf(square);
        int tileID = 0;
        int[] weapons = null;
        if (square.peek().get(0) instanceof Weapon)
            weapons = square.peek().stream().mapToInt(x -> ((Weapon) x).getID()).toArray();
        else {
            tileID = ((AmmoTile) square.peek().get(0)).getId();
        }
        try {
            remoteClient.sendSquareContent(id, tileID, weapons);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPlayers(List<Player> players) {
        int[] avatars = players.stream().mapToInt(x -> x.getGame().getGame().getPlayers().indexOf(x)).toArray();
        String[] names = players.stream().map(x -> x.getName()).toArray(String[]::new);
        try {
            remoteClient.sendPlayers(avatars, names);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPlayerDamages(Player player) {
        List<Player> players = player.getGame().getGame().getPlayers();
        int[] damages = player.getFigure().getDamages().stream()
                .mapToInt(x -> players.indexOf(x.getPlayer()))
                .toArray();
        try {
            remoteClient.sendPlayerDamages(players.indexOf(player), damages);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPlayerMarks(Player player) {
        List<Player> players = player.getGame().getGame().getPlayers();
        int[] marks = player.getFigure().getMarks().stream()
                .mapToInt(x -> players.indexOf(x.getPlayer()))
                .toArray();
        try {
            remoteClient.sendPlayerMarks(players.indexOf(player), marks);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPlayerLocation(Player player) {
        int id = player.getGame().getGame().getPlayers().indexOf(player);
        if(player.getFigure().getLocation()==null){
            try {
                remoteClient.sendPlayerLocation(id, new int[]{-1,-1});
            } catch (RemoteException e) {
                Main.LOGGER.warning(RMI_ERROR);
                close();
            }
        }
        int[] coords = player.getFigure().getLocation().getCoordinates();
        try {
            remoteClient.sendPlayerLocation(id, coords);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPlayerPoints(Player player) {
        try {
            remoteClient.sendPlayerPoints(player.getGame().getGame().getPlayers().indexOf(player)
                    , player.getFigure().getPoints());
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPlayerDeaths(Player player) {
        try {
            remoteClient.sendPlayerDeaths(player.getGame().getGame().getPlayers().indexOf(player)
                    , player.getFigure().getDeaths());
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPlayerAmmo(Player player) {
        int[] ammo = new int[]{0, 0, 0};
        for (int i = 0; i < 3; i++)
            ammo[i] = player.getFigure().getAmmo().value(i);
        try {
            remoteClient.sendPlayerAmmo(player.getGame().getGame().getPlayers().indexOf(player), ammo);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPlayerNPowerUps(Player player) {
        try {
            remoteClient.sendPlayerNPowerUps(player.getGame().getGame().getPlayers().indexOf(player)
                    , player.getFigure().getPowerUps().size());
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendPlayerWeapons(Player player) {
        int[] weaponsIDs = player.getFigure().getWeapons().stream().mapToInt(Weapon::getID).toArray();
        Boolean[] wrapper = player.getFigure().getWeapons().stream().map(Weapon::isLoaded).toArray(Boolean[]::new);
        boolean[] charges = new boolean[wrapper.length];
        for (int i = 0; i < wrapper.length; i++)
            charges[i] = wrapper[i];
        try {
            remoteClient.sendPlayerWeapons(player.getGame().getGame().getPlayers().indexOf(player)
                    , weaponsIDs, charges);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendRemainingActions(int remaining) {
        try {
            remoteClient.sendRemainingActions(remaining);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendGameState(int value) {
        try {
            remoteClient.sendGameState(value);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendCountDown(int value) {
        try {
            remoteClient.sendCountDown(value);
        } catch (RemoteException e) {
            Main.LOGGER.warning(RMI_ERROR);
            close();
        }
    }

    @Override
    public void sendChatMessage(String name, String msg) {
        try {
            remoteClient.sendChatMessage(name, msg);
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
        } catch (FileNotFoundException e) {
            sendMessage("Server Error");
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
    public void quitLobby() {
        try {
            lobbyList.remove(player);
        } catch (IllegalStateException | NoSuchElementException e) {
            sendMessage(e.toString());
        }
    }

    @Override
    public void selectPowerUp(int[] selected) {
        player.selectPowerUp(selected);
    }

    @Override
    public void selectWeapon(int[] selected) {
        player.selectWeaponToReload(selected);
    }

    @Override
    public void selectFireMode(int weaponIndex, int[] selectedFireModes) {
        player.selectWeaponFireMode(weaponIndex, selectedFireModes);
    }

    @Override
    public void selectGrabbable(int index) {
        player.selectGrabbable(index);
    }

    @Override
    public void selectTargettable(int[] selected) {
        player.selectTargettable(selected);
    }

    @Override
    public void selectColor(int color) {
        player.selectColor(color);
    }

    @Override
    public void selectAction(int actionIndex) {
        player.selectAction(actionIndex);
    }

    @Override
    public void chatMessage(String msg) {
        try {
            lobbyList.chatMessage(this, msg);
        } catch (IllegalStateException e) {
            sendMessage(e.getMessage());
        }
    }

    @Override
    public void reconnect() {
        player.setActive();
        player.updateAll();
    }
}
