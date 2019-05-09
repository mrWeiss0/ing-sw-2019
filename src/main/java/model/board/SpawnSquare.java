package model.board;

import model.Game;
import model.Grabbable;
import model.weapon.Weapon;

import java.util.HashSet;
import java.util.Set;

public class SpawnSquare extends AbstractSquare {
    private final int capacity;
    private final Set<Weapon> weapons = new HashSet<>();

    public SpawnSquare(Room room, int[] coordinates) {
        this(room, coordinates, 1);
    }

    public SpawnSquare(Room room, int[] coordinates, int capacity) {
        super(room, coordinates);
        this.capacity = capacity;
    }

    @Override
    public boolean accept(Game game) {
        return game.fillSquare(this);
    }

    @Override
    public boolean refill(Grabbable o) {
        if (weapons.size() < capacity)
            return weapons.add((Weapon) o);
        return false;
    }

    @Override
    public boolean grab(Figure grabber, Grabbable grabbed) {
        try {
            //noinspection SuspiciousMethodCalls
            if (weapons.remove(grabbed)) {
                grabber.grab((Weapon) grabbed);
                return true;
            }
        } catch (ClassCastException ignore) {
            // grabbed was not a weapon
        }
        return false;
    }

    @Override
    public Set<Grabbable> peek() {
        return new HashSet<>(weapons);
    }
}
