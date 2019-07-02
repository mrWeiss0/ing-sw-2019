package client.view.graphic.controllers;

import client.view.View;
import client.view.graphic.loaders.Scenes;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class LobbyChoice implements View {

    //TOP-RIGHT
    @FXML private ImageView quitButton;
    @FXML private ImageView backButton;
    //TITLE
    @FXML private Label title;

    @FXML private Button createButton;
    @FXML private Button joinButton;

    @FXML private ListView<String> lobbies;

    @FXML void initialize() {
        //sets fonts
        title.setFont(Scenes.getItalicFont(50));
        createButton.setFont(Scenes.getItalicFont(13));
        joinButton.setFont(Scenes.getItalicFont(13));

        //sets top-right buttons events
        quitButton.setOnMouseClicked(event -> quitButton.getScene().getWindow().fireEvent(
                new WindowEvent(
                        quitButton.getScene().getWindow(),
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        ));
        backButton.setOnMouseClicked(event -> ((Stage) backButton.getScene().getWindow()).setScene(Scenes.getLoginScreen()));

        //sets available lobbies
        lobbies.setItems(FXCollections.observableArrayList());

        //sets actions
        createButton.setOnAction(create -> lobbies.setItems(FXCollections.observableArrayList("Lobby 1")));
        joinButton.setOnAction(join -> ((Stage) backButton.getScene().getWindow()).setScene(Scenes.getPlayScreen()));
    }

    public void displayLobbies(List<String> lobbyList) {
        lobbies.setItems(FXCollections.observableArrayList("Lobby 1", "Lobby 2"));
    }

    public void start() {};

    public void print(String s) {};

    public void exit() {};
}
