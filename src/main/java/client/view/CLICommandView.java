package client.view;

import client.Client;
import tools.parser.Command;
import tools.parser.CommandException;
import tools.parser.CommandExitException;

import java.util.Arrays;
import java.util.Map;

public class CLICommandView extends CLIView {
    private final String NUMBERERROR = "Argument invalid: please type numbers after the command";

    public CLICommandView(Client controller, String cmdDelimiter, String argsDelimiter) {
        super(controller, cmdDelimiter, argsDelimiter);
    }

    @Override
    protected Map<String, Command> mapCommands() {
        return Map.ofEntries(
                Map.entry("connect", Command.documented(this::connect, "Connect to given host port")),
                Map.entry("login", Command.documented(this::login, "Login with given username")),
                Map.entry("help", this::help),
                Map.entry("quit", this::quit),
                Map.entry("create", Command.documented(this::createLobby, "Create a new lobby")),
                Map.entry("join", Command.documented(this::joinLobby, "Join the lobby with the given name")),
                Map.entry("quit_l", Command.documented(this::quitLobby, "Quit the lobby with the given name")),
                Map.entry("pup", Command.documented(this::selectPowerUp, "Tell the server the powerUps that are being selected")),
                Map.entry("weapon", Command.documented(this::selectWeapon, "Tell the server the weapons that are being selected")),
                Map.entry("fire", Command.documented(this::selectFireMode, "Tell the server the weapon and the fireModes that are being selected")),
                Map.entry("grab", Command.documented(this::selectGrabbable, "Tell the server the grabbable on the figure square that is being selected")),
                Map.entry("target", Command.documented(this::selectTargettable, "Tell the server the targets that are being selected")),
                Map.entry("color", Command.documented(this::selectColor, "Tell the server the color that is being selected")),
                Map.entry("action", Command.documented(this::selectAction, "Tell the server the action that is being selected")),
                Map.entry("lobby_list", Command.documented(this::displayLobby, "Show the lobby list saved in local (Could be not updated)")),
                Map.entry("chat", Command.documented(this::chatMessage, "Send a message to everyone")),
                Map.entry("reconnect", Command.documented(this::reconnect, "If you go inactive because of the timer, use this command to return active"))
        );
    }

    private void connect(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Insert a host");
        controller.connect(args[0]);
    }

    private void login(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Insert an username");
        controller.login(args[0]);
    }

    private void createLobby(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Please select a name for the lobby");
        controller.createLobby(args[0]);
    }

    private void joinLobby(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Please select lobby");
        controller.joinLobby(args[0]);
    }

    private void quitLobby(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Please select the lobby name to exit from");
        controller.quitLobby(args[0]);
    }

    private void selectPowerUp(String[] args) throws CommandException {
        try {
            controller.selectPowerUp(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
        } catch (NumberFormatException e) {
            throw new CommandException(NUMBERERROR);
        }
    }

    private void selectWeapon(String[] args) throws CommandException {
        try {
            controller.selectWeapon(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
        } catch (NumberFormatException e) {
            throw new CommandException(NUMBERERROR);
        }
    }

    private void selectFireMode(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Please select a fire mode");
        try {
            controller.selectFireMode(Integer.parseInt(args[0]), Arrays.stream(args).skip(1).mapToInt(Integer::parseInt).toArray());
        } catch (NumberFormatException e) {
            throw new CommandException(NUMBERERROR);
        }
    }

    private void selectGrabbable(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Please select a grabbable");
        try {
            controller.selectGrabbable(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            throw new CommandException(NUMBERERROR);
        }
    }

    private void selectTargettable(String[] args) throws CommandException {
        try {
            controller.selectTargettable(Arrays.stream(args).mapToInt(Integer::parseInt).toArray());
        } catch (NumberFormatException e) {
            throw new CommandException(NUMBERERROR);
        }
    }

    private void selectColor(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Please select a color");
        try {
            controller.selectColor(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            throw new CommandException(NUMBERERROR);
        }
    }

    private void selectAction(String[] args) throws CommandException {
        if (args.length < 1) throw new CommandException("Please select an action");
        controller.selectAction(Map.ofEntries(Map.entry("move",0),
                                Map.entry("grab",1),
                                Map.entry("shoot",2))
                .get(args[0]));
    }

    private void chatMessage(String[] args) throws CommandException {
        try {
            controller.chatMessage(String.join(" ", args));
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
    }

    private void reconnect(String[] args) throws CommandException {
        controller.reconnect();
    }

    private void displayLobby(String[] args) throws CommandException {
        controller.displayLobby();
    }

    private void quit(String[] args) throws CommandExitException {
        throw new CommandExitException();
    }
}
