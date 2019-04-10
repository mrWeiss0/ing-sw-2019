package model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class AmmoCube {
    private final List<Integer> ammos;
    public AmmoCube(List<Integer> ammos){
        this.ammos=ammos;
    }
    public AmmoCube add(AmmoCube toAdd){
        return new AmmoCube(IntStream.range(0,Math.min(ammos.size(),toAdd.getAmmos().size())).
                mapToObj(i->Integer.min(3,ammos.get(i)+toAdd.getAmmos().get(i))).collect(Collectors.toList()));
    }
    public AmmoCube sub(AmmoCube toSub){
        return new AmmoCube(IntStream.range(0,Math.min(ammos.size(),toSub.getAmmos().size())).
                mapToObj(i->Integer.max(0,ammos.get(i)-toSub.getAmmos().get(i))).collect(Collectors.toList()));
    }

    public List<Integer> getAmmos() {
        return ammos;
    }
}
