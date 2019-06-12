package server.controller;

import server.connection.VirtualClient;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Timer;

public class LobbyList {
    private final Map<String, Player> players = new HashMap<>();
    private final Timer gameStartTimer = new Timer(true);
    private final Map<String, LobbyEntry> lobbyList = new HashMap<>();

    public void registerPlayer(String username, VirtualClient client) {
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
        if (!lobbyList.containsKey(name))
            throw new NoSuchElementException("No game with named " + name);
        lobbyList.get(name).join(player);
        player.getClient().send("Joined lobby " + name);
    }

    public void remove(Player player, String name) {
        if (!lobbyList.containsKey(name))
            throw new NoSuchElementException("No game with named " + name);
        lobbyList.get(name).remove(player);
        player.getClient().send("Deleted lobby " + name);
    }

    public void create(String name) {
        if (lobbyList.containsKey(name))
            throw new IllegalStateException("Name already present");
        lobbyList.put(name, new LobbyEntry(3, 5, 10, gameStartTimer));
        players.values().forEach(x->x.getClient().send("Created lobby " + name));
    }
}
