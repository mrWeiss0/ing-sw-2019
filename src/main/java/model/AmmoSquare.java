package model;

public class AmmoSquare extends AbstractSquare {

    /**
     * sole constructor
     *
     * @param room the <code>Room</code> this square belongs to
     */
    private AmmoTile ammoTile;
    public AmmoSquare(Room room) {
        super(room);
    }

    @Override
    public void accept(Game game) {

    }

    @Override
    public void grab(Figure grabber) {

    }
}
