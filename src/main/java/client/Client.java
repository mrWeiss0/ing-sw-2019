package client;

import client.connection.Connection;
import client.connection.RMIConnection;
import client.connection.SocketConnection;
import client.view.CLICommandView;
import client.view.View;

public class Client {
    private Connection connection;
    private View view;
    private State state;
    private State initState = new State() {
        @Override
        public void onEnter() {

        }
    };

    public Client() {
        view = new CLICommandView(this, "\\s+", "\\s+");
        connection = new SocketConnection(this);
    }

    public void connect(String host){
        try{
            //TODO FROM FILE/CLI
            connection.connect(host,9900);
        }catch(Exception e){
            view.print(e.getMessage());
        }
    }

    public void login(String name){
        connection.login(name);
    }

    public void createLobby(String name){
        connection.createLobby(name);
    }

    public void joinLobby(String name){
        connection.joinLobby(name);
    }

    public void quitLobby(String name){
        connection.quitLobby(name);
    }

    public void selectPowerUp(int[] selected){
        connection.selectPowerUp(selected);
    }

    public void selectWeapon(int[] selected){
        connection.selectWeapon(selected);
    }

    public void selectFireMode(int weaponIndex, int[] selectedFireModes){
        connection.selectFireMode(weaponIndex, selectedFireModes);
    }

    public void selectGrabbable(int index){
        connection.selectGrabbable(index);
    }

    public void selectTargettable(int[] selected){
        connection.selectTargettable(selected);
    }

    public void selectColor(int color){
        connection.selectColor(color);
    }

    public void selectAction(int actionIndex){
        connection.selectAction(actionIndex);
    }

    public void start() {
        view.start();
    }

    public void print(String toPrint){
        view.print(toPrint);
    }
    //TODO IMPLEMENT METHODS
    private interface State {
        default void onEnter() {
        }
    }

}
