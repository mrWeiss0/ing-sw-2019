package model;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Figure implements Targettable {
    private AbstractSquare square;
    private int[] ammocube= new int[3];
    private List<Player> damages;
    private List<Player> marks;
    private int deaths;
    private Set<Weapon> weapons;
    private Set<PowerUp> powerUps;

    public void doDamage(Player dealer) {
        if(damages.size()<12) damages.add(dealer);
    }

    @Override
    public void doMark(Player dealer) {
        if(marks.stream().filter(Predicate.isEqual(dealer)).count()<3) marks.add(dealer);
    }

    public AbstractSquare getSquare() {
        return square;
    }
    public void moveTo(AbstractSquare square) {
        this.square = square;
    }

    public List<Player> getDamages() {
        return damages;
    }

    public List<Player> getMarks() {
        return marks;
    }
}
