package model.board;

import model.AmmoCube;
import model.AmmoTile;
import model.Game;
import model.PowerUp;
import model.weapon.Weapon;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


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
    private final int deathDamage;
    private AbstractSquare square = null;
    private boolean damaged = false;
    private List<Figure> deaths = new ArrayList<>();
    private int remainingActions;
    private int points = 0;
    private AmmoCube ammo = new AmmoCube();
    //TODO CONSUMER FOR DAMAGE
    //TODO END GAME

    public Figure(int maxDamages, int maxMarks, int maxAmmo, int maxWeapons, int maxPowerUps, int deathDamage) {
        this.maxDamages = maxDamages;
        this.maxMarks = maxMarks;
        this.maxAmmo = maxAmmo;
        this.maxWeapons = maxWeapons;
        this.maxPowerUps = maxPowerUps;
        this.deathDamage = deathDamage;
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

    public void setRemainingActions(int remainingActions) {
        this.remainingActions = remainingActions;
    }

    public int getRemainingActions() {
        return remainingActions;
    }

    public void resolveDeath(Game game){
        //TODO IN FRENZY 2-1-1-1 AND NO FIRSTBLOOD IF WITHOUT DAMAGE ENTERING FRENZY
        if(damages.size()>=deathDamage){
            square=null;

            game.addKillCount(damages.get(deathDamage-1));
            deaths.add(damages.get(deathDamage-1));
            if(damages.size()>deathDamage) {
                damages.get(maxDamages-1).markFrom(this,1);
                game.addKillCount(damages.get(maxDamages-1));
            }

            List<Figure> toRemunerate=
            damages.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting())).
                entrySet().stream().sorted((x,y)->{
                    if(x.getValue()>y.getValue()) return -1;
                    else if(x.getValue()<y.getValue()) return 1;
                    else return Integer.compare(damages.indexOf(x.getKey()),damages.indexOf(y.getKey()));
            }).map(Map.Entry::getKey).distinct().collect(Collectors.toList());
            List<Integer> pointSave = Arrays.stream(game.getKillPoints()).boxed().collect(Collectors.toList());
            pointSave.subList(0,deaths.size()-1).clear();
            damages.get(0).addPoints(1);
            for (Figure figure:toRemunerate){
                if(!pointSave.isEmpty()) figure.addPoints(pointSave.remove(0));
                else figure.addPoints(1);
            }
            game.setRemainingKills(game.getRemainingKills()-1);
            damages.clear();
        }
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points+= points;
    }

    public void addPowerUp(PowerUp powerUp){
        powerUps.add(powerUp);
    }
}
