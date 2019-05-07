package model;

import model.weapon.Weapon;

import java.util.*;


public class Figure implements Targettable {
    private AbstractSquare square = null;
    private List<Figure> damages = new ArrayList<>();
    private boolean damaged = false;
    private HashMap<Figure, Integer> marks = new HashMap<>();
    private HashMap<Figure, Integer> newMarks = new HashMap<>();
    private int deaths = 0;
    private Set<Weapon> weapons = new HashSet<>();
    private Set<PowerUp> powerUps = new HashSet<>();
    private AmmoCube ammo;
    private int maxDamages;
    private int maxMarks;

    public Figure(int maxDamages, int maxMarks, AmmoCube ammo) {
        this.ammo = ammo;
        this.maxDamages = maxDamages;
        this.maxMarks = maxMarks;
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
                    Integer.min(n + marks.getOrDefault(dealer, 0), maxDamages - damages.size()),
                    dealer));
            marks.put(dealer, 0);
            damaged = true;
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

    public boolean isDamagedAndClear() {
        boolean d = damaged;
        damaged = false;
        return d;
    }

    public void applyMarks() {
        newMarks.forEach((dealer, n) ->
                marks.put(dealer,
                        Integer.min(marks.getOrDefault(dealer, 0) + n, maxMarks))
        );
        newMarks.clear();
    }

}
