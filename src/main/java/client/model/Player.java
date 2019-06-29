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

    public Player(View view, String name, int avatar){
        this.name=name;
        this.avatar=avatar;
        this.view=view;
    }

    public void setDamages(int[] damages) {
        this.damages = damages;
        view.displayPlayerDamage(this);
    }

    public void setMarks(int[] marks) {
        this.marks = marks;
        view.displayPlayerMarks(this);
    }

    public void setPoints(int points) {
        this.points = points;
        view.displayPlayerPoints(this);
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
        view.displayPlayerDeaths(this);
    }

    public void setWeapons(Weapon[] weapons) {
        this.weapons = weapons;
        view.displayPlayerWeapons(this);
    }

    public void setAmmo(int[] ammo) {
        this.ammo = ammo;
        view.displayPlayerAmmo(this);
    }

    public void setnPowerup(int nPowerup) {
        this.nPowerup = nPowerup;
        view.displayPlayerNPowerUps(this);
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
        view.displayPlayerLocation(this);
    }

    public String toString() {
        return name + " - " + avatar;
    }

    public int[] getDamages() {
        return damages;
    }

    public int[] getMarks() {
        return marks;
    }

    public Weapon[] getWeapons() {
        return weapons;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public int[] getAmmo() {
        return ammo;
    }

    public int getPoints() {
        return points;
    }

    public int getAvatar() {
        return avatar;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getnPowerup() {
        return nPowerup;
    }

}
