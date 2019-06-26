package server.controller;

import server.connection.VirtualClient;
import server.model.PowerUp;
import server.model.board.AbstractSquare;
import server.model.board.Figure;
import server.model.board.Targettable;
import server.model.weapon.FireMode;
import server.model.weapon.Weapon;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Player {
    private static final String NOTSTARTEDMESSAGE = "The game is not started yet";
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
        try {
            game.enqueue(new SelectPowerUpEvent(
                    this,
                    (PowerUp[]) Arrays.stream(index)
                            .mapToObj(x -> figure.getPowerUps().get(x))
                            .distinct()
                            .toArray()
            ));
        } catch (NullPointerException e) {
            client.send(NOTSTARTEDMESSAGE);
        }
    }

    public void selectWeaponToReload(int[] index) {
        try {
            game.enqueue(new SelectWeaponToReloadEvent(
                    this,
                    (Weapon[]) Arrays.stream(index)
                            .mapToObj(x -> figure.getWeapons().get(x))
                            .distinct()
                            .toArray()
            ));
        } catch (NullPointerException e) {
            client.send(NOTSTARTEDMESSAGE);
        }
    }

    public void selectWeaponFireMode(int index, int[] fm) {
        try {
            game.enqueue(new SelectWeaponFireModeEvent(
                    this,
                    figure.getWeapons().get(index),
                    (FireMode[]) Arrays.stream(fm)
                            .mapToObj(x -> figure.getWeapons().get(index).getFireModes().get(x))
                            .distinct()
                            .toArray()
            ));
        } catch (NullPointerException e) {
            client.send(NOTSTARTEDMESSAGE);
        }
    }

    public void selectGrabbable(int index) {
        try {
            game.enqueue(new SelectGrabbableEvent(
                    this,
                    figure.getLocation().peek().get(index)
            ));
        } catch (NullPointerException e) {
            client.send(NOTSTARTEDMESSAGE);
        }
    }

    public void selectTargettable(int type, int[] index) {
        if(game==null){
            client.send(NOTSTARTEDMESSAGE);
            return;
        }
        List<Targettable> list = ((Supplier<List<Targettable>>) new Supplier[]{
                game.getGame().getBoard()::getSquares,
                game.getGame().getBoard()::getFigures,
                game.getGame().getBoard()::getRooms
        }[type]).get();

        game.enqueue(new SelectTargettableEvent(
                this,
                    (Targettable[]) Arrays.stream(index)
                            .mapToObj(list::get)
                            .distinct()
                            .toArray()
            ));
    }

    public void selectColor(int color) {
        try {
            game.enqueue(new SelectColorEvent(
                    this,
                    color
            ));
        } catch (NullPointerException e) {
            client.send(NOTSTARTEDMESSAGE);
        }
    }

    public void selectAction(int index) {
        try {
            game.enqueue(new SelectActionEvent(
                    this,
                    null
            ));
        } catch (NullPointerException e) {
            client.send(NOTSTARTEDMESSAGE);
        }
    }
}
