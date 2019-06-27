package client.view;

import client.model.Player;
import client.model.Square;

public interface View {
    void start();

    void print(String s);

    void exit();

    void displayLobbyList(String[] lobbyList);

    void displayPossibleRoom(int id, Square[] squares);

    void displayPossibleFigure(int id, Player player);

    void displayPossibleSquare(int id, Square square);
}
