package client.view.graphic.sceneController;

import client.ConnectionType;
import client.view.graphic.loader.Scenes;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Settings {

    //TOP-RIGHT
    @FXML private ImageView quitButton;
    @FXML private ImageView backButton;
    //TITLE
    //empty

    @FXML private Label connectionChoiceLabel;
    @FXML private ChoiceBox<ConnectionType> connectionChoice;
    @FXML private Label serverIPLabel;
    @FXML private TextField serverIP;
    @FXML private Label serverPortLabel;
    @FXML private TextField serverPort;
    @FXML private CheckBox fullscreen;
    @FXML private Button save;

    @FXML void initialize() {
        //sets fonts
        connectionChoiceLabel.setFont(Scenes.getItalicFont(13));
        serverIPLabel.setFont(Scenes.getItalicFont(13));
        serverIP.setFont(Scenes.getItalicFont(13));
        serverPortLabel.setFont(Scenes.getItalicFont(13));
        serverPort.setFont(Scenes.getItalicFont(13));
        fullscreen.setFont(Scenes.getItalicFont(13));
        save.setFont(Scenes.getItalicFont(13));

        //sets top-right buttons events
        quitButton.setOnMouseClicked(event -> quitButton.getScene().getWindow().fireEvent(
                new WindowEvent(
                        quitButton.getScene().getWindow(),
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        ));
        backButton.setOnMouseClicked(event -> ((Stage) backButton.getScene().getWindow()).setScene(Scenes.getLoginScreen()));

        //sets connection choice options
        connectionChoice.setTooltip(new Tooltip("Select preferred connection method"));
        connectionChoice.setItems(FXCollections.observableArrayList(ConnectionType.SOCKET, ConnectionType.RMI));

        //sets prompt
        serverIP.setPromptText("IP Address");
        serverPort.setPromptText("Port Number");

        //sets defaults
        connectionChoice.setValue(Scenes.getConnectionChoice());
        serverIP.setText(Scenes.getServerIP());
        serverPort.setText(Integer.toString(Scenes.getServerPort()));
        fullscreen.setSelected(Scenes.getFullscreen());

        //sets events
        save.setOnAction(event -> Scenes.saveSettings(this));

    }

    public ConnectionType getConnectionChoice() { return connectionChoice.getValue(); }
    public String getServerIP() { return serverIP.getText(); }
    public int getServerPort() { return  Integer.parseInt(serverPort.getText()); }
    public boolean getFullscreen() { return fullscreen.isSelected(); }
}