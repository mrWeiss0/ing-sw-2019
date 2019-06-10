package server.model.weapon;

import server.model.AmmoCube;

import static server.model.weapon.FireModes.*;

public enum Weapons{


    LOCK_RIFLE(new OptionalWeapon.Builder().pickupCost(new AmmoCube(0, 0, 1)).reloadCost(new AmmoCube(0, 0, 1))
            .fireModes(lockRifleModes).dependency(lockRifleModes[1], lockRifleModes[0])),

    ELECTROSCYTHE(new Weapon.Builder().pickupCost(new AmmoCube(0,0,1)).fireModes(electroscytheModes)),

    MACHINE_GUN(new OptionalWeapon.Builder().pickupCost(new AmmoCube(0,0,1)).reloadCost(new AmmoCube(1,0,0)).fireModes(machineGunModes)
            .dependency(machineGunModes[1],machineGunModes[0]).dependency(machineGunModes[2],machineGunModes[0])),

    TRACTOR_BEAM(new Weapon.Builder().pickupCost(new AmmoCube(0,0,1)).fireModes(tractorBeamModes)),

    THOR(new OptionalWeapon.Builder().pickupCost(new AmmoCube(0,0,1)).reloadCost(new AmmoCube(1,0,0)).fireModes(thorModes)
            .dependency(thorModes[1],thorModes[0]).dependency(thorModes[2],thorModes[1])),

    PLASMA_GUN(new OptionalWeapon.Builder().pickupCost(new AmmoCube(0,0,1)).reloadCost(new AmmoCube(0,1,0)).fireModes(plasmaGunModes)
            .dependency(plasmaGunModes[2],plasmaGunModes[0])),

    WHISPER(new Weapon.Builder().pickupCost(new AmmoCube(0,0,1)).reloadCost(new AmmoCube(0,1,1)).fireModes(whisperModes)),

    VORTEX_CANNON(new OptionalWeapon.Builder().pickupCost(new AmmoCube(1,0,0)).reloadCost(new AmmoCube(0,0,1)).fireModes(vortexModes)
            .dependency(vortexModes[1],vortexModes[0])),

    FURNACE(new Weapon.Builder().pickupCost(new AmmoCube(1,0,0)).reloadCost(new AmmoCube(0,0,1)).fireModes(furnaceModes)),

    HEATSEEKER(new Weapon.Builder().pickupCost(new AmmoCube(1,0,0)).reloadCost(new AmmoCube(1,1,0)).fireModes(heatseekerModes)),

    HELLION(new Weapon.Builder().pickupCost(new AmmoCube(1,0,0)).reloadCost(new AmmoCube(0,1,0)).fireModes(hellionModes)),

    FLAMETHROWER(new Weapon.Builder().pickupCost(new AmmoCube(1,0,0)).fireModes(flamethrowerModes)),

    GRENADE_LAUNCHER(new OptionalWeapon.Builder().pickupCost(new AmmoCube(1,0,0)).fireModes(grenadeLauncherModes)
            .dependency(grenadeLauncherModes[1],grenadeLauncherModes[0])),

    ROCKET_LAUNCHER(new OptionalWeapon.Builder().pickupCost(new AmmoCube(1,0,0)).reloadCost(new AmmoCube(1,0,0)).fireModes(rocketLauncherModes)
            .dependency(rocketLauncherModes[2],rocketLauncherModes[0])),

    RAILGUN(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)).reloadCost(new AmmoCube(0,1,1)).fireModes(railgunModes)),

    CYBERBLADE(new OptionalWeapon.Builder().pickupCost(new AmmoCube(0,1,0)).reloadCost(new AmmoCube(1,0,0)).fireModes(cyberbladeModes)
            .dependency(cyberbladeModes[2],cyberbladeModes[0])),

    ZX2(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)).reloadCost(new AmmoCube(1,0,0)).fireModes(zx2Modes)),

    SHOTGUN(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)).reloadCost(new AmmoCube(0,1,0)).fireModes(shotgunModes)),

    POWER_GLOVE(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)).reloadCost(new AmmoCube(0,0,1)).fireModes(powerGloveModes)),

    SHOCKWAVE(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)).fireModes(shockwaveModes)),

    SLEDGEHAMMER(new Weapon.Builder().pickupCost(new AmmoCube(0,1,0)).fireModes(sledgehammer));

    private Weapon.Builder builder;
    Weapons(Weapon.Builder builder){
        this.builder=builder;
    }

    public Weapon build(){
        return builder.build();
    }
}
