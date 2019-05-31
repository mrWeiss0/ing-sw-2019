package client.view;

import client.Client;
import tools.parser.*;

import java.util.Map;

public class CLIView implements View {
    protected final Client controller;
    private final Thread inputThread;
    private final Parser parser;

    public CLIView(Client controller, String cmdDelimiter, String argsDelimiter) {
        this.controller = controller;
        parser = new Parser(
                System.in,
                mapCommands(),
                this::commandExceptionHandler,
                cmdDelimiter,
                argsDelimiter
        );
        inputThread = new Thread(parser);
    }

    @Override
    public void start() {
        inputThread.start();
    }

    @SuppressWarnings("squid:S106")
    @Override
    public void print(String s) {
        System.out.println(s);
    }

    public void help(String[] args) throws CommandException {
        if (args.length == 0 || args[0].isEmpty())
            print(parser.help());
        else
            print(parser.help(args[0]));
    }

    private void commandExceptionHandler(CommandException e) {
        print(e.toString());
        if (e instanceof CommandExitException)
            inputThread.interrupt();
    }

    protected Map<String, Command> mapCommands() {
        return Map.of("help", this::help);
    }
}
