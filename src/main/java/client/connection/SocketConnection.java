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
            Map.entry("killtrack",this::sendKillTrack),
            Map.entry("squares",this::sendSquares),
            Map.entry("fill", this::sendSquareContent),
            Map.entry("players", this::sendPlayers),
            Map.entry("damages",this::sendPlayerDamages),
            Map.entry("marks",this::sendPlayerMarks),
            Map.entry("location",this::sendPlayerLocation),
            Map.entry("points",this::sendPlayerPoints),
            Map.entry("deaths",this::sendPlayerDeaths),
            Map.entry("ammo", this::sendPlayerAmmo),
            Map.entry("npups",this::sendPlayerNPowerUps),
            Map.entry("weapons",this::sendPlayerWeapons),
            Map.entry("remaining", this::sendRemainingActions)
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
                .mapToObj(x->new int[]{Integer.parseInt(args[2*x]),Integer.parseInt(args[2*x+1])})
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

    //example: ["1", "1", "+0", "3", "4", "0", "2", "5", "1"] ->
    //  [((1,1), spawn, room 0),((3,4), no spawn, room 0),((2,5), no spawn, room 1)]
    private void sendSquares(String[] args){
        int numOfSquares=args.length/3;
        int[][] coordinates= new int[numOfSquares][];
        int[] rooms= new int[numOfSquares];
        boolean[] spawn= new boolean[numOfSquares];
        for(int i=0;i<numOfSquares;i++){
            coordinates[i]=new int[]{Integer.parseInt(args[3*i],Integer.parseInt(args[3*i+1]))};
            rooms[i]=Integer.parseInt(args[3*i+2]);
            spawn[i]=args[3*i+2].startsWith("+");
        }
        controller.setSquares(coordinates,rooms,spawn);
    }
    //example ["4", "0", "0", "2", "+"] -> fill square 4 with ammotile 2 blue & powerup
    //or ["5", "0", "0", "0", "-", "3", "4", "17"] -> fill square 5 with weapons 3, 4, 17
    private void sendSquareContent(String[] args){
        int squareID = Integer.parseInt(args[0]);
        int[] ammo = new int[]{Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3])};
        boolean powerup = args[4].startsWith("+");
        int[] weapons= Arrays.stream(args)
                .skip(5)
                .mapToInt(Integer::parseInt)
                .toArray();
        controller.setSquareContent(squareID,ammo,powerup,weapons);
    }

    //example ["0", "mario", "1", "luigi"]
    private void sendPlayers(String[] args){
        int[] avatars = IntStream.range(0,args.length).filter(x->x%2!=0).map(x->Integer.parseInt(args[x])).toArray();
        String[] names = IntStream.range(0,args.length).filter(x->x%2==0).mapToObj(x->args[x]).toArray(String[]::new);
        controller.setPlayers(avatars,names);
    }

    private void sendPlayerDamages(String[] args){
        int id=Integer.parseInt(args[0]);
        int[] values= Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();
        controller.setPlayerDamages(id,values);
    }

    private void sendPlayerMarks(String [] args){
        int id=Integer.parseInt(args[0]);
        int[] values= Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();
        controller.setPlayerMarks(id,values);
    }

    private void sendPlayerLocation(String[] args){
        int id=Integer.parseInt(args[0]);
        int[] values= Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();
        controller.setPlayerLocation(id,values);
    }

    private void sendPlayerPoints(String[] args){
        int id=Integer.parseInt(args[0]);
        int points=Integer.parseInt(args[1]);
        controller.setPlayerPoints(id,points);
    }

    private void sendPlayerDeaths(String[] args){
        int id=Integer.parseInt(args[0]);
        int deaths=Integer.parseInt(args[1]);
        controller.setPlayerDeaths(id,deaths);
    }

    private void sendPlayerAmmo(String[] args){
        int id=Integer.parseInt(args[0]);
        int[] ammo= Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();
        controller.setPlayerAmmo(id,ammo);
    }

    private void sendPlayerNPowerUps(String[] args){
        int id=Integer.parseInt(args[0]);
        int nPowerUps=Integer.parseInt(args[1]);
        controller.setPlayerDeaths(id,nPowerUps);
    }

    private void sendPlayerWeapons(String[] args){
        int id=Integer.parseInt(args[0]);
        int[] wIDs =Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray();
        Boolean[] wrapper = Arrays.stream(args).skip(1).map(x->x.startsWith("+")).toArray(Boolean[]::new);
        boolean[] charges= new boolean[wrapper.length];
        for(int i=0;i<wrapper.length;i++)
            charges[i]=wrapper[i];
        controller.setPlayerWeapons(id, wIDs,charges);
    }

    private void sendRemainingActions(String[] args){
        controller.setRemainingActions(Integer.parseInt(args[0]));
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
