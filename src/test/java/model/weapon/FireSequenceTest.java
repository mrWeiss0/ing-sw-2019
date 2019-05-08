package model.weapon;

import model.AmmoCube;
import model.board.Targettable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class machineGunTest {
    static OptionalWeapon machineGun;
    static FireMode base, focus, tripod;

    @BeforeAll
    static void init() {
        machineGun = new OptionalWeaponMock();
        base = new FireMode();
        base.addStep(new FireStep(2,
                (shooter, board, last) -> shooter.getSquare().visibleFigures(),
                (shooter, curr, last) -> {
                    curr.forEach(f -> f.damageFrom(shooter, 1));
                    if (curr.size() < 2) curr.add(null);
                    return curr;
                }));
        machineGun.addFireMode(base);

        focus = new FireMode(new AmmoCube(0, 1));
        focus.addStep(new FireStep(1,
                (shooter, board, last) -> last.stream().limit(2).filter(Objects::nonNull).collect(Collectors.toSet()),
                (shooter, curr, last) -> {
                    Targettable current = curr.get(0);
                    current.damageFrom(shooter, 1);
                    last.set(last.indexOf(current), null);
                    last.add(3, current);
                    return last;
                }));
        machineGun.addFireMode(focus);

        tripod = new FireMode(new AmmoCube(1));
        tripod.addStep(new FireStep(1,
                (shooter, board, last) -> last.stream().limit(2).filter(Objects::nonNull).collect(Collectors.toSet()),
                (shooter, curr, last) -> {
                    Targettable current = curr.get(0);
                    current.damageFrom(shooter, 1);
                    last.set(last.indexOf(current), null);
                    last.add(3, current);
                    return last;
                }));
        tripod.addStep(new FireStep(1,
                (shooter, board, last) -> board.getFigures().stream().filter(x -> !last.contains(x)).collect(Collectors.toSet()),
                (shooter, curr, last) -> {
                    curr.get(0).damageFrom(shooter, 1);
                    return last;
                }));
        machineGun.addFireMode(tripod);

        machineGun.addDependency(focus, base);
        machineGun.addDependency(tripod, base);
    }

    @Test
    void testCost() {
        assertEquals(new AmmoCube(1, 1), machineGun.getFireModes().stream().map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new));
    }
}