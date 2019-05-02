package model.mock;

import model.AmmoCube;
import model.weapon.OptionalWeapon;

public class MockOptionalWeapon extends OptionalWeapon {
    public MockOptionalWeapon() {
        super(new AmmoCube(), new AmmoCube());
    }
}
