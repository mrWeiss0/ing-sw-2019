package server.model.weapon;

import server.model.AmmoCube;


public class FireModes {
    static final FireMode[] lockRifleModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(2).and(Effects.markCurr(1).and(Effects.addCurrToLast())))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(1, 1,
                    TargetGens.visibleFigures().and(TargetGens.differentFigures().less(TargetGens.inLastFigure())),
                    Effects.markCurr(1)))
    };

    static final FireMode[] electroscytheModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.maxDistanceSquares(0),
                    Effects.damageCurr(1))),
            new FireMode(new AmmoCube(1, 0, 1), new FireStep(1, 1,
                    TargetGens.maxDistanceSquares(0),
                    Effects.damageCurr(2)))
    };

    static final FireMode[] machineGunModes = new FireMode[]{
            new FireMode(new FireStep(1, 2,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(1).and(Effects.addCurrToLast().and(Effects.fillLastToSize(2))))),
            new FireMode(new AmmoCube(0, 1, 0), new FireStep(1, 1,
                    TargetGens.otherTarget(),
                    Effects.damageOther(1))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(0, 1,
                    TargetGens.otherTarget(),
                    Effects.damageOther(1)), new FireStep(0, 1,
                    TargetGens.visibleFigures().and(TargetGens.differentFigures().less(TargetGens.inLastFigure())),
                    Effects.damageCurr(1)))
    };

    static final FireMode[] tractorBeamModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.atDistanceFromVisibleSquareFigures(2),
                    Effects.addCurrToLast()), new FireStep(1, 1,
                    TargetGens.maxDistanceFromLastFigures(2).and(TargetGens.visibleSquares()),
                    Effects.moveLastToCurr().and(Effects.damageLast(1)))),
            new FireMode(new AmmoCube(1, 1, 0), new FireStep(1, 1,
                    TargetGens.maxDistanceFigures(2),
                    Effects.addCurrFigureSquareToLast().and(Effects.moveCurrToLast().and(Effects.damageCurr(3)))))
    };

    static final FireMode[] thorModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(2).and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(1, 1,
                    TargetGens.visibleFromLastFigures().less(TargetGens.inLast()),
                    Effects.damageCurr(1).and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(1, 1,
                    TargetGens.visibleFromLastFigures().less(TargetGens.inLast()),
                    Effects.damageCurr(2)))
    };

    static final FireMode[] plasmaGunModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(2).and(Effects.addCurrToLast()))),
            new FireMode(new FireStep(1, 1,
                    TargetGens.maxDistanceSquares(2),
                    Effects.moveToCurr())),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(0, 0,
                    TargetGens.inLastFigure(),
                    Effects.damageLast(1)))
    };

    static final FireMode[] whisperModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures().less(TargetGens.maxDistanceFigures(1)),
                    Effects.damageCurr(3).and(Effects.markCurr(1))))
    };

    static final FireMode[] vortexModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleSquares().and(TargetGens.differentSquares()),
                    Effects.addCurrToLast()), new FireStep(1, 1,
                    TargetGens.maxDistanceFromLastFigures(1),
                    Effects.moveCurrToLast().and(Effects.damageCurr(2)).and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(1, 2,
                    TargetGens.maxDistanceFromLastFigures(1).and(TargetGens.differentFigures().less(TargetGens.inLastFigure())),
                    Effects.moveCurrToLast().and(Effects.damageCurr(1))))
    };

    static final FireMode[] furnaceModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleRoom().and(TargetGens.differentRoom()),
                    Effects.damageCurr(1))),
            new FireMode(new FireStep(1, 1,
                    TargetGens.maxDistanceSquares(1).less(TargetGens.maxDistanceFigures(0)),
                    Effects.damageCurr(1).and(Effects.markCurr(1))))
    };

    static final FireMode[] heatseekerModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.differentFigures().less(TargetGens.visibleFigures()),
                    Effects.damageCurr(3)))
    };

    static final FireMode[] hellionModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures().less(TargetGens.maxDistanceFigures(0)),
                    Effects.damageCurr(1).and(Effects.addCurrFigureSquareToLast().and(Effects.markCurr(1))))),
            new FireMode(new AmmoCube(1, 0, 0), new FireStep(1, 1,
                    TargetGens.visibleFigures().less(TargetGens.maxDistanceFigures(0)),
                    Effects.damageCurr(1).and(Effects.addCurrFigureSquareToLast().and(Effects.markCurr(2)))))
    };

    static final FireMode[] flamethrowerModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.onCardinalSquare().and(TargetGens.maxDistanceSquares(1).less(TargetGens.maxDistanceSquares(0))),
                            Effects.addCurrToLast()),
                    new FireStep(0, 1,
                            TargetGens.onLastFigures(),
                            Effects.damageCurr(1).and(Effects.clearLast().and(Effects.addShooterSquareToLast().and(Effects.addCurrFigureSquareToLast())))),
                    new FireStep(0, 1,
                            TargetGens.sameDirectionAsLastSquares().and(TargetGens.maxDistanceFromLastSquares(2).less(TargetGens.inLast())),
                            Effects.clearLast().and(Effects.addCurrToLast())),
                    new FireStep(0, 1,
                            TargetGens.onLastFigures(),
                            Effects.damageCurr(1))
            ),
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.onCardinalSquare().and(TargetGens.maxDistanceSquares(1).less(TargetGens.maxDistanceSquares(0))),
                            Effects.damageCurr(2).and(Effects.addShooterSquareToLast().and(Effects.addCurrToLast()))),
                    new FireStep(0, 1,
                            TargetGens.sameDirectionAsLastSquares().and(TargetGens.maxDistanceFromLastSquares(2).less(TargetGens.inLast())),
                            Effects.damageCurr(2))
            )
    };

    static final FireMode[] grenadeLauncherModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.visibleFigures(),
                            Effects.damageCurr(1).and(Effects.addCurrToLast())),
                    new FireStep(0, 1,
                            TargetGens.maxDistanceFromLastFigureSquare(1).less(TargetGens.maxDistanceFromLastFigureSquare(0)),
                            Effects.moveLastToCurr().and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(1, 0, 0),
                    new FireStep(1, 1,
                            TargetGens.visibleSquares(),
                            Effects.damageCurr(1)),
                    new FireStep(0, 1,
                            TargetGens.maxDistanceFromLastFigureSquare(1).less(TargetGens.maxDistanceFromLastFigureSquare(0))
                                    .and(TargetGens.ifNotMoved()),
                            Effects.moveFirstLastToCurr()))
    };

    static final FireMode[] rocketLauncherModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.visibleFigures().less(TargetGens.maxDistanceFigures(0)),
                            Effects.damageCurr(2).and(Effects.addCurrFigureSquareToLast()).and(Effects.addCurrToLast())),
                    new FireStep(0, 1,
                            TargetGens.maxDistanceFromLastSquares(1),
                            Effects.moveLastToCurr())
            ),
            new FireMode(new AmmoCube(0, 0, 1),
                    new FireStep(1, 1,
                            TargetGens.maxDistanceSquares(2),
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
                            TargetGens.sameDirectionAsLastFigures().less(TargetGens.inLast()),
                            Effects.damageCurr(2))
            )
    };

    static final FireMode[] cyberbladeModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.maxDistanceFigures(0),
                            Effects.damageCurr(2).and(Effects.addCurrToLast()))),
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.maxDistanceSquares(1),
                            Effects.moveToCurr())),
            new FireMode(new AmmoCube(0, 1, 0),
                    new FireStep(1, 1,
                            TargetGens.maxDistanceFigures(0).less(TargetGens.inLast()),
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
                            TargetGens.maxDistanceFigures(0),
                            Effects.damageCurr(3).and(Effects.addCurrToLast())),
                    new FireStep(0, 1,
                            TargetGens.maxDistanceSquares(1),
                            Effects.moveLastToCurr())),
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.maxDistanceFigures(1).less(TargetGens.maxDistanceFigures(0)),
                            Effects.damageCurr(2)))
    };

    static final FireMode[] powerGloveModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.maxDistanceFigures(1).less(TargetGens.maxDistanceFigures(0)),
                            Effects.damageCurr(1).and(Effects.markCurr(2).and(Effects.addCurrFigureSquareToLast().and(Effects.moveToLast()))))
            ),
            new FireMode(new AmmoCube(0, 0, 1),
                    new FireStep(1, 1,
                            TargetGens.maxDistanceSquares(1).less(TargetGens.maxDistanceSquares(0)),
                            Effects.addShooterSquareToLast().and(Effects.moveToCurr())),
                    new FireStep(1, 1,
                            TargetGens.maxDistanceFigures(0),
                            Effects.damageCurr(2).and(Effects.addShooterSquareToLast())),
                    new FireStep(1, 1,
                            TargetGens.sameDirectionAsLastSquares().and(TargetGens.maxDistanceSquares(1).less(TargetGens.inLast())),
                            Effects.moveToCurr()),
                    new FireStep(1, 1,
                            TargetGens.maxDistanceFigures(0).less(TargetGens.onLastFigures()),
                            Effects.damageCurr(2)))
    };

    static final FireMode[] shockwaveModes = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.maxDistanceFigures(1).less(TargetGens.maxDistanceFigures(0)),
                            Effects.damageCurr(1).and(Effects.addCurrFigureSquareToLast())),
                    new FireStep(0, 1,
                            TargetGens.maxDistanceFigures(1).less(TargetGens.onLastFigures()).less(TargetGens.maxDistanceFigures(0)),
                            Effects.damageCurr(1).and(Effects.addCurrFigureSquareToLast())),
                    new FireStep(0, 1,
                            TargetGens.maxDistanceFigures(1).less(TargetGens.maxDistanceFigures(0)).less(TargetGens.onLastFigures()),
                            Effects.damageCurr(1))),
            new FireMode(new AmmoCube(0, 1, 0),
                    new FireStep(1, 1,
                            TargetGens.maxDistanceSquares(1),
                            Effects.damageNeighbours(1)))
    };

    static final FireMode[] sledgehammer = new FireMode[]{
            new FireMode(
                    new FireStep(1, 1,
                            TargetGens.maxDistanceFigures(0),
                            Effects.damageCurr(2))
            ),
            new FireMode(new AmmoCube(1, 0, 0),
                    new FireStep(1, 1,
                            TargetGens.maxDistanceFigures(0),
                            Effects.damageCurr(3).and(Effects.addCurrToLast())),
                    new FireStep(1, 1,
                            TargetGens.onCardinalSquare().and(TargetGens.maxDistanceSquares(2)),
                            Effects.moveLastToCurr()))
    };

}
