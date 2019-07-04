package client.view.graphic.controllers;

import client.model.GameState;
import client.model.Player;
import client.model.PowerUp;
import client.model.Square;
import client.view.View;
import client.view.graphic.aggregators.WeaponAggregator;
import client.view.graphic.loaders.ImageLoader;
import client.view.graphic.loaders.Scenes;
import javafx.application.Platform;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Play implements View {
    private ImageLoader loader = new ImageLoader();

    private ImageView[] killcountImages = new ImageView[8];
    private ImageView[] playerPortraits = new ImageView[5];
    private ImageView[][] weapImages = new ImageView[5][3];
    private Text[] pupNumber = new Text[4];
    private ImageView[] yourPupImages = new ImageView[4];
    private ImageView[][] actionImages = new ImageView[5][3];
    private ImageView[][] damageImages = new ImageView[5][12];
    private ImageView[][] markImages = new ImageView[5][4];
    private ImageView[][] ammoCubeImages = new ImageView[5][3];
    private WeaponAggregator[][] weapAggr = new WeaponAggregator[5][3];
    private WeaponAggregator[] pupAggr = new WeaponAggregator[4];
    private ImageView[][] squareContents = new ImageView[12][6];

    //Killtrack
    @FXML ImageView killtrackImage;
    @FXML ImageView kill0;
    @FXML ImageView kill1;
    @FXML ImageView kill2;
    @FXML ImageView kill3;
    @FXML ImageView kill4;
    @FXML ImageView kill5;
    @FXML ImageView kill6;
    @FXML ImageView kill7;

    //Map
    @FXML ImageView mapImage;

    //Player portraits
    @FXML ImageView player0Portrait;
    @FXML ImageView player1Portrait;
    @FXML ImageView player2Portrait;
    @FXML ImageView player3Portrait;
    @FXML ImageView yourPortrait;

    //Weapons
    @FXML ImageView player0Weapon0;
    @FXML ImageView player0Weapon1;
    @FXML ImageView player0Weapon2;
    @FXML ImageView player1Weapon0;
    @FXML ImageView player1Weapon1;
    @FXML ImageView player1Weapon2;
    @FXML ImageView player2Weapon0;
    @FXML ImageView player2Weapon1;
    @FXML ImageView player2Weapon2;
    @FXML ImageView player3Weapon0;
    @FXML ImageView player3Weapon1;
    @FXML ImageView player3Weapon2;
    @FXML ImageView yourWeapon0;
    @FXML ImageView yourWeapon1;
    @FXML ImageView yourWeapon2;

    //Other players PowerUps
    @FXML Text player0NPup;
    @FXML Text player1NPup;
    @FXML Text player2NPup;
    @FXML Text player3NPup;

    //Your PowerUps
    @FXML ImageView yourPowerUp0;
    @FXML ImageView yourPowerUp1;
    @FXML ImageView yourPowerUp2;
    @FXML ImageView yourPowerUp3;

    @FXML Button yourWeapon2fireMode1;
    @FXML Button yourWeapon2fireMode2;
    @FXML Button yourWeapon2fireMode3;
    @FXML Button yourWeapon0fireMode0;
    @FXML Button yourWeapon0fireMode1;
    @FXML Button yourWeapon0fireMode2;
    @FXML Button yourWeapon1fireMode1;
    @FXML Button yourWeapon1fireMode2;
    @FXML Button yourWeapon1fireMode3;

    //Actions
    @FXML ImageView yourAction0;
    @FXML ImageView yourAction1;
    @FXML ImageView yourAction2;

    //AmmoCubes
    @FXML ImageView player0RedAmmo;
    @FXML ImageView player0YellowAmmo;
    @FXML ImageView player0BlueAmmo;
    @FXML ImageView player1RedAmmo;
    @FXML ImageView player1YellowAmmo;
    @FXML ImageView player1BlueAmmo;
    @FXML ImageView player2RedAmmo;
    @FXML ImageView player2YellowAmmo;
    @FXML ImageView player2BlueAmmo;
    @FXML ImageView player3RedAmmo;
    @FXML ImageView player3YellowAmmo;
    @FXML ImageView player3BlueAmmo;
    @FXML ImageView yourRedAmmo;
    @FXML ImageView yourYellowAmmo;
    @FXML ImageView yourBlueAmmo;

    //Damages
    @FXML ImageView player0Damage0;
    @FXML ImageView player0Damage1;
    @FXML ImageView player0Damage2;
    @FXML ImageView player0Damage3;
    @FXML ImageView player0Damage4;
    @FXML ImageView player0Damage5;
    @FXML ImageView player0Damage6;
    @FXML ImageView player0Damage7;
    @FXML ImageView player0Damage8;
    @FXML ImageView player0Damage9;
    @FXML ImageView player0Damage10;
    @FXML ImageView player0Damage11;
    @FXML ImageView player1Damage0;
    @FXML ImageView player1Damage1;
    @FXML ImageView player1Damage2;
    @FXML ImageView player1Damage3;
    @FXML ImageView player1Damage4;
    @FXML ImageView player1Damage5;
    @FXML ImageView player1Damage6;
    @FXML ImageView player1Damage7;
    @FXML ImageView player1Damage8;
    @FXML ImageView player1Damage9;
    @FXML ImageView player1Damage10;
    @FXML ImageView player1Damage11;
    @FXML ImageView player2Damage0;
    @FXML ImageView player2Damage1;
    @FXML ImageView player2Damage2;
    @FXML ImageView player2Damage3;
    @FXML ImageView player2Damage4;
    @FXML ImageView player2Damage5;
    @FXML ImageView player2Damage6;
    @FXML ImageView player2Damage7;
    @FXML ImageView player2Damage8;
    @FXML ImageView player2Damage9;
    @FXML ImageView player2Damage10;
    @FXML ImageView player2Damage11;
    @FXML ImageView player3Damage0;
    @FXML ImageView player3Damage1;
    @FXML ImageView player3Damage2;
    @FXML ImageView player3Damage3;
    @FXML ImageView player3Damage4;
    @FXML ImageView player3Damage5;
    @FXML ImageView player3Damage6;
    @FXML ImageView player3Damage7;
    @FXML ImageView player3Damage8;
    @FXML ImageView player3Damage9;
    @FXML ImageView player3Damage10;
    @FXML ImageView player3Damage11;
    @FXML ImageView yourDamage0;
    @FXML ImageView yourDamage1;
    @FXML ImageView yourDamage2;
    @FXML ImageView yourDamage3;
    @FXML ImageView yourDamage4;
    @FXML ImageView yourDamage5;
    @FXML ImageView yourDamage6;
    @FXML ImageView yourDamage7;
    @FXML ImageView yourDamage8;
    @FXML ImageView yourDamage9;
    @FXML ImageView yourDamage10;
    @FXML ImageView yourDamage11;

    //Mark
    @FXML ImageView player0Marks0;
    @FXML ImageView player0Marks1;
    @FXML ImageView player0Marks2;
    @FXML ImageView player0Marks3;
    @FXML ImageView player1Marks0;
    @FXML ImageView player1Marks1;
    @FXML ImageView player1Marks2;
    @FXML ImageView player1Marks3;
    @FXML ImageView player2Marks0;
    @FXML ImageView player2Marks1;
    @FXML ImageView player2Marks2;
    @FXML ImageView player2Marks3;
    @FXML ImageView player3Marks0;
    @FXML ImageView player3Marks1;
    @FXML ImageView player3Marks2;
    @FXML ImageView player3Marks3;
    @FXML ImageView yourMarks0;
    @FXML ImageView yourMarks1;
    @FXML ImageView yourMarks2;
    @FXML ImageView yourMarks3;

    public void initialize() {
        //Killtrack
        killcountImages[0] = kill0;
        killcountImages[1] = kill1;
        killcountImages[2] = kill2;
        killcountImages[3] = kill3;
        killcountImages[4] = kill4;
        killcountImages[5] = kill5;
        killcountImages[6] = kill6;
        killcountImages[7] = kill7;

        //Player portrait
        playerPortraits[0] = player0Portrait;
        playerPortraits[1] = player1Portrait;
        playerPortraits[2] = player2Portrait;
        playerPortraits[3] = player3Portrait;
        playerPortraits[4] = yourPortrait;

        //Weapons
        weapImages[0][0] = player0Weapon0;
        weapImages[0][1] = player0Weapon1;
        weapImages[0][2] = player0Weapon2;
        weapImages[1][0] = player1Weapon0;
        weapImages[1][1] = player1Weapon1;
        weapImages[1][2] = player1Weapon2;
        weapImages[2][0] = player2Weapon0;
        weapImages[2][1] = player2Weapon1;
        weapImages[2][2] = player2Weapon2;
        weapImages[3][0] = player3Weapon0;
        weapImages[3][1] = player3Weapon1;
        weapImages[3][2] = player3Weapon2;
        weapImages[4][0] = yourWeapon0;
        weapImages[4][1] = yourWeapon1;
        weapImages[4][2] = yourWeapon2;

        //Other players PowerUps
        pupNumber[0] = player0NPup;
        pupNumber[1] = player1NPup;
        pupNumber[2] = player2NPup;
        pupNumber[3] = player3NPup;

        //Your PowerUps
        yourPupImages[0] = yourPowerUp0;
        yourPupImages[1] = yourPowerUp1;
        yourPupImages[2] = yourPowerUp2;
        yourPupImages[3] = yourPowerUp3;

        //Actions
        actionImages[4][0] = yourAction0;
        actionImages[4][1] = yourAction1;
        actionImages[4][2] = yourAction2;

        //Ammocubes
        ammoCubeImages[0][0] = player0RedAmmo;
        ammoCubeImages[0][1] = player0YellowAmmo;
        ammoCubeImages[0][2] = player0BlueAmmo;
        ammoCubeImages[1][0] = player1RedAmmo;
        ammoCubeImages[1][1] = player1YellowAmmo;
        ammoCubeImages[1][2] = player1BlueAmmo;
        ammoCubeImages[2][0] = player2RedAmmo;
        ammoCubeImages[2][1] = player2YellowAmmo;
        ammoCubeImages[2][2] = player2BlueAmmo;
        ammoCubeImages[3][0] = player3RedAmmo;
        ammoCubeImages[3][1] = player3YellowAmmo;
        ammoCubeImages[3][2] = player3BlueAmmo;
        ammoCubeImages[4][0] = yourRedAmmo;
        ammoCubeImages[4][1] = yourYellowAmmo;
        ammoCubeImages[4][2] = yourBlueAmmo;

        //Damages
        damageImages[0][0] = player0Damage0;
        damageImages[0][1] = player0Damage1;
        damageImages[0][2] = player0Damage2;
        damageImages[0][3] = player0Damage3;
        damageImages[0][4] = player0Damage4;
        damageImages[0][5] = player0Damage5;
        damageImages[0][6] = player0Damage6;
        damageImages[0][7] = player0Damage7;
        damageImages[0][8] = player0Damage8;
        damageImages[0][9] = player0Damage9;
        damageImages[0][10] = player0Damage10;
        damageImages[0][11] = player0Damage11;
        damageImages[1][0] = player1Damage0;
        damageImages[1][1] = player1Damage1;
        damageImages[1][2] = player1Damage2;
        damageImages[1][3] = player1Damage3;
        damageImages[1][4] = player1Damage4;
        damageImages[1][5] = player1Damage5;
        damageImages[1][6] = player1Damage6;
        damageImages[1][7] = player1Damage7;
        damageImages[1][8] = player1Damage8;
        damageImages[1][9] = player1Damage9;
        damageImages[1][10] = player1Damage10;
        damageImages[1][11] = player1Damage11;
        damageImages[2][0] = player2Damage0;
        damageImages[2][1] = player2Damage1;
        damageImages[2][2] = player2Damage2;
        damageImages[2][3] = player2Damage3;
        damageImages[2][4] = player2Damage4;
        damageImages[2][5] = player2Damage5;
        damageImages[2][6] = player2Damage6;
        damageImages[2][7] = player2Damage7;
        damageImages[2][8] = player2Damage8;
        damageImages[2][9] = player2Damage9;
        damageImages[2][10] = player2Damage10;
        damageImages[2][11] = player2Damage11;
        damageImages[3][0] = player3Damage0;
        damageImages[3][1] = player3Damage1;
        damageImages[3][2] = player3Damage2;
        damageImages[3][3] = player3Damage3;
        damageImages[3][4] = player3Damage4;
        damageImages[3][5] = player3Damage5;
        damageImages[3][6] = player3Damage6;
        damageImages[3][7] = player3Damage7;
        damageImages[3][8] = player3Damage8;
        damageImages[3][9] = player3Damage9;
        damageImages[3][10] = player3Damage10;
        damageImages[3][11] = player3Damage11;
        damageImages[4][0] = yourDamage0;
        damageImages[4][1] = yourDamage1;
        damageImages[4][2] = yourDamage2;
        damageImages[4][3] = yourDamage3;
        damageImages[4][4] = yourDamage4;
        damageImages[4][5] = yourDamage5;
        damageImages[4][6] = yourDamage6;
        damageImages[4][7] = yourDamage7;
        damageImages[4][8] = yourDamage8;
        damageImages[4][9] = yourDamage9;
        damageImages[4][10] = yourDamage10;
        damageImages[4][11] = yourDamage11;

        //Marks
        markImages[0][0] = player0Marks0;
        markImages[0][1] = player0Marks1;
        markImages[0][2] = player0Marks2;
        markImages[0][3] = player0Marks3;
        markImages[1][0] = player1Marks0;
        markImages[1][1] = player1Marks1;
        markImages[1][2] = player1Marks2;
        markImages[1][3] = player1Marks3;
        markImages[2][0] = player2Marks0;
        markImages[2][1] = player2Marks1;
        markImages[2][2] = player2Marks2;
        markImages[2][3] = player2Marks3;
        markImages[3][0] = player3Marks0;
        markImages[3][1] = player3Marks1;
        markImages[3][2] = player3Marks2;
        markImages[3][3] = player3Marks3;
        markImages[4][0] = yourMarks0;
        markImages[4][1] = yourMarks1;
        markImages[4][2] = yourMarks2;
        markImages[4][3] = yourMarks3;


    }


    public void displayMessage(String message) {}

    public void displayPossibleRoom(int id, Square[] squares) {}

    public void displayPossibleFigure(int id, Player player) {}

    public void displayPossibleSquare(int id, Square square) {}

    public void displayMinToSelect(int min) {}

    public void displayMaxToSelect(int max) {}

    public void displayPowerUps(PowerUp[] powerUps) {
        Platform.runLater( () -> {
                    for (int i = 0; i < powerUps.length; i++) {
                        pupAggr[i] = loader.getPowerUpImages(powerUps[i].getType(), powerUps[i].getColor());
                        yourPupImages[i].setImage(pupAggr[i].getPortrait());
                    }
        });
    }

    public void displayPossibleActions(int[] actions) {
        Platform.runLater( () -> {
            if (actions.length == 3 &&
                    Scenes.getClient().getModel().getActionSetID() == 0) {
                actionImages[4][0].setImage(loader.getAction(0));
                actionImages[4][1].setImage(loader.getAction(1));
                actionImages[4][2].setImage(loader.getAction(2));
            } else if (actions.length == 3 &&
                    Scenes.getClient().getModel().getActionSetID() == 1) {
                actionImages[4][0].setImage(loader.getAction(5));
                actionImages[4][1].setImage(loader.getAction(6));
                actionImages[4][2].setImage(loader.getAction(7));
            } else if (actions.length == 2 && Scenes.getClient().getModel().getActionSetID() == 2) {
                actionImages[4][0].setImage(loader.getAction(8));
                actionImages[4][1].setImage(loader.getAction(9));
                actionImages[4][2].setImage(loader.getAction(-1));
            } else {
                actionImages[4][0].setImage(loader.getAction(-1));
                actionImages[4][1].setImage(loader.getAction(-1));
                actionImages[4][2].setImage(loader.getAction(-1));
            }
        });
    }

    public void displayCurrentPlayer(int currPlayer) {}

    public void displayRemainingActions(int remaining) {};

    public void displayMapType(int mapID) {
        Platform.runLater( () -> mapImage.setImage(loader.getMap(mapID)));
    }

    public void displayMaxKills(int max) {
        Platform.runLater( () -> killtrackImage.setImage(loader.getKillTrack(max)));
    }

    public void displayKillTrack(int[] killTrack, boolean[] overkills) {
        Platform.runLater( () -> {
            for (int i = 0; i < killTrack.length; i++) {
                killcountImages[i].setImage(loader.getDamage(killTrack[i], (overkills[i] ? 2 : 1) ));
            }
        });
    }

    public void displayPlayers(Player[] players) {
        Platform.runLater( () -> {
            for(int i = 0; i < players.length; i++) {
               playerPortraits[getRelativeID(Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(players[i]), Scenes.getClient().getModel().getPlayerID())]
                       .setImage(loader.getPlayerPortrait(Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(players[i])));
            }

        });
    }

    public void displaySquares(Square[] squares) {}

    public void displayPlayerDamage(Player player) {
        Platform.runLater( () -> {
        int playerID = Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(player);
        int myID = Scenes.getClient().getModel().getPlayerID();
        int relativeID = getRelativeID(playerID, myID);
        for (int i = 0; i < 11; i++) {
            damageImages[relativeID][i].setImage(loader.getDamage(getRelativeID(player.getDamages()[i] ,myID),1));
        }
        if(playerID == myID && player.getDamages().length > 2) {
            actionImages[4][1].setImage(loader.getAction(3));
        }
        if(playerID == myID && player.getDamages().length > 5) {
            actionImages[4][2].setImage(loader.getAction(5));
        }
        });
    }

    public void displayPlayerMarks(Player player) {
        Platform.runLater( () -> {
            int playerID = Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(player);
            int myID = Scenes.getClient().getModel().getPlayerID();
            int relativeID = getRelativeID(playerID, myID);
            Map<Integer, Long> x = Arrays.stream(player.getMarks()).boxed().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            for (int i = 0; i < 3; i++) {
                markImages[relativeID][i].setImage(loader.getDamage(playerID, ((int)(long) x.get(playerID) )));
            }
        });
    }

    public void displayPlayerPoints(Player player) {};

    public void displayPlayerDeaths(Player player) {};

    public void displayPlayerWeapons(Player player) {
        Platform.runLater( () -> {
            int playerID = Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(player);
            int myID = Scenes.getClient().getModel().getPlayerID();
            int relativeID = getRelativeID(playerID, myID);
            for (int i = 0; i < 3; i++) {
                weapAggr[relativeID][i] = loader.getWeaponImages(player.getWeapons()[i].getId());
                weapImages[relativeID][i].setImage(weapAggr[relativeID][i].getPortrait());
            }
        });
    }

    public void displayPlayerAmmo(Player player) {
        int playerID = Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(player);
        int myID = Scenes.getClient().getModel().getPlayerID();
        int relativeID = getRelativeID(playerID, myID);
        for (int i = 0; i < 3; i++) {
            ammoCubeImages[relativeID][i].setImage(loader.getAmmoCubesImages(player.getAmmo()).getRybImages()[i]);
        }
    }

    public void displayPlayerNPowerUps(Player player) {
        int playerID = Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(player);
        int myID = Scenes.getClient().getModel().getPlayerID();
        int relativeID = getRelativeID(playerID, myID);
        pupNumber[relativeID].setText(Integer.toString(player.getnPowerup()));
    }

    public void displayPlayerLocation(Player player) {};

    public void displaySquareContent(Square square) {};

    public void displayGameState(GameState state) {};

    public void displayChat(List<String[]> chat) {};

    public void displayRemainingTime(int v) {};

    public int getRelativeID(int playerID, int myID) {
        if(playerID > myID) {
            return playerID - 1;
        } else if(playerID < myID) {
            return playerID;
        } else {
            return 4;
        }
    }

    public int getAbsoluteIndex(int relativeID, int myID) {
        if (relativeID == 4) {
            return myID;
        }
        else if (relativeID >= myID) {
            return relativeID + 1;
        }
        else {
            return relativeID;
        }
    }

}
