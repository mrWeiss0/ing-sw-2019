package server.model.board;

import server.model.Game;
import server.model.Grabbable;
import server.model.weapon.Weapon;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class SpawnSquare extends AbstractSquare {
    private final int capacity;
    private final Set<Weapon> weapons = new HashSet<>();

    public SpawnSquare(Room room, int[] coordinates, int capacity) {
        super(room, coordinates);
        this.capacity = capacity;
    }

    @Override
    public void accept(Game game) {
        try {
            while (weapons.size() < capacity)
                game.fillSquare(this);
        } catch (NoSuchElementException ignore) {
            // Weapon dek is empty
        }
    }

    @Override
    public void refill(Grabbable o) {
        Weapon w = (Weapon) o;
        if (weapons.size() >= capacity)
            throw new IllegalStateException("Square is already filled");
        weapons.add(w);
    }

    @Override
    public void grab(Figure grabber, Grabbable grabbed) {
        Weapon w = (Weapon) grabbed;
        if (!weapons.remove(w))
            throw new IllegalStateException("Square " + this + " does not contain item " + w);
        grabber.grab(w);
    }

    @Override
    public Set<Grabbable> peek() {
        return new HashSet<>(weapons);
    }
}
