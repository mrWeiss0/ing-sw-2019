package connection.messages.requests;

import connection.messages.RequestInterpreter;

import java.io.Serializable;
//TODO ESTENDI PER NUOVI COMANDI
public abstract class Request implements Serializable {
    private String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public abstract String prompt();
    public abstract void handle(RequestInterpreter requestInterpreter);
}
