package tools.parser;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {
    private final Map<String, Command> commands;
    private final Pattern cmdDelimiter;
    private final Pattern argsDelimiter;

    public Parser(Map<String, Command> commands, String cmdDelimiter, String argsDelimiter) {
        this.commands = commands;
        this.cmdDelimiter = Pattern.compile(cmdDelimiter);
        this.argsDelimiter = Pattern.compile(argsDelimiter);
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

    public void parse(String args) throws CommandException {
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
