package client.view;

import client.Client;
import tools.parser.Command;
import tools.parser.CommandExitException;

import java.util.Map;

public class CLICommandView extends CLIView {
    public CLICommandView(Client controller, String cmdDelimiter, String argsDelimiter) {
        super(controller, cmdDelimiter, argsDelimiter);
    }

    @Override
    protected Map<String, Command> mapCommands() {
        return Map.ofEntries(
                Map.entry("connect", Command.documented(this::connect, "Connect to given host port")),
                Map.entry("login", Command.documented(this::login, "Login with given username")),
                Map.entry("help", Command.documented(this::help, "Get help")),
                Map.entry("quit", Command.documented(this::quit, "Quit"))
        );
    }

    public void connect(String[] args) {
        print("CONNECT");
    }

    public void login(String[] args) {
        print("LOGIN");
    }

    public void quit(String[] args) throws CommandExitException {
        throw new CommandExitException();
    }
}
