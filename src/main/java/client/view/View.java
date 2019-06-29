package client.view;

import client.model.Player;
import client.model.PowerUp;
import client.model.Square;

public interface View {
    void start();

    void print(String s);

    void exit();

    void displayMessage(String message);

    void displayLobbyList(String[] lobbyList);

    void displayPossibleRoom(int id, Square[] squares);

    void displayPossibleFigure(int id, Player player);

    void displayPossibleSquare(int id, Square square);

    void displayMinToSelect(int min);

    void displayMaxToSelect(int max);

    void displayPowerUps(PowerUp[] powerUps);

    void displayPossibleActions(int[] actions);

    void displayCurrentPlayer(int currPlayer);

    void displayRemainingActions(int remaining);

    void displayMapType(int mapID);

    void displayMaxKills(int max);

    void displayKillTrack(int[] killTrack, boolean[] overkills);

    void displayPlayers(Player[] players);

    void displaySquares(Square[] squares);

    void displayPlayerDamage(Player player);

    void displayPlayerMarks(Player player);

    void displayPlayerPoints(Player player);

    void displayPlayerDeaths(Player player);

    void displayPlayerWeapons(Player player);

    void displayPlayerAmmo(Player player);

    void displayPlayerNPowerUps(Player player);

    void displayPlayerLocation(Player player);

    void displaySquareContent(Square square);

}
