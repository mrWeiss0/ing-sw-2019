package tools;

import model.board.*;

import java.util.*;

class BoardBuilder {
    private final List<Room> rooms = new ArrayList<>();
    private final JsonSquare[] jsonSquares;
    private final Map<Integer, AbstractSquare> squaresMap = new HashMap<>();
    private final Board board = new Board();

    BoardBuilder(JsonSquare[] jsonSquares) {
        this.jsonSquares = jsonSquares;
    }

    Room getRoom(int i) {
        Room roomObj;
        if (i >= rooms.size())
            rooms.addAll(Collections.nCopies(i - rooms.size() + 1, null));
        if ((roomObj = rooms.get(i)) == null) {
            roomObj = new Room();
            rooms.set(i, roomObj);
        }
        return roomObj;
    }

    AbstractSquare getSquare(int i) {
        return squaresMap.get(i);
    }

    void addSquare(int id, AbstractSquare square) throws MalformedDataException {
        if (squaresMap.containsKey(id))
            throw new MalformedDataException("Duplicate cell ID " + id);
        squaresMap.put(id, square);
        board.addSquare(square);
    }

    Board build() throws MalformedDataException {
        for (JsonSquare s : jsonSquares)
            s.build(this);
        for (JsonSquare s : jsonSquares)
            s.connect(this);
        return board;
    }
}

@SuppressWarnings("unused")
class JsonSquare {
    private int id;
    private boolean spawn;
    private int[] coords;
    private int roomId;
    private int[] adjacent;
    private AbstractSquare squareObj;

    private JsonSquare() {
    }

    void build(BoardBuilder boardBuilder) throws MalformedDataException {
        Room room = boardBuilder.getRoom(roomId);
        if (coords == null)
            throw new MalformedDataException("Missing coordinates in square " + id);
        if (coords.length != 2)
            throw new MalformedDataException("Cell " + id + " has " + coords.length + " coordinate");
        squareObj = spawn ?
                new SpawnSquare(room, coords, 3) :
                new AmmoSquare(room, coords);
        boardBuilder.addSquare(id, squareObj);
    }

    void connect(BoardBuilder boardBuilder) {
        if (adjacent != null)
            for (int i : adjacent)
                boardBuilder.getSquare(i).connect(squareObj);
    }
}
