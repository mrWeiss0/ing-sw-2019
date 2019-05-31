package tools.parser;

public class CommandNotFoundException extends CommandException {
    public CommandNotFoundException() {
        super();
    }

    public CommandNotFoundException(String message) {
        super(message);
    }
}
