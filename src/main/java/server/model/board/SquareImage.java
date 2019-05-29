package server.model.board;

@SuppressWarnings("squid:ClassVariableVisibilityCheck")
public class SquareImage {
    public int id;
    public boolean spawn;
    public int[] coords;
    public int roomId;
    public int color;
    public int[] adjacent = new int[]{};

    public SquareImage setId(int id) {
        this.id = id;
        return this;
    }

    public SquareImage setSpawn() {
        spawn = true;
        return this;
    }

    public SquareImage setCoords(int... coords) {
        this.coords = coords;
        return this;
    }

    public SquareImage setRoomId(int roomId) {
        this.roomId = roomId;
        return this;
    }

    public SquareImage setAdjacent(int... adjacent) {
        this.adjacent = adjacent;
        return this;
    }

    public SquareImage setColor(int color) {
        this.color = color;
        return this;
    }
}
