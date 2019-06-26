package client.view.graphic.loader;

import client.ConnectionType;
import client.view.graphic.sceneController.Confirm;
import client.view.graphic.sceneController.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;

import java.io.IOException;

public class Scenes {

    //Main scenes
    private static Scene loginScreen;
    private static Scene settingsScreen;
    private static Scene lobbyScreen;

    //Confirm scene
    private static Scene confirmScreen;
    private static Confirm confirmController;

    //Settings
    private static int windowHeight = 480;
    private static int windowWidth = 640;
    private static boolean fullscreen = false;
    private static ConnectionType connectionChoice = ConnectionType.SOCKET;
    private static String serverIP = "localhost";
    private static int serverPort = 1099;

    public static void initScenes() throws IOException {

        FXMLLoader loader = new FXMLLoader();

        Parent loginRoot = loader.load(Scenes.class.getResource("../../../client/view/fxml/login.fxml"));
        loginScreen = new Scene(loginRoot, windowWidth, windowHeight);
        Parent settingsRoot = loader.load(Scenes.class.getResource("../../../client/view/fxml/settings.fxml"));
        settingsScreen = new Scene(settingsRoot, windowWidth, windowHeight);
        Parent lobbyRoot = loader.load(Scenes.class.getResource("../../../client/view/fxml/lobbyChoice.fxml"));
        lobbyScreen = new Scene(lobbyRoot, windowWidth, windowHeight);

        FXMLLoader confirmLoader = new FXMLLoader(Scenes.class.getResource("../../../client/view/fxml/confirm.fxml"));
        Parent confirmRoot = confirmLoader.load();
        confirmController = confirmLoader.getController();
        confirmScreen = new Scene(confirmRoot, 300, 150);
    }

    public static Scene getLoginScreen() {
        return loginScreen;
    }

    public static Scene getSettingsScreen() { return settingsScreen; }

    public static Scene getLobbyScreen() { return lobbyScreen;  }

    public static Scene getConfirmScreen() { return confirmScreen; }

    public static Confirm getConfirmController() { return confirmController; }

    public static Font getRegularFont(double size) {
        return Font.loadFont(Scenes.class.getResourceAsStream("../../../client/view/fonts/ethnocentric/ethnocentric rg.tff"), size);
    }

    public static Font getItalicFont(double size) {
        return Font.loadFont(Scenes.class.getResourceAsStream("../../../client/view/fonts/ethnocentric/ethnocentric rg it.ttf"), size);
    }


    //TODO
    public static void saveSettings (Settings settingScene) {  }

    public static int getWindowWidth () { return windowWidth; }

    public static int getWindowHeight () { return windowHeight; }

    public static boolean getFullscreen () { return fullscreen; }

    public static String getServerIP () { return  serverIP; }

    public static int getServerPort () { return serverPort; }

    public static ConnectionType getConnectionChoice () { return connectionChoice; }

}
