package client.view.graphic;

import client.view.graphic.loaders.Scenes;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AdrenalineApp extends Application implements ConfirmHandler {

    private Stage mainWindow;
    private static GUIView view;
    public static void setView(GUIView view) {
        AdrenalineApp.view = view;}

    @Override
    public void start(Stage stage) throws IOException {

        //set window proprieties
        mainWindow = stage;
        view.setMainWindow(mainWindow);
        mainWindow.setTitle("Adrenaline");
        mainWindow.setResizable(true);
        //mainWindow.initStyle(StageStyle.UNDECORATED);

        //init Scenes
        Scenes.initScenes();
        //enter login screen
        mainWindow.setScene(Scenes.getLoginScreen());

        //close request
        mainWindow.setOnCloseRequest(event -> {
            event.consume();
            ConfirmBox closeBox = new ConfirmBox("Close", "Do you want to quit?", "Confirm", "Cancel", this);
        });

        mainWindow.show();
    }

    public void confirmHandle(boolean choice) {
        if(choice) {
            mainWindow.close();
        }
    }

}
