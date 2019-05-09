package controller.states;

import connection.messages.responses.TextResponse;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;
import controller.Controller;

import java.rmi.RemoteException;
//TODO PUOI EREDITARE PER CREARE NUOVI STATE
public class State {
    private Controller controller;

    public State(Controller controller){
        this.controller=controller;
    }

    public void logout(String id) throws RemoteException {
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Logout by "+controller.getUsersByID().get(id).getName()));
    }

    public void sendText(String text, String id) throws RemoteException {
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Unrecognised command"));
    }

    public void login(){

    }

    public Controller getController() {
        return controller;
    }
}
