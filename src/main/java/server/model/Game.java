package server.model;

import client.model.GameState;
import server.controller.Player;
import server.model.board.*;
import server.model.weapon.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * The <code>Game</code> class contains all of the major elements of a game:
 * its scoreboard, players, board as well as its ammo tile, weapon and powerup
 * decks.
 * <p>
 * It keeps track of <code>Player</code>s and their order, as well
 * as provides methods to manage them.
 * <p>
 * It provides a method to pass itself its board's squares so the may call this
 * game appropriate refilling methods.
 * <p>
 * The class also provides a <code>Builder</code> to allow the game's setup
 * and creation. A <code>Game</code> may only be instantiated through the
 * <code>Game.Builder</code> class.
 */
public class Game {
    // Killshot Track
    private final List<Figure> killCount = new ArrayList<>(); // Kills and overkills done by players
    private final List<Boolean> overkills = new ArrayList<>();
    private final List<Player> players;
    private final Deck<AmmoTile> ammoTileDeck = new Deck<>();
    private final Deck<Weapon> weaponDeck = new Deck<>();
    private final Deck<PowerUp> powerUpDeck = new Deck<>();
    private final boolean frenzyOn;
    private final int[] frenzyPoints;
    private final Board board;
    private int remainingKills; // Kills to finish game
    private int currPlayer = -1;
    private int lastPlayer = -1;
    private final int mapType;
    private final int maxKills;
    private final int turnTimeout;
    private final int otherTimeout;

    private Game(Builder builder) {
        remainingKills = builder.nKills;
        board = builder.boardBuilder.build();
        board.getFigures().forEach(x -> {
            x.setKillPoints(builder.killPoints);
            x.setFirstBlood(true);
        });
        players = Collections.unmodifiableList(builder.players);
        weaponDeck.discard(Arrays.asList(builder.weapons));
        powerUpDeck.discard(Arrays.stream(builder.powerUps)
                .map(powerup -> {
                    int[] ammoVal = new int[powerup.color + 1];
                    ammoVal[powerup.color] = 1;
                    return new PowerUp(
                            powerup.type, new AmmoCube(ammoVal),
                            builder.boardBuilder.getSpawnByColor(powerup.color), powerUpDeck::discard);
                })
                .collect(Collectors.toList()));
        ammoTileDeck.discard(Arrays.stream(builder.ammoTiles)
                .map(tile -> new AmmoTile(
                        tile.id,
                        new AmmoCube(tile.ammo), tile.powerUp ? powerUpDeck::draw : () -> null,
                        ammoTileDeck::discard))
                .collect(Collectors.toList()));
        frenzyPoints = builder.frenzyPoints;
        frenzyOn = builder.frenzyOn;
        this.mapType = builder.mapType;
        this.maxKills = builder.nKills;
        players.forEach(x -> x.getFigure().getPowerUps().add(powerUpDeck.draw()));
        turnTimeout = builder.turnTimeout;
        otherTimeout = builder.otherTimeout;
    }

    public int getMapType() {
        return mapType;
    }

    public int getMaxKills() {
        return maxKills;
    }

    /**
     * Returns this game's board.
     *
     * @return this game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Updates the current player with the next player in line.
     * If the last available player has completed its turn, the current player
     * cycles back to the first one.
     *
     * @return the player whose turn is next
     */
    public Player nextPlayer() {
        fillBoard();
        currPlayer++;
        currPlayer %= players.size();
        players.forEach(x -> x.sendCurrentPlayer(currPlayer));
        return players.get(currPlayer);
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Asks every square to fill itself calling the appropriate <code>Game</code>'s
     * method.
     */
    public void fillBoard() {
        for (AbstractSquare current : board.getSquares())
            current.accept(this);
        board.getSquares().forEach(x -> players.forEach(y -> y.sendSquareContent(x)));
    }

    public PowerUp drawPowerup() {
        return powerUpDeck.draw();
    }

    /**
     * Refills the <code>AmmoSquare</code> by drawing an <code>AmmoTile</code>.
     *
     * @param square the square to refill
     */
    public void fillSquare(AmmoSquare square) {
        square.refill(ammoTileDeck.draw());
    }

    /**
     * Refills the <code>SpawnSquare</code> by drawing a <code>Weapon</code>.
     *
     * @param square the square to refill
     */
    public void fillSquare(SpawnSquare square) {
        square.refill(weaponDeck.draw());
    }

    public void addKillCount(int val, Figure f) {
        killCount.add(f);
        overkills.add(val > 1);
        if (lastPlayer >= 0) {
            f.setKillPoints(frenzyPoints);
            f.setFirstBlood(false);
        }
        --remainingKills;
        players.forEach(x -> x.sendKillTrack(killCount, overkills));
    }

    public void endTurn() {
        if (lastPlayer == currPlayer)
            endGame();
        else if (remainingKills <= 0)
            toggleFrenzy();
    }

    public void toggleFrenzy() {
        if (frenzyOn) {
            board.getFigures().stream().filter(x -> x.getDamages().isEmpty()).forEach(x -> {
                x.setKillPoints(frenzyPoints);
                x.setFirstBlood(false);
            });
            lastPlayer = currPlayer;
        } else
            endGame();
    }

    private void endGame() {
        // TODO give last points, determine winner
        players.forEach(x -> x.sendGameState(GameState.ENDED.ordinal()));
    }

    public List<Boolean> getOverkills() {
        return overkills;
    }

    public int getRemainingKills() {
        return remainingKills;
    }

    public List<Figure> getKillCount() {
        return killCount;
    }

    public int getActionsID() {
        if (lastPlayer < 0)
            return 0;
        return currPlayer > lastPlayer ? 2 : 1;
    }

    public int getTurnTimeout() {
        return turnTimeout;
    }

    public int getOtherTimeout() {
        return otherTimeout;
    }

    /**
     * The <code>Game.Builder</code> class allows the construction of a new
     * <code>Game</code>.
     */
    public static class Builder {
        private final Board.Builder boardBuilder = new Board.Builder();
        private final List<Player> players = new ArrayList<>();
        private int nKills;
        private int killDamages;
        private int maxDamages;
        private int maxMarks;
        private int maxAmmo;
        private int maxWeapons;
        private int maxPowerUps;
        private int mapType;
        private boolean frenzyOn;
        private int[] killPoints;
        private int[] frenzyPoints;
        private int turnTimeout;
        private int otherTimeout;
        private AmmoCube defaultAmmo = new AmmoCube();
        private AmmoTileImage[] ammoTiles = new AmmoTileImage[]{};
        private Weapon[] weapons = new Weapon[]{};
        private PowerUpImage[] powerUps = new PowerUpImage[]{};

        /**
         * Returns this builder with the given value set as the number of kills
         * needed to enter the endgame.
         *
         * @param val the number of kills needed to finish the game
         * @return this builder
         */
        public Builder nKills(int val) {
            nKills = val;
            return this;
        }

        public Builder killPoints(int[] val) {
            killPoints = val;
            return this;
        }

        public Builder frenzyPoints(int[] val) {
            frenzyPoints = val;
            return this;
        }

        public Builder frenzyOn(boolean val) {
            frenzyOn = val;
            return this;
        }

        /**
         * Returns this builder with the given value set as the amount of
         * damages needed to kill a figure.
         *
         * @param val the amount of damages needed to kill a figure
         * @return this builder
         */
        public Builder killDamages(int val) {
            killDamages = val;
            return this;
        }

        /**
         * Returns this builder with the given value set as the maximum amount
         * of damages a figure can take.
         *
         * @param val the maximum amount of damages a figure can take
         * @return this builder
         */
        public Builder maxDamages(int val) {
            maxDamages = val;
            return this;
        }


        /**
         * Returns this builder with the given value set as the maximum amount
         * of marks a figure can take from a single other figure.
         *
         * @param val the maximum amount of marks a figure can take from a
         *            single other figure
         * @return this builder
         */
        public Builder maxMarks(int val) {
            maxMarks = val;
            return this;
        }

        /**
         * Returns this builder with the given value set as the maximum amount
         * of ammo for a single color a figure can hold.
         *
         * @param val the maximum amount of ammo for a single color a figure
         *            can hold
         * @return this builder
         */
        public Builder maxAmmo(int val) {
            maxAmmo = val;
            return this;
        }

        /**
         * Returns this builder with the given value set as the maximum number
         * of weapons a figure can hold.
         *
         * @param val the maximum number of weapons a figure can hold
         * @return this builder
         */
        public Builder maxWeapons(int val) {
            maxWeapons = val;
            return this;
        }

        /**
         * Returns this builder with the given value set as the maximum number
         * of powerups a figure can hold.
         *
         * @param val the maximum number of powerups a figure can hold
         * @return this builder
         */
        public Builder maxPowerUps(int val) {
            maxPowerUps = val;
            return this;
        }

        /**
         * Returns this builder with the given value set as the starting
         * amount of ammo for every figure.
         *
         * @param val the starting amount of ammo for every figure.
         * @return this builder
         */
        public Builder defaultAmmo(AmmoCube val) {
            defaultAmmo = val;
            return this;
        }

        /**
         * Returns this builder with the given value set as the maximum number
         * of weapons a spawnpoint can contain.
         *
         * @param val the maximum number of weapons a spawnpoint can contain
         * @return this builder
         */
        public Builder spawnCapacity(int val) {
            boardBuilder.spawnCapacity(val);
            return this;
        }

        /**
         * Returns this builder with the specified <code>SquareImages</code>
         * set as its <code>Board</code> builder supplier.
         *
         * @param val the images to set as a supplier
         * @return this builder
         */
        public Builder squares(SquareImage... val) {
            return squares(() -> val);
        }

        /**
         * Returns this builder with the specified <code>SquareImage</code> set
         * supplier set for its <code>Board</code> builder.
         *
         * @param supplier the supplier to be set
         * @return this builder
         */
        public Builder squares(Supplier<SquareImage[]> supplier) {
            boardBuilder.squares(supplier);
            return this;
        }

        /**
         * Returns this builder with the specified <code>AmmoTileImage</code>s
         * set as basis for the game's <code>AmmoTile</code> deck
         *
         * @param arr the <code>AmmoTileImage</code>s to use as basis for the
         *            <code>AmmoTile</code> deck
         * @return this builder
         */
        public Builder ammoTiles(AmmoTileImage... arr) {
            ammoTiles = arr;
            return this;
        }

        /**
         * Returns this builder with the specified <code>Weapon</code>s
         * set as basis for the game's weapon deck
         *
         * @param arr the <code>Weapon</code>s to use as basis for the
         *            weapon deck
         * @return this builder
         */
        public Builder weapons(Weapon... arr) {
            weapons = arr;
            return this;
        }

        /**
         * Returns this builder with the specified <code>PowerUpImage</code>s
         * set as basis for the game's <code>PowerUp</code> deck
         *
         * @param arr the <code>PowerUpImage</code>s to use as basis for the
         *            <code>PowerUp</code> deck
         * @return this builder
         */
        public Builder powerUps(PowerUpImage... arr) {
            powerUps = arr;
            return this;
        }

        /**
         * Returns this builder with the specified player added to the
         * player list.
         *
         * @param player the player to add to the player list
         * @return this builder
         */
        public Builder addPlayer(Player player) {
            players.add(player);
            return this;
        }

        /**
         * Returns this builder with the specified player removed from the
         * player list
         *
         * @param player the player to remove from the player list.
         * @return this builder
         */
        public Builder removePlayer(Player player) {
            players.remove(player);
            return this;
        }

        public Builder mapType(int mapType) {
            this.mapType = mapType;
            return this;
        }

        public Builder turnTimeout(int turnTimeout) {
            this.turnTimeout = turnTimeout;
            return this;
        }

        public Builder otherTimeout(int otherTimeout) {
            this.otherTimeout = otherTimeout;
            return this;
        }

        public List<Player> getJoinedPlayers() {
            return players;
        }

        /**
         * Returns an instance of <code>Game</code> created from the fields
         * set on this builder.
         *
         * @return the game instantiated
         */
        public Game build() {
            for (Player p : players) {
                Figure figure = new Figure(killDamages, maxDamages, maxMarks, maxAmmo, maxWeapons, maxPowerUps);
                figure.addAmmo(defaultAmmo);
                p.setFigure(figure);
                boardBuilder.figures(figure);
            }
            return new Game(this);
        }
    }
}
