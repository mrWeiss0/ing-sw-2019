package server.model.board;

import server.controller.Action;
import server.controller.Player;
import server.model.*;
import server.model.weapon.Weapon;

import java.util.*;
import java.util.function.ObjIntConsumer;

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
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final int killDamages;
    private final int maxDamages;
    private final int maxMarks;
    private final int maxAmmo;
    private final int maxWeapons;
    private final int maxPowerUps;
    private AbstractSquare location = null;
    private boolean damaged = false;
    private int deaths;
    private int remainingActions;
    private int points;
    private AmmoCube ammo = new AmmoCube();
    private ObjIntConsumer<List<Figure>> pointGiver; // TODO ?
    private Player player;
    private boolean frenzyTurnLeft = true; // TODO ?
    private List<Targettable> possibleTargets;
    private List<Action> possibleAction= new ArrayList<>();
    /**
     * Constructs a figure with the given damage, marks, ammo, weapon and powerup limits.
     */
    public Figure(int killDamages, int maxDamages, int maxMarks, int maxAmmo, int maxWeapons, int maxPowerUps) {
        this.killDamages = killDamages;
        this.maxDamages = maxDamages;
        this.maxMarks = maxMarks;
        this.maxAmmo = maxAmmo;
        this.maxWeapons = maxWeapons;
        this.maxPowerUps = maxPowerUps;
    }

    /**
     * Returns the square this figure is on.
     *
     * @return the location this figure is on
     */
    public AbstractSquare getLocation() {
        return location;
    }

    /**
     * Returns the set of this player's weapons.
     *
     * @return the set of this player's weapons
     */
    public List<Weapon> getWeapons() {
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
     * @param ammo the ammo to be subtracted
     * @throws IllegalStateException if the ammo to subtract is more than
     *                               what the figure has
     */
    public void subAmmo(AmmoCube ammo) {
        if (!this.ammo.greaterEqThan(ammo))
            throw new IllegalStateException("Not enough ammo");
        this.ammo = this.ammo.sub(ammo);
    }

    /**
     * Returns this figure's PowerUp.
     *
     * @return this figure's PowerUp
     */
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    /**
     * Sets the current location to the one given and takes care of removing
     * and adding this figure from the occupants list of the respective squares.
     *
     * @param square the location the figure is to be moved to
     */
    public void moveTo(AbstractSquare square) {
        if (this.location != null) this.location.removeOccupant(this);
        this.location = square;
        if (this.location != null) this.location.addOccupant(this);
    }

    /**
     * Adds the specified weapon the figure's weapon set.
     *
     * @param grabbed the weapon to be added.
     * @throws IllegalStateException if the weapon size limit has already been
     *                               reached.
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

    /**
     * Applies to this figure its newly acquired marks, up to the mark's
     * threshold for each dealer. Also sets the damaged flag to false.
     */
    public void applyMarks() {
        newMarks.forEach((dealer, n) ->
                marks.put(dealer,
                        Integer.min(marks.getOrDefault(dealer, 0) + n, maxMarks))
        );
        newMarks.clear();
        damaged = false;
    }

    public boolean isFrenzyTurnLeft() {
        return frenzyTurnLeft;
    } // TODO ?

    public void setFrenzyTurnLeft(boolean frenzyTurnLeft) {
        this.frenzyTurnLeft = frenzyTurnLeft;
    } // TODO ?

    public int getRemainingActions() {
        return remainingActions;
    } // TODO ?

    public void setRemainingActions(int remainingActions) {
        this.remainingActions = remainingActions;
    }

    public void resolveDeath(Game game) {
        if (damages.size() >= killDamages) {
            moveTo(null);
            ++deaths;
            game.addKillCount(damages.get(maxDamages - 2));
            if (damages.size() > killDamages) {
                damages.get(maxDamages - 1).markFrom(this, 1);
                game.addKillCount(damages.get(maxDamages - 1));
            }
            pointGiver.accept(damages, deaths);
            damages.clear();
        }
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void setPointGiver(ObjIntConsumer<List<Figure>> consumer) {
        this.pointGiver = consumer;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Targettable> getPossibleTargets(){
        return possibleTargets;
    }

    public void setPossibleTargets(Set<Targettable> targets){
        this.possibleTargets= new ArrayList<>(targets);
    }

    public List<String> getPossibleActions(boolean beforeFirstPlayer, boolean finalFrenzyOn) {
        List<String> possibleAction = new ArrayList<>();
        possibleAction.add("move");
        possibleAction.add("grab");
        if (damages.size() >= 3) {
            possibleAction.remove("grab");
            possibleAction.add("grab_a");
        }
        if (!weapons.isEmpty()) {
            possibleAction.add("shoot");
            if (damages.size() >= 6) {
                possibleAction.remove("shoot");
                possibleAction.add("shoot_a");
            }
        }
        if (finalFrenzyOn) {
            possibleAction.clear();

            if (!weapons.isEmpty())
                possibleAction.add("shoot_f2");
            possibleAction.add("grab_f2");

            if (beforeFirstPlayer) {
                possibleAction.clear();
                if (!weapons.isEmpty())
                    possibleAction.add("shoot_f1");
                possibleAction.add("move_f1");
                possibleAction.add("grab_f1");
            }
        }
        if (powerUps.stream().anyMatch(x -> x.getType().equals(PowerUpType.NEWTON))) {
            possibleAction.add("newton");
        }
        if (powerUps.stream().anyMatch(x -> x.getType().equals(PowerUpType.TELEPORTER))) {
            possibleAction.add("teleporter");
        }
        return possibleAction;
    }
}
