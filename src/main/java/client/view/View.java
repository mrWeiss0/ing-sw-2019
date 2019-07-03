package client.view;

import client.model.GameState;
import client.model.Player;
import client.model.PowerUp;
import client.model.Square;

import java.util.List;

public interface View {
    default void start() {}

    default void print(String s) {}

    default void exit() {}

    default void displayMessage(String message) {}

    default void displayLobbyList(String[] lobbyList) {}

    default void displayPossibleRoom(int id, Square[] squares) {}

    default void displayPossibleFigure(int id, Player player) {}

    default void displayPossibleSquare(int id, Square square) {}

    default void displayMinToSelect(int min) {}

    default void displayMaxToSelect(int max) {}

    default void displayPowerUps(PowerUp[] powerUps) {}

    default void displayPossibleActions(int[] actions) {}

    default void displayCurrentPlayer(int currPlayer) {}

    default void displayRemainingActions(int remaining) {}

    default void displayMapType(int mapID) {}

    default void displayMaxKills(int max) {}

    default void displayKillTrack(int[] killTrack, boolean[] overkills) {}

    default void displayPlayers(Player[] players) {}

    default void displaySquares(Square[] squares) {}

    default void displayPlayerDamage(Player player) {}

    default void displayPlayerMarks(Player player) {}

    default void displayPlayerPoints(Player player) {}

    default void displayPlayerDeaths(Player player) {}

    default void displayPlayerWeapons(Player player) {}

    default void displayPlayerAmmo(Player player) {}

    default void displayPlayerNPowerUps(Player player) {}

    default void displayPlayerLocation(Player player) {}

    default void displaySquareContent(Square square) {}

    default void displayGameState(GameState state) {}

    default void displayChat(List<String[]> chat) {}

    default void displayLeaderBoard(Player player) {}

    default void displayNKills(Player player) {}

    default void displayRemainingTime(int v) {}
}
