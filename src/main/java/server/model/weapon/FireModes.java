package server.model.weapon;

import server.model.AmmoCube;

public class FireModes {
    public static FireMode[] lockRifleModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(2).and(Effects.markCurr(1).and(Effects.addCurrToLast())))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(1, 1,
                    TargetGens.visibleFigures().and(TargetGens.differentFigures().less(TargetGens.inLastFigure())),
                    Effects.markCurr(1)))
    };

    public static FireMode[] electroscytheModes = new FireMode[]{
        new FireMode(new FireStep(1,1,
                TargetGens.maxDistanceSquares(0),
                Effects.damageCurr(1))),
            new FireMode(new AmmoCube(1,0,1),new FireStep(1,1,
                    TargetGens.maxDistanceSquares(0),
                    Effects.damageCurr(2)))
    };

    public static FireMode[] machineGunModes = new FireMode[]{
            new FireMode(new FireStep(1,2,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(1).and(Effects.addCurrToLast().and(Effects.fillLastToSize(2))))),
            new FireMode(new AmmoCube(0,1,0), new FireStep(1, 1,
                    TargetGens.otherTarget(),
                    Effects.damageOther(1))),
            new FireMode(new AmmoCube(0,0,1), new FireStep(0, 1,
                    TargetGens.otherTarget(),
                    Effects.damageOther(1)), new FireStep(0, 1,
                    TargetGens.visibleFigures().and(TargetGens.differentFigures().less(TargetGens.inLastFigure())),
                    Effects.damageCurr(1)))
    };

    public static FireMode[] tractorBeamModes = new FireMode[]{
            new FireMode(new FireStep(1,1,
                    TargetGens.atDistanceFromVisibleSquareFigures(2),
                    Effects.addCurrToLast()), new FireStep(1, 1,
                    TargetGens.maxDistanceFromLastFigures(2).and(TargetGens.visibleSquares()),
                    Effects.moveLastToCurr().and(Effects.damageLast(1)))),
            new FireMode(new AmmoCube(1,1,0),new FireStep(1,1,
                    TargetGens.maxDistanceFigures(2),
                    Effects.addCurrFigureSquareToLast().and(Effects.moveCurrToLast().and(Effects.damageCurr(3)))))
    };

    public static FireMode[] thorModes = new FireMode[]{
            new FireMode(new FireStep(1,1,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(2).and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(0,0,1), new FireStep(1,1,
                    TargetGens.visibleFromLastFigures().less(TargetGens.inLast()),
                    Effects.damageCurr(1).and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(0,0,1), new FireStep(1,1,
                    TargetGens.visibleFromLastFigures().less(TargetGens.inLast()),
                    Effects.damageCurr(2)))
    };

    public static FireMode[] plasmaGunModes = new FireMode[]{
            new FireMode(new FireStep(1,1,
                    TargetGens.visibleFigures(),
                    Effects.damageCurr(2).and(Effects.addCurrToLast()))),
            new FireMode(new FireStep(1,1,
                    TargetGens.maxDistanceSquares(2),
                    Effects.moveToCurr())),
            new FireMode(new AmmoCube(0,0,1), new FireStep(0,0,
                    TargetGens.inLastFigure(),
                    Effects.damageLast(1)))
    };

    public static FireMode[] whisperModes = new FireMode[]{
            new FireMode(new FireStep(1,1,
                    TargetGens.visibleFigures().less(TargetGens.maxDistanceFigures(1)),
                    Effects.damageCurr(3).and(Effects.markCurr(1))))
    };

    public static FireMode[] vortexModes = new FireMode[]{
            new FireMode(new FireStep(1, 1,
                    TargetGens.visibleSquares().and(TargetGens.differentSquares()),
                    Effects.addCurrToLast()), new FireStep(1, 1,
                    TargetGens.maxDistanceFromLastFigures(1),
                    Effects.moveCurrToLast().and(Effects.damageCurr(2)).and(Effects.addCurrToLast()))),
            new FireMode(new AmmoCube(0, 0, 1), new FireStep(1, 2,
                    TargetGens.maxDistanceFromLastFigures(1).and(TargetGens.differentFigures().less(TargetGens.inLastFigure())),
                    Effects.moveCurrToLast().and(Effects.damageCurr(1))))
    };

    public static FireMode[] furnaceModes = new FireMode[]{
            new FireMode(new FireStep(1,1,
                    TargetGens.visibleRoom().and(TargetGens.differentRoom()),
                    Effects.damageCurr(1))),
            new FireMode(new FireStep(1,1,
                    TargetGens.maxDistanceSquares(1).less(TargetGens.maxDistanceFigures(0)),
                    Effects.damageCurr(1).and(Effects.markCurr(1))))
    };

    public static FireMode[] heatseekerModes = new FireMode[]{
            new FireMode(new FireStep(1,1,
                    TargetGens.differentFigures().less(TargetGens.visibleFigures()),
                    Effects.damageCurr(3)))
    };

    public static FireMode[] hellionModes = new FireMode[]{
            new FireMode(new FireStep(1,1,
                    TargetGens.visibleFigures().less(TargetGens.maxDistanceFigures(0)),
                    Effects.damageCurr(1).and(Effects.addCurrFigureSquareToLast().and(Effects.markCurr(1))))),
            new FireMode(new AmmoCube(1,0,0), new FireStep(1,1,
                    TargetGens.visibleFigures().less(TargetGens.maxDistanceFigures(0)),
                    Effects.damageCurr(1).and(Effects.addCurrFigureSquareToLast().and(Effects.markCurr(2)))))
    };
}
