package connection.rmi;

import connection.messages.responses.Response;
import connection.server.VirtualView;
import controller.GameController;

import java.rmi.RemoteException;

public class VirtualViewRMI implements VirtualView {
    private RemoteView remoteView;
    public VirtualViewRMI(RemoteView remoteView){
        this.remoteView= remoteView;
    }
    @Override
    public void handle(Response response) {
        try {
            remoteView.handle(response);
        }catch(RemoteException ignored){

        }
    }

    @Override
    public void setController(GameController controller) {
        try {
            remoteView.setController(new GameControllerStub(controller));
        }catch(RemoteException ignored){

        }
    }

}
