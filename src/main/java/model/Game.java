package model;

import model.board.*;
import model.weapon.FireStep;
import model.weapon.Weapon;

import java.util.*;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <code>Game</code> is a class containing all of the major elements of a game
 * such as the <code>Board</code>, card <code>Deck</code>s and
 * <code>Figure</code>s, as well as the players.
 * <p>
 * It keeps track of <code>Player</code>s and their order, as well
 * as provides methods to manage them.
 */
public class Game {
    // Killshot Track
    private final List<Figure> killCount = new ArrayList<>(); // Kills and overkills done by players
    private final List<Player> players;
    private final Deck<AmmoTile> ammoTileDeck = new Deck<>();
    private final Deck<Weapon> weaponDeck = new Deck<>();
    private final Deck<PowerUp> powerUpDeck = new Deck<>();
    private final boolean frenzyOn;
    private final int[] killPoints;
    private final int[] frenzyPoints;
    private final Board board;
    private int remainingKills; // Kills to finish game
    private int currPlayer = -1;


    private List<List<FireStep>> powerUpEffects = Arrays.asList(
            Collections.singletonList(new FireStep(1,1,
                    (shooter,gameBoard,last)->new HashSet<>(gameBoard.getDamaged()),
                    (shooter, curr, last)->
                        curr.forEach(x->x.damageFrom(shooter,1))
                    )),
            Arrays.asList(new FireStep(1,1,
                    (shooter,gameBoard,last)->new HashSet<>(gameBoard.getFigures()),
                    (shooter, curr, last)->{
                    }
                    ),new FireStep(1,1,
                    (shooter,gameBoard,last)->((Figure)last.toArray()[0]).getSquare().atDistance(1,2).
                            stream().filter(x->
                            x.getCoordinates()[0]==((Figure)last.toArray()[0]).getSquare().getCoordinates()[0] ||
                                    x.getCoordinates()[1]==((Figure)last.toArray()[0]).getSquare().getCoordinates()[1]).collect(Collectors.toSet()),
                    (shooter, curr, last)->
                        ((Figure)last.toArray()[0]).moveTo((AbstractSquare) curr.toArray()[0])
            )),
            Collections.singletonList(new FireStep(1,1,
                    (shooter,gameBoard,last)->new HashSet<>(Collections.singletonList(shooter.getDamages().get(shooter.getDamages().size()-1))),
                    (shooter, curr, last)-> curr.forEach(x->x.markFrom(shooter,1))
                    )),
            Collections.singletonList(new FireStep(1,1,
                    (shooter,gameBoard,last)->new HashSet<>(gameBoard.getSquares()),
                    (shooter, curr, last)->shooter.moveTo((AbstractSquare) curr.toArray()[0])
                    )
    ));

    private ObjIntConsumer<List<Figure>> normalPointGiver = (damages, deaths)->{
        List<Figure> toRemunerate=
                damages.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting())).
                        entrySet().stream().sorted((x,y)->{
                    if(x.getValue()>y.getValue()) return -1;
                    else if(x.getValue()<y.getValue()) return 1;
                    else return Integer.compare(damages.indexOf(x.getKey()),damages.indexOf(y.getKey()));
                }).map(Map.Entry::getKey).distinct().collect(Collectors.toList());
        List<Integer> pointSave = Arrays.stream(getKillPoints()).boxed().collect(Collectors.toList());
        pointSave.subList(0,deaths-1).clear();
        damages.get(0).addPoints(1);
        for (Figure figure:toRemunerate){
            if(!pointSave.isEmpty()) figure.addPoints(pointSave.remove(0));
            else figure.addPoints(1);
        }
        remainingKills--;
    };

    public void toggleFrenzy() {
        board.getFigures().stream().filter(x->x.getDamages().size()==0).forEach(x->x.setPointGiver(frenzyPointGiver));
    }

    private ObjIntConsumer<List<Figure>> frenzyPointGiver = (damages,deaths)->{
        List<Figure> toRemunerate=
                damages.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting())).
                        entrySet().stream().sorted((x,y)->{
                    if(x.getValue()>y.getValue()) return -1;
                    else if(x.getValue()<y.getValue()) return 1;
                    else return Integer.compare(damages.indexOf(x.getKey()),damages.indexOf(y.getKey()));
                }).map(Map.Entry::getKey).distinct().collect(Collectors.toList());
        List<Integer> pointSave = Arrays.stream(getFrenzyPoints()).boxed().collect(Collectors.toList());
        for (Figure figure:toRemunerate){
            if(!pointSave.isEmpty()) figure.addPoints(pointSave.remove(0));
            else figure.addPoints(1);
        }
        remainingKills--;

    };

    private Game(Builder builder) {
        remainingKills = builder.nKills;
        board = builder.boardBuilder.build();
        board.getFigures().forEach(x->x.setPointGiver(normalPointGiver));
        players = Collections.unmodifiableList(builder.players);

        weaponDeck.discard(Arrays.asList(builder.weapons));
        powerUpDeck.discard(Arrays.stream(builder.powerUps)
                .map(powerup -> {
                    int[] ammoVal = new int[powerup.color + 1];
                    ammoVal[powerup.color] = 1;
                    return new PowerUp(new AmmoCube(ammoVal), builder.boardBuilder.getSpawnByColor(powerup.color),
                            powerUpDeck::discard,powerUpEffects.get(powerup.type.ordinal()),powerup.type);
                })
                .collect(Collectors.toList()));
        ammoTileDeck.discard(Arrays.stream(builder.ammoTiles)
                .map(tile -> new AmmoTile(new AmmoCube(tile.ammo), tile.powerUp ? powerUpDeck::draw : () -> null, ammoTileDeck::discard))
                .collect(Collectors.toList()));
        killPoints=builder.killPoints;
        frenzyPoints = builder.frenzyPoints;
        frenzyOn = builder.frenzyOn;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Returns the player whose turn its taking place.
     *
     * @return the player whose turn its taking place
     */
    public Player currentPlayer() {
        return players.get(currPlayer);
    }

    /**
     * Indicates that a player's turn has passed by updating the current player
     * and returning it.
     * If the last player has completed its turn, the method cycles back to the
     * first player.
     *
     * @return the player that comes after the one that has completed
     * its turn
     */
    public Player nextPlayer() {
        currPlayer++;
        currPlayer %= players.size();
        return currentPlayer();
    }

    public void fillBoard() {
        for (AbstractSquare current : board.getSquares())
            current.accept(this);
    }

    public PowerUp drawPowerup(){
        return powerUpDeck.draw();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void fillSquare(AmmoSquare square) {
        square.refill(ammoTileDeck.draw());
    }

    public void fillSquare(SpawnSquare square) {
        square.refill(weaponDeck.draw());
    }

    public void addKillCount(Figure f){
        killCount.add(f);
    }

    public int getRemainingKills(){
        return remainingKills;
    }

    public boolean isFrenzy(){
        return frenzyOn && remainingKills<=0;
    }
    //TODO IF POSSIBLE REMOVE
    private int[] getKillPoints() {
        return killPoints;
    }

    private int[] getFrenzyPoints() {
        return frenzyPoints;
    }

    public List<Figure> getKillCount() {
        return killCount;
    }


    public static class Builder {

        private final Board.Builder boardBuilder = new Board.Builder();
        private final List<Player> players = new ArrayList<>();
        private int nKills;
        private int maxDamages;
        private int maxMarks;
        private int maxAmmo;
        private int maxWeapons;
        private int maxPowerUps;
        private int deathDamage;
        private boolean frenzyOn;
        private int[] killPoints;
        private int[] frenzyPoints;
        private AmmoCube defaultAmmo = new AmmoCube();
        private AmmoTileImage[] ammoTiles = new AmmoTileImage[]{};
        private Weapon[] weapons = new Weapon[]{};
        private PowerUpImage[] powerUps = new PowerUpImage[]{};

        public Builder nKills(int val) {
            nKills = val;
            return this;
        }

        public Builder killPoints(int[] val){
            killPoints=val;
            return this;
        }

        public Builder frenzyPoints(int[] val){
            frenzyPoints=val;
            return this;
        }

        public Builder frenzyOn(boolean val){
            frenzyOn=val;
            return this;
        }

        public Builder maxDamages(int val) {
            maxDamages = val;
            return this;
        }

        public Builder maxMarks(int val) {
            maxMarks = val;
            return this;
        }

        public Builder maxAmmo(int val) {
            maxAmmo = val;
            return this;
        }

        public Builder maxWeapons(int val) {
            maxWeapons = val;
            return this;
        }

        public Builder maxPowerUps(int val) {
            maxPowerUps = val;
            return this;
        }
        public Builder deathDamage(int val){
            deathDamage = val;
            return this;
        }

        public Builder defaultAmmo(AmmoCube val) {
            defaultAmmo = val;
            return this;
        }

        public Builder spawnCapacity(int val) {
            boardBuilder.spawnCapacity(val);
            return this;
        }

        public Builder squares(SquareImage... val) {
            return squares(() -> val);
        }

        public Builder squares(Supplier<SquareImage[]> supplier) {
            boardBuilder.squares(supplier);
            return this;
        }

        public Builder ammoTiles(AmmoTileImage... arr) {
            ammoTiles = arr;
            return this;
        }

        public Builder weapons(Weapon... arr) {
            weapons = arr;
            return this;
        }

        public Builder powerUps(PowerUpImage... arr) {
            powerUps = arr;
            return this;
        }

        public Builder player(Player player) {
            players.add(player);
            return this;
        }

        public Builder removePlayer(Player player) {
            players.remove(player);
            return this;
        }

        public Game build() {
            for (Player p : players) {
                Figure figure = new Figure(maxDamages, maxMarks, maxAmmo, maxWeapons, maxPowerUps, deathDamage);
                figure.addAmmo(defaultAmmo);
                p.setFigure(figure);
                boardBuilder.figures(figure);
            }
            return new Game(this);
        }
    }
}
