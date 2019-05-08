package controller.states;

import connection.messages.responses.TextResponse;
import controller.Controller;

import java.rmi.RemoteException;

public class WaitingState extends State{
    private Controller controller;

    public WaitingState(Controller controller) {
        super(controller);
    }

    @Override
    public void sendText(String text, String id) throws RemoteException {

    }
}
