package server.connection;

import client.model.GameState;
import server.Main;
import server.controller.LobbyList;
import server.controller.Player;
import server.model.AmmoCube;
import server.model.AmmoTile;
import server.model.PowerUp;
import server.model.board.*;
import server.model.weapon.Weapon;
import server.model.weapon.Weapons;
import tools.parser.CommandException;
import tools.parser.CommandExitException;
import tools.parser.CommandNotFoundException;
import tools.parser.Parser;

import java.io.*;
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
    private static final String NUMERIC = "[0-9]+";
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
            Map.entry("action", this::selectAction),
            Map.entry("reconnect", this::reconnect),
            Map.entry("chat", this::chatMessage),
            Map.entry("end", this::endTurn)
    ), CMD_DELIMITER, ARG_DELIMITER);

    public ClientSocket(LobbyList lobbyList, Socket socket) throws IOException {
        super(lobbyList);
        this.socket = socket;
        ostream = new PrintStream(socket.getOutputStream());
        sendGameState(GameState.NOT_LOGGED_IN.ordinal());
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
    public void sendLobbyList(String[] s) {
        send("lobby" + CMD_DELIMITER + String.join(ARG_DELIMITER, s));
        if (ostream.checkError()) {
            Main.LOGGER.warning("Socket send exception");
            close();
        }
    }

    @Override
    public void sendTargets(int min, int max, List<Targettable> targets, Board board) {
        send("targets" + CMD_DELIMITER
                + min + ARG_DELIMITER
                + max + ARG_DELIMITER
                + targets.stream()
                .map(board::getID)
                .map(x -> Integer.toString(x))
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void sendPowerUps(List<PowerUp> powerUps) {
        send("pups" + CMD_DELIMITER
                + powerUps.stream()
                .map(x -> x.getType().ordinal() + ARG_DELIMITER + x.getColor())
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void sendCurrentPlayer(int currentPlayer) {
        send("curr_p" + CMD_DELIMITER + currentPlayer);
    }

    @Override
    public void sendPossibleActions(int actionSetID) {
        send("actions" + CMD_DELIMITER + actionSetID);
    }

    @Override
    public void sendGameParams(List<Integer> gameParams) {
        send("game_p" + CMD_DELIMITER + gameParams.stream()
                .map(x -> x.toString())
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void sendKillTrack(List<Figure> killTrack, List<Boolean> overkills) {
        String[] res = killTrack.stream()
                .map(x -> Integer.toString(player.getGame().getGame().getBoard().getFigures().indexOf(x)))
                .toArray(String[]::new);
        IntStream.range(0, overkills.size()).forEach(x -> {
            if (overkills.get(x)) res[x] = "+" + res[x];
        });
        send("killtrack" + CMD_DELIMITER + String.join(ARG_DELIMITER, res));
    }

    @Override
    public void sendSquares(List<AbstractSquare> squares) {
        send("squares" + CMD_DELIMITER
                + squares.stream()
                .map(x -> {
                    int[] coord = x.getCoordinates();
                    String s = Integer.toString(player.getGame().getGame().getBoard().getRooms().indexOf(x.getRoom()));
                    String pre = coord[0] + ARG_DELIMITER + coord[1] + ARG_DELIMITER;
                    return pre + (x instanceof SpawnSquare ? "+" + s : s);
                })
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void sendSquareContent(AbstractSquare square) {
        int id = player.getGame().getGame().getBoard().getSquares().indexOf(square);
        int tileID = -1;
        int[] weapons = null;
        int[][] pcost = null;
        if (square instanceof SpawnSquare) {
            weapons = square.peek().stream().mapToInt(x -> ((Weapon) x).getID()).toArray();
            pcost = square.peek().stream().map(x -> {
                        AmmoCube cost = ((Weapon) x).getPickupCost();
                        int[] ammo = new int[]{0, 0, 0};
                        for (int i = 0; i < 3; i++)
                            ammo[i] = cost.value(i);
                        return ammo;
                    }
            ).toArray(int[][]::new);
        } else tileID = square.peek().isEmpty() ? -1 : ((AmmoTile) square.peek().get(0)).getId();

        send("fill" + CMD_DELIMITER + id
                + ARG_DELIMITER + tileID
                + ARG_DELIMITER
                + (weapons == null ? "-1" + ARG_DELIMITER + "0" :
                Arrays.stream(weapons)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining("%%")) + ARG_DELIMITER
                        + String.join("%%", Arrays.stream(pcost)
                        .map(x -> Arrays.stream(x).mapToObj(Integer::toString)
                                .collect(Collectors.joining("££"))).toArray(String[]::new)))
        );
    }

    @Override
    public void sendPlayers(List<Player> players) {
        send("players" + CMD_DELIMITER
                + players.stream()
                .map(x -> x.getGame().getGame().getPlayers().indexOf(x) + ARG_DELIMITER + x.getName())
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void sendMessage(String s) {
        send("message" + CMD_DELIMITER + s);
    }

    @Override
    public void sendPlayerDamages(Player player) {
        List<Player> players = player.getGame().getGame().getPlayers();
        send("damages" + CMD_DELIMITER
                + players.indexOf(player) + (player.getFigure().getDamages().isEmpty() ? "" : ARG_DELIMITER)
                + player.getFigure().getDamages().stream()
                .map(x -> Integer.toString(players.indexOf(x.getPlayer())))
                .collect(Collectors.joining(ARG_DELIMITER))
        );
    }

    @Override
    public void sendPlayerMarks(Player player) {
        List<Player> players = player.getGame().getGame().getPlayers();
        send("marks" + CMD_DELIMITER
                + players.indexOf(player) + (player.getFigure().getMarks().isEmpty() ? "" : ARG_DELIMITER)
                + player.getFigure().getMarks().stream()
                .map(x -> Integer.toString(players.indexOf(x.getPlayer())))
                .collect(Collectors.joining(ARG_DELIMITER))
        );
    }

    @Override
    public void sendPlayerLocation(Player player) {
        send("location" + CMD_DELIMITER
                + player.getGame().getGame().getPlayers().indexOf(player) + ARG_DELIMITER
                + (player.getFigure().getLocation() == null ? "-1" + ARG_DELIMITER + "-1" :
                Arrays.stream(player.getFigure().getLocation().getCoordinates())
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining(ARG_DELIMITER)))
        );
    }

    @Override
    public void sendPlayerPoints(Player player) {
        send("points" + CMD_DELIMITER
                + player.getGame().getGame().getPlayers().indexOf(player) + ARG_DELIMITER
                + player.getFigure().getPoints()
        );
    }

    @Override
    public void sendPlayerDeaths(Player player) {
        send("deaths" + CMD_DELIMITER
                + player.getGame().getGame().getPlayers().indexOf(player) + ARG_DELIMITER
                + player.getFigure().getDeaths()
        );
    }

    @Override
    public void sendPlayerAmmo(Player player) {
        int[] ammo = new int[]{0, 0, 0};
        for (int i = 0; i < 3; i++)
            ammo[i] = player.getFigure().getAmmo().value(i);
        send("ammo" + CMD_DELIMITER
                + player.getGame().getGame().getPlayers().indexOf(player) + ARG_DELIMITER
                + Arrays.stream(ammo).mapToObj(Integer::toString).collect(Collectors.joining(ARG_DELIMITER))
        );
    }

    @Override
    public void sendPlayerNPowerUps(Player player) {
        send("npups" + CMD_DELIMITER
                + player.getGame().getGame().getPlayers().indexOf(player) + ARG_DELIMITER
                + player.getFigure().getPowerUps().size()
        );
    }

    @Override
    public void sendPlayerWeapons(Player player) {
        int[][] lcost = player.getFigure().getWeapons().stream().map(x -> {
            int[] ammo = new int[]{0, 0, 0};
            for (int i = 0; i < 3; i++)
                ammo[i] = x.getReloadCost().value(i);
            return ammo;
        }).toArray(int[][]::new);
        String[] names = player.getFigure().getWeapons().stream()
                .map(x -> Weapons.values()[x.getID()].toString())
                .toArray(String[]::new);
        send("weapons" + CMD_DELIMITER
                + player.getGame().getGame().getPlayers().indexOf(player) + ARG_DELIMITER
                + player.getFigure().getWeapons().stream()
                .map(x -> (x.isLoaded() ? "+" : "") + x.getID())
                .collect(Collectors.joining("%%")) + ARG_DELIMITER
                + String.join("%%", names) + ARG_DELIMITER
                + String.join("%%", Arrays.stream(lcost)
                .map(x -> Arrays.stream(x)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining("££"))).toArray(String[]::new))
        );
    }

    @Override
    public void sendRemainingActions(int remaining) {
        send("remaining" + CMD_DELIMITER + remaining);
    }

    @Override
    public void sendGameState(int value) {
        send("state" + CMD_DELIMITER + value);
    }

    @Override
    public void sendCountDown(int value) {
        send("cd" + CMD_DELIMITER + value);
    }

    @Override
    public void sendChatMessage(String name, String msg) {
        send("chat" + CMD_DELIMITER
                + name + ARG_DELIMITER
                + msg
        );
    }

    @Override
    public void sendPlayerID(int id) {
        send("pid" + CMD_DELIMITER + id);
    }

    @Override
    public void sendLeaderBoard(int[] points) {
        send("leader" + CMD_DELIMITER + Arrays.stream(points)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public void sendNKills(int[] kills) {
        send("nkills" + CMD_DELIMITER + Arrays.stream(kills)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(ARG_DELIMITER)));
    }

    @Override
    public boolean ping() {
        return !socket.isClosed();
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

    private void endTurn(String[] args) {
        player.endTurn();
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
        } catch (FileNotFoundException e) {
            sendMessage("Server Error");
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
        try {
            lobbyList.remove(player);
        } catch (IllegalStateException | NoSuchElementException e) {
            sendMessage(e.toString());
        }
    }

    private void selectPowerUp(String[] args) {
        player.selectPowerUp(Arrays.stream(args).filter(x -> x.matches(NUMERIC)).mapToInt(Integer::parseInt).toArray());
    }

    private void selectWeapon(String[] args) {
        player.selectWeaponToReload(Arrays.stream(args).filter(x -> x.matches(NUMERIC)).mapToInt(Integer::parseInt).toArray());
    }

    private void selectFireMode(String[] args) {
        if (args.length < 2)
            player.sendMessage("Please select at least one weapon and one fire mode");
        player.selectWeaponFireMode(Integer.parseInt(args[0]), Arrays.stream(args).filter(x -> x.matches(NUMERIC)).skip(1).mapToInt(Integer::parseInt).toArray());
    }

    private void selectGrabbable(String[] args) {
        player.selectGrabbable(Integer.parseInt(args[0]));
    }

    private void selectTargettable(String[] args) {
        player.selectTargettable(Arrays.stream(args).filter(x -> x.matches(NUMERIC)).mapToInt(Integer::parseInt).toArray());
    }

    private void selectColor(String[] args) {
        player.selectColor(Integer.parseInt(args[0]));
    }

    private void selectAction(String[] args) {
        player.selectAction(Integer.parseInt(args[0]));
    }

    private void chatMessage(String[] args) {
        try {
            lobbyList.chatMessage(this, String.join(ARG_DELIMITER, args));
        } catch (IllegalStateException e) {
            sendMessage(e.getMessage());
        }
    }

    private void reconnect(String[] args) {
        player.reconnect();
    }
}
