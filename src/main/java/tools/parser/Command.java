package tools.parser;

@FunctionalInterface
public interface Command {
    static Command documented(Command c, String docString) {
        return new Command() {
            @Override
            public void execute(String[] args) throws CommandException {
                c.execute(args);
            }

            @Override
            public String docString() {
                return c.docString() + docString;
            }
        };
    }

    void execute(String[] args) throws CommandException;

    default String docString() {
        return "";
    }
}
