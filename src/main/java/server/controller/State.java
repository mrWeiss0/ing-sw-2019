package server.controller;

import client.model.GameState;
import server.model.AmmoCube;
import server.model.Grabbable;
import server.model.PowerUp;
import server.model.PowerUpType;
import server.model.board.Figure;
import server.model.board.SpawnSquare;
import server.model.board.Targettable;
import server.model.weapon.FireMode;
import server.model.weapon.FireSequence;
import server.model.weapon.FireStep;
import server.model.weapon.Weapon;

import java.util.*;
import java.util.stream.Collectors;

abstract class State {
    protected GameController controller;
    private final String INVALID_MOMENT ="There's a time and place for everything, but not now.";
    protected State(GameController controller) {
        this.controller = controller;
    }

    void selectPowerUp(Player player, PowerUp[] powerUps) {
        player.sendMessage(INVALID_MOMENT);
    }

    void selectWeapon(Player player, Weapon[] weapons) {
        player.sendMessage(INVALID_MOMENT);
    }

    void selectFireMode(Player player, Weapon weapon, FireMode[] fireModes) {
        player.sendMessage(INVALID_MOMENT);
    }

    void selectGrabbable(Player player, Grabbable grabbable) {
        player.sendMessage(INVALID_MOMENT);
    }

    void selectAction(Player player, Action action) {
        player.sendMessage(INVALID_MOMENT);
    }

    void selectTargettable(Player player, Targettable[] targettables) {
        player.sendMessage(INVALID_MOMENT);
    }

    void selectColor(Player player, int color) {
        player.sendMessage(INVALID_MOMENT);
    }

    void onEnter() {
    }
}

class TurnState extends State {
    private final Player current;
    private int remainingActions;
    private TimerTask turnTimer;

    public TurnState(GameController controller) {
        super(controller);
        int actionsID;
        current = controller.getGame().nextPlayer();
        actionsID = controller.getGame().getActionsID();
        remainingActions = actionsID % 2 == 0 ? 2 : 1;
        current.sendPossibleActions(actionsID);
        current.setActions(Actions.values()[actionsID].getActionList());
    }

    @Override
    public void onEnter() {
        current.sendGameState(GameState.TURN.ordinal());
        if(!current.isActive()){
            controller.setState(new TurnState(controller));
            return;
        }
        if (turnTimer == null) {
            turnTimer = new TimerTask() {
                private int c = controller.getGame().getTurnTimeout();

                @Override
                public void run() {
                    if (--c <= 0 && turnTimer != null)
                        timeout();
                    else if (c <= 5) current.getClient().sendCountDown(c);
                }
            };
            controller.getTimer().schedule(turnTimer, 0, 1000);
        }
        if (current.getFigure().getLocation() == null) {
            controller.addState(this);
            controller.setState(new SelectSpawnState(controller, Collections.singletonList(current)));
        } else if (remainingActions-- <= 0) {
            current.sendMessage("No action left");
            turnTimer.cancel();
            controller.addState(new EndTurnState(controller, current));
            controller.setState(new SelectReloadState(controller, current));
        }
    }

    private void timeout() {
        turnTimer.cancel();
        current.setInactive();
        controller.clearStack();
        controller.setState(new EndTurnState(controller, current));
    }

    @Override
    public void selectAction(Player player, Action action) {
        controller.addState(this);
        List<State> states = action.getStates(controller, player);
        Collections.reverse(states);
        controller.addStates(states);
        controller.nextState();
    }

    @Override
    public void selectPowerUp(Player player, PowerUp[] powerUps) {
        if (player != this.current || powerUps.length < 1)
            return;
        PowerUpType type = powerUps[0].getType();
        if (Arrays.asList(PowerUpType.NEWTON, PowerUpType.TELEPORTER).contains(type)) {
            //TODO NON SCARTA POWERUP E CONSUMA AZIONE
            controller.addState(this);
            controller.setState(new FireState(controller, current, type.getStepList()));
        }
    }
}

class SelectSpawnState extends State {
    private final List<Player> current;

    public SelectSpawnState(GameController controller, List<Player> players) {
        super(controller);
        current = new ArrayList<>(players);
        current.forEach(x->x.setSpawnPowerUp(controller.getGame().drawPowerup()));
        current.forEach(x->x.sendGameState(GameState.SPAWN.ordinal()));
        current.forEach(x->{
            List<PowerUp> powerUps=new ArrayList<>(x.getFigure().getPowerUps());
            powerUps.add(x.getSpawnPowerUp());
            x.sendPowerUps(powerUps);
        });
    }

    @Override
    public void onEnter() {
        if (current.isEmpty())
            controller.nextState();
    }

    @Override
    public void selectPowerUp(Player player, PowerUp[] powerUps) {
        if (current.contains(player) && powerUps.length >= 1) {
            player.setActive();
            Figure figure = player.getFigure();
            List<PowerUp> currentPowerUps = figure.getPowerUps();
            if (currentPowerUps.remove(powerUps[0]))
                currentPowerUps.add(player.getSpawnPowerUp());
            player.setSpawnPowerUp(null);
            figure.moveTo(powerUps[0].getSpawn());
            powerUps[0].discard();
            current.remove(player);
            player.sendPowerUps(player.getFigure().getPowerUps());
            controller.setState(this);
        }
    }
}

class SelectReloadState extends State {
    private final Player current;
    private final AmmoCube total;

    public SelectReloadState(GameController controller, Player player) {
        super(controller);
        current = player;
        total = player.getFigure().getTotalAmmo();
        current.sendGameState(GameState.SELECT_RELOAD.ordinal());
    }
    @Override
    public void selectWeapon(Player player, Weapon[] weapons) {
        if (player != current)
            return;
        AmmoCube cost = Arrays.stream(weapons)
                .map(Weapon::getReloadCost)
                .reduce(AmmoCube::add)
                .orElseGet(AmmoCube::new);
        if (total.greaterEqThan(cost)) {
            controller.addState(new ReloadState(controller, weapons));
            controller.setState(new PayState(controller, current, cost));
        }else{
            current.sendMessage("You can't afford reloading those, please select another weapon or no weapons");
        }
    }
}

class ReloadState extends State {
    private final Weapon[] weapons;

    public ReloadState(GameController controller, Weapon[] weapons) {
        super(controller);
        this.weapons = weapons;
    }

    @Override
    public void onEnter() {
        for (Weapon weapon : weapons)
            weapon.load();
        controller.nextState();
    }
}

class PayState extends State {
    private final Player current;
    private final AmmoCube cost;

    public PayState(GameController controller, Player player, AmmoCube cost) {
        super(controller);
        current = player;
        this.cost = cost;
        current.sendGameState(GameState.PAY.ordinal());
        current.sendPowerUps(current.getFigure().getPowerUps());
        current.sendPlayerAmmo(current);
    }

    @Override
    public void selectPowerUp(Player player, PowerUp[] powerUps) {
        if (player != current)
            return;
        AmmoCube paying = cost.sub(Arrays.stream(powerUps)
                .map(PowerUp::getAmmo)
                .reduce(AmmoCube::add)
                .orElseGet(AmmoCube::new));

        if (current.getFigure().getAmmo().greaterEqThan(paying)) {
            current.getFigure().subAmmo(paying);
            for (PowerUp powerUp : powerUps) {
                current.getFigure().getPowerUps().remove(powerUp);
                powerUp.discard();
            }
            current.sendPowerUps(current.getFigure().getPowerUps());
            controller.getGame().getPlayers().forEach(x->x.sendPlayerNPowerUps(current));
            controller.nextState();
        }else{
            current.sendMessage("Please select some powerups because you don't have enough ammo");
        }
    }
}

class PayAnyColorState extends State {
    private final Player current;
    private final AmmoCube total;

    public PayAnyColorState(GameController controller, Player player) {
        super(controller);
        current = player;
        total = player.getFigure().getTotalAmmo();
        current.sendGameState(GameState.PAY_ANY.ordinal());
    }

    @Override
    public void selectColor(Player player, int color) {
        if (player != current)
            return;
        int[] cost = new int[color + 1];
        cost[color] = 1;
        AmmoCube ammoCost = new AmmoCube(cost);
        if (total.greaterEqThan(ammoCost))
            controller.setState(new PayState(controller, current, ammoCost));
    }
}

class FireModeSelectionState extends State {
    private final Player current;
    private final AmmoCube total;

    public FireModeSelectionState(GameController controller, Player player) {
        super(controller);
        current = player;
        total = player.getFigure().getTotalAmmo();
    }

    @Override
    public void onEnter(){
        current.sendGameState(GameState.FIRE_MODE.ordinal());
        current.sendPlayerWeapons(current);
    }

    @Override
    public void selectFireMode(Player player, Weapon weapon, FireMode[] fireModes) {
        if (player != current)
            return;
        if(!weapon.isLoaded())
            return;
        AmmoCube ammoCost = FireMode.flatCost(Arrays.asList(fireModes));
        if (total.greaterEqThan(ammoCost)) {
            weapon.unload();
            controller.addState(new FireState(controller, current, FireMode.flatSteps(Arrays.asList(fireModes))));
            controller.setState(new PayState(controller, current, ammoCost));
        }
    }
}

class FireState extends State {
    private final FireSequence fireSequence;

    public FireState(GameController controller, Player player, List<FireStep> stepList) {
        super(controller);
        this.fireSequence = new FireSequence(player.getFigure(), controller.getGame().getBoard(), stepList);
    }

    @Override
    public void onEnter() {
        if(fireSequence.getTargets().size()<fireSequence.getMinTargets()){
            fireSequence.getShooter().getPlayer().sendMessage("Not enough targets");
            controller.nextState();
            return;
        }
        if(fireSequence.getTargets().size()<=fireSequence.getMaxTargets()){
            fireSequence.getShooter().getPlayer().sendMessage("Automatically chosen targets");
            selectTargettable(fireSequence.getShooter().getPlayer(),fireSequence.getTargets().toArray(Targettable[]::new));
        }
        fireSequence.getShooter().getPlayer().sendGameState(GameState.FIRE.ordinal());
        fireSequence.getShooter().getPlayer().sendTargets(fireSequence.getMinTargets()
                ,fireSequence.getMaxTargets()
                ,fireSequence.getTargets(),
                controller.getGame().getBoard());

    }

    @Override
    public void selectTargettable(Player player, Targettable[] targettables) {
        if (player != fireSequence.getShooter().getPlayer())
            return;
        Set<Targettable> targets = Set.of(targettables);
        if(fireSequence.validateTargets(targets))
            fireSequence.run(targets);
        if (fireSequence.hasNext())
            controller.setState(this);
        else {
            controller.getGame().getBoard().applyMarks();
            Set<Figure> damaged = controller.getGame().getBoard().getDamaged();
            if(!damaged.isEmpty()) controller.addState(new ScopeState(controller, player));
            controller.setState(new TagbackState(controller, player, damaged));
        }
    }
}

class SelectGrabState extends State {
    private final Player current;

    public SelectGrabState(GameController controller, Player player) {
        super(controller);
        current = player;
        current.sendGameState(GameState.SELECT_GRAB.ordinal());
    }

    @Override
    public void onEnter() {
        List<Grabbable> list = current.getFigure().getLocation().peek();
        if (list.isEmpty())
            controller.nextState();
        //TODO NON PRENDE PUP
        else if (list.size() == 1)
            selectGrabbable(current, list.get(0));
        else if(current.getFigure().getLocation()!=null)
            current.sendSquareContent(current.getFigure().getLocation());
    }

    @Override
    public void selectGrabbable(Player player, Grabbable grabbable) {
        if (player != current)
            return;
        controller.addState(new GrabState(controller, current, grabbable));
        if (grabbable instanceof Weapon) {
            if(current.getFigure().getTotalAmmo().greaterEqThan( ((Weapon) grabbable).getPickupCost()))
                controller.setState(new PayState(controller, current, ((Weapon) grabbable).getPickupCost()));
            else current.sendMessage("You can't afford that weapon's pickup cost");
        }
        else
            controller.nextState();
    }
}

class GrabState extends State {
    private final Player current;
    private final Grabbable grabbable;
    private Weapon discard;

    public GrabState(GameController controller, Player player, Grabbable grabbable) {
        super(controller);
        current = player;
        this.grabbable = grabbable;
        current.sendGameState(GameState.GRAB.ordinal());
    }

    @Override
    public void onEnter() {
        Figure figure = current.getFigure();

        try {
            figure.getLocation().grab(figure, grabbable);
            if (discard != null) figure.getLocation().refill(discard);

            controller.getGame().getPlayers().forEach(x->x.sendSquareContent(figure.getLocation()));
            controller.nextState();
        } catch (IllegalStateException e) {
            current.sendMessage("You have to discard a weapon");
        }
    }

    @Override
    public void selectWeapon(Player player, Weapon[] weapons) {
        if (!(player == current && current.getFigure().getLocation() instanceof SpawnSquare))
            return;
        //TODO POSSO SCARTARE PIù DI UN'ARMA
        if (weapons.length != 0) {
            discard = weapons[0];
            discard.load();
            current.getFigure().getWeapons().remove(discard);
        }
        controller.getGame().getPlayers().forEach(x->x.sendPlayerWeapons(current));
        controller.setState(this);
    }
}

class ScopeState extends State {
    private final Player current;

    public ScopeState(GameController controller, Player player) {
        super(controller);
        current = player;
    }

    @Override
    public void onEnter(){
        current.sendGameState(GameState.SCOPE.ordinal());
    }

    @Override
    public void selectPowerUp(Player player, PowerUp[] powerUps) {
        if (player != current)
            return;
        if (powerUps.length < 1) {
            controller.nextState();
            return;
        }
        PowerUpType type = powerUps[0].getType();
        if (type != PowerUpType.SCOPE)
            return;
        controller.addState(this);
        controller.addState(new FireState(controller, current, type.getStepList()));
        controller.setState(new PayAnyColorState(controller, current));
    }
}

class TagbackState extends State {
    private final Player current;
    private final List<Figure> damaged;
    private TimerTask tagbackTimer;
    private boolean timeout;

    public TagbackState(GameController controller, Player player, Set<Figure> damaged) {
        super(controller);
        current = player;
        this.damaged = damaged.stream().filter(figure -> figure.getPlayer().isActive()).collect(Collectors.toList());
        current.sendGameState(GameState.TAGBACK.ordinal());
        damaged.stream()
                .map(Figure::getPlayer)
                .peek(Player::setInactive)
                .forEach(x->x.sendGameState(GameState.TAGBACK.ordinal()));
        tagbackTimer = new TimerTask() {
            private int c = controller.getGame().getOtherTimeout();

            @Override
            public void run() {
                if (--c <= 0 && tagbackTimer != null)
                    timeout();
                else if (c <= 5) current.getClient().sendCountDown(c);
            }
        };
        controller.getTimer().schedule(tagbackTimer, 0, 1000);
    }

    @Override
    public void onEnter() {
        if (timeout || damaged.isEmpty()){
            tagbackTimer.cancel();
            controller.nextState();
        }
    }

    private void timeout() {
        timeout = true;
        controller.setState(this);
    }

    @Override
    public void selectPowerUp(Player player, PowerUp[] powerUps) {
        if (!damaged.contains(player.getFigure()))
            return;
        Arrays.stream(powerUps).filter(x -> x.getType() == PowerUpType.TAGBACK).forEach(
                powerUp -> {
                    player.getFigure().getPowerUps().remove(powerUp);
                    powerUp.discard();
                    FireSequence fs = new FireSequence(player.getFigure(), controller.getGame().getBoard(), powerUp.getType().getStepList());
                    fs.run(new HashSet<>(fs.getTargets()));
                }
        );
        player.sendPowerUps(player.getFigure().getPowerUps());
        controller.getGame().getPlayers().forEach(x->x.sendPlayerNPowerUps(player));
        damaged.remove(player.getFigure());
        player.setActive();
        controller.setState(this);
    }
}

class EndTurnState extends State {
    private final Player current;
    private TimerTask respawnTimer;

    public EndTurnState(GameController controller, Player player) {
        super(controller);
        current = player;
        current.sendGameState(GameState.GRAB.ordinal());
        current.sendPossibleActions(-1);
    }

    @Override
    public void onEnter() {
        if (respawnTimer != null) {
            timeout();
            return;
        }
        respawnTimer = new TimerTask() {
            private int c = controller.getGame().getOtherTimeout();

            @Override
            public void run() {
                if (--c <= 0 && respawnTimer != null)
                    timeout();
                else if (c <= 5) current.getClient().sendCountDown(c);
            }
        };
        controller.getTimer().schedule(respawnTimer, 0, 1000);
        List<Player> kills = controller.getGame().getBoard().getFigures().stream()
                .filter(figure -> figure.resolveDeath(controller.getGame()))
                .map(Figure::getPlayer)
                .collect(Collectors.toList());
        if (kills.size() > 1)
            current.getFigure().addPoints(1);
        controller.addState(this);
        controller.addState(new SelectSpawnState(controller, kills.stream().filter(Player::isActive).collect(Collectors.toList())));
        kills.forEach(Player::setInactive);
        controller.nextState();
    }

    private void timeout() {
        respawnTimer.cancel();
        controller.clearStack();
        controller.getGame().endTurn();
        controller.setState(new TurnState(controller));
    }
}
