package client.view.graphic.controllers;

import client.view.View;
import client.view.graphic.loaders.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Login implements View {

    //TOP-RIGHT
    @FXML private ImageView quitButton;
    @FXML private ImageView settingsButton;
    //TITLE
    //empty

    @FXML private Label usernameLabel;
    @FXML private TextField username;
    @FXML private Button loginButton;
    @FXML private Label failedLogin;

    @FXML public void initialize() {
        //set fonts
        usernameLabel.setFont(Scenes.getItalicFont(20));
        loginButton.setFont(Scenes.getItalicFont(13));
        username.setFont(Scenes.getItalicFont(13));
        failedLogin.setFont(Scenes.getItalicFont(13));

        //sets top-right buttons events
        quitButton.setOnMouseClicked(event -> quitButton.getScene().getWindow().fireEvent(
                new WindowEvent(
                        quitButton.getScene().getWindow(),
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        ));
        settingsButton.setOnMouseClicked(event -> ((Stage) settingsButton.getScene().getWindow()).setScene(Scenes.getSettingsScreen()));

        //sets actions
        //TODO sends login message
        loginButton.setOnAction(event -> ((Stage) loginButton.getScene().getWindow()).setScene(Scenes.getLobbyChoiceScreen()));
    }

    @FXML public void handleLoginError() {
        failedLogin.setVisible(true);
    }

    @FXML public void handleLoginSuccess() { ((Stage) loginButton.getScene().getWindow()).setScene(Scenes.getLobbyChoiceScreen()); }

    public void start() {};

    public void print(String s) {};

    public void exit() {};
}
