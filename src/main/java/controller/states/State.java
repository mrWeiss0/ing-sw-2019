package controller.states;

import connection.messages.responses.TextResponse;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;
import controller.Controller;

import java.rmi.RemoteException;
//TODO PUOI EREDITARE PER CREARE NUOVI STATE
public class State implements RemoteController {
    private Controller controller;

    public State(Controller controller){
        this.controller=controller;
    }

    @Override
    public void notifyConnection(RemoteView remoteView) throws RemoteException {
        remoteView.handle(new TextResponse("You are already connected"));
    }

    @Override
    public void logout(String id) throws RemoteException {
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Unrecognised command"));
    }

    @Override
    public void sendText(String text, String id) throws RemoteException {
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Unrecognised command"));
    }

    @Override
    public void loginSelected(String username, String id, int selectionIndex) throws RemoteException {
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Unrecognised command"));
    }

    public Controller getController() {
        return controller;
    }
}
