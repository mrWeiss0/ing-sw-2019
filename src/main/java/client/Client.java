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

    public void start() {
        view.start();
    }

    private interface State {
        default void onEnter() {
        }
    }

}
