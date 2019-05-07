package model.mock;

import model.board.AmmoSquare;
import model.board.Room;

public class AmmoSquareMock extends AmmoSquare {
    public AmmoSquareMock(Room room) {
        super(room, new int[]{0, 0});
    }
}
