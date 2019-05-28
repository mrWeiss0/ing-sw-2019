package connection;

import model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Pattern;

public class ClientSocket implements Runnable, VirtualClient {
    private static final Pattern CMD_DELIMITER = Pattern.compile("\\s*:\\s*");
    private static final Pattern ARGS_DELIMITER = Pattern.compile("\\s*,\\s*");
    private final Server server;
    private final Socket socket;
    private final PrintStream ostream;
    private Player player;
    private final Map<String, Command> commands = Map.ofEntries(
            Map.entry("login", this::login)
    );

    public ClientSocket(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        ostream = new PrintStream(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            BufferedReader istream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!socket.isClosed())
                parse(istream.readLine());
        } catch (IOException e) {
            close();
        }
    }

    @Override
    public void send(String s) {
        ostream.println(s);
        if (ostream.checkError()) close();
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            Main.logger.warning(e::toString);
        }
    }

    private void parse(String s) {
        String[] p = CMD_DELIMITER.split(s, 2);
        try {
            send(commands.getOrDefault(p[0], args -> {
                throw new BadRequestException("Unknown command");
            }).execute(ARGS_DELIMITER.split(p[1])));
        } catch (BadRequestException e) {
            send(e.toString());
        }
    }

    private String login(String[] args) throws BadRequestException {
        if (args.length < 1) throw new BadRequestException("Choose a username");
        player = server.registerPlayer(args[0]);
        player.setClient(this);
        return "Logged in";
    }

    @FunctionalInterface
    private interface Command {
        String execute(String[] args) throws BadRequestException;
    }
}
