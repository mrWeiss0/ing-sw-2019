package client.connection;

import java.io.Closeable;
import java.io.IOException;

public interface Connection extends Closeable {
    void connect(String host, int port) throws IOException;

    void login(String username);

    void createLobby(String name);

    void joinLobby(String name);

    void quitLobby();

    void selectPowerUp(int[] selected);

    void selectWeapon(int[] selected);

    void selectFireMode(int weaponIndex, int[] selectedFireModes);

    void selectGrabbable(int index);

    void selectTargettable(int[] selected);

    void selectColor(int color);

    void selectAction(int actionIndex);

    void sendChat(String msg);

    void reconnect();

    void endTurn();

    @Override
    void close();
}
