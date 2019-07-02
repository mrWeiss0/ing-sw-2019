package server.controller;

import server.connection.VirtualClient;
import server.model.PowerUp;
import server.model.board.Figure;
import server.model.board.Targettable;
import server.model.weapon.FireMode;
import server.model.weapon.Weapon;

import java.util.Arrays;

public class Player {
    private static final String NOT_STARTED_MESSAGE = "The game is not started yet";
    private static final String INVALID_ARGUMENT_MESSAGE="You have chosen an invalid index";
    private final String name;
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
        figure.setPlayer(this);
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
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if(Arrays.stream(index).anyMatch(x->x<0 || x>=figure.getPowerUps().size())){
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectPowerUpEvent(
                this,
                Arrays.stream(index)
                        .mapToObj(x -> figure.getPowerUps().get(x))
                        .distinct()
                        .toArray(PowerUp[]::new)
        ));
    }

    public void selectWeaponToReload(int[] index) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if(Arrays.stream(index).anyMatch(x->x<0 || x>=figure.getWeapons().size())){
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectWeaponToReloadEvent(
                this,
                Arrays.stream(index)
                        .mapToObj(x -> figure.getWeapons().get(x))
                        .distinct()
                        .toArray(Weapon[]::new)
        ));
    }

    public void selectWeaponFireMode(int index, int[] fm) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if(index<0 || index >=figure.getWeapons().size()
                || Arrays.stream(fm)
                .anyMatch(x->x<0 || x>=figure.getWeapons().get(index).getFireModes().size())){
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectWeaponFireModeEvent(
                this,
                figure.getWeapons().get(index),
                Arrays.stream(fm)
                        .mapToObj(x -> figure.getWeapons().get(index).getFireModes().get(x))
                        .distinct()
                        .toArray(FireMode[]::new)
        ));
    }

    public void selectGrabbable(int index) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if(index<0 || index>=figure.getLocation().peek().size()){
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectGrabbableEvent(
                this,
                figure.getLocation().peek().get(index)
        ));
    }

    public void selectTargettable(int[] index) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        try {
            game.enqueue(new SelectTargettableEvent(
                    this,
                    Arrays.stream(index)
                            .mapToObj(game.getGame().getBoard()::resolveID)
                            .distinct()
                            .toArray(Targettable[]::new)
            ));
        }catch(IndexOutOfBoundsException e){
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
        }
    }

    public void selectColor(int color) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if(color<0 || color >=3){
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectColorEvent(
                this,
                color
        ));
    }

    public void selectAction(int index) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        //TODO WHEN ACTIONS DONE
        if(true){
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        } else{}
        game.enqueue(new SelectActionEvent(
                this,
                null
        ));
    }

    public void reconnect() {
        this.active = true;
    }
}
