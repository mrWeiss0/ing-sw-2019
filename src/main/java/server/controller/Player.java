package server.controller;

import server.connection.VirtualClient;
import server.model.PowerUp;
import server.model.board.Figure;
import server.model.board.Targettable;
import server.model.weapon.FireMode;
import server.model.weapon.Weapon;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    public void selectWeaponToReload(int[] index) {
        game.enqueue(
                new SelectWeaponToReloadEvent(this, Arrays.stream(index)
                        .mapToObj(x->figure.getWeapons().get(x)).collect(Collectors.toList()).toArray(Weapon[]::new))
        );
    }

    public void selectWeaponFireMode(int index, int[] fm){
        game.enqueue(new SelectWeaponFireModeEvent(this, figure.getWeapons().get(index), Arrays.stream(fm)
                .mapToObj(x->figure.getWeapons().get(index).getFireModes().get(x)).collect(Collectors.toList()).toArray(FireMode[]::new)));
    }

    public void selectGrabbable(int index) {
        game.enqueue(new SelectGrabbableEvent(this, figure.getLocation().peek().get(index)));
    }

    public void selectTargettable(int[] index) {
        game.enqueue(new SelectTargettableEvent(this, Arrays.stream(index)
                .mapToObj(x->figure.getPossibleTargets().get(x)).collect(Collectors.toList()).toArray(Targettable[]::new)));
    }

    public void selectColor(int color) {
        game.enqueue(new SelectColorEvent(this, color));
    }

    public void selectAction(int index) {
        game.enqueue(new SelectActionEvent(this, null));
    }
}
