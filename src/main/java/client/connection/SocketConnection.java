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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SocketConnection implements Connection, Runnable {
    private static final String CMD_DELIMITER = "&&";
    private static final String ARG_DELIMITER = "--";
    private final Client controller;
    private Socket socket;
    private PrintStream ostream;
    private Parser parser = new Parser(Map.ofEntries(
            Map.entry("message", this::print),
            Map.entry("lobby",this::sendLobbyList),
            Map.entry("targets",this::sendTargets)
    ), CMD_DELIMITER, ARG_DELIMITER);


    public SocketConnection(Client controller) {
        this.controller = controller;
    }

    public void connect(String host, int port) throws Exception {
        socket = new Socket(host, port);
        ostream = new PrintStream(socket.getOutputStream());
        new Thread(this).start();
    }

    @Override
    public void login(String username) {
        send("login" + CMD_DELIMITER + username);
    }

    @Override
    public void createLobby(String name) {
        send("create" + CMD_DELIMITER + name);
    }

    @Override
    public void joinLobby(String name) {
        send("join" + CMD_DELIMITER + name);
    }

    @Override
    public void quitLobby(String name) {
        send("quit_l" + CMD_DELIMITER + name);
    }

    @Override
    public void selectPowerUp(int[] selected) {
        send("pup" + CMD_DELIMITER + Arrays.stream(selected)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void selectWeapon(int[] selected) {
        send("weapon" + CMD_DELIMITER + Arrays.stream(selected)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void selectFireMode(int weaponIndex, int[] selectedFireModes) {
        send("fire" + CMD_DELIMITER + weaponIndex + ARG_DELIMITER + Arrays.stream(selectedFireModes)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void selectGrabbable(int index) {
        send("grab" + CMD_DELIMITER + index);
    }

    @Override
    public void selectTargettable(int[] selected) {
        send("target" + CMD_DELIMITER +Arrays.stream(selected)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void selectColor(int color) {
        send("color" + CMD_DELIMITER + color);
    }

    @Override
    public void selectAction(int actionIndex) {
        send("action" + CMD_DELIMITER + actionIndex);
    }

    @Override
    public void run() {
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
            controller.print(e.toString());
        }
    }

    private void print(String[] args){
        controller.print(String.join(ARG_DELIMITER, args));
    }

    private void sendLobbyList(String[] args){
        controller.setLobbyList(args);
    }

    private void sendTargets(String[] args){
        //TODO
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            controller.print(e.toString());
        }
    }

    public void send(String s) {
        try {
            ostream.println(s);
        } catch (NullPointerException e) {
            controller.print("Error, you are not connected, use connect first");
            return;
        }
        if (ostream.checkError()) {
            controller.print("Error in sending the command");
            close();
        }
    }

}
