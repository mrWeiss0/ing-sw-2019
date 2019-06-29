package server.controller;

import server.model.Grabbable;
import server.model.PowerUp;
import server.model.board.Targettable;
import server.model.weapon.FireMode;
import server.model.weapon.Weapon;

public abstract class Event {
    private final Player player;

    public Event(Player player) {
        this.player = player;
    }

    Player getPlayer() {
        return player;
    }

    abstract void accept(GameController visitor);
}

class SelectPowerUpEvent extends Event {
    private final PowerUp[] powerUps;

    SelectPowerUpEvent(Player player, PowerUp[] powerUps) {
        super(player);
        this.powerUps = powerUps;
    }

    @Override
    void accept(GameController visitor) {
        visitor.visit(this);
    }

    PowerUp[] getPowerUps() {
        return powerUps;
    }
}

class SelectWeaponToReloadEvent extends Event {
    private final Weapon[] weapons;

    SelectWeaponToReloadEvent(Player player, Weapon[] weapons) {
        super(player);
        this.weapons = weapons;
    }

    @Override
    void accept(GameController visitor) {
        visitor.visit(this);
    }

    Weapon[] getWeapons() {
        return weapons;
    }
}

class SelectWeaponFireModeEvent extends Event {
    private final Weapon weapon;
    private final FireMode[] fireModes;

    SelectWeaponFireModeEvent(Player player, Weapon weapon, FireMode[] fireModes) {
        super(player);
        this.weapon = weapon;
        this.fireModes = fireModes;
    }

    @Override
    void accept(GameController visitor) {
        visitor.visit(this);
    }

    Weapon getWeapon() {
        return weapon;
    }

    FireMode[] getFireModes() {
        return fireModes;
    }
}

class SelectGrabbableEvent extends Event {
    private final Grabbable grabbable;

    SelectGrabbableEvent(Player player, Grabbable grabbable) {
        super(player);
        this.grabbable = grabbable;
    }

    @Override
    void accept(GameController visitor) {
        visitor.visit(this);
    }

    Grabbable getGrabbable() {
        return grabbable;
    }
}

class SelectActionEvent extends Event {
    private final Action action;

    SelectActionEvent(Player player, Action action) {
        super(player);
        this.action = action;
    }

    @Override
    void accept(GameController visitor) {
        visitor.visit(this);
    }

    Action getAction() {
        return action;
    }
}

class SelectTargettableEvent extends Event {
    private final Targettable[] targettables;

    SelectTargettableEvent(Player player, Targettable[] targettables) {
        super(player);
        this.targettables = targettables;
    }

    @Override
    void accept(GameController visitor) {
        visitor.visit(this);
    }

    Targettable[] getTargettables() {
        return targettables;
    }
}

class SelectColorEvent extends Event {
    private final int color;

    SelectColorEvent(Player player, int color) {
        super(player);
        this.color = color;
    }

    @Override
    void accept(GameController visitor) {
        visitor.visit(this);
    }

    int getColor() {
        return color;
    }
}
