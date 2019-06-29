package client.view;

import client.Client;
import client.model.Player;
import client.model.PowerUp;
import client.model.Square;
import tools.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
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
    public void start() {
        thread.start();
    }

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
                .mapToObj(Integer::toString)
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


    private void parse(String line) {
        try {
            parser.parse(line);
        } catch (CommandExitException e) {
            print(e.toString());
            exit();
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
