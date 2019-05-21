package connection.server;

import connection.messages.responses.Response;
import controller.GameController;

public interface VirtualView {
    void handle(Response response);
    void setController(GameController controller);
}
