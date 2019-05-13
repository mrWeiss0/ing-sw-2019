package connection.client;

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
            view.handle(new TextResponse("Welcome!"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (in.hasNext()) {
                String read = in.nextLine();
                if (read.startsWith("quit")) break;
                if (client.isConnected()) {
                    handleLineCommand(read);
                } else {
                    notConnectedCommand(read);
                }
            }
            view.handle(new TextResponse("Client disconnected"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO SCRIVERE LA GENERAZIONE DEL COMANDO
    private void handleLineCommand(String line) throws IOException {
        if (line.startsWith("logout")) {
            client.getController().logout(client.getID());
            view.handle(new TextResponse("Logout"));
        } else {
            client.getController().sendText(line,client.getID());
        }
    }

    private void notConnectedCommand(String line) throws IOException, NotBoundException{
        if (line.startsWith("/n ")) {
            client.setName(line.substring(3));
        } else if(line.startsWith("/connectRMI")){
            client.logout();
            client.connectRMI();
        } else if(line.startsWith("/connectSocket")){
            client.logout();
            client.connectSocket();
        }
    }
}
