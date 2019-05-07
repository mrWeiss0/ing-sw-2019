package model;

import model.weapon.Weapon;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class SpawnSquare extends AbstractSquare {
    private final int capacity = 3;
    private Set<Weapon> weapons = new HashSet<>();

    public SpawnSquare(Room room, int[] coordinates) {
        super(room, coordinates);
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
    public void grab(Figure grabber, Grabbable grabbed) {
        weapons.stream().filter(Predicate.isEqual(grabbed)).findFirst().ifPresent(g -> {
            grabber.grab(g);
            weapons.remove(g);
        });
    }

    @Override
    public Set<Grabbable> peek() {
        return new HashSet<>(weapons);
    }
}