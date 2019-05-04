package model.mock;

import model.AmmoCube;
import model.weapon.Weapon;

public class WeaponMock extends Weapon {
    public WeaponMock() {
        super(new AmmoCube(), new AmmoCube());
    }
}
