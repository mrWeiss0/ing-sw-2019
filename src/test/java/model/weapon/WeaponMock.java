package model.weapon;

import model.AmmoCube;

public class WeaponMock extends Weapon {
    public WeaponMock() {
        super(new AmmoCube(), new AmmoCube());
    }
}
