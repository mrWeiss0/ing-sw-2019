package server.controller;

import client.model.GameState;
import server.connection.VirtualClient;
import server.model.PowerUp;
import server.model.board.AbstractSquare;
import server.model.board.Board;
import server.model.board.Figure;
import server.model.board.Targettable;
import server.model.weapon.FireMode;
import server.model.weapon.Weapon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Player {
    private static final String NOT_STARTED_MESSAGE = "The game is not started yet";
    private static final String INVALID_ARGUMENT_MESSAGE = "You have chosen an invalid index";
    private final String name;
    private VirtualClient client;
    private Figure figure;
    private GameController game;
    private boolean active = true;
    private boolean online = true;
    private List<Action> actions;

    public Player(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public VirtualClient getClient() {
        return client;
    }

    public void setClient(VirtualClient client) {
        this.client = client;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
        figure.setPlayer(this);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive() {
        active = true;
    }

    public void setInactive() {
        active = false;
    }

    public boolean isOnline() {
        online=client.ping();
        return online;
    }

    public void setOnline() {
        online = true;
        updateAll();
    }

    public void setOffline() {
        online = false;
    }

    public GameController getGame() {
        return game;
    }

    public void setGame(GameController game) {
        this.game = game;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void selectPowerUp(int[] index) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if (Arrays.stream(index).anyMatch(x -> x < 0 || x >= figure.getPowerUps().size())) {
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectPowerUpEvent(
                this,
                Arrays.stream(index)
                        .mapToObj(x -> figure.getPowerUps().get(x))
                        .distinct()
                        .toArray(PowerUp[]::new)
        ));
    }

    public void selectWeaponToReload(int[] index) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if (Arrays.stream(index).anyMatch(x -> x < 0 || x >= figure.getWeapons().size())) {
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectWeaponToReloadEvent(
                this,
                Arrays.stream(index)
                        .mapToObj(x -> figure.getWeapons().get(x))
                        .distinct()
                        .toArray(Weapon[]::new)
        ));
    }

    public void selectWeaponFireMode(int index, int[] fm) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if (index < 0 || index >= figure.getWeapons().size()
                || Arrays.stream(fm)
                .anyMatch(x -> x < 0 || x >= figure.getWeapons().get(index).getFireModes().size())) {
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectWeaponFireModeEvent(
                this,
                figure.getWeapons().get(index),
                Arrays.stream(fm)
                        .mapToObj(x -> figure.getWeapons().get(index).getFireModes().get(x))
                        .distinct()
                        .toArray(FireMode[]::new)
        ));
    }

    public void selectGrabbable(int index) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if(figure.getLocation()==null){
            client.sendMessage("You are not on a square");
            return;
        }
        if (index < 0 || index >= figure.getLocation().peek().size()) {
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectGrabbableEvent(
                this,
                figure.getLocation().peek().get(index)
        ));
    }

    public void selectTargettable(int[] index) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        try {
            game.enqueue(new SelectTargettableEvent(
                    this,
                    Arrays.stream(index)
                            .mapToObj(game.getGame().getBoard()::resolveID)
                            .distinct()
                            .toArray(Targettable[]::new)
            ));
        } catch (IndexOutOfBoundsException e) {
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
        }
    }

    public void selectColor(int color) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if (color < 0 || color >= 3) {
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectColorEvent(
                this,
                color
        ));
    }

    public void selectAction(int index) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if(actions==null){
            client.sendMessage("You have no action to choose from");
            return;
        }
        if (index <0 || index >= actions.size()) {
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        game.enqueue(new SelectActionEvent(
                this,
                actions.get(index)
        ));
    }

    public void updateAll(){
        sendGameState(GameState.ENEMY_TURN.ordinal());
        game.getGame().getPlayers().forEach(this::sendPlayerAmmo);
        game.getGame().getPlayers().forEach(this::sendPlayerDamages);
        game.getGame().getPlayers().forEach(this::sendPlayerMarks);
        game.getGame().getPlayers().forEach(this::sendPlayerDeaths);
        game.getGame().getPlayers().forEach(this::sendPlayerPoints);
        game.getGame().getPlayers().forEach(this::sendPlayerNPowerUps);
        game.getGame().getPlayers().forEach(this::sendPlayerLocation);
        game.getGame().getPlayers().forEach(this::sendPlayerWeapons);
        sendGameParams(Arrays.asList(game.getGame().getMapType(), game.getGame().getMaxKills()));
        sendKillTrack(game.getGame().getKillCount(),game.getGame().getOverkills());
        sendPlayers(game.getGame().getPlayers());
        sendPowerUps(figure.getPowerUps());
        sendPlayerID(game.getGame().getPlayers().indexOf(this));
        sendPossibleActions(-1);
        sendSquares(game.getGame().getBoard().getSquares());
        game.getGame().getBoard().getSquares().forEach(this::sendSquareContent);
        sendRemainingActions(0);
        sendTargets(0,0, Collections.emptyList(),game.getGame().getBoard());
    }

    public void sendMessage(String s){
        if(!isOnline()) return;
        client.sendMessage(s);
    }

    public void sendLobbyList(String[] s){
        if(!isOnline()) return;
        client.sendLobbyList(s);
    }

    public void sendChatMessage(String name, String msg){
        if(!isOnline()) return;
        client.sendChatMessage(name, msg);
    }

    public void sendTargets(int min, int max, List<Targettable> targets, Board board){
        if(!isOnline()) return;
        client.sendTargets(min, max, targets, board);
    }

    public void sendPowerUps(List<PowerUp> powerUps){
        if(!isOnline()) return;
        client.sendPowerUps(powerUps);
    }

    public void sendCurrentPlayer(int currentPlayer){
        if(!isOnline()) return;
        client.sendCurrentPlayer(currentPlayer);
    }

    public void sendPossibleActions(int actionSetID){
        if(!isOnline()) return;
        client.sendPossibleActions(actionSetID);
    }

    //0:map_type, 1:max_kills
    public void sendGameParams(List<Integer> gameParams){
        if(!isOnline()) return;
        client.sendGameParams(gameParams);
    }

    public void sendKillTrack(List<Figure> killTrack, List<Boolean> overkills){
        if(!isOnline()) return;
        client.sendKillTrack(killTrack, overkills);
    }

    public void sendSquares(List<AbstractSquare> squares){
        if(!isOnline()) return;
        client.sendSquares(squares);
    }

    public void sendSquareContent(AbstractSquare square){
        if(!isOnline()) return;
        client.sendSquareContent(square);
    }

    public void sendPlayers(List<Player> players){
        if(!isOnline()) return;
        client.sendPlayers(players);
    }

    public void sendPlayerDamages(Player player){
        if(!isOnline()) return;
        client.sendPlayerDamages(player);
    }

    public void sendPlayerMarks(Player player){
        if(!isOnline()) return;
        client.sendPlayerMarks(player);
    }

    public void sendPlayerLocation(Player player){
        if(!isOnline()) return;
        client.sendPlayerLocation(player);
    }

    public void sendPlayerPoints(Player player){
        if(!isOnline()) return;
        client.sendPlayerPoints(player);
    }

    public void sendPlayerNPowerUps(Player player){
        if(!isOnline()) return;
        client.sendPlayerNPowerUps(player);
    }

    public void sendPlayerDeaths(Player player){
        if(!isOnline()) return;
        client.sendPlayerDeaths(player);
    }

    public void sendPlayerAmmo(Player player){
        if(!isOnline()) return;
        client.sendPlayerAmmo(player);
    }

    public void sendPlayerWeapons(Player player){
        if(!isOnline()) return;
        client.sendPlayerWeapons(player);
    }

    public void sendRemainingActions(int remaining){
        if(!isOnline()) return;
        client.sendRemainingActions(remaining);
    }

    public void sendGameState(int value){
        if(!isOnline()) return;
        client.sendGameState(value);
    }

    public void sendCountDown(int value){
        if(!isOnline()) return;
        client.sendCountDown(value);
    }

    public void sendPlayerID(int id){
        if(!isOnline()) return;
        client.sendPlayerID(id);
    }
}
