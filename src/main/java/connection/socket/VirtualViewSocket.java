package connection.socket;

import connection.messages.responses.Response;
import connection.rmi.RemoteController;
import connection.rmi.RemoteView;
import connection.server.VirtualView;
import connection.socket.ClientHandlerSocket;
import controller.GameController;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class VirtualViewSocket implements VirtualView {
    private ObjectOutputStream outputStream;
    private ClientHandlerSocket clientHandlerSocket;

    public VirtualViewSocket(ObjectOutputStream outputStream, ClientHandlerSocket clientHandlerSocket) {
        this.outputStream = outputStream;
        this.clientHandlerSocket = clientHandlerSocket;
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
    public void setController(GameController gameController) {
        clientHandlerSocket.setReferenceController(gameController);
    }
}
