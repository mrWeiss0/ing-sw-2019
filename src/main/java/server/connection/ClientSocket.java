package server.connection;

import server.Main;
import server.controller.LobbyList;
import tools.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

public class ClientSocket extends VirtualClient implements Runnable {
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
            Map.entry("color",this::selectColor),
            Map.entry("action", this::selectAction)
    ), " ", " ");

    public ClientSocket(LobbyList lobbyList, Socket socket) throws IOException {
        super(lobbyList);
        this.socket = socket;
        ostream = new PrintStream(socket.getOutputStream());
        send("Connected");
    }

    @Override
    public void send(String s) {
        ostream.println(s);
        if (ostream.checkError()) {
            Main.LOGGER.warning("Socket send exception");
            close();
        }
    }

    public boolean ping(){
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
            send(e.toString());
        }
    }

    public void help(String[] args) throws CommandNotFoundException {
        if (args.length == 0 || args[0].isEmpty())
            send(parser.help());
        else
            send(parser.help(args[0]));
    }

    private void login(String[] args) throws CommandException {
        if (args.length < 1)
            throw new CommandException("Choose a username");
        String username = args[0].trim();
        if (username.isEmpty())
            throw new CommandException("Choose a username");
        lobbyList.registerPlayer(username, this);
    }

    private void createLobby(String[] args) throws CommandException{
        if (args.length<1) throw  new CommandException("Please select a name for the lobby");
        lobbyList.create(args[0]);
    }

    private void joinLobby(String[] args) throws CommandException{
        if (args.length<1) throw  new CommandException("Please select lobby");
        lobbyList.join(player, args[0]);
    }

    private void quitLobby(String[] args) throws CommandException{
        if (args.length<1) throw  new CommandException("Please select the lobby name to exit from");
        lobbyList.remove(player, args[0]);
    }

    private void selectPowerUp(String[] args) throws CommandException{
        player.selectPowerUp(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
    }

    private void selectWeapon(String[] args) throws CommandException{
        player.selectWeaponToReload(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
    }

    private void selectFireMode(String[] args) throws CommandException{
        player.selectWeaponFireMode(Integer.parseInt(args[0]), Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray());
    }

    private void selectGrabbable(String[] args) throws CommandException{
        player.selectGrabbable(Integer.parseInt(args[0]));
    }

    private void selectTargettable(String[] args) throws CommandException{
        player.selectTargettable(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
    }

    private void selectColor(String[] args) throws CommandException{
        player.selectColor(Integer.parseInt(args[0]));
    }

    private void selectAction(String[] args) throws CommandException{
        player.selectAction(Integer.parseInt(args[0]));
    }
}
