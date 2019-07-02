package server.model.weapon;

import server.model.AmmoCube;

import static server.model.weapon.FireModes.*;

public enum Weapons {
    LOCK_RIFLE(new OptionalWeapon.Builder().id(0).pickupCost(new AmmoCube(0, 0, 1)).reloadCost(new AmmoCube(0, 0, 1))
            .fireModes(lockRifleModes).dependency(lockRifleModes[1], lockRifleModes[0])),

    ELECTROSCYTHE(new Weapon.Builder().id(5).pickupCost(new AmmoCube(0, 0, 1)).fireModes(electroscytheModes)),

    MACHINE_GUN(new OptionalWeapon.Builder().id(1).pickupCost(new AmmoCube(0, 0, 1)).reloadCost(new AmmoCube(1, 0, 0)).fireModes(machineGunModes)
            .dependency(machineGunModes[1], machineGunModes[0]).dependency(machineGunModes[2], machineGunModes[0])),

    TRACTOR_BEAM(new Weapon.Builder().id(6).pickupCost(new AmmoCube(0, 0, 1)).fireModes(tractorBeamModes)),

    THOR(new OptionalWeapon.Builder().id(2).pickupCost(new AmmoCube(0, 0, 1)).reloadCost(new AmmoCube(1, 0, 0)).fireModes(thorModes)
            .dependency(thorModes[1], thorModes[0]).dependency(thorModes[2], thorModes[1])),

    PLASMA_GUN(new OptionalWeapon.Builder().id(3).pickupCost(new AmmoCube(0, 0, 1)).reloadCost(new AmmoCube(0, 1, 0)).fireModes(plasmaGunModes)
            .dependency(plasmaGunModes[2], plasmaGunModes[0])),

    WHISPER(new Weapon.Builder().id(4).pickupCost(new AmmoCube(0, 0, 1)).reloadCost(new AmmoCube(0, 1, 1)).fireModes(whisperModes)),

    VORTEX_CANNON(new OptionalWeapon.Builder().id(7).pickupCost(new AmmoCube(1, 0, 0)).reloadCost(new AmmoCube(0, 0, 1)).fireModes(vortexModes)
            .dependency(vortexModes[1], vortexModes[0])),

    FURNACE(new Weapon.Builder().id(8).pickupCost(new AmmoCube(1, 0, 0)).reloadCost(new AmmoCube(0, 0, 1)).fireModes(furnaceModes)),

    HEATSEEKER(new Weapon.Builder().id(9).pickupCost(new AmmoCube(1, 0, 0)).reloadCost(new AmmoCube(1, 1, 0)).fireModes(heatseekerModes)),

    HELLION(new Weapon.Builder().id(10).pickupCost(new AmmoCube(1, 0, 0)).reloadCost(new AmmoCube(0, 1, 0)).fireModes(hellionModes)),

    FLAMETHROWER(new Weapon.Builder().id(11).pickupCost(new AmmoCube(1, 0, 0)).fireModes(flamethrowerModes)),

    GRENADE_LAUNCHER(new OptionalWeapon.Builder().id(12).pickupCost(new AmmoCube(1, 0, 0)).fireModes(grenadeLauncherModes)
            .dependency(grenadeLauncherModes[1], grenadeLauncherModes[0])),

    ROCKET_LAUNCHER(new OptionalWeapon.Builder().id(13).pickupCost(new AmmoCube(1, 0, 0)).reloadCost(new AmmoCube(1, 0, 0)).fireModes(rocketLauncherModes)
            .dependency(rocketLauncherModes[2], rocketLauncherModes[0])),

    RAILGUN(new Weapon.Builder().id(14).pickupCost(new AmmoCube(0, 1, 0)).reloadCost(new AmmoCube(0, 1, 1)).fireModes(railgunModes)),

    CYBERBLADE(new OptionalWeapon.Builder().id(15).pickupCost(new AmmoCube(0, 1, 0)).reloadCost(new AmmoCube(1, 0, 0)).fireModes(cyberbladeModes)
            .dependency(cyberbladeModes[2], cyberbladeModes[0])),

    ZX2(new Weapon.Builder().id(16).pickupCost(new AmmoCube(0, 1, 0)).reloadCost(new AmmoCube(1, 0, 0)).fireModes(zx2Modes)),

    SHOTGUN(new Weapon.Builder().id(17).pickupCost(new AmmoCube(0, 1, 0)).reloadCost(new AmmoCube(0, 1, 0)).fireModes(shotgunModes)),

    POWER_GLOVE(new Weapon.Builder().id(18).pickupCost(new AmmoCube(0, 1, 0)).reloadCost(new AmmoCube(0, 0, 1)).fireModes(powerGloveModes)),

    SHOCKWAVE(new Weapon.Builder().id(19).pickupCost(new AmmoCube(0, 1, 0)).fireModes(shockwaveModes)),

    SLEDGEHAMMER(new Weapon.Builder().id(20).pickupCost(new AmmoCube(0, 1, 0)).fireModes(sledgehammer));

    private final Weapon.Builder builder;

    Weapons(Weapon.Builder builder) {
        this.builder = builder;
    }

    public Weapon build() {
        return builder.build();
    }

}

final class FireModes {
    static final FireMode[] lockRifleModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(2).and(Effects.markCurr(1).and(Effects.addCurrToLast())))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(1, 1,
                    TargetGens.visibleFigures().and(TargetGens.differentFigures().not(TargetGens.inLastFigure())),
                    Effects.markCurr(1)))
    };
    static final FireMode[] electroscytheModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.atDistanceSquares(0),
                    Effects.damageCurr(1))),
            new FireMode(new AmmoCube(1, 0, 1), new FireStep(1, 1,
                    TargetGens.atDistanceSquares(0),
                    Effects.damageCurr(2)))
    };
    static final FireMode[] machineGunModes = new FireMode[]{
            new FireMode(new FireStep(1, 2,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(1).and(Effects.addCurrToLast().and(Effects.fillLastToSize(2))))),
            new FireMode(new AmmoCube(0, 1, 0), new FireStep(1, 1,
                    TargetGens.otherTarget(),
                    Effects.damageOther())),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(0, 1,
                    TargetGens.otherTarget(),
                    Effects.damageOther()), new FireStep(0, 1,
                    TargetGens.visibleFigures().and(TargetGens.differentFigures().not(TargetGens.inLastFigure())),
                    Effects.damageCurr(1)))
    };
    static final FireMode[] tractorBeamModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.atDistanceFromVisibleSquareFigures(2),
                    Effects.addCurrToLast()), new FireStep(1, 1,
                    TargetGens.atDistanceFromLastFigures(2).and(TargetGens.visibleSquares()),
                    Effects.moveLastToCurr().and(Effects.damageLast(1)))),
            new FireMode(new AmmoCube(1, 1, 0), new FireStep(1, 1,
                    TargetGens.atDistanceFigures(2),
                    Effects.addCurrFigureSquareToLast().and(Effects.moveCurrToLast().and(Effects.damageCurr(3)))))
    };
    static final FireMode[] thorModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(2).and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(1, 1,
                    TargetGens.visibleFromLastFigures().not(TargetGens.inLast()),
                    Effects.damageCurr(1).and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(1, 1,
                    TargetGens.visibleFromLastFigures().not(TargetGens.inLast()),
                    Effects.damageCurr(2)))
    };
    static final FireMode[] plasmaGunModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(2).and(Effects.addCurrToLast()))),
            new FireMode(new FireStep(1, 1,
                    TargetGens.atDistanceSquares(2),
                    Effects.moveToCurr())),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(0, 0,
                    TargetGens.inLastFigure(),
                    Effects.damageLast(1)))
    };
    static final FireMode[] whisperModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures().and(TargetGens.atDistanceFigures(2, -1)),
                    Effects.damageCurr(3).and(Effects.markCurr(1))))
    };
    static final FireMode[] vortexModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleSquares().and(TargetGens.differentSquares()),
                    Effects.addCurrToLast()), new FireStep(1, 1,
                    TargetGens.atDistanceFromLastFigures(1),
                    Effects.moveCurrToLast().and(Effects.damageCurr(2)).and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(1, 2,
                    TargetGens.atDistanceFromLastFigures(1).and(TargetGens.differentFigures().not(TargetGens.inLastFigure())),
                    Effects.moveCurrToLast().and(Effects.damageCurr(1))))
    };
    static final FireMode[] furnaceModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleRoom().and(TargetGens.differentRoom()),
                    Effects.damageCurr(1))),
            new FireMode(new FireStep(1, 1,
                    TargetGens.atDistanceSquares(1, 1),
                    Effects.damageCurr(1).and(Effects.markCurr(1))))
    };
    static final FireMode[] heatseekerModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.differentFigures().not(TargetGens.visibleFigures()),
                    Effects.damageCurr(3)))
    };
    static final FireMode[] hellionModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures().and(TargetGens.atDistanceFigures(1, -1)),
                    Effects.damageCurr(1).and(Effects.addCurrFigureSquareToLast().and(Effects.markCurr(1))))),
            new FireMode(new AmmoCube(1, 0, 0), new FireStep(1, 1,
                    TargetGens.visibleFigures().and(TargetGens.atDistanceFigures(1, -1)),
                    Effects.damageCurr(1).and(Effects.addCurrFigureSquareToLast().and(Effects.markCurr(2)))))
    };
    static final FireMode[] flamethrowerModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.onCardinalSquare().and(TargetGens.atDistanceSquares(1, 1)),
                            Effects.addCurrToLast()),
                    new FireStep(0, 1,
                            TargetGens.onLastFigures(),
                            Effects.damageCurr(1).and(Effects.clearLast().and(Effects.addShooterSquareToLast().and(Effects.addCurrFigureSquareToLast())))),
                    new FireStep(0, 1,
                            TargetGens.sameDirectionAsLastSquares().and(TargetGens.atDistanceFromLastSquares(2).not(TargetGens.inLast())),
                            Effects.clearLast().and(Effects.addCurrToLast())),
                    new FireStep(0, 1,
                            TargetGens.onLastFigures(),
                            Effects.damageCurr(1))
            ),
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.onCardinalSquare().and(TargetGens.atDistanceSquares(1, 1)),
                            Effects.damageCurr(2).and(Effects.addShooterSquareToLast().and(Effects.addCurrToLast()))),
                    new FireStep(0, 1,
                            TargetGens.sameDirectionAsLastSquares().and(TargetGens.atDistanceFromLastSquares(2).not(TargetGens.inLast())),
                            Effects.damageCurr(2))
            )
    };
    static final FireMode[] grenadeLauncherModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.visibleFigures(),
                            Effects.damageCurr(1).and(Effects.addCurrToLast())),
                    new FireStep(0, 1,
                            TargetGens.atDistanceFromLastFigureSquare(1, 1),
                            Effects.moveLastToCurr().and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(1, 0, 0),
                    new FireStep(1, 1,
                            TargetGens.visibleSquares(),
                            Effects.damageCurr(1)),
                    new FireStep(0, 1,
                            TargetGens.atDistanceFromLastFigureSquare(1, 1)
                                    .and(TargetGens.ifNotMoved()),
                            Effects.moveFirstLastToCurr()))
    };
    static final FireMode[] rocketLauncherModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.visibleFigures().and(TargetGens.atDistanceFigures(1, -1)),
                            Effects.damageCurr(2).and(Effects.addCurrFigureSquareToLast()).and(Effects.addCurrToLast())),
                    new FireStep(0, 1,
                            TargetGens.atDistanceFromLastSquares(1),
                            Effects.moveLastToCurr())
            ),
            new FireMode(new AmmoCube(0, 0, 1),
                    new FireStep(1, 1,
                            TargetGens.atDistanceSquares(2),
                            Effects.moveToCurr())),
            new FireMode(new AmmoCube(0, 1, 0),
                    new FireStep(0, 2,
                            TargetGens.inLast(),
                            Effects.damageLast(1)))
    };
    static final FireMode[] railgunModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.onCardinalFigures(),
                            Effects.damageCurr(3))
            ),
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.onCardinalFigures(),
                            Effects.damageCurr(2).and(Effects.addShooterSquareToLast().and(Effects.addCurrFigureSquareToLast().and(Effects.addCurrToLast())))),
                    new FireStep(1, 1,
                            TargetGens.sameDirectionAsLastFigures().not(TargetGens.inLast()),
                            Effects.damageCurr(2))
            )
    };
    static final FireMode[] cyberbladeModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.atDistanceFigures(0),
                            Effects.damageCurr(2).and(Effects.addCurrToLast()))),
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.atDistanceSquares(1),
                            Effects.moveToCurr())),
            new FireMode(new AmmoCube(0, 1, 0),
                    new FireStep(1, 1,
                            TargetGens.atDistanceFigures(0).not(TargetGens.inLast()),
                            Effects.damageCurr(2)))
    };
    static final FireMode[] zx2Modes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.visibleFigures(),
                            Effects.damageCurr(1).and(Effects.markCurr(2)))),
            new FireMode(
                    new FireStep(0, 3,
                            TargetGens.visibleFigures(),
                            Effects.markCurr(1)))
    };
    static final FireMode[] shotgunModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.atDistanceFigures(0),
                            Effects.damageCurr(3).and(Effects.addCurrToLast())),
                    new FireStep(0, 1,
                            TargetGens.atDistanceSquares(1),
                            Effects.moveLastToCurr())),
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.atDistanceFigures(1, 1),
                            Effects.damageCurr(2)))
    };
    static final FireMode[] powerGloveModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.atDistanceFigures(1, 1),
                            Effects.damageCurr(1).and(Effects.markCurr(2).and(Effects.addCurrFigureSquareToLast().and(Effects.moveToLast()))))
            ),
            new FireMode(new AmmoCube(0, 0, 1),
                    new FireStep(1, 1,
                            TargetGens.atDistanceSquares(1, 1),
                            Effects.addShooterSquareToLast().and(Effects.moveToCurr())),
                    new FireStep(1, 1,
                            TargetGens.atDistanceFigures(0),
                            Effects.damageCurr(2).and(Effects.addShooterSquareToLast())),
                    new FireStep(1, 1,
                            TargetGens.sameDirectionAsLastSquares().and(TargetGens.atDistanceSquares(1)).not(TargetGens.inLast()),
                            Effects.moveToCurr()),
                    new FireStep(1, 1,
                            TargetGens.atDistanceFigures(0).not(TargetGens.onLastFigures()),
                            Effects.damageCurr(2)))
    };
    static final FireMode[] shockwaveModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.atDistanceFigures(1, 1),
                            Effects.damageCurr(1).and(Effects.addCurrFigureSquareToLast())),
                    new FireStep(0, 1,
                            TargetGens.atDistanceFigures(1, 1).not(TargetGens.onLastFigures()),
                            Effects.damageCurr(1).and(Effects.addCurrFigureSquareToLast())),
                    new FireStep(0, 1,
                            TargetGens.atDistanceFigures(1, 1).not(TargetGens.onLastFigures()),
                            Effects.damageCurr(1))),
            new FireMode(new AmmoCube(0, 1, 0),
                    new FireStep(1, 1,
                            TargetGens.atDistanceSquares(1),
                            Effects.damageNeighbours(1)))
    };
    static final FireMode[] sledgehammer = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.atDistanceFigures(0),
                            Effects.damageCurr(2))
            ),
            new FireMode(new AmmoCube(1, 0, 0),
                    new FireStep(1, 1,
                            TargetGens.atDistanceFigures(0),
                            Effects.damageCurr(3).and(Effects.addCurrToLast())),
                    new FireStep(1, 1,
                            TargetGens.onCardinalSquare().and(TargetGens.atDistanceSquares(2)),
                            Effects.moveLastToCurr()))
    };

    private FireModes() {
    }

}
