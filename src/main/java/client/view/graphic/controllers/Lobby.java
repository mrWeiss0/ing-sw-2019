package client.view.graphic.controllers;

import client.view.graphic.loaders.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class Lobby {

    @FXML Label title;
    @FXML ImageView player1;
    @FXML ImageView player2;
    @FXML ImageView player3;
    @FXML ImageView player4;
    @FXML ImageView player5;
    @FXML ImageView map;

    @FXML public void initialize() {
        title.setFont(Scenes.getItalicFont(50));
    }

}
