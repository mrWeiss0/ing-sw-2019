package model.mock;

import model.Room;
import model.SpawnSquare;

public class MockSpawnSquare extends SpawnSquare {
    public MockSpawnSquare(Room room) {
        super(room, new int[]{0, 0});
    }
}
