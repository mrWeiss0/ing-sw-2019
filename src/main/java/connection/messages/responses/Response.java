package connection.messages.responses;

import connection.messages.ResponseDisplay;

import java.io.Serializable;

public abstract class Response implements Serializable {
    public abstract String prompt();
    public abstract void handle(ResponseDisplay responseDisplay);
}
