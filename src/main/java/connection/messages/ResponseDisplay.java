package connection.messages;

import connection.messages.responses.LoginResponse;
import connection.messages.responses.TextResponse;

public interface ResponseDisplay {
    void handle(LoginResponse m);

    void handle(TextResponse m);
}
