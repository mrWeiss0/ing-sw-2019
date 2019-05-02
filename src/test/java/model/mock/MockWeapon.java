package model.mock;

import model.AmmoCube;
import model.weapon.Weapon;

public class MockWeapon extends Weapon {
    public MockWeapon() {
        super(new AmmoCube(), new AmmoCube());
    }
}
