package server.model.board;

import server.controller.Player;
import server.model.AmmoCube;
import server.model.AmmoTile;
import server.model.Game;
import server.model.PowerUp;
import server.model.weapon.Weapon;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

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
    private int points;
    private AmmoCube ammo = new AmmoCube();
    private Player player = new Player(null);
    private int[] killPoints;
    private boolean firstBlood;

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

    public List<Figure> getMarks() {
        List<Figure> result = new ArrayList<>();
        for (Figure f : marks.keySet())
            result.addAll(Collections.nCopies(marks.get(f), f));
        return result;
    }

    public int getDeaths() {
        return deaths;
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
        if (player != null)
            player.broadcastAmmo();
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
        player.broadcastAmmo();
    }

    /**
     * Returns this figure's PowerUp.
     *
     * @return this figure's PowerUp
     */
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public AmmoCube getTotalAmmo() {
        return getPowerUps().stream()
                .map(PowerUp::getAmmo)
                .reduce(AmmoCube::add)
                .orElseGet(AmmoCube::new)
                .add(player.getFigure().getAmmo());
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
        player.broadcastLocation();
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
        player.broadcastWeapons();
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
        player.broadcastNPowerUps();
        player.sendPowerUps(powerUps);
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
            player.broadcastDamages();
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
        player.broadcastMarks();
    }

    public boolean resolveDeath(Game game) {
        if (damages.size() >= killDamages) {
            int val = 1;
            if (damages.size() >= maxDamages) {
                damages.get(maxDamages - 1).markFrom(this, 1);
                val = 2;
            }
            game.addKillCount(val, damages.get(killDamages - 1));
            givePoints();
            moveTo(null);
            ++deaths;
            damages.clear();
            player.broadcastDeaths();
            return true;
        }
        return false;
    }

    public void givePoints() {
        List<Figure> figures = damages.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Comparator
                        .comparingLong((ToLongFunction<Map.Entry<Figure, Long>>) Map.Entry::getValue).reversed()
                        .thenComparingInt(x -> damages.indexOf(x.getKey())))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int i = deaths;
        for (Figure figure : figures) {
            figure.addPoints(killPoints[i]);
            if (i < killPoints.length - 1)
                ++i;
        }

        if (firstBlood && damages.size() > 1)
            damages.get(0).addPoints(1); // First blood

    }

    public void setKillPoints(int[] killPoints) {
        this.killPoints = killPoints;
    }

    public void setFirstBlood(boolean blood) {
        firstBlood = blood;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
        player.broadcastPoints();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
