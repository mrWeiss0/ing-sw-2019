package server.model.weapon;

import server.model.AmmoCube;

import static server.model.weapon.FireModes.*;

public enum Weapons{


    LOCK_RIFLE(new OptionalWeapon.Builder().pickupCost(new AmmoCube(0, 0, 1)).reloadCost(new AmmoCube(0, 0, 1))
            .fireModes(lockRifleModes).dependency(lockRifleModes[1], lockRifleModes[0])),

    ELECTROSCYTHE(new Weapon.Builder().pickupCost(new AmmoCube(0,0,1)).fireModes(electroscytheModes)),

    MACHINE_GUN(new OptionalWeapon.Builder().pickupCost(new AmmoCube(0,0,1)).reloadCost(new AmmoCube(1,0,0))),

    TRACTOR_BEAM(new Weapon.Builder().pickupCost(new AmmoCube(0,0,1))),

    THOR(new OptionalWeapon.Builder().pickupCost(new AmmoCube(0,0,1)).reloadCost(new AmmoCube(1,0,0))),

    PLASMA_GUN(new OptionalWeapon.Builder().pickupCost(new AmmoCube(0,0,1)).reloadCost(new AmmoCube(0,1,0))),

    WHISPER(new Weapon.Builder().pickupCost(new AmmoCube(0,0,1)).reloadCost(new AmmoCube(0,1,1))),

    VORTEX_CANNON(new OptionalWeapon.Builder().pickupCost(new AmmoCube(1,0,0)).reloadCost(new AmmoCube(0,0,1))),

    FURNACE(new Weapon.Builder().pickupCost(new AmmoCube(1,0,0)).reloadCost(new AmmoCube(0,0,1))),

    HEATSEEKER(new Weapon.Builder().pickupCost(new AmmoCube(1,0,0)).reloadCost(new AmmoCube(1,1,0))),

    HELLION(new Weapon.Builder().pickupCost(new AmmoCube(1,0,0)).reloadCost(new AmmoCube(0,1,0))),

    FLAMETHROWER(new Weapon.Builder().pickupCost(new AmmoCube(1,0,0))),

    GRENADE_LAUNCHER(new OptionalWeapon.Builder().pickupCost(new AmmoCube(1,0,0))),

    ROCKET_LAUNCHER(new OptionalWeapon.Builder().pickupCost(new AmmoCube(1,0,0)).reloadCost(new AmmoCube(1,0,0))),

    RAILGUN(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)).reloadCost(new AmmoCube(0,1,1))),

    CYBERBLADE(new OptionalWeapon.Builder().pickupCost(new AmmoCube(0,1,0)).reloadCost(new AmmoCube(1,0,0))),

    ZX2(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)).reloadCost(new AmmoCube(1,0,0))),

    SHOTGUN(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)).reloadCost(new AmmoCube(0,1,0))),

    POWER_GLOVE(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)).reloadCost(new AmmoCube(0,0,1))),

    SHOCKWAVE(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0))),

    SLEDGEHAMMER(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)));

    private Weapon.Builder builder;
    Weapons(Weapon.Builder builder){
        this.builder=builder;
    }

    public Weapon build(){
        return builder.build();
    }
}
