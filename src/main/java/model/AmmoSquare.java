package model;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AmmoSquare extends AbstractSquare {
    private AmmoTile ammoTile;

    public AmmoSquare(Room room, int[] coordinates) {
        super(room, coordinates);
        ammoTile = null;
    }

    @Override
    public void accept(Game game) {
        game.fillSquare(this);
    }

    @Override
    public void refill(Grabbable o) {
        ammoTile = (AmmoTile) o;
    }

    @Override
    public void grab(Figure grabber, Grabbable grabbed) {
        Optional.ofNullable(ammoTile).filter(Predicate.isEqual(grabbed)).ifPresent((g) -> {
            grabber.grab(g);
            ammoTile = null;
        });
    }

    @Override
    public Set<Grabbable> peek() {
        return Optional.ofNullable(ammoTile).map(Stream::of).orElseGet(Stream::empty).collect(Collectors.toSet());
    }
}
