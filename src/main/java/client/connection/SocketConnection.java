package client.connection;

import client.Client;

import java.net.Socket;

public class SocketConnection implements Connection {
    private final Client controller;
    private Socket socket;

    public SocketConnection(Client controller) {
        this.controller = controller;
    }

    public void connect(String host, int port) {
        /*socket = new Socket(host, port);*/
    }

}
