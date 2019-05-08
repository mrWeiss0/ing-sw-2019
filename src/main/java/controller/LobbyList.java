package controller;

import connection.messages.responses.ControllerProposalResponse;
import connection.messages.responses.TextResponse;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class LobbyList extends UnicastRemoteObject implements RemoteController, Serializable {
    private ArrayList<Controller> controllers;
    HashMap<String, RemoteView> viewsByID;
    public LobbyList() throws RemoteException {
        super();
        controllers = new ArrayList<>();
        this.viewsByID = new HashMap<>();
    }

    public void addController(Controller c) {
        controllers.add(c);
    }

    public ArrayList<Controller> getControllers() {
        return controllers;
    }

    public String toString() {
        return controllers.stream()
                .map(x -> "-" + controllers.indexOf(x) + "  " + x.getName() + " : " + x.occupancy() + "\n")
                .collect(Collectors.joining());
    }
    //TODO AGGIUNGERE IL COMANDO ANCHE QUA
    @Override
    public void notifyConnection(RemoteView remoteView) throws RemoteException {
        String id = UUID.randomUUID().toString();
        viewsByID.put(id,remoteView);
        remoteView.handle(new ControllerProposalResponse(toString(), id));
        System.out.println("Registered new view "+id);
    }

    @Override
    public void logout(String id) throws RemoteException {
        viewsByID.remove(id);
    }

    @Override
    public void sendText(String text, String id) throws RemoteException {

        if (text.equals("resend")) notifyConnection(viewsByID.get(id));
        else viewsByID.get(id).handle(
                new TextResponse("You have to connect first!, select a valid chat from the list\n" +
                                "(type text:resend to get a new list)"));

    }

    @Override
    public void loginSelected(String username, String id, int selectionIndex) throws RemoteException{
        if (!viewsByID.keySet().contains(id)) return;
        if (selectionIndex >= 0 && selectionIndex < controllers.size() && controllers.get(selectionIndex).canJoin()) {
            Controller selectedController = controllers.get(selectionIndex);
            System.out.println(viewsByID);
            selectedController.login(username, viewsByID.get(id), id);
            viewsByID.get(id).setController(selectedController);
        } else {
            viewsByID.get(id).handle(
                    new TextResponse("The selected chat is full or invalid, please select a new one\n" +
                            "(or type text:resend to get a new one)"));
        }
    }
}
