package controller.states;

import connection.messages.responses.TextResponse;
import controller.Controller;
import model.Player;

import java.rmi.RemoteException;

//TODO PUOI EREDITARE PER CREARE NUOVI STATE
public class State {

    public void logout(Controller controller, String id) throws RemoteException {
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Logout by " + controller.getUsersByID().get(id).getName()));
    }

    public void sendText(Controller controller, String text, String id) throws RemoteException {
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Unrecognised command"));
    }

    public void login(Controller controller) throws RemoteException {
        for (Player u : controller.getUsersByID().values())
            u.getView().handle(new TextResponse("Player tried to login in invalid state"));
    }

}
