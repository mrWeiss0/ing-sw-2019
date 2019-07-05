package client.model;

import client.view.Colors;
import client.view.View;

public class Player {
    private final View view;
    private final String name;
    private final int avatar;
    private int[] damages = new int[]{};
    private int[] marks = new int[]{};
    private int points = 0;
    private int deaths = 0;
    private Weapon[] weapons = new Weapon[]{};
    private int[] ammo = new int[]{0, 0, 0};
    private int nPowerup = 0;
    private int[] coordinates = new int[]{-1, -1};
    private int leaderBoard = 0;
    private int nKills = 0;

    public Player(View view, String name, int avatar) {
        this.name = name;
        this.avatar = avatar;
        this.view = view;
    }

    public String toString() {
        return name + " - " + Colors.values()[avatar].toString();
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
        this.coordinates = coordinates==null?new int[]{-1,-1}:coordinates;
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

    public int getLeaderBoard() {
        return leaderBoard;
    }

    public void setLeaderBoard(int value) {
        leaderBoard = value;
        view.displayLeaderBoard(this);
    }

    public int getNKills() {
        return nKills;
    }

    public void setNKills(int v) {
        nKills = v;
        view.displayNKills(this);
    }
}
