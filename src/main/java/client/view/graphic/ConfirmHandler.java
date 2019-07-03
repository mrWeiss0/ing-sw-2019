package client.view.graphic;

import tools.parser.CommandExitException;

public interface ConfirmHandler {
    public void confirmHandle (boolean value) throws CommandExitException;
}
