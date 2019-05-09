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
    public boolean accept(Game game) {
        return game.fillSquare(this);
    }

    @Override
    public boolean refill(Grabbable o) {
        if (ammoTile == null) {
            ammoTile = (AmmoTile) o;
            return true;
        }
        return false;
    }

    @Override
    public boolean grab(Figure grabber, Grabbable grabbed) {
        if (ammoTile == grabbed) {
            grabber.grab(ammoTile);
            ammoTile = null;
            return true;
        }
        return false;
    }

    @Override
    public Set<Grabbable> peek() {
        return Optional.ofNullable(ammoTile).map(Stream::of).orElseGet(Stream::empty).collect(Collectors.toSet());
    }
}
