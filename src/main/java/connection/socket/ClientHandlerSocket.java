package connection.socket;

import connection.messages.*;
import connection.messages.requests.*;
import connection.rmi.RemoteController;
import controller.LobbyList;
import connection.server.VirtualViewSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientHandlerSocket implements Runnable, RequestInterpreter {
    private Socket socket;
    private ObjectInputStream sin;
    private ObjectOutputStream sout;
    private LobbyList lobbyList;
    private VirtualViewSocket connectionVirtualView;
    private RemoteController referenceController;

    public ClientHandlerSocket(Socket s, LobbyList lobbyList) throws IOException {
        socket = s;
        sin = new ObjectInputStream(s.getInputStream());
        sout = new ObjectOutputStream(s.getOutputStream());
        this.lobbyList = lobbyList;
        connectionVirtualView = new VirtualViewSocket(sout, this);
    }

    public void run() {
        try {
            while (socket.isConnected()) {
                try {
                    Request received = (Request) sin.readObject();
                    received.handle(this);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            sin.close();
            sout.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReferenceController(RemoteController referenceController) {
        this.referenceController = referenceController;
    }
    //TODO IMPLEMENTARE LA RICEZIONE DEL COMANDO

    @Override
    public void handle(TextRequest request){
        try {
            referenceController.sendText(request.prompt(),request.getSender());
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void handle(LoginRequest request){
        try {
            lobbyList.notifyConnection(connectionVirtualView, request.getUsername());
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void handle(LogoutRequest request){
        try{
            referenceController.logout(request.getSender());
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }
}
