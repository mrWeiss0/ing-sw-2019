package client.view.graphic.controllers;

import client.model.GameState;
import client.model.Player;
import client.model.PowerUp;
import client.model.Square;
import client.view.View;
import client.view.graphic.aggregators.WeaponAggregator;
import client.view.graphic.loaders.ImageLoader;
import client.view.graphic.loaders.Scenes;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import java.util.Arrays;
import java.util.List;

public class Play implements View {
    private ImageLoader loader = new ImageLoader();
    //private Pane[][] mapCells = new Pane[3][4];
    //private ImageView[][] weapIm = new ImageView[5][3];
    private WeaponAggregator[][] weapAggr = new WeaponAggregator[5][3];


    @FXML ImageView yourWeapon0;
    @FXML Button yourWeapon0fireMode0;
    @FXML Button yourWeapon0fireMode1;
    @FXML Button yourWeapon0fireMode2;
    @FXML ImageView yourWeapon1;
    @FXML Button yourWeapon1fireMode1;
    @FXML Button yourWeapon1fireMode2;
    @FXML Button yourWeapon1fireMode3;
    @FXML ImageView yourWeapon2;
    @FXML Button yourWeapon2fireMode1;
    @FXML Button yourWeapon2fireMode2;
    @FXML Button yourWeapon2fireMode3;

    public void initialize() {

    }


    public void displayMessage(String message) {}

    public void displayPossibleRoom(int id, Square[] squares) {};

    public void displayPossibleFigure(int id, Player player) {};

    public void displayPossibleSquare(int id, Square square) {};

    public void displayMinToSelect(int min) {};

    public void displayMaxToSelect(int max) {};

    public void displayPowerUps(PowerUp[] powerUps) {

    }

    public void displayPossibleActions(int[] actions) {};

    public void displayCurrentPlayer(int currPlayer) {};

    public void displayRemainingActions(int remaining) {};

    public void displayMapType(int mapID) {};

    public void displayMaxKills(int max) {};

    public void displayKillTrack(int[] killTrack, boolean[] overkills) {};

    public void displayPlayers(Player[] players) {};

    public void displaySquares(Square[] squares) {};

    public void displayPlayerDamage(Player player) {};

    public void displayPlayerMarks(Player player) {};

    public void displayPlayerPoints(Player player) {};

    public void displayPlayerDeaths(Player player) {};

    public void displayPlayerWeapons(Player player) {
        int playerID = Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(player);
        int myID = Scenes.getClient().getModel().getPlayerID();
        for (int i = 0; i < 3; i++) {
            weapAggr[playerID][i] = loader.getWeaponImages(player.getWeapons()[i].getId());
        }
        if (myID == playerID) {
            yourWeapon0.setImage(weapAggr[myID][0].getPortrait());
            yourWeapon1.setImage(weapAggr[myID][1].getPortrait());
            yourWeapon2.setImage(weapAggr[myID][2].getPortrait());
        } else {}
    }

    public void displayPlayerAmmo(Player player) {
        int playerID = Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(player);
        int myID = Scenes.getClient().getModel().getPlayerID();
    }

    public void displayPlayerNPowerUps(Player player) {};

    public void displayPlayerLocation(Player player) {};

    public void displaySquareContent(Square square) {};

    public void displayGameState(GameState state) {};

    //
    public void displayChat(List<String[]> chat) {};

    public void displayRemainingTime(int v) {};

}
