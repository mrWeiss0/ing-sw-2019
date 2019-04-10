package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <code>Game</code> is a class used for containing all of the major
 * elements of a game.
 * It also keeps track of <code>Player</code>s and their order, as well as containing methods to
 * manage them.
 */
public class Game {
    // Killshot Track
    private List<Figure> killCount; // Kills and overkills done by players
    private int remainingKills;     // Kills to finish game

    private List<Player> players;
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

    /**
     * Constructor that initializes remaining kills, used for determine the game
     * length, and readies the game to accept players.
     *
     * @param nKills the number of kills needed to reach the end game or
     *               the frenzy turn.
     */
    public Game(Collection<Weapon> weapons, Collection<AmmoTile> ammoTiles, int nKills) {
        killCount = new ArrayList<>();
        remainingKills = nKills;
        players = new ArrayList<>();
        weaponDeck = new Deck<>(weapons);
        ammoTileDeck = new Deck<>(ammoTiles);
    }

    public Game(Collection<Weapon> weapons, Collection<AmmoTile> ammoTiles) {
        this(weapons, ammoTiles, 0);
    }

    /**
     * Adds a passed <code>Player</code> as the last to the player list.
     *
     * @param player the player to be added
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes the passed <code>Player</code> from the player list. This
     * should be only done at the end of a game.
     *
     * @param player the player to be removed
     */
    public void removePlayer(Player player) { // TODO throw Exception if game already started
        players.remove(player);
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
    public Player nextPlayer() { // TODO throw Exception if game not yet started
        currPlayer++;
        currPlayer %= players.size();
        return currentPlayer();
    }

    /**
     * Gets the player whose turn its taking place.
     *
     * @return the player whose turn its taking place.
     */
    public Player currentPlayer() {
        return players.get(currPlayer);
    }

    public void fillSquare(AmmoSquare square) {
        square.refill(ammoTileDeck.draw());
    }

    public void fillSquare(SpawnSquare square) {
        try {
            square.refill(weaponDeck.draw());
        } catch (NoSuchElementException ignore) {
        } // If deck is empty do nothing
    }
}
