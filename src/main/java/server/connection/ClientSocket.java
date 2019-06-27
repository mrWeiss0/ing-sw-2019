package server.connection;

import server.Main;
import server.controller.LobbyList;
import server.model.PowerUp;
import server.model.board.Board;
import server.model.board.Figure;
import server.model.board.Targettable;
import tools.parser.CommandException;
import tools.parser.CommandExitException;
import tools.parser.CommandNotFoundException;
import tools.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClientSocket extends VirtualClient implements Runnable {
    private static final String CMD_DELIMITER = "&&";
    private static final String ARG_DELIMITER = "--";
    private final Socket socket;
    private final PrintStream ostream;
    private final Parser parser = new Parser(Map.ofEntries(
            Map.entry("help", this::help),
            Map.entry("login", this::login),
            Map.entry("create", this::createLobby),
            Map.entry("join", this::joinLobby),
            Map.entry("quit_l", this::quitLobby),
            Map.entry("pup", this::selectPowerUp),
            Map.entry("weapon", this::selectWeapon),
            Map.entry("fire", this::selectFireMode),
            Map.entry("grab", this::selectGrabbable),
            Map.entry("target", this::selectTargettable),
            Map.entry("color", this::selectColor),
            Map.entry("action", this::selectAction)
    ), CMD_DELIMITER, ARG_DELIMITER);

    public ClientSocket(LobbyList lobbyList, Socket socket) throws IOException {
        super(lobbyList);
        this.socket = socket;
        ostream = new PrintStream(socket.getOutputStream());
        sendMessage("Connected");
    }

    @Override
    public void send(String s) {
        ostream.println(s);
        if (ostream.checkError()) {
            Main.LOGGER.warning("Socket send exception");
            close();
        }
    }

    @Override
    public void sendLobbyList(String[] s){
        send("lobby"+CMD_DELIMITER+ String.join(ARG_DELIMITER,s));
        if (ostream.checkError()) {
            Main.LOGGER.warning("Socket send exception");
            close();
        }
    }

    @Override
    public void sendTargets(int min, int max, List<Targettable> targets, Board board){
        send("targets"+CMD_DELIMITER
                +Integer.toString(min)+ARG_DELIMITER
                +Integer.toString(max)+ARG_DELIMITER
                +targets.stream()
                .map(board::getID)
                .map(x->Integer.toString(x))
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void sendPowerUps(List<PowerUp> powerUps){
        send("pups"+CMD_DELIMITER
            +powerUps.stream()
                .map(x->x.getType().ordinal()+ARG_DELIMITER+x.getColor())
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void sendCurrentPlayer(int currentPlayer){
        send("curr_p"+CMD_DELIMITER+currentPlayer);
    }

    @Override
    public void sendPossibleActions(List<Integer> possibleActions){
        send("actions"+CMD_DELIMITER+possibleActions.stream()
                .map(x->x.toString())
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void sendGameParams(List<Integer> gameParams){
        send("game_p"+CMD_DELIMITER+gameParams.stream()
                .map(x->x.toString())
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void sendKillTrack(List<Figure> killTrack, List<Boolean> overkills){
        String[] res = killTrack.stream()
                .map(x->Integer.toString(player.getGame().getGame().getBoard().getFigures().indexOf(x)))
                .toArray(String[]::new);
        IntStream.range(0,overkills.size()).forEach(x->{
            if(overkills.get(x)) res[x]="+"+res[x];
        });
        send("killtrack"+CMD_DELIMITER+ String.join(ARG_DELIMITER, res));
    }

    @Override
    public void sendMessage(String s){
        send("message"+CMD_DELIMITER+s);
    }

    public boolean ping() {
        return !ostream.checkError();
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

    @Override
    public void close() {
        player.setOffline();
        try {
            socket.close();
        } catch (IOException e) {
            Main.LOGGER.warning(e::toString);
        }
    }

    private void parse(String line) {
        try {
            parser.parse(line);
        } catch (CommandExitException e) {
            close();
        } catch (CommandException e) {
            sendMessage(e.toString());
        }
    }

    public void help(String[] args) throws CommandNotFoundException {
        if (args.length == 0 || args[0].isEmpty())
            sendMessage(parser.help());
        else
            sendMessage(parser.help(args[0]));
    }

    private void login(String[] args) throws CommandException {
        if (args.length < 1)
            throw new CommandException("Choose a username");
        String username = args[0].trim();
        if (username.isEmpty())
            throw new CommandException("Choose a username");
        lobbyList.registerPlayer(username, this);
    }

    private void createLobby(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Please select a name for the lobby");
        try {
            lobbyList.create(args[0]);
        } catch (IllegalStateException e) {
            sendMessage(e.toString());
        }
    }

    private void joinLobby(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Please select lobby");
        try {
            lobbyList.join(player, args[0]);
        } catch (IllegalStateException | NoSuchElementException e) {
            sendMessage(e.toString());
        }
    }

    private void quitLobby(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Please select the lobby name to exit from");
        try {
            lobbyList.remove(player, args[0]);
        } catch (IllegalStateException | NoSuchElementException e) {
            sendMessage(e.toString());
        }
    }

    private void selectPowerUp(String[] args) throws CommandException {
        player.selectPowerUp(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
    }

    private void selectWeapon(String[] args) throws CommandException {
        player.selectWeaponToReload(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
    }

    private void selectFireMode(String[] args) throws CommandException {
        player.selectWeaponFireMode(Integer.parseInt(args[0]), Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray());
    }

    private void selectGrabbable(String[] args) throws CommandException {
        player.selectGrabbable(Integer.parseInt(args[0]));
    }

    private void selectTargettable(String[] args) throws CommandException {
        if(args[1].equals(""))
            args=new String[]{args[0]};
        player.selectTargettable(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
    }

    private void selectColor(String[] args) throws CommandException {
        player.selectColor(Integer.parseInt(args[0]));
    }

    private void selectAction(String[] args) throws CommandException {
        player.selectAction(Integer.parseInt(args[0]));
    }
}
