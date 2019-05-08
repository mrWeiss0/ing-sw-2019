package model.weapon;

import model.AmmoCube;

public class OptionalWeaponMock extends OptionalWeapon {
    public OptionalWeaponMock() {
        super(new AmmoCube(), new AmmoCube());
    }
}
