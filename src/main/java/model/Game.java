package model;

import model.weapon.Weapon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
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
    private List<Figure> killCount = new ArrayList<>(); // Kills and overkills done by players
    private int remainingKills;     // Kills to finish game

    private List<Player> players = new ArrayList<>();
    private List<Figure> figures = new ArrayList<>();
    private int currPlayer = -1;

    private Deck<AmmoTile> ammoTileDeck;
    private Deck<Weapon> weaponDeck;
    private Deck<PowerUp> powerUpDeck;

    private Board board;

    /**
     * Constructor used for testing purposes
     */
    public Game() {
        this(new ArrayList<>(), new ArrayList<>(), 0);
    }

    public Game(Collection<Weapon> weapons, Collection<AmmoTile> ammoTiles) {
        this(weapons, ammoTiles, 0);
    }

    /**
     * Constructor that initializes remaining kills with given value,
     * determining game length.
     *
     * @param nKills the number of kills needed to reach the end game or
     *               the frenzy turn
     */
    public Game(Collection<Weapon> weapons, Collection<AmmoTile> ammoTiles, int nKills) {
        remainingKills = nKills;
        weaponDeck = new Deck<>(weapons);
        ammoTileDeck = new Deck<>(ammoTiles);
    }

    /**
     * Adds a specified player as the last to the player list. This should
     * be done only before the beginning of a game.
     *
     * @param player the player to be added
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes the specified player from the player list. This should
     * be done only at the end of a game.
     *
     * @param player the player to be removed
     */
    public void removePlayer(Player player) {
        players.remove(player);
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

    public List<Figure> getFigures() {
        return figures.stream().filter(f -> f.getSquare() != null).collect(Collectors.toList());
    }

    public void fillSquare(AmmoSquare square) {
        square.refill(ammoTileDeck.draw());
    }

    public void fillSquare(SpawnSquare square) {
        try {
            square.refill(weaponDeck.draw());
        } catch (NoSuchElementException ignore) {
            // If deck is empty do nothing
        }
    }
}
