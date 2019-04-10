package model;

import java.util.*;


public class Figure implements Targettable {
    private AbstractSquare square;
    private int[] ammocube = new int[3];//TODO type ammo
    private List<Figure> damages;
    private HashMap<Figure, Integer> marks;
    private int deaths;
    private Set<Weapon> weapons;
    private Set<PowerUp> powerUps;

    public Figure() {
        damages = new ArrayList<>();
        marks = new HashMap<>();
        weapons = new HashSet<>();
        powerUps = new HashSet<>();
        square = null;
    }

    @Override
    public void damageFrom(Figure dealer, int n) {
        if (dealer != this) {
            damages.addAll(Collections.nCopies(
                    Integer.min(n + marks.getOrDefault(dealer, 0), 12 - damages.size()),
                    dealer));
            marks.put(dealer, 0);
        }
    }

    @Override
    public void markFrom(Figure dealer, int n) {
        if (dealer != this) {
            marks.put(dealer,
                    Integer.min(marks.getOrDefault(dealer, 0) + n, 3));
        }
    }

    public AbstractSquare getSquare() {
        return square;
    }

    public void moveTo(AbstractSquare square) {
        if (this.square != null) this.square.removeOccupant(this);
        this.square = square;
        if (this.square != null) this.square.addOccupant(this);
    }

    public List<Figure> getDamages() {
        return damages;
    }
    public void addPowerUp(PowerUp toAdd){
        powerUps.add(toAdd);
    }

    public void grab(Weapon grabbed) {
        weapons.add(grabbed);
    }

    public void grab(AmmoTile grabbed) {
        // TODO
    }
}
