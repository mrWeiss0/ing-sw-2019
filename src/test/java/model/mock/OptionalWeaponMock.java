package model.mock;

import model.AmmoCube;
import model.weapon.OptionalWeapon;

public class OptionalWeaponMock extends OptionalWeapon {
    public OptionalWeaponMock() {
        super(new AmmoCube(), new AmmoCube());
    }
}
