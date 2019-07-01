package server.controller;

import server.Config;
import server.Main;
import server.model.Game;
import server.model.weapon.Weapon;
import server.model.weapon.Weapons;
import tools.FileParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.function.Predicate.not;

public class LobbyEntry {
    private final Config config;
    private final Timer timer;
    private final Game.Builder builder = new Game.Builder();
    private GameController controller;
    private boolean joinable = true;
    private TimerTask countdown;

    LobbyEntry(Config config, Timer timer) {
        this.config=config;
        if (config.MIN_PLAYERS > config.MAX_PLAYERS)
            throw new IllegalArgumentException("min players > max players");
        if (config.TIMEOUT_START < 0)
            throw new IllegalArgumentException("Negative timeout");
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
        joinable = count < config.MAX_PLAYERS;
        if (count < config.MIN_PLAYERS)
            resetCountdown();
        else if (countdown == null)
            setCountdown();

    }
    //TODO CREATE BOARD FROM FILE
    private void start() {
        resetCountdown();
        try {
            controller = new GameController(builder
                    .frenzyOn(config.FRENZY_ON)
                    .nKills(config.N_KILLS)
                    .killDamages(config.KILL_DAMAGE)
                    .maxDamages(config.MAX_DAMAGE)
                    .maxMarks(config.MAX_MARKS)
                    .maxAmmo(config.MAX_AMMO)
                    .maxWeapons(config.MAX_WEAPONS)
                    .maxPowerUps(config.MAX_POWERUPS)
                    .killPoints(config.KILL_POINTS)
                    .frenzyPoints(config.FRENZY_POINTS)
                    .weapons(Arrays.stream(Weapons.values()).map(Weapons::build).toArray(Weapon[]::new))
                    .ammoTiles(FileParser.readAmmoTiles(new FileReader("src/main/resources/" + config.AMMO_TILE_FILE)))
                    .powerUps(FileParser.readPowerUps(new FileReader("src/main/resources/" + config.POWER_UP_FILE)))
                    .squares(FileParser.readSquares(new FileReader("src/main/resources/" + config.MAP_FILE)))
                    .build());
        }catch (FileNotFoundException e){
            Main.LOGGER.severe(e.getMessage());
        }
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
            private int c = config.TIMEOUT_START;

            @Override
            public void run() {
                checkPlayerCount();
                if (--c <= 0 && countdown != null)
                    start();
                else if(c<=5) builder.getJoinedPlayers().forEach(x->x.getClient().sendCountDown(c));
            }
        };
        timer.schedule(countdown, 0, 1000);
    }

    private void resetCountdown() {
        if (countdown != null)
            countdown.cancel();
        countdown = null;
    }

    public String getOccupancy() {
        return builder.getJoinedPlayers().size() + "/" + config.MAX_PLAYERS;
    }
}
