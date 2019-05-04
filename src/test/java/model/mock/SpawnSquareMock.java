package model.mock;

import model.Room;
import model.SpawnSquare;

public class SpawnSquareMock extends SpawnSquare {
    public SpawnSquareMock(Room room) {
        super(room, new int[]{0, 0});
    }
}
