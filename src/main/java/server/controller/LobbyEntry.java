package server.controller;

import server.model.Game;

import java.util.Timer;
import java.util.TimerTask;

import static java.util.function.Predicate.not;

public class LobbyEntry {
    private final int minPlayers;
    private final int maxPlayers;
    private final int timeout;
    private final Timer timer;
    private final Game.Builder builder = new Game.Builder();
    private GameController controller;
    private boolean joinable = true;
    private TimerTask countdown;

    LobbyEntry(int minPlayers, int maxPlayers, int timeout, Timer timer) {
        if (minPlayers > maxPlayers)
            throw new IllegalArgumentException("min players > max players");
        if (timeout < 0)
            throw new IllegalArgumentException("Negative timeout");
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.timeout = timeout;
        this.timer = timer;
    }

    private void checkPlayerCount() {
        builder
                .getJoinedPlayers()
                .stream()
                .filter(not(Player::isOnline))
                .forEach(builder::removePlayer);
        long count = builder
                .getJoinedPlayers()
                .size();
        joinable = count < maxPlayers;
        if (count < minPlayers)
            resetCountdown();
        else if (countdown == null)
            setCountdown();

    }

    private void start() {
        resetCountdown();
        controller = new GameController(builder.build());
    }

    public void join(Player player) {
        if (!joinable)
            throw new IllegalStateException("You can't join this game");
        builder.addPlayer(player);
        checkPlayerCount();
    }

    public void remove(Player player) {
        builder.removePlayer(player);
        checkPlayerCount();
    }

    public boolean isPresent(Player player) {
        return builder.getJoinedPlayers().contains(player);
    }


    private void setCountdown() {
        countdown = new TimerTask() {
            private int c = timeout;

            @Override
            public void run() {
                checkPlayerCount();
                if (--c <= 0 && countdown != null)
                    start();
            }
        };
        timer.schedule(countdown, 0, 1000);
    }

    private void resetCountdown() {
        if (countdown != null)
            countdown.cancel();
        countdown = null;
    }
}
