package client;

import client.connection.Connection;
import client.connection.RMIConnection;
import client.connection.SocketConnection;
import client.model.MiniModel;
import client.model.PowerUp;
import client.model.Square;
import client.view.CLICommandView;
import client.view.View;

import java.util.List;
import java.util.stream.IntStream;

public class Client {
    private Connection connection;
    private View view;
    private State state;
    private MiniModel model;
    private State initState = new State() {
        @Override
        public void onEnter() {

        }
    };

    public Client() {
        view = new CLICommandView(this, "\\s+", "\\s+");
        connection = new SocketConnection(this);
        model= new MiniModel(view);
    }

    public void connect(String host) {
        try {
            //TODO FROM FILE/CLI
            connection.connect(host, 9900);
        } catch (Exception e) {
            view.print(e.toString());
        }
    }

    //CLIENT->SERVER METHODS:

    public void login(String name) {
        connection.login(name);
    }

    public void createLobby(String name) {
        connection.createLobby(name);
    }

    public void joinLobby(String name) {
        connection.joinLobby(name);
    }

    public void quitLobby(String name) {
        connection.quitLobby(name);
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

    //END

    public void start() {
        view.start();
    }

    public void print(String toPrint) {
        view.print(toPrint);
    }

    public void setLobbyList(String[] s){
        model.setLobbyList(s);
    }

    public void setPossibleTargets(int min, int max, int[] targets){
        model.setMinToSelect(min);
        model.setMaxToSelect(max);
        model.setPossibleTargets(targets);
    }

    public void setPowerUps(int[][] powerUps){
        model.setPowerups(powerUps);
    }

    public void setCurrentPlayer(int currentPlayer){
        model.setCurrentPlayer(currentPlayer);
    }

    public void setPossibleActions(int[] possibleActions){
        model.setPossibleActions(possibleActions);
    }

    public void setGameParams(int[] gameParams){
        model.getBoard().setMapType(gameParams[0]);
        model.getBoard().setMaxKills(gameParams[1]);
    }

    public void setKillTrack(int[] killTrack, boolean[] overkills){
        model.getBoard().setKillTrack(killTrack);
        model.getBoard().setOverkills(overkills);
    }

    public void setSquares(int[][] coords, int[] rooms,boolean[] spawn ){
        model.getBoard().setSquares(IntStream.range(0,coords.length)
                .mapToObj(x->new Square(coords[x],spawn[x],rooms[x]))
                .toArray(Square[]::new));
    }

    public void setSquareContent(int squareID, int[] ammo, boolean powerUp, int[] weapons){
        model.getBoard().getSquares()[squareID].setAmmo(ammo);
        model.getBoard().getSquares()[squareID].setPowerup(powerUp);
        model.getBoard().getSquares()[squareID].setWeapons(weapons);
    }

    public void displayLobby() {
        view.displayLobbyList(model.getLobbyList());
    }

    //TODO IMPLEMENT METHODS
    private interface State {
        default void onEnter() {
        }
    }

}
