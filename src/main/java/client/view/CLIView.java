package client.view;

import client.Client;
import tools.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class CLIView implements View, Runnable {
    protected final Client controller;
    private final Parser parser;
    private final Thread thread = new Thread(this);

    public CLIView(Client controller, String cmdDelimiter, String argsDelimiter) {
        this.controller = controller;
        parser = new Parser(
                mapCommands(),
                cmdDelimiter,
                argsDelimiter
        );
    }

    @Override
    public void start() {
        thread.start();
    }

    @SuppressWarnings("squid:S106")
    @Override
    public void print(String s) {
        System.out.println(s);
    }

    @Override
    public void run() {
        try (BufferedReader istream = new BufferedReader(new InputStreamReader(System.in))) {
            while (!Thread.currentThread().isInterrupted())
                parse(istream.readLine());
        } catch (IOException ignore) {
            // Nothing to do
        }
    }

    @Override
    public void exit() {
        thread.interrupt();
    }

    private void parse(String line) {
        try {
            parser.parse(line);
        } catch (CommandExitException e) {
            print(e.toString());
            exit();
        } catch (CommandException e) {
            print(e.toString());
        }
    }

    protected void help(String[] args) throws CommandNotFoundException {
        if (args.length == 0 || args[0].isEmpty())
            print(parser.help());
        else
            print(parser.help(args[0]));
    }

    protected Map<String, Command> mapCommands() {
        return Map.of("help", this::help);
    }
}
