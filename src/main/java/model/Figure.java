package model;

public class Figure implements Targettable{
    private AbstractSquare square;
    @Override
    public void doDamage(Figure dealer) {

    }

    @Override
    public void doMark(Figure dealer) {

    }

    public AbstractSquare getSquare() {
        return square;
    }


}
