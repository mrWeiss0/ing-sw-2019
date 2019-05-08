package connection.messages;


import connection.messages.requests.ConnectionRequest;
import connection.messages.requests.ControllerSelectionRequest;
import connection.messages.requests.LogoutRequest;
import connection.messages.requests.TextRequest;
//TODO AGGIUNGERE L'INTERPRETAZIONE DEL COMANDO
public interface RequestInterpreter {
    void handle(ControllerSelectionRequest request);
    void handle(TextRequest request);
    void handle(ConnectionRequest request);
    void handle(LogoutRequest request);
}
