package tools.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser implements Runnable {
    private final BufferedReader inputStream;
    private final Map<String, Command> commands;
    private final Consumer<CommandException> exceptionHandler;
    private final Pattern cmdDelimiter;
    private final Pattern argsDelimiter;

    public Parser(InputStream inputStream, Map<String, Command> commands, Consumer<CommandException> exceptionHandler, String cmdDelimiter, String argsDelimiter) {
        this.inputStream = new BufferedReader(new InputStreamReader(inputStream));
        this.commands = commands;
        this.exceptionHandler = exceptionHandler;
        this.cmdDelimiter = Pattern.compile(cmdDelimiter);
        this.argsDelimiter = Pattern.compile(argsDelimiter);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                parse(inputStream.readLine());
            } catch (IOException e) {
                Thread.currentThread().interrupt();
            } catch (CommandException e) {
                exceptionHandler.accept(e);
            }
        }
    }

    public String help() {
        return commands.entrySet().stream()
                .map(e -> help0(
                        e.getKey(),
                        e.getValue().docString()
                ))
                .collect(Collectors.joining("\n"));
    }

    public String help(String cmdName) throws CommandNotFoundException {
        Command command = commands.get(cmdName);
        if (command == null)
            throw new CommandNotFoundException(cmdName);
        return help0(cmdName, command.docString());
    }

    private void parse(String args) throws CommandException {
        if (args == null)
            throw new CommandExitException();
        String[] p = splitCommand(args);
        Command command = commands.get(p[0]);
        if (command == null)
            throw new CommandNotFoundException(p[0]);
        command.execute(splitArgs(p));
    }

    private String[] splitCommand(String s) {
        return cmdDelimiter.split(s, 2);
    }

    private String[] splitArgs(String[] cmd) {
        return cmd.length > 1 ? argsDelimiter.split(cmd[1], -1) : new String[]{};
    }

    private String help0(String cmdName, String docString) {
        return cmdName + "\t" + docString;
    }
}
