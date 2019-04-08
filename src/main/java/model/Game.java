package model;

import java.util.*;

public class Game {
    private List<Player> killCount; // Kills and overkills done by players
    private int remainingKills;     // Kills to finish game
    private List<Player> players;   // TODO Circular list ?
    private Board board;

    public Game(int nKills){
        remainingKills = nKills;
        players = new ArrayList<>();
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }
}
