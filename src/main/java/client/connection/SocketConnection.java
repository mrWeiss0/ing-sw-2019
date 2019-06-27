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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SocketConnection implements Connection, Runnable {
    private static final String CMD_DELIMITER = "&&";
    private static final String ARG_DELIMITER = "--";
    private final Client controller;
    private Socket socket;
    private PrintStream ostream;
    private Parser parser = new Parser(Map.ofEntries(
            Map.entry("message", this::print),
            Map.entry("lobby",this::sendLobbyList),
            Map.entry("targets",this::sendTargets),
            Map.entry("pups",this::sendPowerUps),
            Map.entry("curr_p",this::sendCurrentPlayer),
            Map.entry("actions",this::sendPossibleActions),
            Map.entry("game_p",this::sendGameParams),
            Map.entry("killtrack",this::sendKillTrack)
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
        controller.setPossibleTargets(Integer.parseInt(args[0]), Integer.parseInt(args[1]),Arrays.stream(args).skip(2).mapToInt(Integer::parseInt).toArray());
    }

    private void sendPowerUps(String[] args){
        controller.setPowerUps((int[][])IntStream.range(0,args.length/2)
                .mapToObj(x->new int[]{Integer.parseInt(args[x]),Integer.parseInt(args[x+1])})
                .toArray());
    }

    private void sendCurrentPlayer(String[] args){
        controller.setCurrentPlayer(Integer.parseInt(args[0]));
    }

    private void sendPossibleActions(String[] args){
        controller.setPossibleActions(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
    }

    private void sendGameParams(String[] args){
        controller.setGameParams(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
    }

    private void sendKillTrack(String[] args){
        Boolean[] wrapper= Arrays.stream(args).map(x->x.startsWith("+")).toArray(Boolean[]::new);
        boolean[] overkills= new boolean[wrapper.length];
        for(int i=0;i<wrapper.length;i++)
            overkills[i]=wrapper[i];
        controller.setKillTrack(Arrays.stream(args).mapToInt(Integer::parseInt).toArray(),
                overkills);
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
