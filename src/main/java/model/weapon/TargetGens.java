package model.weapon;

import java.util.stream.Collectors;


public enum TargetGens{

    VORTEX_BASE(){
        @Override
        public TargetGen get(int... params) {
            return (shooter, board, last)-> shooter.getSquare().visibleSquares().stream().filter(t -> t != shooter.getSquare()).collect(Collectors.toSet());
        }
    }
    , VISIBLE(){
        @Override
        public TargetGen get(int... params) {
            return  (shooter, board, last)->shooter.getSquare().visibleFigures().stream().filter(t -> t != shooter).collect(Collectors.toSet());
        }
    };

    public abstract TargetGen get(int ... params);
}
