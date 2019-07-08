package client.view.graphic.loaders;

import client.Client;
import client.ConnectionType;
import client.view.View;
import client.view.graphic.controllers.Confirm;
import client.view.graphic.controllers.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;

import java.io.IOException;

public class Scenes {

    //Main scenes
    private static Scene loginScreen;
    private static View loginController;
    private static Scene settingsScreen;
    private static View settingsController;
    private static Scene lobbyChoiceScreen;
    private static View lobbyChoiceController;
    private static Scene lobbyScreen;
    private static View lobbyController;
    private static Scene playScreen;
    private static View playController;

    //Client
    private static Client client;

    //Confirm scene
    private static Scene confirmScreen;
    private static Confirm confirmController;

    //Settings
    private static int windowHeight = 480;
    private static int windowWidth = 640;
    private static int playWindowHeight = 768;
    private static int playWindowWidth = 1024;
    private static ConnectionType connectionChoice = ConnectionType.SOCKET;
    private static String serverIP = "localhost";
    private static int serverPort = 1099;

    public static void setClient(Client client) {
        Scenes.client = client;
        System.out.println("client set");
    }
    public static Client getClient() {return client;}

    public static void initScenes() throws IOException {

        FXMLLoader loginLoader = new FXMLLoader(Scenes.class.getResource("../../../../client/view/fxml/login.fxml"));
        Parent loginRoot = loginLoader.load();
        loginScreen = new Scene(loginRoot, windowWidth, windowHeight);
        loginController = loginLoader.getController();
        loginScreen.setUserData(loginController);

        FXMLLoader settingsLoader = new FXMLLoader(Scenes.class.getResource("../../../../client/view/fxml/settings.fxml"));
        Parent settingsRoot = settingsLoader.load();
        settingsScreen = new Scene(settingsRoot, windowWidth, windowHeight);
        settingsController = loginLoader.getController();
        loginScreen.setUserData(settingsController);

        FXMLLoader lobbyChoiceLoader = new FXMLLoader(Scenes.class.getResource("../../../../client/view/fxml/lobbyChoice.fxml"));
        Parent lobbyChoiceRoot = lobbyChoiceLoader.load();
        lobbyChoiceScreen = new Scene(lobbyChoiceRoot, windowWidth, windowHeight);
        lobbyChoiceController = lobbyChoiceLoader.getController();
        lobbyChoiceScreen.setUserData(lobbyChoiceController);

        FXMLLoader lobbyLoader = new FXMLLoader(Scenes.class.getResource("../../../../client/view/fxml/lobby.fxml"));
        Parent lobbyRoot = lobbyLoader.load();
        lobbyScreen = new Scene(lobbyRoot, windowWidth, windowHeight);
        lobbyController = lobbyLoader.getController();
        lobbyScreen.setUserData(lobbyController);

        FXMLLoader playLoader = new FXMLLoader(Scenes.class.getResource("../../../../client/view/fxml/play.fxml"));
        Parent playRoot = playLoader.load();
        playScreen = new Scene(playRoot, playWindowWidth, playWindowHeight);
        playController = playLoader.getController();
        playScreen.setUserData(playController);

        FXMLLoader confirmLoader = new FXMLLoader(Scenes.class.getResource("../../../../client/view/fxml/confirm.fxml"));
        Parent confirmRoot = confirmLoader.load();
        confirmController = confirmLoader.getController();
        confirmScreen = new Scene(confirmRoot, 300, 150);

    }

    public static Scene getLoginScreen() {
        return loginScreen;
    }

    public static Scene getSettingsScreen() { return settingsScreen; }

    public static Scene getLobbyChoiceScreen() { return lobbyChoiceScreen;  }

    public static Scene getLobbyScreen() {  return lobbyScreen; }

    public static Scene getPlayScreen() { return playScreen; }

    public static Scene getConfirmScreen() { return confirmScreen; }

    public static Confirm getConfirmController() { return confirmController; }

    public static Font getRegularFont(double size) {
        return Font.loadFont(Scenes.class.getResourceAsStream("../../../../client/view/fonts/ethnocentric/ethnocentric rg.tff"), size);
    }

    public static Font getItalicFont(double size) {
        return Font.loadFont(Scenes.class.getResourceAsStream("../../../../client/view/fonts/ethnocentric/ethnocentric rg it.ttf"), size);
    }

    //TODO
    public static void saveSettings (ConnectionType connectionChoice, String serverIP, int serverPort) {
        Scenes.connectionChoice = connectionChoice;
        Scenes.serverIP = serverIP;
        Scenes.serverPort = serverPort;
    }

    public static ConnectionType getConnectionChoice () { return connectionChoice; }

    public static String getServerIP () { return  serverIP; }

    public static int getServerPort () { return serverPort; }

}
