package client.view.graphic.controllers;

import client.model.GameState;
import client.view.View;
import client.view.graphic.loaders.Scenes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Lobby implements View {

    @FXML Label title;
    @FXML Text countdown;
    @FXML ImageView backButton;

    @FXML public void initialize() {
        title.setFont(Scenes.getItalicFont(50));
        countdown.setFont(Scenes.getItalicFont(45));

        //TODO salvare nome lobby
        backButton.setOnMouseClicked(quitFromLobby -> Scenes.getClient().quitLobby());
    }

    public void displayRemainingTime(int v) {
        countdown.setText(Integer.toString(v));
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }


    public void displayGameState(GameState currState) {
        Platform.runLater(() -> {
            if (currState == GameState.CHOOSING_LOBBY) {
                ((Stage) backButton.getScene().getWindow()).setScene(Scenes.getLobbyScreen());
            } else if (currState == GameState.ENEMY_TURN) {
                ((Stage) countdown.getScene().getWindow()).setScene(Scenes.getLobbyScreen());
            }
        });
    }
}
