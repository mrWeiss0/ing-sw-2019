package client;

import client.connection.Connection;
import client.connection.RMIConnection;
import client.connection.SocketConnection;
import client.model.*;
import client.view.commandLine.CLICommandView;
import client.view.View;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Client {
    private final Connection connection;
    private final View view;
    private final MiniModel model;
    private final Config config;

    public Client(Config config) {
        this.config = config;
        this.view = viewFactory();
        this.connection = connectionFactory();
        model = new MiniModel(view);
    }

    @SuppressWarnings("unchecked")
    private View viewFactory() {
        Supplier<View>[] viewSupplier = new Supplier[]{
                () -> new CLICommandView(this, config.CMD_DELIMITER, config.ARG_DELIMITER),
                () -> null//TODO GUI
        };
        return viewSupplier[config.view_type].get();
    }

    @SuppressWarnings("unchecked")
    private Connection connectionFactory() {
        Supplier<Connection>[] connectionSupp = new Supplier[]{
                () -> new SocketConnection(this),
                () -> new RMIConnection(this)
        };
        return connectionSupp[config.connection_type].get();
    }

    public void connect(String host) {
        try {
            connection.close();
            connection.connect(host, config.port[config.connection_type]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //CLIENT->SERVER METHODS:

    public void endTurn() {
        connection.endTurn();
    }

    public void login(String name) {
        connection.login(name);
    }

    public void createLobby(String name) {
        connection.createLobby(name);
    }

    public void joinLobby(String name) {
        connection.joinLobby(name);
    }

    public void quitLobby() {
        connection.quitLobby();
    }

    public void selectPowerUp(int[] selected) {
        connection.selectPowerUp(selected);
    }

    public void selectWeapon(int[] selected) {
        connection.selectWeapon(selected);
    }

    public void selectFireMode(int weaponIndex, int[] selectedFireModes) {
        connection.selectFireMode(weaponIndex, selectedFireModes);
    }

    public void selectGrabbable(int index) {
        connection.selectGrabbable(index);
    }

    public void selectTargettable(int[] selected) {
        connection.selectTargettable(selected);
    }

    public void selectColor(int color) {
        connection.selectColor(color);
    }

    public void selectAction(int actionIndex) {
        connection.selectAction(actionIndex);
    }

    public void chatMessage(String msg) {
        connection.sendChat(msg);
    }

    public void reconnect() {
        connection.reconnect();
    }

    //END

    public void start() {
        view.start();
    }

    public void print(String toPrint) {
        view.displayMessage(toPrint);
    }

    public void setLobbyList(String[] s) {
        model.setLobbyList(s);
    }

    public void setPossibleTargets(int min, int max, int[] targets) {
        model.setMinToSelect(min);
        model.setMaxToSelect(max);
        model.setPossibleTargets(targets);
    }

    public void setPowerUps(int[][] powerUps) {
        model.setPowerups(powerUps);
    }

    public void setCurrentPlayer(int currentPlayer) {
        model.setCurrentPlayer(currentPlayer);
    }

    public void setPossibleActions(int actionSetID) {
        model.setPossibleActions(actionSetID);
    }

    public void setGameParams(int[] gameParams) {
        model.getBoard().setMapType(gameParams[0]);
        model.getBoard().setMaxKills(gameParams[1]);
    }

    public void setKillTrack(int[] killTrack, boolean[] overkills) {
        model.getBoard().setKillTrack(killTrack);
        model.getBoard().setOverkills(overkills);
    }

    public void setSquares(int[][] coords, int[] rooms, boolean[] spawn) {
        model.getBoard().setSquares(IntStream.range(0, coords.length)
                .mapToObj(x -> new Square(view, coords[x], spawn[x], rooms[x]))
                .toArray(Square[]::new));
    }

    public void setSquareContent(int squareID, int tileID, int[] weapons, int[][] pcost) {
        model.getBoard().getSquares()[squareID].setTileID(tileID);
        model.getBoard().getSquares()[squareID].setWeapons(weapons, pcost);
    }

    public void setPlayers(int[] avatars, String[] names) {
        model.getBoard().setPlayers(IntStream
                .range(0, avatars.length)
                .mapToObj(x -> new Player(view, names[x], avatars[x]))
                .toArray(Player[]::new));
    }

    public void setPlayerDamages(int id, int[] damages) {
        model.getBoard().getPlayers()[id].setDamages(damages);
    }

    public void setPlayerMarks(int id, int[] marks) {
        model.getBoard().getPlayers()[id].setMarks(marks);
    }

    public void setPlayerLocation(int id, int[] coords) {
        model.getBoard().getPlayers()[id].setCoordinates(coords);
    }

    public void setPlayerPoints(int id, int points) {
        model.getBoard().getPlayers()[id].setPoints(points);
    }

    public void setPlayerNPowerUps(int id, int nPowerUps) {
        model.getBoard().getPlayers()[id].setnPowerup(nPowerUps);
    }

    public void setPlayerDeaths(int id, int deaths) {
        model.getBoard().getPlayers()[id].setDeaths(deaths);
    }

    public void setPlayerAmmo(int id, int[] ammo) {
        model.getBoard().getPlayers()[id].setAmmo(ammo);
    }

    public void setPlayerWeapons(int id, int[] weaponIDs, String[] names, int[][] lcost, boolean[] charges) {
        Weapon[] weapons = IntStream.range(0, weaponIDs.length).mapToObj(x -> new Weapon(weaponIDs[x], names[x], lcost[x])).toArray(Weapon[]::new);
        IntStream.range(0, weapons.length).forEach(x -> weapons[x].setLoaded(charges[x]));
        model.getBoard().getPlayers()[id].setWeapons(weapons);
    }

    public void setGameState(int value) {
        model.setGameState(value);
    }

    public void setRemainingTime(int remainingTime) {
        model.setRemainingTime(remainingTime);
    }

    public void addChatMessage(String name, String msg) {
        model.addChatMessage(name, msg);
    }

    public void setRemainingActions(int remainingActions) {
        model.setRemainingActions(remainingActions);
    }

    public void setPlayerID(int id) {
        model.setPlayerID(id);
    }

    public void setPlayerLeaderBoard(int[] points) {
        IntStream.range(0, points.length)
                .forEach(x -> model.getBoard().getPlayers()[x].setLeaderBoard(points[x]));
    }

    public void setNKills(int[] nKills) {
        IntStream.range(0, nKills.length)
                .forEach(x -> model.getBoard().getPlayers()[x].setNKills(nKills[x]));
    }

    public void displayLobby() {
        view.displayLobbyList(model.getLobbyList());
    }

    public void exit() {
        connection.close();
        view.exit();
    }
}
