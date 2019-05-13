package connection.socket;

import connection.client.Client;
import connection.messages.requests.*;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;


import java.io.IOException;
import java.io.ObjectOutputStream;

public class VirtualController implements RemoteController {
    private Client client;
    private ObjectOutputStream objectOutputStream;
    public VirtualController(Client client, ObjectOutputStream objectOutputStream){
        this.objectOutputStream=objectOutputStream;
        this.client=client;
    }
    //TODO IMPLEMENTARE L'INVIO DEL COMANDO

    public void notifyConnection(RemoteView remoteView, String username){
        send(new LoginRequest(username));
    }

    @Override
    public void logout(String id) {
       send(new LogoutRequest());
    }

    @Override
    public void sendText(String text, String id){
        send(new TextRequest(text));
    }


    private void send(Request r){
        try{
            r.setSender(client.getID());
            objectOutputStream.writeObject(r);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
