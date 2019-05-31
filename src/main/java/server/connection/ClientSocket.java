package server.connection;

import server.Main;
import server.Server;
import tools.parser.CommandException;
import tools.parser.CommandExitException;
import tools.parser.CommandNotFoundException;
import tools.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;

public class ClientSocket extends VirtualClient implements Runnable {
    private final Socket socket;
    private final PrintStream ostream;
    private final Parser parser = new Parser(Map.ofEntries(
            Map.entry("help", this::help),
            Map.entry("login", this::login)
    ), "\\s*:\\s*", "\\s*,\\s*");

    public ClientSocket(Server server, Socket socket) throws IOException {
        super(server);
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
        server.registerPlayer(username, this);
    }
}
