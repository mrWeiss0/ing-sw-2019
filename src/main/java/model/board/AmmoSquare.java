package model.board;

import model.AmmoTile;
import model.Game;
import model.Grabbable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AmmoSquare extends AbstractSquare {
    private AmmoTile ammoTile = null;

    public AmmoSquare(Room room, int[] coordinates) {
        super(room, coordinates);
    }

    @Override
    public void accept(Game game) {
        if (ammoTile == null)
            game.fillSquare(this);
    }

    @Override
    public void refill(Grabbable o) {
        AmmoTile t = (AmmoTile) o;
        if (ammoTile != null)
            throw new IllegalStateException("Square is already filled");
        ammoTile = t;
    }

    @Override
    public void grab(Figure grabber, Grabbable grabbed) {
        AmmoTile t = (AmmoTile) grabbed;
        if (ammoTile != t)
            throw new IllegalStateException("Square " + this + " does not contain item " + t);
        grabber.grab(t);
        ammoTile = null;
    }

    @Override
    public Set<Grabbable> peek() {
        return Optional.ofNullable(ammoTile).map(Stream::of).orElseGet(Stream::empty).collect(Collectors.toSet());
    }
}
