package model.weapon;

public class Effects {

    public static Effect damageCurr(int n){
        return (shooter, currentTargets, lastTargets) -> currentTargets.forEach(x->x.damageFrom(shooter,n));
    }

    public static Effect markCurr(int n){
        return (shooter, currentTargets, lastTargets) -> currentTargets.forEach(x->x.markFrom(shooter,n));
    }

    public static Effect damageSameSquare(int n){
        return (shooter, currentTargets, lastTargets) -> shooter.getSquare().damageFrom(shooter, n);
    }

    public static Effect addCurrToLast(){
        return (shooter, currentTargets, lastTargets) -> lastTargets.addAll(currentTargets);
    }
}
