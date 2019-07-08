package client.view.graphic.controllers;

import client.model.GameState;
import client.model.Player;
import client.model.PowerUp;
import client.model.Square;
import client.view.View;
import client.view.graphic.ButtonType;
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
import server.model.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Play implements View {
    private ImageLoader loader = new ImageLoader();

    private ButtonType lastType;
    private List<Integer> toSend = new ArrayList<Integer>();

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
    private int[] prevCord = new int[4];

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

    //Control
    @FXML ImageView cancelButton;
    @FXML ImageView confirmButton;
    @FXML ImageView returnButton;

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

    //Firemodes
    @FXML Button yourWeapon0Fire0;
    @FXML Button yourWeapon0Fire1;
    @FXML Button yourWeapon0Fire2;
    @FXML Button yourWeapon1Fire0;
    @FXML Button yourWeapon1Fire1;
    @FXML Button yourWeapon1Fire2;
    @FXML Button yourWeapon2Fire0;
    @FXML Button yourWeapon2Fire1;
    @FXML Button yourWeapon2Fire2;

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

    //Actions
    @FXML ImageView yourAction0;
    @FXML ImageView yourAction1;
    @FXML ImageView yourAction2;
    @FXML ImageView yourReload;
    @FXML ImageView yourEndTurn;

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

    //Square
    @FXML Button cell_0_0;
    @FXML Button cell_0_1;
    @FXML Button cell_0_2;
    @FXML Button cell_0_3;
    @FXML Button cell_1_0;
    @FXML Button cell_1_1;
    @FXML Button cell_1_2;
    @FXML Button cell_1_3;
    @FXML Button cell_2_0;
    @FXML Button cell_2_1;
    @FXML Button cell_2_2;
    @FXML Button cell_2_3;

    //Room
    @FXML ImageView room0;
    @FXML ImageView room1;
    @FXML ImageView room2;
    @FXML ImageView room3;
    @FXML ImageView room4;
    @FXML ImageView room5;

    //Square fill
    @FXML ImageView player0Cell_0_0;
    @FXML ImageView player0Cell_0_1;
    @FXML ImageView player0Cell_0_2;
    @FXML ImageView player0Cell_0_3;
    @FXML ImageView player0Cell_1_0;
    @FXML ImageView player0Cell_1_1;
    @FXML ImageView player0Cell_1_2;
    @FXML ImageView player0Cell_1_3;
    @FXML ImageView player0Cell_2_0;
    @FXML ImageView player0Cell_2_1;
    @FXML ImageView player0Cell_2_2;
    @FXML ImageView player0Cell_2_3;
    @FXML ImageView player1Cell_0_0;
    @FXML ImageView player1Cell_0_1;
    @FXML ImageView player1Cell_0_2;
    @FXML ImageView player1Cell_0_3;
    @FXML ImageView player1Cell_1_0;
    @FXML ImageView player1Cell_1_1;
    @FXML ImageView player1Cell_1_2;
    @FXML ImageView player1Cell_1_3;
    @FXML ImageView player1Cell_2_0;
    @FXML ImageView player1Cell_2_1;
    @FXML ImageView player1Cell_2_2;
    @FXML ImageView player1Cell_2_3;
    @FXML ImageView player2Cell_0_0;
    @FXML ImageView player2Cell_0_1;
    @FXML ImageView player2Cell_0_2;
    @FXML ImageView player2Cell_0_3;
    @FXML ImageView player2Cell_1_0;
    @FXML ImageView player2Cell_1_1;
    @FXML ImageView player2Cell_1_2;
    @FXML ImageView player2Cell_1_3;
    @FXML ImageView player2Cell_2_0;
    @FXML ImageView player2Cell_2_1;
    @FXML ImageView player2Cell_2_2;
    @FXML ImageView player2Cell_2_3;
    @FXML ImageView player3Cell_0_0;
    @FXML ImageView player3Cell_0_1;
    @FXML ImageView player3Cell_0_2;
    @FXML ImageView player3Cell_0_3;
    @FXML ImageView player3Cell_1_0;
    @FXML ImageView player3Cell_1_1;
    @FXML ImageView player3Cell_1_2;
    @FXML ImageView player3Cell_1_3;
    @FXML ImageView player3Cell_2_0;
    @FXML ImageView player3Cell_2_1;
    @FXML ImageView player3Cell_2_2;
    @FXML ImageView player3Cell_2_3;
    @FXML ImageView player4Cell_0_0;
    @FXML ImageView player4Cell_0_1;
    @FXML ImageView player4Cell_0_2;
    @FXML ImageView player4Cell_0_3;
    @FXML ImageView player4Cell_1_0;
    @FXML ImageView player4Cell_1_1;
    @FXML ImageView player4Cell_1_2;
    @FXML ImageView player4Cell_1_3;
    @FXML ImageView player4Cell_2_0;
    @FXML ImageView player4Cell_2_1;
    @FXML ImageView player4Cell_2_2;
    @FXML ImageView player4Cell_2_3;
    @FXML ImageView ammoCell_0_0;
    @FXML ImageView ammoCell_0_1;
    @FXML ImageView ammoCell_0_2;
    @FXML ImageView ammoCell_0_3;
    @FXML ImageView ammoCell_1_0;
    @FXML ImageView ammoCell_1_1;
    @FXML ImageView ammoCell_1_2;
    @FXML ImageView ammoCell_1_3;
    @FXML ImageView ammoCell_2_0;
    @FXML ImageView ammoCell_2_1;
    @FXML ImageView ammoCell_2_2;
    @FXML ImageView ammoCell_2_3;



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

        //Squares
        squareContents[0][0] = player0Cell_0_0;
        squareContents[1][0] = player0Cell_0_1;
        squareContents[2][0] = player0Cell_0_2;
        squareContents[3][0] = player0Cell_0_3;
        squareContents[4][0] = player0Cell_1_0;
        squareContents[5][0] = player0Cell_1_1;
        squareContents[6][0] = player0Cell_1_2;
        squareContents[7][0] = player0Cell_1_3;
        squareContents[8][0] = player0Cell_2_0;
        squareContents[9][0] = player0Cell_2_1;
        squareContents[10][0] = player0Cell_2_2;
        squareContents[11][0] = player0Cell_2_3;
        squareContents[0][1] = player1Cell_0_0;
        squareContents[1][1] = player1Cell_0_1;
        squareContents[2][1] = player1Cell_0_2;
        squareContents[3][1] = player1Cell_0_3;
        squareContents[4][1] = player1Cell_1_0;
        squareContents[5][1] = player1Cell_1_1;
        squareContents[6][1] = player1Cell_1_2;
        squareContents[7][1] = player1Cell_1_3;
        squareContents[8][1] = player1Cell_2_0;
        squareContents[9][1] = player1Cell_2_1;
        squareContents[10][1] = player1Cell_2_2;
        squareContents[11][1] = player1Cell_2_3;
        squareContents[0][2] = player2Cell_0_0;
        squareContents[1][2] = player2Cell_0_1;
        squareContents[2][2] = player2Cell_0_2;
        squareContents[3][2] = player2Cell_0_3;
        squareContents[4][2] = player2Cell_1_0;
        squareContents[5][2] = player2Cell_1_1;
        squareContents[6][2] = player2Cell_1_2;
        squareContents[7][2] = player2Cell_1_3;
        squareContents[8][2] = player2Cell_2_0;
        squareContents[9][2] = player2Cell_2_1;
        squareContents[10][2] = player2Cell_2_2;
        squareContents[11][2] = player2Cell_2_3;
        squareContents[0][3] = player3Cell_0_0;
        squareContents[1][3] = player3Cell_0_1;
        squareContents[2][3] = player3Cell_0_2;
        squareContents[3][3] = player3Cell_0_3;
        squareContents[4][3] = player3Cell_1_0;
        squareContents[5][3] = player3Cell_1_1;
        squareContents[6][3] = player3Cell_1_2;
        squareContents[7][3] = player3Cell_1_3;
        squareContents[8][3] = player3Cell_2_0;
        squareContents[9][3] = player3Cell_2_1;
        squareContents[10][3] = player3Cell_2_2;
        squareContents[11][3] = player3Cell_2_3;
        squareContents[0][4] = player4Cell_0_0;
        squareContents[1][4] = player4Cell_0_1;
        squareContents[2][4] = player4Cell_0_2;
        squareContents[3][4] = player4Cell_0_3;
        squareContents[4][4] = player4Cell_1_0;
        squareContents[5][4] = player4Cell_1_1;
        squareContents[6][4] = player4Cell_1_2;
        squareContents[7][4] = player4Cell_1_3;
        squareContents[8][4] = player4Cell_2_0;
        squareContents[9][4] = player4Cell_2_1;
        squareContents[10][4] = player4Cell_2_2;
        squareContents[11][4] = player4Cell_2_3;
        squareContents[0][5] = ammoCell_0_0;
        squareContents[1][5] = ammoCell_0_1;
        squareContents[2][5] = ammoCell_0_2;
        squareContents[3][5] = ammoCell_0_3;
        squareContents[4][5] = ammoCell_1_0;
        squareContents[5][5] = ammoCell_1_1;
        squareContents[6][5] = ammoCell_1_2;
        squareContents[7][5] = ammoCell_1_3;
        squareContents[8][5] = ammoCell_2_0;
        squareContents[9][5] = ammoCell_2_1;
        squareContents[10][5] = ammoCell_2_2;
        squareContents[11][5] = ammoCell_2_3;

        yourWeapon0.setOnMouseClicked( yourWeap0 -> {
            if (lastType != ButtonType.WEAPON) {
                toSend = new ArrayList<Integer>();
            }
            lastType = ButtonType.WEAPON;
            toSend.add(0);
        });
        yourWeapon1.setOnMouseClicked(  yourWeap1 -> {
            if (lastType != ButtonType.WEAPON) {
                toSend = new ArrayList<Integer>();
            }
            lastType = ButtonType.WEAPON;
            toSend.add(1);
        });
        yourWeapon2.setOnMouseClicked( yourWaep2 -> {
            if (lastType != ButtonType.WEAPON) {
                toSend = new ArrayList<Integer>();
            }
            lastType = ButtonType.WEAPON;
            toSend.add(2);
        });

        yourPowerUp0.setOnMouseClicked( Pup0 -> {
            if (lastType != ButtonType.POWERUP) {
                toSend = new ArrayList<Integer>();
            }
            lastType =  ButtonType.POWERUP;
            toSend.add(0);
        });
        yourPowerUp1.setOnMouseClicked( Pup1 -> {
            if (lastType != ButtonType.POWERUP) {
                toSend = new ArrayList<Integer>();
            }
            lastType =  ButtonType.POWERUP;
            toSend.add(1);
        });
        yourPowerUp2.setOnMouseClicked( Pup2 -> {
            if (lastType != ButtonType.POWERUP) {
                toSend = new ArrayList<Integer>();
            }
            lastType =  ButtonType.POWERUP;
            toSend.add(2);
        });
        yourPowerUp3.setOnMouseClicked( Pup3 -> {
            if (lastType != ButtonType.POWERUP) {
                toSend = new ArrayList<Integer>();
            }
            lastType =  ButtonType.POWERUP;
            toSend.add(3);
        });

        yourWeapon0Fire0.setOnAction( weap0Fire0 -> {
            if (lastType != ButtonType.FIREMODE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIREMODE;
                toSend.add(0, 0);
                toSend.add(0);
            } else if (!toSend.isEmpty() && toSend.get(0) != 0) {
                toSend = new ArrayList<Integer>();
                toSend.add(0, 0);
                toSend.add(0);
            } else {
                toSend.add(0);
            }
        });
        yourWeapon0Fire1.setOnAction( weap0Fire1 -> {
            if (lastType != ButtonType.FIREMODE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIREMODE;
                toSend.add(0, 0);
                toSend.add(1);
            } else if (!toSend.isEmpty() && toSend.get(0) != 0) {
                toSend = new ArrayList<Integer>();
                toSend.add(0, 0);
                toSend.add(1);
            } else {
                toSend.add(1);
            }
        });
        yourWeapon0Fire2.setOnAction( weap0Fire2 -> {
            if (lastType != ButtonType.FIREMODE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIREMODE;
                toSend.add(0, 0);
                toSend.add(2);
            } else if (!toSend.isEmpty() && toSend.get(0) != 0) {
                toSend = new ArrayList<Integer>();
                toSend.add(0, 0);
                toSend.add(2);
            } else {
                toSend.add(2);
            }
        });
        yourWeapon1Fire0.setOnAction( weap1Fire0 -> {
            if (lastType != ButtonType.FIREMODE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIREMODE;
                toSend.add(0, 1);
                toSend.add(0);
            } else if (!toSend.isEmpty() && toSend.get(0) != 1) {
                toSend = new ArrayList<Integer>();
                toSend.add(0, 1);
                toSend.add(0);
            } else {
                toSend.add(0);
            }
        });
        yourWeapon1Fire1.setOnAction( weap1Fire1 -> {
            if (lastType != ButtonType.FIREMODE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIREMODE;
                toSend.add(0, 1);
                toSend.add(1);
            } else if (!toSend.isEmpty() && toSend.get(0) != 1) {
                toSend = new ArrayList<Integer>();
                toSend.add(0, 1);
                toSend.add(1);
            } else {
                toSend.add(1);
            }
        });
        yourWeapon1Fire2.setOnAction( weap1Fire2 -> {
            if (lastType != ButtonType.FIREMODE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIREMODE;
                toSend.add(0, 1);
                toSend.add(2);
            } else if (!toSend.isEmpty() && toSend.get(0) != 1) {
                toSend = new ArrayList<Integer>();
                toSend.add(0, 1);
                toSend.add(2);
            } else {
                toSend.add(2);
            }
        });
        yourWeapon2Fire0.setOnAction( weap2Fire0 -> {
            if (lastType != ButtonType.FIREMODE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIREMODE;
                toSend.add(0, 2);
                toSend.add(0);
            } else if (!toSend.isEmpty() && toSend.get(0) != 2) {
                toSend = new ArrayList<Integer>();
                toSend.add(0, 2);
                toSend.add(0);
            } else {
                toSend.add(0);
            }
        });
        yourWeapon2Fire1.setOnAction( weap2Fire1 -> {
            if (lastType != ButtonType.FIREMODE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIREMODE;
                toSend.add(0, 2);
                toSend.add(1);
            } else if (!toSend.isEmpty() && toSend.get(0) != 2) {
                toSend = new ArrayList<Integer>();
                toSend.add(0, 2);
                toSend.add(1);
            } else {
                toSend.add(1);
            }
        });
        yourWeapon2Fire2.setOnAction( weap1Fire2 -> {
            if (lastType != ButtonType.FIREMODE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIREMODE;
                toSend.add(0, 2);
                toSend.add(2);
            } else if (!toSend.isEmpty() && toSend.get(0) != 2) {
                toSend = new ArrayList<Integer>();
                toSend.add(0, 2);
                toSend.add(2);
            } else {
                toSend.add(2);
            }
        });
        yourAction0.setOnMouseClicked( action0 -> {
            if (lastType != ButtonType.ACTION) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.ACTION;
            }
            toSend.add(0);
        });
        yourAction1.setOnMouseClicked( action1 -> {
            if (lastType != ButtonType.ACTION) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.ACTION;
            }
            toSend.add(1);
        });
        yourAction2.setOnMouseClicked( action2 -> {
            if (lastType != ButtonType.ACTION) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.ACTION;
            }
            toSend.add(2);
        });
        yourRedAmmo.setOnMouseClicked( red -> {
            if (lastType != ButtonType.COLOR) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.COLOR;
            }
            toSend.add(0);
        });
        yourYellowAmmo.setOnMouseClicked( yellow -> {
            if (lastType != ButtonType.COLOR) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.COLOR;
            }
            toSend.add(1);
        });
        yourBlueAmmo.setOnMouseClicked( blue -> {
            if (lastType != ButtonType.COLOR) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.COLOR;
            }
            toSend.add(2);
        });

        player0Portrait.setOnMouseClicked( player0 -> {
           if (lastType != ButtonType.FIGURE) {
               toSend = new ArrayList<Integer>();
               lastType = ButtonType.FIGURE;
           }
           toSend.add(getAbsoluteIndex(0, Scenes.getClient().getModel().getPlayerID()));
        });
        player1Portrait.setOnMouseClicked( player1 -> {
            if (lastType != ButtonType.FIGURE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIGURE;
            }
            toSend.add(getAbsoluteIndex(1, Scenes.getClient().getModel().getPlayerID()));
        });
        player2Portrait.setOnMouseClicked( player2 -> {
            if (lastType != ButtonType.FIGURE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIGURE;
            }
            toSend.add(getAbsoluteIndex(2, Scenes.getClient().getModel().getPlayerID()));
        });
        player3Portrait.setOnMouseClicked( player2 -> {
            if (lastType != ButtonType.FIGURE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.FIGURE;
            }
            toSend.add(getAbsoluteIndex(3, Scenes.getClient().getModel().getPlayerID()));
        });

        cell_0_0.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(0);
        });
        cell_0_1.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(1);
        });
        cell_0_2.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(2);
        });
        cell_0_3.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(3);
        });
        cell_1_0.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(4);
        });
        cell_1_1.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(5);
        });
        cell_1_2.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(6);
        });
        cell_1_3.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(7);
        });
        cell_2_0.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(8);
        });
        cell_2_1.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(9);
        });
        cell_2_2.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(10);
        });
        cell_2_3.setOnAction( cell -> {
            if (lastType != ButtonType.SQUARE) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.SQUARE;
            }
            toSend.add(11);
        });

        room0.setOnMouseClicked( room0 -> {
            if (lastType != ButtonType.ROOM) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.ROOM;
            }
            toSend.add(0);
        });
        room1.setOnMouseClicked( room1 -> {
            if (lastType != ButtonType.ROOM) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.ROOM;
            }
            toSend.add(1);
        });
        room2.setOnMouseClicked( room2 -> {
            if (lastType != ButtonType.ROOM) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.ROOM;
            }
            toSend.add(2);
        });
        room3.setOnMouseClicked( room3 -> {
            if (lastType != ButtonType.ROOM) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.ROOM;
            }
            toSend.add(3);
        });
        room4.setOnMouseClicked( room4 -> {
            if (lastType != ButtonType.ROOM) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.ROOM;
            }
            toSend.add(4);
        });
        room5.setOnMouseClicked( room5 -> {
            if (lastType != ButtonType.ROOM) {
                toSend = new ArrayList<Integer>();
                lastType = ButtonType.ROOM;
            }
            toSend.add(5);
        });
        yourEndTurn.setOnMouseClicked( end -> {
            lastType = ButtonType.END;
        });
        cancelButton.setOnMouseClicked( delete -> {
            toSend = new ArrayList<>();
        });
        confirmButton.setOnMouseClicked( send -> {
            GameState currState = Scenes.getClient().getModel().getState();
            if (currState == GameState.FIRE_MODE) {
                if(lastType == ButtonType.WEAPON){
                    Scenes.getClient().selectFireMode(toSend.remove(0), toSend.stream().mapToInt(Integer::intValue).toArray());
                }
            } else if (currState == GameState.FIRE) {
                if(lastType == ButtonType.ROOM) {
                    Scenes.getClient().selectTargettable(toSend.stream().map( x -> x * 3).mapToInt(Integer::intValue).toArray());
                } else if (lastType == ButtonType.SQUARE) {
                    Scenes.getClient().selectTargettable(toSend.stream().map( x -> x * 3 + 1).mapToInt(Integer::intValue).toArray());
                } else if (lastType == ButtonType.FIGURE) {
                    Scenes.getClient().selectTargettable(toSend.stream().map( x -> x * 3 + 2).mapToInt(Integer::intValue).toArray());
                }
            } else if (currState == GameState.GRAB) {
                //TODO
            } else if(currState == GameState.SELECT_RELOAD) {
                if(lastType == ButtonType.WEAPON){
                    Scenes.getClient().selectWeapon(toSend.stream().mapToInt(Integer::intValue).toArray());
                }
            } else if (currState == GameState.PAY_ANY) {
                if(lastType == ButtonType.COLOR) {
                    Scenes.getClient().selectColor(toSend.get(toSend.size() - 1));
                }
            } else if (currState == GameState.PAY) {
                if (lastType == ButtonType.POWERUP) {
                    Scenes.getClient().selectPowerUp(toSend.stream().mapToInt(Integer::intValue).toArray());
                }
            } else if (currState == GameState.RELOAD) {
                if (lastType == ButtonType.WEAPON) {
                    Scenes.getClient().selectWeapon(toSend.stream().mapToInt(Integer::intValue).toArray());
                }
            } else if (currState == GameState.SCOPE) {
                if (lastType == ButtonType.WEAPON) {
                    Scenes.getClient().selectPowerUp(toSend.stream().mapToInt(Integer::intValue).toArray());
                }
            } else if (currState == GameState.SELECT_GRAB) {
                //TODO
            } else if (currState == GameState.SELECT_RELOAD) {
                if (lastType == ButtonType.WEAPON) {
                    Scenes.getClient().selectWeapon(toSend.stream().mapToInt(Integer::intValue).toArray());
                }
            } else if (currState == GameState.SPAWN) {

            } else if (currState == GameState.TAGBACK) {
                if (lastType == ButtonType.POWERUP) {
                    Scenes.getClient().selectPowerUp(toSend.stream().mapToInt(Integer::intValue).toArray());
                }
            } else if (currState == GameState.TURN) {
                if (lastType == ButtonType.ACTION) {
                    Scenes.getClient().selectAction(toSend.get(toSend.size() - 1));
                } else if (lastType == ButtonType.POWERUP) {
                    Scenes.getClient().selectPowerUp(toSend.stream().mapToInt(Integer::intValue).toArray());
                } else if (lastType == ButtonType.END) {
                    Scenes.getClient().endTurn();
                }
            }
        });
        //returnButton.setOnMouseClicked( back -> Scenes.getClient().close());

    }


    public void displayMessage(String message) {System.out.println(message);}

    public void displayPossibleRoom(int id, Square[] squares) {}

    public void displayPossibleFigure(int id, Player player) {}

    public void displayPossibleSquare(int id, Square square) {}

    public void displayMinToSelect(int min) {System.out.println(min);}

    public void displayMaxToSelect(int max) {System.out.println(max);}

    public void displayPowerUps(PowerUp[] powerUps) {
        Platform.runLater( () -> {
                    int i = 0;
                    while(i <= powerUps.length){
                        pupAggr[i] = loader.getPowerUpImages(powerUps[i].getType(), powerUps[i].getColor());
                        yourPupImages[i].setImage(pupAggr[i].getPortrait());
                        i++;
                    }
                    while(i < 4) {
                        pupAggr[i] = loader.getPowerUpImages();
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
                actionImages[4][2].setImage(null);
            } else {
                for(int i = 0; i < 3; i++) {
                    actionImages[4][i].setImage(null);
                }
            }
        });
    }

    public void displayCurrentPlayer(int currPlayer) {};

    public void displayRemainingActions(int remaining) {};

    public void displayMapType(int mapID) {
        Platform.runLater( () -> mapImage.setImage(loader.getMap(mapID)));
        if(mapID == 3) {
            room0.setImage(loader.getRoomImage(0));
            room1.setImage(loader.getRoomImage(5));
            room2.setImage(loader.getRoomImage(4));
            room3.setImage(loader.getRoomImage(2));
        } else if (mapID == 2) {
            room0.setImage(loader.getRoomImage(0));
            room1.setImage(loader.getRoomImage(1));
            room2.setImage(loader.getRoomImage(5));
            room3.setImage(loader.getRoomImage(4));
            room4.setImage(loader.getRoomImage(2));
        }
        else if(mapID == 3) {
            room0.setImage(loader.getRoomImage(5));
            room1.setImage( loader.getRoomImage(0));
            room2.setImage(loader.getRoomImage(1));
            room3.setImage(loader.getRoomImage(3));
            room4.setImage(loader.getRoomImage(4));
            room5.setImage(loader.getRoomImage(2));
        }
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
        int i = 0;
        while(i < player.getDamages().length) {
            damageImages[relativeID][i].setImage(loader.getDamage(getRelativeID(player.getDamages()[i] ,myID),1));
            i++;
        }
        while(i < 11) {
            damageImages[relativeID][i].setImage(null);
        }

        if(playerID == myID && player.getDamages().length > 2) {
            actionImages[4][1].setImage(loader.getAction(3));
        }
        if(playerID == myID && player.getDamages().length > 5) {
            actionImages[4][2].setImage(loader.getAction(4));
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
        Platform.runLater( () -> {
            int playerID = Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(player);
            int myID = Scenes.getClient().getModel().getPlayerID();
            int relativeID = getRelativeID(playerID, myID);
            for (int i = 0; i < 3; i++) {
                ammoCubeImages[relativeID][i].setImage(loader.getAmmoCubesImages(player.getAmmo()).getRybImages()[i]);
            }
        });
    }

    public void displayPlayerNPowerUps(Player player) {
        int playerID = Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(player);
        int myID = Scenes.getClient().getModel().getPlayerID();
        int relativeID = getRelativeID(playerID, myID);
        pupNumber[relativeID].setText(Integer.toString(player.getnPowerup()));
    }

    public void displayPlayerLocation(Player player) {
        Platform.runLater( () -> {
            int playerID = Arrays.asList(Scenes.getClient().getModel().getBoard().getPlayers()).indexOf(player);
            int squareN = player.getCoordinates()[0]*4 + player.getCoordinates()[1];
            squareContents[prevCord[playerID]][playerID].setImage(null);
            prevCord[playerID] = squareN;
            squareContents[squareN][playerID].setImage(loader.getPlaceholder(playerID));
        });
    }

    public void displaySquareContent(Square square) {
        Platform.runLater( () -> {
            if (square.isSpawn()) {
                // TODO: 7/4/2019 recognise spawn
            } else {
                squareContents[square.getCoordinates()[0] * 4 + square.getCoordinates()[1]][5].setImage(loader.getAmmotile(square.getTileId()));
            }
        });
    }

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
