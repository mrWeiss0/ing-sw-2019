package model.board;

public class SpawnSquareMock extends SpawnSquare {
    public SpawnSquareMock(Room room) {
        super(room, new int[]{0, 0});
    }

    public SpawnSquareMock(Room room, int capacity) {
        super(room, new int[]{0, 0}, capacity);
    }
}
