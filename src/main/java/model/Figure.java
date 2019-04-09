package model;

import java.util.*;


public class Figure implements Targettable {
    private AbstractSquare square;
    private int[] ammocube = new int[3];
    private List<Player> damages;
    private HashMap<Player, Integer> marks;
    private int deaths;
    private Set<Weapon> weapons;
    private Set<PowerUp> powerUps;

    public Figure() {
        damages = new ArrayList<>();
        marks = new HashMap<>();
        weapons = new HashSet<>();
        powerUps = new HashSet<>();

    }

    @Override
    public void doDamage(Player dealer, int n) {
        damages.addAll(Collections.nCopies(Integer.min(n + marks.getOrDefault(dealer, 0), 12 - damages.size()), dealer));
        marks.put(dealer, 0);
    }

    @Override
    public void doMark(Player dealer, int n) {
        marks.put(dealer, Integer.min(marks.getOrDefault(dealer, 0) + n, 3));
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

    public HashMap<Player, Integer> getMarks() {
        return marks;
    }
}
