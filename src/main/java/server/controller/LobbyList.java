package server.controller;

import server.connection.VirtualClient;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.function.Predicate;

public class LobbyList {
    private final Map<String, Player> players = new HashMap<>();
    private final Timer gameStartTimer = new Timer(true);
    private final Map<String, LobbyEntry> lobbyMap = new HashMap<>();

    public void registerPlayer(String username, VirtualClient client) {
        if(players.values().stream().map(Player::getClient).anyMatch(Predicate.isEqual(client))) {
            client.send("You are already logged in");
            return;
        }
        Player player = players.get(username);
        if (player == null) {
            player = new Player(username);
            players.put(username, player);
        } else if (player.isOnline()) {
            client.send("Username " + username + " already taken");
            return;
        } else player.setOnline();
        client.send("Registered as " + username);
        client.setPlayer(player);
    }

    public void join(Player player, String name) {
        if(player==null)
            throw new IllegalStateException("Not logged in");
        if(!lobbyMap.containsKey(name))
            throw new NoSuchElementException("No game with name: " + name);
        lobbyMap.values().forEach(x->x.remove(player));
        lobbyMap.get(name).join(player);
        player.getClient().send("Joined lobby with name: " + name);
    }

    public void remove(Player player, String name) {
        if(player==null)
            throw new IllegalStateException("Not logged in");
        if (!lobbyMap.containsKey(name))
            throw new NoSuchElementException("No game with name: " + name);
        if(!lobbyMap.get(name).isPresent(player))
            throw new IllegalStateException("You are not in that lobby");
        lobbyMap.get(name).remove(player);
        player.getClient().send("Exit from lobby " + name);
    }

    public void create(String name) {
        String trimmed=name.trim();
        if (lobbyMap.containsKey(trimmed))
            throw new IllegalStateException("Name already present");
        if(trimmed.isEmpty())
            throw new IllegalStateException("Name not valid");
        lobbyMap.put(trimmed, new LobbyEntry(3, 5, 10, gameStartTimer));
        players.values().forEach(x->x.getClient().send("Created lobby " + trimmed));
    }
}
