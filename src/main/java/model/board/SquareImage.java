package model.board;

public class SquareImage {
    private int id;
    private boolean spawn;
    private int[] coords;
    private int roomId;
    private int[] adjacent = new int[]{};
    private Board.Builder boardBuilder;

    public SquareImage id(int id) {
        this.id = id;
        return this;
    }

    public SquareImage spawn() {
        spawn = true;
        return this;
    }

    public SquareImage coords(int... coords) {
        this.coords = coords;
        return this;
    }

    public SquareImage roomId(int roomId) {
        this.roomId = roomId;
        return this;
    }

    public SquareImage adjacent(int... adjacent) {
        this.adjacent = adjacent;
        return this;
    }

    public SquareImage boardBuilder(Board.Builder boardBuilder) {
        this.boardBuilder = boardBuilder;
        return this;
    }

    AbstractSquare build() {
        Room room = boardBuilder.getRoom(roomId);
        if (coords == null)
            throw new IllegalArgumentException("Missing coordinates in square " + id);
        if (coords.length != 2)
            throw new IllegalArgumentException("Cell " + id + " has " + coords.length + " coordinate");
        AbstractSquare squareObj = spawn ?
                new SpawnSquare(room, coords, boardBuilder.getCapacity()) :
                new AmmoSquare(room, coords);
        boardBuilder.addSquare(id, squareObj);
        return squareObj;
    }

    void connect() {
        for (int i : adjacent)
            boardBuilder.getSquare(id).connect(boardBuilder.getSquare(i));
    }
}
