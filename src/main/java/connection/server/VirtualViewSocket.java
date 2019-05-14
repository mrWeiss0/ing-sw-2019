package connection.server;

import connection.messages.responses.Response;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;
import connection.socket.ClientHandlerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class VirtualViewSocket implements RemoteView {
    private ObjectOutputStream outputStream;
    private ClientHandlerSocket clientHandlerSocket;

    public VirtualViewSocket(ObjectOutputStream outputStream, ClientHandlerSocket clientHandlerSocket) {
        this.outputStream = outputStream;
        this.clientHandlerSocket=clientHandlerSocket;
    }

    public void handle(Response response) {
        try {
            outputStream.writeObject(response);
            outputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setController(RemoteController remoteController){
        clientHandlerSocket.setReferenceController(remoteController);
    }
}
