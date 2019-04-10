package model;

import java.util.HashSet;
import java.util.Set;

public class SpawnSquare extends AbstractSquare {
    private Set<Weapon> weapons;

    public SpawnSquare(Room room) {
        super(room);
        weapons = new HashSet<>();
    }

    @Override
    public void accept(Game game) {

    }

    @Override
    public void refill(Grabbable o) {

    }

    @Override
    public void grab(Figure grabber, Grabbable grabbed) {
        if (weapons.contains(grabbed))
            grabber.grab((Weapon) grabbed);
    }

    @Override
    public Set<Grabbable> peek() {
        return new HashSet<>(weapons);
    }
}
