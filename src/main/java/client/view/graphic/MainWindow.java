package client.view.graphic;

import client.view.View;
import client.view.graphic.loaders.Scenes;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainWindow extends Application implements ConfirmHandler {
    private Stage mainWindow;
    private View currController;

    @Override
    public void start(Stage stage) throws IOException {
        //set window proprieties
        this.mainWindow = stage;
        stage.setTitle("Adrenaline");
        stage.setResizable(false);
        //stage.initStyle(StageStyle.UNDECORATED);

        //initialize scenes
        Scenes.initScenes();
        //enter login screen
        stage.setScene(Scenes.getLoginScreen());

        //close request
        stage.setOnCloseRequest(event -> {
            event.consume();
            ConfirmBox closeBox = new ConfirmBox("Close", "Do you want to quit?", "Confirm", "Cancel", this);
        });


        stage.show();

    }

    public void confirmHandle(boolean value) {
        if(value) {
            mainWindow.close();
        }
    }

    //public View getCurrController () {};
}
