package client;

import client.connection.Connection;
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
            view.print("Exception");
        }

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
