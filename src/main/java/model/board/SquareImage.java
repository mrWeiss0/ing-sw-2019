package model.board;

public class SquareImage {
    private int id;
    private boolean spawn;
    private int[] coords;
    private int roomId;
    private int[] adjacent = new int[]{};
    private AbstractSquare squareObj;

    public SquareImage id(int id) {
        this.id = id;
        return this;
    }

    public SquareImage spawn(boolean spawn) {
        this.spawn = spawn;
        return this;
    }

    public SquareImage coords(int[] coords) {
        this.coords = coords;
        return this;
    }

    public SquareImage roomId(int roomId) {
        this.roomId = roomId;
        return this;
    }

    public SquareImage adjacent(int[] adjacent) {
        this.adjacent = adjacent;
        return this;
    }


    void build(Board.Builder builder) {
        Room room = builder.getRoom(roomId);
        if (coords == null)
            throw new IllegalArgumentException("Missing coordinates in square " + id);
        if (coords.length != 2)
            throw new IllegalArgumentException("Cell " + id + " has " + coords.length + " coordinate");
        squareObj = spawn ?
                new SpawnSquare(room, coords, builder.getCapacity()) :
                new AmmoSquare(room, coords);
        builder.addSquare(id, squareObj);
    }

    void connect(Board.Builder builder) {
        for (int i : adjacent)
            builder.getSquare(i).connect(squareObj);
    }
}
