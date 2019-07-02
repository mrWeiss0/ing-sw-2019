package client.view.graphic.controllers;

import client.view.View;
import client.view.graphic.ConfirmBox;
import client.view.graphic.loaders.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Confirm {
    @FXML private Label message;
    @FXML private Button left;
    @FXML private Button right;

    @FXML public void initialize() {
        message.setFont(Scenes.getItalicFont(20));
        left.setFont(Scenes.getItalicFont(13));
        right.setFont(Scenes.getItalicFont(13));
        System.out.println("init");
    }

    public void set(String message, String left, String right, ConfirmBox handler) {
        this.message.setText(message);
        this.left.setText(left);
        this.right.setText(right);
        this.left.setOnAction(e -> handler.leftClicked());
        this.right.setOnAction(e -> handler.rightClicked());
        System.out.println("set");
    }
}
