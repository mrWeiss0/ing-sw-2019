package model;

import model.board.*;
import model.weapon.Weapon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    private final Board board;
    private int remainingKills; // Kills to finish game
    private int currPlayer = -1;

    private Game(Builder builder) {
        remainingKills = builder.nKills;
        board = builder.boardBuilder.build();
        players = Collections.unmodifiableList(builder.players);

        weaponDeck.discard(Arrays.asList(builder.weapons));
        powerUpDeck.discard(Arrays.stream(builder.powerUps)
                .map(powerup -> {
                    int[] ammoVal = new int[powerup.color + 1];
                    ammoVal[powerup.color] = 1;
                    return new PowerUp(new AmmoCube(ammoVal), builder.boardBuilder.getSpawnByColor(powerup.color));
                })
                .collect(Collectors.toList()));
        ammoTileDeck.discard(Arrays.stream(builder.ammoTiles)
                .map(tile -> new AmmoTile(new AmmoCube(tile.ammo), tile.powerUp ? powerUpDeck::draw : () -> null, ammoTileDeck::discard))
                .collect(Collectors.toList()));
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

    public List<Player> getPlayers() {
        return players;
    }

    public void fillSquare(AmmoSquare square) {
        square.refill(ammoTileDeck.draw());
    }

    public void fillSquare(SpawnSquare square) {
        square.refill(weaponDeck.draw());
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
        private AmmoCube defaultAmmo = new AmmoCube();
        private AmmoTileImage[] ammoTiles = new AmmoTileImage[]{};
        private Weapon[] weapons = new Weapon[]{};
        private PowerUpImage[] powerUps = new PowerUpImage[]{};

        public Builder nKills(int val) {
            nKills = val;
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
                Figure figure = new Figure(maxDamages, maxMarks, maxAmmo, maxWeapons, maxPowerUps);
                figure.addAmmo(defaultAmmo);
                p.setFigure(figure);
                boardBuilder.figures(figure);
            }
            return new Game(this);
        }
    }
}
