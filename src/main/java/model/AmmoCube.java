package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class AmmoCube {
    private final List<Integer> ammos;

    public AmmoCube() {
        this.ammos = new ArrayList<>();
    }

    public AmmoCube(List<Integer> ammos) {
        this.ammos = ammos;
    }

    public AmmoCube add(AmmoCube toAdd) {
        return new AmmoCube(this.rangeAmmos(toAdd).
                mapToObj(i -> ammos.get(i) + toAdd.getAmmos().get(i)).collect(Collectors.toList()));
    }

    public AmmoCube sub(AmmoCube toSub) {
        return new AmmoCube(this.rangeAmmos(toSub).
                mapToObj(i -> ammos.get(i) - toSub.getAmmos().get(i)).collect(Collectors.toList()));
    }

    public List<Integer> getAmmos() {
        return ammos;
    }

    public AmmoCube cap(int c) {
        return new AmmoCube(ammos.stream().map(x -> Integer.min(c, x)).collect(Collectors.toList()));
    }

    public boolean greaterThan(AmmoCube other) {
        return this.rangeAmmos(other).filter(i -> ammos.get(i) < other.getAmmos().get(i)).count() == 0;

    }

    private IntStream rangeAmmos(AmmoCube other) {
        return IntStream.range(0, Math.min(ammos.size(), other.getAmmos().size()));
    }
}
