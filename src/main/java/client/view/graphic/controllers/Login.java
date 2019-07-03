package client.view.graphic.controllers;

import client.view.View;
import client.view.graphic.loaders.Scenes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Login implements View {

    //TOP-RIGHT
    @FXML private ImageView quitButton;
    @FXML private ImageView settingsButton;

    //CENTER
    @FXML private Button connectButton;
    @FXML private Label usernameLabel;
    @FXML private TextField username;
    @FXML private Button loginButton;
    @FXML private Label failedLogin;

    @FXML public void initialize() {
        //set fonts
        connectButton.setFont(Scenes.getItalicFont(30));
        usernameLabel.setFont(Scenes.getItalicFont(20));
        loginButton.setFont(Scenes.getItalicFont(13));
        username.setFont(Scenes.getItalicFont(13));
        failedLogin.setFont(Scenes.getItalicFont(10));

        //sets top-right buttons events
        quitButton.setOnMouseClicked(quitClicked -> quitButton.getScene().getWindow().fireEvent(
                new WindowEvent(
                        quitButton.getScene().getWindow(),
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        ));
        settingsButton.setOnMouseClicked(settingsClicked -> ((Stage) settingsButton.getScene().getWindow()).setScene(Scenes.getSettingsScreen()));

        //sets actions
        //TODO sends login message
        connectButton.setOnAction(connectButton -> Scenes.getClient().connect(Scenes.getServerIP()));
        loginButton.setOnAction(loginPressed -> {
                Scenes.getClient().login(username.getText());
                });
        //loginButton.setOnAction(event -> ((Stage) loginButton.getScene().getWindow()).setScene(Scenes.getLobbyChoiceScreen()));
    }

    public void displayMessage(String message) {
        Platform.runLater(() -> {
            if ("Connected".equals(message)) {
                handleConnectionSuccess();
            }
            else if(message.startsWith("Logged in as")) {
                handleLoginSuccess();
            } else {
                handleLoginFailure(message);
            }
        });
    }

    public void handleConnectionSuccess() {
        ((StackPane)connectButton.getParent()).getChildren().remove(connectButton);
    }

    public void handleLoginFailure(String message) {
        failedLogin.setText(message);
        failedLogin.setVisible(true);
    }
    public void handleLoginSuccess() { ((Stage) loginButton.getScene().getWindow()).setScene(Scenes.getLobbyChoiceScreen()); }
}
