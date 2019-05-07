package model;

import model.board.*;
import model.weapon.Weapon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <code>Game</code> is a class containing all of the major elements of a game
 * such as the <code>Board</code>, card <code>Deck</code>s and
 * <code>Figure</code>s, as well as the players.
 * <p>
 * It keeps track of <code>Player</code>s and their order, as well
 * as provides methods to manage them.
 */
public class Game {
    private final int maxDamages;
    private final int maxMarks;
    private final AmmoCube defaultAmmo;
    private final int maxAmmo;
    // Killshot Track
    private List<Figure> killCount = new ArrayList<>(); // Kills and overkills done by players
    private int remainingKills; // Kills to finish game
    private List<Figure> players = new ArrayList<>();
    private int currPlayer = -1;

    private Deck<AmmoTile> ammoTileDeck;
    private Deck<Weapon> weaponDeck;
    private Deck<PowerUp> powerUpDeck;

    private Board board;

    /**
     * Constructor that initializes remaining kills with given value,
     * determining game length.
     *
     * @param nKills the number of kills needed to reach the end game or
     *               the frenzy turn
     */
    public Game(int nKills, int maxDamages, int maxMarks, AmmoCube defaultAmmo, int maxAmmo) {
        remainingKills = nKills;
        this.maxDamages = maxDamages;
        this.maxMarks = maxMarks;
        this.defaultAmmo = defaultAmmo;
        this.maxAmmo = maxAmmo;
    }

    public Game setAmmoTiles(Collection<AmmoTile> c) {
        ammoTileDeck = new Deck<>(c);
        return this;
    }

    public Game setWeapons(Collection<Weapon> c) {
        weaponDeck = new Deck<>(c);
        return this;
    }

    public Game setPowerUps(Collection<PowerUp> c) {
        powerUpDeck = new Deck<>(c);
        return this;
    }

    public Game setBoard(Board b) {
        board = b;
        return this;
    }

    public Figure newPlayer() {
        Figure player = new Figure(maxDamages, maxMarks, defaultAmmo);
        players.add(player);
        board.getFigures().add(player);
        return player;
    }

    /**
     * Removes the specified player from the player list. This should
     * be done only at the end of a game.
     *
     * @param player the player to be removed
     */
    public void removePlayer(Figure player) {
        players.remove(player);
        board.getFigures().remove(player);
    }

    /**
     * Returns the player whose turn its taking place.
     *
     * @return the player whose turn its taking place
     */
    public Figure currentPlayer() {
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
    public Figure nextPlayer() {
        currPlayer++;
        currPlayer %= players.size();
        return currentPlayer();
    }

    public void fillBoard() {
        for (AbstractSquare current : board.getSquares())
            while (current.accept(this)) ;
    }

    public boolean fillSquare(AmmoSquare square) {
        return square.refill(ammoTileDeck.draw());
    }

    public boolean fillSquare(SpawnSquare square) {
        try {
            return square.refill(weaponDeck.draw());
        } catch (NoSuchElementException ignore) {
            return false; // If deck is empty do nothing
        }
    }
}
