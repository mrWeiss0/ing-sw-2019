package model.mock;

import model.AmmoSquare;
import model.Room;

public class MockAmmoSquare extends AmmoSquare {
    public MockAmmoSquare(Room room) {
        super(room, new int[]{0, 0});
    }
}
