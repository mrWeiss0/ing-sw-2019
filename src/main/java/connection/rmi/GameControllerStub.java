package connection.rmi;

import controller.GameController;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameControllerStub extends UnicastRemoteObject implements RemoteController, Serializable {
    GameController controller;
    public GameControllerStub(GameController controller) throws RemoteException{
        super();
        this.controller=controller;
    }

    @Override
    public void logout(String id) throws RemoteException {
        controller.logout(id);
    }

    @Override
    public void sendText(String text, String id) throws RemoteException {
        controller.sendText(text,id);
    }
}
