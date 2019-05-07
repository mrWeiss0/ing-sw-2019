package model.mock;

import model.board.Room;
import model.board.SpawnSquare;

public class SpawnSquareMock extends SpawnSquare {
    public SpawnSquareMock(Room room) {
        super(room, new int[]{0, 0});
    }
}
