package client.view.commandLine;

import client.Client;
import client.model.*;
import client.view.View;
import tools.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CLIView implements View, Runnable {
    protected final Client controller;
    private final Parser parser;
    private final Thread thread = new Thread(this);

    public CLIView(Client controller, String cmdDelimiter, String argsDelimiter) {
        this.controller = controller;
        parser = new Parser(
                mapCommands(),
                cmdDelimiter,
                argsDelimiter
        );
    }

    @Override
    public void start() { thread.start(); }

    @SuppressWarnings("squid:S106")
    @Override
    public void print(String s) {
        System.out.println(s);
    }

    @Override
    public void run() {
        try (BufferedReader istream = new BufferedReader(new InputStreamReader(System.in))) {
            while (!Thread.currentThread().isInterrupted())
                parse(istream.readLine());
        } catch (IOException ignore) {
            // Nothing to do
        }
    }

    @Override
    public void exit() {
        thread.interrupt();
    }

    @Override
    public void displayMessage(String message) {
        print("SYSTEM: " + message);
    }

    @Override
    public void displayLobbyList(String[] lobbyList) {
        print("Updated Lobby List: ");
        Arrays.stream(lobbyList).forEach(this::print);
    }

    @Override
    public void displayPossibleRoom(int id, Square[] squares) {
        print(id + " room: " + Arrays.stream(squares)
                .map(Square::toString)
                .collect(Collectors.joining(" ")));
    }

    @Override
    public void displayPossibleFigure(int id, Player player) {
        print(id + " figure: " + player.toString());
    }

    @Override
    public void displayPossibleSquare(int id, Square square) {
        print(id + " square: " + square.toString());
    }

    @Override
    public void displayMinToSelect(int min) {
        print("Select a minimum of " + min + " targets");
    }

    @Override
    public void displayMaxToSelect(int max) {
        print("Select a maximum of " + max + " targets");
    }

    @Override
    public void displayPowerUps(PowerUp[] powerUps) {
        print("Owned PowerUps: " + Arrays.stream(powerUps)
                .map(PowerUp::toString)
                .collect(Collectors.joining(", ")));
    }

    @Override
    public void displayPossibleActions(int[] actions) {
        print("Possible actions: " + Arrays.stream(actions)
                .mapToObj(x -> Map.ofEntries(Map.entry(0, "grab"),
                        Map.entry(1, "shoot"),
                        Map.entry(2, "move"),
                        Map.entry(-1, "none")).get(x))
                .collect(Collectors.joining(", ")));
    }

    @Override
    public void displayCurrentPlayer(int currPlayer) {
        print("Current player is: " + currPlayer);
    }

    @Override
    public void displayRemainingActions(int remaining) {
        print("Actions left: " + remaining);
    }

    @Override
    public void displayMapType(int mapID) {
        print("Using map: " + mapID);
    }

    @Override
    public void displayMaxKills(int max) {
        print("Max kills: " + max);
    }

    @Override
    public void displayKillTrack(int[] killTrack, boolean[] overkills) {
        print("KillTrack: " + IntStream.range(0, killTrack.length)
                .mapToObj(x -> killTrack[x] + (overkills[x] ? "x2" : ""))
                .collect(Collectors.joining(", ")));
    }

    @Override
    public void displayPlayers(Player[] players) {
        print("Players: " + Arrays.stream(players)
                .map(Player::toString)
                .collect(Collectors.joining(", ")));
    }

    @Override
    public void displaySquares(Square[] squares) {
        print("Squares: " + Arrays.stream(squares)
                .map(Square::toString)
                .collect(Collectors.joining(", ")));
    }

    @Override
    public void displayPlayerDamage(Player player) {
        print("Player damages of " + player.toString() + " [" + Arrays.stream(player.getDamages())
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", ")) + "] ");
    }

    @Override
    public void displayPlayerMarks(Player player) {
        print("Player marks of " + player.toString() + " [" + Arrays.stream(player.getMarks())
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", ")) + "] ");
    }

    @Override
    public void displayPlayerPoints(Player player) {
        print(player.toString() + " has " + player.getPoints() + " points");
    }

    @Override
    public void displayPlayerDeaths(Player player) {
        print(player.toString() + " has " + player.getDeaths() + " deaths");
    }

    @Override
    public void displayPlayerWeapons(Player player) {
        print(player.toString() + " has these weapons: " + Arrays.stream(player.getWeapons())
                .map(Weapon::toString)
                .collect(Collectors.joining(", ")));
    }

    @Override
    public void displayPlayerAmmo(Player player) {
        print(player.toString() + " has these ammo: ["
                + Arrays.stream(player.getAmmo())
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", ")) + "]");
    }

    @Override
    public void displayPlayerNPowerUps(Player player) {
        print(player.toString() + " has " + player.getnPowerup() + " powerups");
    }

    @Override
    public void displayPlayerLocation(Player player) {
        print(player.toString() + " is at (" + player.getCoordinates()[0] + "," + player.getCoordinates()[1] + ")");
    }

    @Override
    public void displaySquareContent(Square square) {
        if (square.isSpawn())
            print("Spawn square at (" + Arrays.stream(square.getCoordinates())
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining(",")) + ") contains weapons: ["
                    + Arrays.stream(square.getWeapons())
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining(", ")) + "]"
            );
        else
            print("Normal square at ("
                    + Arrays.stream(square.getCoordinates())
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining(", "))
                    + ") contains ammotile: " + (square.getTileId() == -1 ? "no ammotile" : square.getTileId())
            );
    }

    @Override
    public void displayGameState(GameState value) {
        print("Game state: " + value.toString());
    }

    @Override
    public void displayRemainingTime(int v) {
        print("Remaining Time: " + v);
    }

    @Override
    public void displayChat(List<String[]> chat) {
        print(">> " + String.join(" : ", chat.get(chat.size() - 1)));
    }

    @Override
    public void displayLeaderBoard(Player player) {
        print(player.getName() + " is in position: " + player.getLeaderBoard());
    }

    @Override
    public void displayNKills(Player player) {
        print(player.getName() + " has " + player.getNKills() + " kills");
    }

    private void parse(String line) {
        try {
            parser.parse(line);
        } catch (CommandExitException e) {
            controller.exit();
        } catch (CommandException e) {
            print(e.toString());
        }
    }

    protected void help(String[] args) throws CommandNotFoundException {
        if (args.length == 0 || args[0].isEmpty())
            print(parser.help());
        else
            print(parser.help(args[0]));
    }

    protected Map<String, Command> mapCommands() {
        return Map.of("help", this::help);
    }
}
