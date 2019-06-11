package client.connection;

public interface Connection {
    void connect(String host, int port) throws Exception;
    void login(String username);
    void createLobby(String name);
    void joinLobby(String name);
    void quitLobby(String name);
    void selectPowerUp(int[] selected);
    void selectWeapon(int[] selected);
    void selectFireMode(int weaponIndex, int[] selectedFireModes);
    void selectGrabbable(int index);
    void selectTargettable(int[] selected);
    void selectColor(int color);
    void selectAction(int actionIndex);
}
