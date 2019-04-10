package model;

import java.util.ArrayList;
import java.util.List;

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
        this(0);
    }

    /**
     * Constructor that initializes remaining kills with given value,
     * determining game length.
     *
     * @param nKills the number of kills needed to reach the end game or
     *               the frenzy turn
     */
    public Game(int nKills) {
        killCount = new ArrayList<>();
        remainingKills = nKills;
        players = new ArrayList<>();
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
     * Returns the player whose turn its taking place.
     *
     * @return the player whose turn its taking place
     */
    public Player currentPlayer() {
        return players.get(currPlayer);
    }

}
