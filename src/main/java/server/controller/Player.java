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

import java.util.ArrayList;
import java.util.Arrays;
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
    private PowerUp spawnPowerUp;

    private GameState lastState=GameState.ENEMY_TURN;
    private int lastActions =-1;
    private int lastRemainingActions=0;
    private int lastMinTargets=0;
    private int lastMaxtargets=0;
    private List<Targettable> lastTargets=new ArrayList<>();

    public void setSpawnPowerUp(PowerUp pup){
        spawnPowerUp=pup;
    }

    public PowerUp getSpawnPowerUp(){
        return spawnPowerUp;
    }

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
        return active && isOnline();
    }

    public void setActive() {
        active = true;
    }

    public void setInactive() {
        active = false;
        signalDisconnect();
    }

    public boolean isOnline() {
        if(online && !client.ping())
            setOffline();
        return online;
    }

    public void setOnline() {
        online = true;
        active= true;
        signalReconnect();
        updateAll();
    }

    public void setOffline() {
        online = false;
        active=false;
        signalDisconnect();
    }

    private void signalDisconnect(){
        game.getGame().getPlayers().stream()
                .filter(Player::isOnline)
                .forEach(x->x.sendMessage("Player "+name+" has disconnected!"));
    }

    private void signalReconnect(){
        game.getGame().getPlayers().stream()
                .filter(Player::isOnline)
                .forEach(x->x.sendMessage("Player "+name+" has reconnected!"));
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

    public void endTurn(){
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        game.enqueue(new EndTurnEvent(this));
    }

    public void selectPowerUp(int[] index) {
        if (game == null) {
            client.sendMessage(NOT_STARTED_MESSAGE);
            return;
        }
        if(index.length==1 && index[0]==figure.getPowerUps().size() && spawnPowerUp!=null){
            game.enqueue(new SelectPowerUpEvent(this, new PowerUp[]{spawnPowerUp}));
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
            client.sendMessage("You have no actions to choose from");
            return;
        }
        if (index <0 || index >= actions.size()) {
            client.sendMessage(INVALID_ARGUMENT_MESSAGE);
            return;
        }
        if(index==1 && figure.getWeapons().stream().noneMatch(Weapon::isLoaded)){
            client.sendMessage("You have no weapons to shoot with");
            return;
        }
        game.enqueue(new SelectActionEvent(
                this,
                actions.get(index)
        ));
    }

    public void reconnect(){
        if(active){
            client.sendMessage("You are active, don't need to reconnect");
            return;
        }
        setActive();
        updateAll();
        signalReconnect();
    }

    public void updateAll(){
        sendGameState(lastState.ordinal());
        sendGameParams(Arrays.asList(game.getGame().getMapType(), game.getGame().getMaxKills()));
        sendSquares(game.getGame().getBoard().getSquares());
        sendPlayers(game.getGame().getPlayers());
        sendPlayerID(game.getGame().getPlayers().indexOf(this));
        game.getGame().getPlayers().forEach(this::sendPlayerAmmo);
        game.getGame().getPlayers().forEach(this::sendPlayerDamages);
        game.getGame().getPlayers().forEach(this::sendPlayerMarks);
        game.getGame().getPlayers().forEach(this::sendPlayerDeaths);
        game.getGame().getPlayers().forEach(this::sendPlayerPoints);
        game.getGame().getPlayers().forEach(this::sendPlayerNPowerUps);
        game.getGame().getPlayers().forEach(this::sendPlayerLocation);
        game.getGame().getPlayers().forEach(this::sendPlayerWeapons);
        sendKillTrack(game.getGame().getKillCount(),game.getGame().getOverkills());
        sendPowerUps(figure.getPowerUps());
        sendPossibleActions(lastActions);
        game.getGame().getBoard().getSquares().forEach(this::sendSquareContent);
        sendRemainingActions(lastRemainingActions);
        sendTargets(lastMinTargets,lastMaxtargets, lastTargets,game.getGame().getBoard());
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
        lastMinTargets=min;
        lastMaxtargets=max;
        lastTargets=targets;
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
        lastActions=actionSetID;
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
        lastRemainingActions=remaining;
    }

    public void sendGameState(int value){
        if(!isOnline()) return;
        client.sendGameState(value);
        lastState=GameState.values()[value];
    }

    public void sendCountDown(int value){
        if(!isOnline()) return;
        client.sendCountDown(value);
    }

    public void sendPlayerID(int id){
        if(!isOnline()) return;
        client.sendPlayerID(id);
    }

    public void sendLeaderBoard(int[] points){
        if(!isOnline()) return;
        client.sendLeaderBoard(points);
    }

    public void sendNKills(int[] nKills){
        if(!isOnline()) return;
        client.sendNKills(nKills);
    }
}
