package connection.socket;

import connection.client.Client;
import connection.messages.requests.*;
import connection.rmi.RemoteConnectionHandler;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;


import java.io.IOException;
import java.io.ObjectOutputStream;

public class VirtualController implements RemoteController, RemoteConnectionHandler {
    private Client client;
    private ObjectOutputStream objectOutputStream;
    public VirtualController(Client client, ObjectOutputStream objectOutputStream){
        this.objectOutputStream=objectOutputStream;
        this.client=client;
    }
    //TODO IMPLEMENTARE L'INVIO DEL COMANDO

    public RemoteController notifyConnection(RemoteView remoteView, String username){
        send(new LoginRequest(username));
        return this;
    }

    @Override
    public void logout(String id) {
       send(new LogoutRequest());
    }

    @Override
    public void sendText(String text, String id){
        send(new TextRequest(text));
    }

    public RemoteController reconnect(String id, RemoteView remoteView){
        send(new ReconnectRequest());
        return this;
    }
    private void send(Request r){
        try{
            r.setSender(client.getID());
            objectOutputStream.writeObject(r);
            objectOutputStream.reset();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
