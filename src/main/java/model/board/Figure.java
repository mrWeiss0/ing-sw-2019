package model.board;

import model.AmmoCube;
import model.AmmoTile;
import model.PowerUp;
import model.weapon.Weapon;

import java.util.*;

/**
 * The <code>Figure</code> class represents a player's figure, his location
 * as well as his damages, marks, ammo, weapons and powerups, with their limit
 * size.
 * <p>
 * It provides methods for returning its location as well as damages, marks,
 * ammo and weapons.
 * <p>
 * It provides methods to add and subtract from a figure's ammo and to grab a
 * new weapon or PowerUp.
 * <p>
 * It implements the <code>Targettable</code> interface but as the final
 * recipient of the damage, it is distributed as follows: damaging a figure
 * sets its status as damaged, as well as applying all damages; marking a
 * figure only adds marks to a newly acquired marks set; to apply the marks
 * the <code>applyMarks</code> method should be used once every damage for the
 * current action is done.
 */
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
    private AbstractSquare square = null; //TODO refactor to location
    private boolean damaged = false;
    private int deaths = 0;
    private AmmoCube ammo = new AmmoCube();

    /**
     * Constructs a figure with the given damage, marks, ammo, weapon and powerup limits.
     */
    public Figure(int maxDamages, int maxMarks, int maxAmmo, int maxWeapons, int maxPowerUps) {
        this.maxDamages = maxDamages;
        this.maxMarks = maxMarks;
        this.maxAmmo = maxAmmo;
        this.maxWeapons = maxWeapons;
        this.maxPowerUps = maxPowerUps;
    }

    /**
     * Returns the square this figure is on.
     *
     * @return the square this figure is on
     */
    public AbstractSquare getSquare() {
        return square;
    }

    /**
     * Returns the set of this player's weapons.
     *
     * @return the set of this player's weapons
     */
    public Set<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * Returns the player's damages: it is a list in which each element
     * represent a single point of damage this figure has taken and the figure
     * pointed to is the dealer.
     *
     * @return the list of damages the figure has taken
     */
    public List<Figure> getDamages() {
        return damages;
    }

    /**
     * Returns this figure's ammo.
     *
     * @return this figure's ammo
     */
    public AmmoCube getAmmo() {
        return ammo;
    }

    /**
     * Adds the given ammo to this figure's.
     *
     * @param ammo the ammo to be added
     */
    public void addAmmo(AmmoCube ammo) {
        this.ammo = this.ammo.add(ammo).cap(maxAmmo);
    }

    /**
     * Subtracts the given ammo to this figure's.
     *
     * @throws IllegalStateException if the ammo to subtract is more than
     * what the figure has
     * @param ammo the ammo to be subtracted
     */
    public void subAmmo(AmmoCube ammo) {
        if (!this.ammo.greaterEqThan(ammo))
            throw new IllegalStateException("Ammo " + this.ammo + " not enough to pay " + ammo);
        this.ammo = this.ammo.sub(ammo);
    }

    /**
     * Returns this figure's PowerUp.
     *
     * @return this figure's PowerUp
     */
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

    /**
     * Adds the specified weapon the figure's weapon set.
     *
     * @throws IllegalStateException if the weapon size limit has already been
     * reached.
     * @param grabbed the weapon to be added.
     */
    public void grab(Weapon grabbed) {
        if (weapons.size() >= maxWeapons)
            throw new IllegalStateException("Reached limit of " + maxWeapons + " weapons");
        weapons.add(grabbed);
    }

    /**
     * Adds the <code>AmmoTile</code>'s ammo and PowerUp to the figure's.
     * If the PowerUp size limit has been reached, the tile's PowerUp is not
     * added.
     *
     * @param grabbed the <code>AmmoTile</code> which ammo and PowerUps are to
     *                be added.
     */
    public void grab(AmmoTile grabbed) {
        addAmmo(grabbed.getAmmo());
        if (powerUps.size() < maxPowerUps)
            grabbed.getPowerUp().ifPresent(powerUps::add);
        grabbed.discard();
    }

    /**
     * Adds the given amount of damage to this figure and setting the specified
     * one as the dealer; it also clears the dealer's marks, adding them as
     * damage. Finally this figure is set as damaged.
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
     * Adds the given amount of marks to this figure setting the specified one
     * as the dealer. The newly acquired marks are not applied, but stashed
     * until a provided function is called.
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

    /**
     * Returns true if the figure is damaged.
     *
     * @return true if the figure is damaged
     */
    public boolean isDamaged() {
        return damaged;
    }

    //TODO should be called inside apply marks
    /**
     * Sets this figure's damaged status as false.
     */
    public void clearDamaged() {
        damaged = false;
    }

    /**
     * Applies to this figure its newly acquired marks, up to the mark's
     * threshold for each dealer.
     */
    public void applyMarks() {
        newMarks.forEach((dealer, n) ->
                marks.put(dealer,
                        Integer.min(marks.getOrDefault(dealer, 0) + n, maxMarks))
        );
        newMarks.clear();
    }
}
