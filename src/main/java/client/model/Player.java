package client.model;

import client.view.View;

public class Player {
    private View view;
    private String name;
    private int avatar;
    private int[] damages;
    private int[] marks;
    private int points;
    private int deaths;
    private Weapon[] weapons;
    private int[] ammo;
    private int nPowerup;
    private int[] coordinates;

    public Player(View view, String name, int avatar) {
        this.name = name;
        this.avatar = avatar;
        this.view = view;
    }

    public String toString() {
        return name + " - " + avatar;
    }

    public int[] getDamages() {
        return damages;
    }

    public void setDamages(int[] damages) {
        this.damages = damages;
        view.displayPlayerDamage(this);
    }

    public int[] getMarks() {
        return marks;
    }

    public void setMarks(int[] marks) {
        this.marks = marks;
        view.displayPlayerMarks(this);
    }

    public Weapon[] getWeapons() {
        return weapons;
    }

    public void setWeapons(Weapon[] weapons) {
        this.weapons = weapons;
        view.displayPlayerWeapons(this);
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
        view.displayPlayerLocation(this);
    }

    public int[] getAmmo() {
        return ammo;
    }

    public void setAmmo(int[] ammo) {
        this.ammo = ammo;
        view.displayPlayerAmmo(this);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
        view.displayPlayerPoints(this);
    }

    public int getAvatar() {
        return avatar;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
        view.displayPlayerDeaths(this);
    }

    public int getnPowerup() {
        return nPowerup;
    }

    public void setnPowerup(int nPowerup) {
        this.nPowerup = nPowerup;
        view.displayPlayerNPowerUps(this);
    }

    public String getName() {
        return name;
    }
}
