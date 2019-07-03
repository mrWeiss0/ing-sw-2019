package client.view.graphic.controllers;

import client.view.View;
import client.view.graphic.loaders.Scenes;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class LobbyChoice implements View {

    //TOP-RIGHT
    @FXML private ImageView quitButton;
    //TITLE
    @FXML private Label title;
    //BUTTONS
    @FXML private TextField newLobbyName;
    @FXML private Button createButton;
    @FXML private Button joinButton;

    @FXML private ListView<String> lobbies;

    @FXML void initialize() {
        //sets fonts
        title.setFont(Scenes.getItalicFont(50));
        newLobbyName.setFont(Scenes.getItalicFont(13));
        createButton.setFont(Scenes.getItalicFont(13));
        joinButton.setFont(Scenes.getItalicFont(13));

        //sets top-right buttons events
        quitButton.setOnMouseClicked(event -> quitButton.getScene().getWindow().fireEvent(
                new WindowEvent(
                        quitButton.getScene().getWindow(),
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        ));

        //sets available lobbies
        lobbies.setItems(FXCollections.observableArrayList());

        //sets actions
        createButton.setOnAction(create -> Scenes.getClient().createLobby(newLobbyName.getText()));
        joinButton.setOnAction(join -> Scenes.getClient().joinLobby( (lobbies.getSelectionModel().getSelectedItem()).split("\\s+")[0] ));
    }

    public void displayLobbyList(String[] lobbyList) {
        Platform.runLater(()-> lobbies.setItems(FXCollections.observableArrayList(lobbyList)));
    }

    public void displayGameState(GameState currState) {
        Platform.runLater(() -> {
            if (currState == GameState.NOT_STARTED)
            ((Stage) joinButton.getScene().getWindow()).setScene(Scenes.getLobbyScreen());
        });
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }
}
