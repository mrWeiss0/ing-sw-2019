package model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    // Killshot Track
    private List<Player> killCount; // Kills and overkills done by players
    private int remainingKills;     // Kills to finish game

    private List<Player> players;
    private int currPlayer = -1;

    private Deck<AmmoTile> ammoTileDeck;
    private Deck<Weapon> weaponDeck;
    private Deck<PowerUp> powerUpDeck;

    private Board board;

    public Game() {
        this(0);
    }

    public Game(int nKills) {
        remainingKills = nKills;
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) { // TODO throw Exception if game already started
        players.remove(player);
    }

    public Player nextPlayer() { // TODO throw Exception if game not yet started
        currPlayer++;
        currPlayer %= players.size();
        return currentPlayer();
    }

    public Player currentPlayer() {
        return players.get(currPlayer);
    }

}
