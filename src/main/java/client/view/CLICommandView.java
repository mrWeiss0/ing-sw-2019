package client.view;

import client.Client;
import tools.parser.Command;
import tools.parser.CommandException;
import tools.parser.CommandExitException;

import java.util.Map;

public class CLICommandView extends CLIView {
    public CLICommandView(Client controller, String cmdDelimiter, String argsDelimiter) {
        super(controller, cmdDelimiter, argsDelimiter);
    }

    @Override
    protected Map<String, Command> mapCommands() {
        return Map.ofEntries(
                //TODO IMPLEMENTS COMMAND
                Map.entry("connect", Command.documented(this::connect, "Connect to given host port")),
                Map.entry("login", Command.documented(this::login, "Login with given username")),
                Map.entry("help", this::help),
                Map.entry("quit", this::quit)
        );
    }

    private void connect(String[] args) throws CommandException{
        if (args.length<1) throw  new CommandException("Insert a host");
        controller.connect(args[0]);
    }

    private void login(String[] args) {
        print("LOGIN");
    }

    private void quit(String[] args) throws CommandExitException {
        throw new CommandExitException();
    }
}
