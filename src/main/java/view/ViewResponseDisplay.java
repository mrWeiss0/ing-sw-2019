package view;

import client.Client;
import connection.messages.*;
import connection.messages.responses.LoginResponse;
import connection.messages.responses.TextResponse;

public class ViewResponseDisplay implements ResponseDisplay {
    private Client client;
    public ViewResponseDisplay(Client client){
        this.client=client;
    }

    @Override
    public void handle(LoginResponse m) {
        client.saveUuid(m.getId());
        System.out.println(m.prompt());
    }


    @Override
    public void handle(TextResponse m) {
        System.out.println(m.prompt());
    }
}
