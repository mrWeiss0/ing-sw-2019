package model;

import java.util.Collections;
import java.util.Set;

public class AmmoSquare extends AbstractSquare {
    private AmmoTile ammoTile;

    public AmmoSquare(Room room) {
        super(room);
        ammoTile = null;
    }

    @Override
    public void accept(Game game) {

    }

    @Override
    public void refill(Grabbable o) {

    }

    @Override
    public void grab(Figure grabber, Grabbable grabbed) {
        if (ammoTile == grabbed)
            grabber.grab((AmmoTile) grabbed);
    }

    @Override
    public Set<Grabbable> peek() {
        return Collections.singleton(ammoTile);
    }
}
