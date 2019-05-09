package connection.messages;


import connection.messages.requests.LoginRequest;
import connection.messages.requests.LogoutRequest;
import connection.messages.requests.TextRequest;
//TODO AGGIUNGERE L'INTERPRETAZIONE DEL COMANDO
public interface RequestInterpreter {
    void handle(TextRequest request);
    void handle(LoginRequest request);
    void handle(LogoutRequest request);
}
