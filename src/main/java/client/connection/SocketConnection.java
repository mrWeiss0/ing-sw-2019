package client.connection;

import client.Client;
import server.Main;
import tools.parser.CommandException;
import tools.parser.CommandExitException;
import tools.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SocketConnection implements Connection, Runnable {
    private final Client controller;
    private Socket socket;
    private PrintStream ostream;
    private Parser parser;
    private static final String CMD_DELIMITER = " ";
    private static final String ARG_DELIMITER = " ";



    public SocketConnection(Client controller) {
        this.controller = controller;
    }

    public void connect(String host, int port) throws Exception{
        socket = new Socket(host, port);
        ostream= new PrintStream(socket.getOutputStream());
        run();
    }

    @Override
    public void login(String username) {
        send("login"+ CMD_DELIMITER +username);
    }

    @Override
    public void createLobby(String name) {
        send("create"+CMD_DELIMITER+name);
    }

    @Override
    public void joinLobby(String name) {
        send("join"+CMD_DELIMITER+name);
    }

    @Override
    public void quitLobby(String name) {
        send("quit"+CMD_DELIMITER+name);
    }

    @Override
    public void selectPowerUp(int[] selected) {
        send("pup"+CMD_DELIMITER+Arrays.stream(selected)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void selectWeapon(int[] selected) {
        send("weapon"+CMD_DELIMITER+Arrays.stream(selected)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void selectFireMode(int weaponIndex, int[] selectedFireModes) {
        send("fire"+CMD_DELIMITER+weaponIndex+ARG_DELIMITER+Arrays.stream(selectedFireModes)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void selectGrabbable(int index) {
        send("grab"+CMD_DELIMITER+index);
    }

    @Override
    public void selectTargettable(int[] selected) {
        send("target"+CMD_DELIMITER+Arrays.stream(selected)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void selectColor(int color) {
        send("color"+CMD_DELIMITER+color);
    }

    @Override
    public void selectAction(int actionIndex) {
        send("action"+CMD_DELIMITER+actionIndex);
    }

    @Override
    public void run(){
        try (BufferedReader istream = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!socket.isClosed())
                parse(istream.readLine());
        } catch (IOException e) {
            Main.LOGGER.info(e::toString);
        } finally {
            close();
        }
    }

    private void parse(String line) {
        try {
            parser.parse(line);
        } catch (CommandExitException e) {
            close();
        } catch (CommandException e) {
            send(e.toString());
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            Main.LOGGER.warning(e::toString);
        }
    }

    public void send(String s) {
        ostream.println(s);
        if (ostream.checkError()) {
            Main.LOGGER.warning("Socket send exception");
            close();
        }
    }

}
