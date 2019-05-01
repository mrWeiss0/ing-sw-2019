package model;

import model.weapon.Weapon;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class SpawnSquare extends AbstractSquare {
    private Set<Weapon> weapons;

    public SpawnSquare(Room room, int[] coordinates) {
        super(room,coordinates);
        weapons = new HashSet<>();
    }

    @Override
    public void accept(Game game) {
        game.fillSquare(this);
    }

    @Override
    public void refill(Grabbable o) {
        weapons.add((Weapon) o);
    }

    @Override
    public void grab(Figure grabber, Grabbable grabbed) {
        weapons.stream().filter(Predicate.isEqual(grabbed)).findFirst().ifPresent((g) -> {
            grabber.grab(g);
            weapons.remove(g);
        });
    }

    @Override
    public Set<Grabbable> peek() {
        return new HashSet<>(weapons);
    }
}