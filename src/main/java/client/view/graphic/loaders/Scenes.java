package client.view.graphic.loaders;

import client.ConnectionType;
import client.view.graphic.controllers.Confirm;
import client.view.graphic.controllers.MessageHandler;
import client.view.graphic.controllers.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;

import java.io.IOException;

public class Scenes {

    //Main scenes
    private static Scene loginScreen;
    private static MessageHandler loginController;
    private static Scene settingsScreen;
    private static MessageHandler settingsController;
    private static Scene lobbyChoiceScreen;
    private static MessageHandler lobbyController;
    private static Scene lobbyScreen;
    private static Scene playScreen;
    private static MessageHandler playController;

    //Confirm scene
    private static Scene confirmScreen;
    private static Confirm confirmController;

    //Settings
    private static int windowHeight = 480;
    private static int windowWidth = 640;
    private static int playWindowHeight = 768;
    private static int playWindowWidth = 1024;
    private static boolean fullscreen = false;
    private static ConnectionType connectionChoice = ConnectionType.SOCKET;
    private static String serverIP = "localhost";
    private static int serverPort = 1099;

    public static void initScenes() throws IOException {

        FXMLLoader loader = new FXMLLoader();

        Parent loginRoot = loader.load(Scenes.class.getResource("../../../../client/view/fxml/login.fxml"));
        loginScreen = new Scene(loginRoot, windowWidth, windowHeight);
        Parent settingsRoot = loader.load(Scenes.class.getResource("../../../../client/view/fxml/settings.fxml"));
        settingsScreen = new Scene(settingsRoot, windowWidth, windowHeight);
        Parent lobbyChoiceRoot = loader.load(Scenes.class.getResource("../../../../client/view/fxml/lobbyChoice.fxml"));
        lobbyChoiceScreen = new Scene(lobbyChoiceRoot, windowWidth, windowHeight);
        Parent playRoot = loader.load(Scenes.class.getResource("../../../../client/view/fxml/play.fxml"));
        playScreen = new Scene(playRoot, playWindowWidth, playWindowHeight);

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
    public static void saveSettings (Settings settingScene) {  }

    public static boolean getFullscreen () { return fullscreen; }

    public static String getServerIP () { return  serverIP; }

    public static int getServerPort () { return serverPort; }

    public static ConnectionType getConnectionChoice () { return connectionChoice; }

}
