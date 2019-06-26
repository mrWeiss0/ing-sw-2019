package client.view;

public interface View {
    void start();

    void print(String s);

    void exit();

    void displayLobbyList(String[] lobbyList);
}
