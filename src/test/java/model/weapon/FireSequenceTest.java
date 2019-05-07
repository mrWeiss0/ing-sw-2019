package model.weapon;

import model.mock.OptionalWeaponMock;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class FireSequenceTest {
    @Test
    void machineGunTest() {
        Weapon machineGun = new OptionalWeaponMock();
        /*FireMode fm = new FireMode();
        TargetGen targetGen1 = () -> {return null;};
        fm.addStep(new FireStep(2, (shoot, curr, last) -> {
            return last;
        }).addTargetGen((shoot, game, last) -> {
            return new HashSet<>();
        }));
        machineGun.addFireMode(fm);
        */
        TargetGen tg1 = (s, b, l) -> new HashSet<>(b.getFigures());
        TargetGen tg2 = (s, b, l) -> new HashSet<>(b.getFigures());
    }
}