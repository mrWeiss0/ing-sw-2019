package model;

import model.weapon.Weapon;

import java.util.*;


public class Figure implements Targettable {
    private AbstractSquare square;
    private AmmoCube ammo;
    private List<Figure> damages;
    private HashMap<Figure, Integer> marks, newMarks;
    private int deaths;
    private int maxDamage, maxMarks;
    private Set<Weapon> weapons;
    private Set<PowerUp> powerUps;

    public Figure(int maxDamage, int maxMarks) {
        damages = new ArrayList<>();
        marks = new HashMap<>();
        newMarks = new HashMap<>();
        weapons = new HashSet<>();
        powerUps = new HashSet<>();
        square = null;
        this.maxDamage = maxDamage;
        this.maxMarks = maxMarks;
        ammo = new AmmoCube(1, 1, 1);
    }

    /**
     * Returns the square this figure is on.
     *
     * @return the square this figure is on
     */
    public AbstractSquare getSquare() {
        return square;
    }

    public Set<Weapon> getWeapons() {
        return weapons;
    }


    /**
     * Returns the list in which each element represent a single point of
     * damage this figure has taken, where the figure pointed is the dealer.
     *
     * @return the list of damages the figure has taken
     */
    public List<Figure> getDamages() {
        return damages;
    }

    /**
     * Sets the current square to the one given and takes care of removing
     * and adding this figure from the occupants list of the respective squares.
     *
     * @param square the square the figure is to be moved to
     */
    public void moveTo(AbstractSquare square) {
        if (this.square != null) this.square.removeOccupant(this);
        this.square = square;
        if (this.square != null) this.square.addOccupant(this);
    }

    public void grab(Weapon grabbed) {
        weapons.add(grabbed);
    }

    public void grab(AmmoTile grabbed) {
        // TODO
    }

    /**
     * Adds the given amount of damage to this figure and setting the specified
     * figure. It also clears the dealer's marks, adding them as damage.
     * All damage is dealt up to the overkill threshold.
     *
     * @param dealer the figure that has dealt the damage
     * @param n      the amount of damage given to this figure.
     */
    @Override
    public void damageFrom(Figure dealer, int n) {
        if (dealer != this) {
            damages.addAll(Collections.nCopies(
                    Integer.min(n + marks.getOrDefault(dealer, 0), maxDamage - damages.size()),
                    dealer));
            marks.put(dealer, 0);
        }
    }

    /**
     * Adds the given amount of marks to this figure from the specified figure.
     * Marks from the specified dealer are added up to the maximum threshold.
     *
     * @param dealer the figure that has dealt the marks
     * @param n      the amount of marks given.
     */
    @Override
    public void markFrom(Figure dealer, int n) {
        if (dealer != this) {
            newMarks.put(dealer, n);
        }
    }

    public void applyMarks() {
        newMarks.forEach((dealer, n) ->
                marks.put(dealer,
                        Integer.min(marks.getOrDefault(dealer, 0) + n, maxMarks))
        );
        newMarks.clear();
    }

}
