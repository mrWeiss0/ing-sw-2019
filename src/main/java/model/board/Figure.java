package model.board;

import model.AmmoCube;
import model.AmmoTile;
import model.PowerUp;
import model.weapon.Weapon;

import java.util.*;


public class Figure implements Targettable {
    private final List<Figure> damages = new ArrayList<>();
    private final HashMap<Figure, Integer> marks = new HashMap<>();
    private final HashMap<Figure, Integer> newMarks = new HashMap<>();
    private final Set<Weapon> weapons = new HashSet<>();
    private final Set<PowerUp> powerUps = new HashSet<>();
    private final int maxDamages;
    private final int maxMarks;
    private final int maxAmmo;
    private final int maxWeapons;
    private final int maxPowerUps;
    private AbstractSquare square = null;
    private boolean damaged = false;
    private int deaths = 0;
    private AmmoCube ammo = new AmmoCube();

    public Figure(int maxDamages, int maxMarks, int maxAmmo, int maxWeapons, int maxPowerUps) {
        this.maxDamages = maxDamages;
        this.maxMarks = maxMarks;
        this.maxAmmo = maxAmmo;
        this.maxWeapons = maxWeapons;
        this.maxPowerUps = maxPowerUps;
    }

    /**
     * AmmoTile
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

    public AmmoCube getAmmo() {
        return ammo;
    }

    public void addAmmo(AmmoCube ammo) {
        this.ammo = this.ammo.add(ammo).cap(maxAmmo);
    }

    public void subAmmo(AmmoCube ammo) {
        if (!this.ammo.greaterEqThan(ammo))
            throw new IllegalStateException("Ammo " + this.ammo + " not enough to pay " + ammo);
        this.ammo = this.ammo.sub(ammo);
    }

    public Set<PowerUp> getPowerUps() {
        return powerUps;
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
        if (weapons.size() >= maxWeapons)
            throw new IllegalStateException("Reached limit of " + maxWeapons + " weapons");
        weapons.add(grabbed);
    }

    public void grab(AmmoTile grabbed) {
        addAmmo(grabbed.getAmmo());
        if (powerUps.size() < maxPowerUps)
            grabbed.getPowerUp().ifPresent(powerUps::add);
        grabbed.discard();
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

    public boolean isDamaged() {
        return damaged;
    }

    public void clearDamaged() {
        damaged = false;
    }

    public void applyMarks() {
        newMarks.forEach((dealer, n) ->
                marks.put(dealer,
                        Integer.min(marks.getOrDefault(dealer, 0) + n, maxMarks))
        );
        newMarks.clear();
    }
}
