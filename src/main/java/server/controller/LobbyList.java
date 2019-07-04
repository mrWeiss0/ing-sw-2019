package server.controller;

import client.model.GameState;
import server.Config;
import server.connection.VirtualClient;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LobbyList {
    private final Map<String, Player> players = new HashMap<>();
    private final Timer gameStartTimer = new Timer(true);
    private final Map<String, LobbyEntry> lobbyMap = new HashMap<>();
    private final Config config;

    public LobbyList(Config config) {
        this.config = config;
    }

    public void registerPlayer(String username, VirtualClient client) {
        if (players.values().stream().map(Player::getClient).anyMatch(Predicate.isEqual(client))) {
            client.sendMessage("You are already logged in");
            return;
        }
        Player player = players.get(username);
        if (player == null) {
            player = new Player(username);
            players.put(username, player);
            client.sendMessage("Registered as " + username);
            client.setPlayer(player);
            player.sendGameState(GameState.CHOOSING_LOBBY.ordinal());
            player.sendLobbyList(repr());
        } else if (player.isOnline()) {
            client.sendMessage("Username " + username + " already taken");
        } else {
            client.setPlayer(player);
            player.setOnline();
        }
    }

    public void join(Player player, String name) {
        if (player == null)
            throw new IllegalStateException("Not logged in");
        if (!lobbyMap.containsKey(name))
            throw new NoSuchElementException("No game with name: " + name);
        lobbyMap.values().forEach(x -> x.remove(player));
        lobbyMap.get(name).join(player);
        player.sendMessage("Joined lobby with name: " + name);
        player.sendGameState(GameState.NOT_STARTED.ordinal());
    }

    public void remove(Player player) {
        if (player == null)
            throw new IllegalStateException("Not logged in");
        if (lobbyMap.values().stream().noneMatch(x->x.isPresent(player)))
            throw new NoSuchElementException("You are not in a lobby ");
        lobbyMap.values().stream().filter(x->x.isPresent(player)).forEach(x->x.remove(player));
        player.sendMessage("Exit from lobby ");
        player.sendGameState(GameState.CHOOSING_LOBBY.ordinal());
    }

    public void create(String name) throws FileNotFoundException {
        String trimmed = name.trim();
        if (lobbyMap.containsKey(trimmed))
            throw new IllegalStateException("Name already present");
        if (trimmed.isEmpty())
            throw new IllegalStateException("Name not valid");
        lobbyMap.put(trimmed, new LobbyEntry(config, gameStartTimer));
        players.values().forEach(x -> x.sendMessage("Created lobby " + trimmed));
        players.values().forEach(x -> x.sendLobbyList(repr()));
    }

    public void chatMessage(VirtualClient v, String msg) {
        if (!players.values().contains(v.getPlayer()))
            throw new IllegalStateException("Please choose an username first");
        players.values().forEach(x -> x.sendChatMessage(v.getPlayer().getName(), msg));
    }

    private String[] repr() {
        return lobbyMap.keySet().stream()
                .map(y -> y + ":" + lobbyMap.get(y).getOccupancy())
                .collect(Collectors.toList())
                .toArray(String[]::new);
    }
}
