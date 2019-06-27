package client.view.graphic;

import client.view.graphic.loaders.Scenes;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmBox {
    private final String title;
    private final String message;
    private final String left;
    private final String right;
    private final ConfirmHandler caller;

    private final Stage confirmWindow;

    public ConfirmBox(String title, String message, String left, String right, ConfirmHandler caller) {
        this.title = title;
        this.message = message;
        this.left = left;
        this.right = right;
        this.caller = caller;


        this.confirmWindow = new Stage();

        //set window proprieties
        confirmWindow.setTitle(title);
        confirmWindow.setResizable(false);
        confirmWindow.initStyle(StageStyle.UNDECORATED);
        confirmWindow.initModality(Modality.APPLICATION_MODAL);

        confirmWindow.setScene(Scenes.getConfirmScreen());
        Scenes.getConfirmController().set(message, left, right, this);

        confirmWindow.show();
    }

    public void leftClicked() {
        confirmWindow.close();
        caller.confirmHandle(true);
    }

    public void rightClicked() {
        confirmWindow.close();
        caller.confirmHandle(false);
    }


}
