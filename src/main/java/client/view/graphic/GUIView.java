package client.view.graphic;

import client.Client;
import client.model.Player;
import client.model.PowerUp;
import client.model.Square;
import client.view.View;
import client.view.graphic.AdrenalineApp;
import client.view.graphic.loaders.Scenes;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.List;

public class GUIView implements View, Runnable {
    protected final Client controller;
    private final Thread thread = new Thread(this);
    private Stage mainWindow;

    public GUIView(Client controller) {
        this.controller = controller;
    }

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void start() {
        Scenes.setClient(controller);
        AdrenalineApp.setView(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Application.launch(AdrenalineApp.class);
        } catch (Exception e) {e.printStackTrace();}
    }

    public void exit() {thread.interrupt();}

    public void displayMessage(String message) {((View)mainWindow.getScene().getUserData()).displayMessage(message); }

    public void displayLobbyList(String[] lobbyList) {((View)mainWindow.getScene().getUserData()).displayLobbyList(lobbyList);}

    public void displayPossibleRoom(int id, Square[] squares) {((View)mainWindow.getScene().getUserData()).displayPossibleRoom(id, squares);};

    public void displayPossibleFigure(int id, Player player) {((View)mainWindow.getScene().getUserData()).displayPossibleFigure(id, player);}

    public void displayPossibleSquare(int id, Square square) {((View)mainWindow.getScene().getUserData()).displayPossibleSquare(id, square);}

    public void displayMinToSelect(int min) {((View)mainWindow.getScene().getUserData()).displayMinToSelect(min);};

    public void displayMaxToSelect(int max) {((View)mainWindow.getScene().getUserData()).displayMaxToSelect(max);};

    public void displayPowerUps(PowerUp[] powerUps) {((View)mainWindow.getScene().getUserData()).displayPowerUps(powerUps);};

    public void displayPossibleActions(int[] actions) {((View)mainWindow.getScene().getUserData()).displayPossibleActions(actions);};

    public void displayCurrentPlayer(int currPlayer) {((View)mainWindow.getScene().getUserData()).displayCurrentPlayer(currPlayer);}

    public void displayRemainingActions(int remaining) {((View)mainWindow.getScene().getUserData()).displayRemainingActions(remaining);};

    public void displayMapType(int mapID) {((View)mainWindow.getScene().getUserData()).displayMapType(mapID);}

    public void displayMaxKills(int max) {((View)mainWindow.getScene().getUserData()).displayMaxKills(max);}

    public void displayKillTrack(int[] killTrack, boolean[] overkills) {((View)mainWindow.getScene().getUserData()).displayKillTrack(killTrack, overkills);}

    public void displayPlayers(Player[] players) {((View)mainWindow.getScene().getUserData()).displayPlayers(players);}

    public void displaySquares(Square[] squares) {((View)mainWindow.getScene().getUserData()).displaySquares(squares);}

    public void displayPlayerDamage(Player player) {((View)mainWindow.getScene().getUserData()).displayPlayerDamage(player);}

    public void displayPlayerMarks(Player player) {((View)mainWindow.getScene().getUserData()).displayPlayerMarks(player);}

    public void displayPlayerPoints(Player player) {((View)mainWindow.getScene().getUserData()).displayPlayerPoints(player);}

    public void displayPlayerDeaths(Player player) {((View)mainWindow.getScene().getUserData()).displayPlayerDeaths(player);}

    public void displayPlayerWeapons(Player player) {((View)mainWindow.getScene().getUserData()).displayPlayerWeapons(player);}

    public void displayPlayerAmmo(Player player) {((View)mainWindow.getScene().getUserData()).displayPlayerAmmo(player);}

    public void displayPlayerNPowerUps(Player player) {((View)mainWindow.getScene().getUserData()).displayPlayerNPowerUps(player);};

    public void displayPlayerLocation(Player player) {((View)mainWindow.getScene().getUserData()).displayPlayerLocation(player);};

    public void displaySquareContent(Square square) {((View)mainWindow.getScene().getUserData()).displaySquareContent(square);}

    public void displayEndGame(boolean value) {((View)mainWindow.getScene().getUserData()).displayEndGame(value);}

    public void displayChat(List<String[]> chat) {((View)mainWindow.getScene().getUserData()).displayChat(chat);}

    public void displayRemainingTime(int v) {((View)mainWindow.getScene().getUserData()).displayRemainingTime(v);}


}
