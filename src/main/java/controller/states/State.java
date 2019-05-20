package controller.states;

import connection.messages.responses.TextResponse;
import controller.GameController;
import model.Player;

import java.rmi.RemoteException;
import java.util.List;

//TODO PUOI EREDITARE PER CREARE NUOVI STATE
public interface State {

    default void logout(GameController controller, String id) throws RemoteException {
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Logout by " + controller.getUsersByID().get(id).getName()));
    }

    default void sendText(GameController controller, String text, String id) throws RemoteException {
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Unrecognised command"));
    }

    default void login(GameController controller) throws RemoteException {
        for (Player u : controller.getUsersByID().values())
            u.getView().handle(new TextResponse("Player tried to login in invalid state"));
    }
    default void onEnter(GameController controller) throws RemoteException{
    }

    default void select(int[] selections, GameController controller, String id) throws RemoteException{
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Unrecognised command"));
    }

}
