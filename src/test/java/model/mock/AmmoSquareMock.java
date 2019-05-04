package model.mock;

import model.AmmoSquare;
import model.Room;

public class AmmoSquareMock extends AmmoSquare {
    public AmmoSquareMock(Room room) {
        super(room, new int[]{0, 0});
    }
}
