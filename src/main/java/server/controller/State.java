package server.controller;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

abstract class State {
    protected GameController controller;

    protected State(GameController controller) {
        this.controller = controller;
    }

    void selectPowerUp(Player player, PowerUp[] powerUps) {
    }

    void selectWeapon(Player player, Weapon[] weapons) {
    }

    void selectFireMode(Player player, Weapon weapon, FireMode[] fireModes) {
    }

    void selectGrabbable(Player player, Grabbable grabbable) {
    }

    void selectAction(Player player, Action action) {
    }

    void selectTargettable(Player player, Targettable[] targettables) {
    }

    void selectColor(Player player, int color) {
    }

    void onEnter() {
    }
}

class TurnState extends State {
    private final Player current;
    private int remainingActions;

    public TurnState(GameController controller) {
        super(controller);
        int actionsID;
        current = controller.getGame().nextPlayer();
        actionsID = controller.getGame().getActionsID();
        remainingActions = actionsID % 2 == 0 ? 2 : 1;
        current.setActions(Actions.values()[actionsID].getActionList());
    }

    @Override
    public void onEnter() {
        if (current.getFigure().getLocation() == null) {
            controller.addState(this);
            controller.setState(new SelectSpawnState(controller, Collections.singletonList(current)));
        }
        if (remainingActions-- <= 0) {
            // TODO stop timer
            controller.addState(new TurnState(controller));
            controller.setState(new SelectReloadState(controller, current));
        }
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
            controller.addState(this);
            controller.setState(new FireState(controller, current, type.getStepList()));
        }
    }
}

class SelectSpawnState extends State {
    private final List<Player> current;
    private final PowerUp powerUp;

    public SelectSpawnState(GameController controller, List<Player> players) {
        super(controller);
        current = players;
        powerUp = controller.getGame().drawPowerup();
        // TODO SEND
    }

    @Override
    public void onEnter() {
        if (current.isEmpty())
            controller.nextState();
    }

    @Override
    public void selectPowerUp(Player player, PowerUp[] powerUps) {
        if (current.contains(player) && powerUps.length >= 1) {
            Figure figure = player.getFigure();
            List<PowerUp> currentPowerUps = figure.getPowerUps();
            if (currentPowerUps.remove(powerUps[0]))
                currentPowerUps.add(powerUp);
            figure.moveTo(powerUps[0].getSpawn());
            powerUps[0].discard();
            current.remove(player);
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
            controller.nextState();
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
    public void selectFireMode(Player player, Weapon weapon, FireMode[] fireModes) {
        if (player != current)
            return;
        AmmoCube ammoCost = FireMode.flatCost(Arrays.asList(fireModes));
        if (total.greaterEqThan(ammoCost)) {
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
        // TODO SEND TARGETS fireSequence.getTargets();
    }

    @Override
    public void selectTargettable(Player player, Targettable[] targettables) {
        if (player != fireSequence.getShooter().getPlayer())
            return;
        Set<Targettable> targets = Set.of(targettables);
        fireSequence.validateTargets(targets);
        fireSequence.run(targets);
        if (fireSequence.hasNext())
            controller.setState(this);
        else {
            controller.getGame().getBoard().applyMarks();
            controller.addState(new ScopeState(controller, player));
            controller.setState(new TagbackState(controller, player, controller.getGame().getBoard().getDamaged()));
        }
    }
}

class SelectGrabState extends State {
    private final Player current;

    public SelectGrabState(GameController controller, Player player) {
        super(controller);
        current = player;
    }

    @Override
    public void onEnter() {
        List<Grabbable> list = current.getFigure().getLocation().peek();
        if (list.isEmpty())
            controller.nextState();
        else if (list.size() == 1)
            selectGrabbable(current, list.get(0));
        // TODO else send
    }

    @Override
    public void selectGrabbable(Player player, Grabbable grabbable) {
        if (player != current)
            return;
        controller.addState(new GrabState(controller, current, grabbable));
        if (grabbable instanceof Weapon)
            controller.setState(new PayState(controller, current, ((Weapon) grabbable).getPickupCost()));
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
    }

    @Override
    public void onEnter() {
        Figure figure = current.getFigure();

        try {
            figure.getLocation().grab(figure, grabbable);
            if (discard != null) figure.getLocation().refill(discard);
            controller.nextState();
        } catch (IllegalStateException e) {
            // TODO "you have to discard"
        }
    }

    @Override
    public void selectWeapon(Player player, Weapon[] weapons) {
        if (!(player == current && current.getFigure().getLocation() instanceof SpawnSquare))
            return;
        if (weapons.length != 0) {
            discard = weapons[0];
            current.getFigure().getWeapons().remove(discard);
        }
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
    private final Set<Figure> damaged;

    public TagbackState(GameController controller, Player player, Set<Figure> damaged) {
        super(controller);
        current = player;
        this.damaged = damaged;
    }

    @Override
    public void onEnter() {
        // TODO add timer controller.nextState();
        if (damaged.isEmpty())
            controller.nextState();
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
                    fs.run(fs.getTargets());
                }
        );
        damaged.remove(player.getFigure());
        controller.setState(this);
    }
}

class EndTurnState extends State {
    private final Player current;

    public EndTurnState(GameController controller, Player player) {
        super(controller);
        current = player;
    }

    @Override
    public void onEnter() {
        List<Player> kills = controller.getGame().getBoard().getFigures().stream()
                .filter(figure -> figure.resolveDeath(controller.getGame()))
                .map(Figure::getPlayer)
                .collect(Collectors.toList());
        if (kills.size() > 1)
            current.getFigure().addPoints(1);
        controller.addState(new TurnState(controller));
        controller.setState(new SelectSpawnState(controller, kills));
    }
}
