package server.controller;

import server.connection.VirtualClient;
import server.model.PowerUp;
import server.model.board.Figure;

public class Player {
    private String name;
    private VirtualClient client;
    private Figure figure;
    private GameController game;
    private boolean active = true;
    private boolean online = true;

    public Player(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public VirtualClient getClient() {
        return client;
    }

    public void setClient(VirtualClient client) {
        this.client = client;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive() {
        active = true;
    }

    public void setInactive() {
        active = false;
    }

    public boolean isOnline() {
        // TODO ping client
        return online;
    }

    public void setOnline() {
        online = true;
    }

    public void setOffline() {
        online = false;
    }

    public GameController getGame() {
        return game;
    }

    public void setGame(GameController game) {
        this.game = game;
    }

    public void selectPowerUp(int[] index) {
        PowerUp[] powerUps = figure.getPowerUps().toArray(PowerUp[]::new);
        game.enqueue(new SelectPowerUpEvent(this, powerUps));
    }

    public void selectWeapon(int[] index) {
        game.enqueue(null);
    }

    public void selectGrabbable(int[] index) {
        game.enqueue(null);
    }

    public void selectTargettable(int[] index) {

        game.enqueue(null);
    }

    public void selectColor(int index) {
        game.enqueue(null);
    }

    public void selectAction(int index) {
        game.enqueue(null);
    }
}
