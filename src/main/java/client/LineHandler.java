package client;

import connection.messages.responses.TextResponse;
import view.TextView;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class LineHandler {
    private Scanner in;
    private TextView view;
    private Client client;

    public LineHandler(Scanner in, TextView view, Client client) {
        this.in = in;
        this.view = view;
        this.client = client;
        try {
            view.handle(new TextResponse("Welcome!\n" +
                    "Use select:*integer* to choose a chat\n" +
                    "Use text:*message* to send a text message\n"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (in.hasNext()) {
                String read = in.nextLine();
                if (read.startsWith("quit")) break;
                handleLineCommand(read);
            }
            view.handle(new TextResponse("Client disconnected"));
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }
    //TODO SCRIVERE LA GENERAZIONE DEL COMANDO
    private void handleLineCommand(String line) throws IOException, NotBoundException {

        if (line.startsWith("text:")) {
            client.getController().sendText(line.substring(5),client.getID());
        } else if (line.startsWith("logout")){
            client.getController().logout(client.getID());
        } else if (line.startsWith("connect:RMI")){
            client.connectRMI();
        } else if(line.startsWith("connect:socket")){
            client.connectSocket();
        }
    }
}
