package tools;

import model.*;

import java.util.*;

class BoardBuilder {
    private List<Room> rooms = new ArrayList<>();
    private JsonSquare[] jsonSquares;
    private Map<Integer, AbstractSquare> squaresMap = new HashMap<>();
    private Board board = new Board();

    BoardBuilder(JsonSquare[] jsonSquares) {
        this.jsonSquares = jsonSquares;
    }

    Room getRoom(int i) {
        Room roomObj;
        if (i >= rooms.size()) {
            rooms.addAll(Collections.nCopies(i - rooms.size() + 1, null));
        }
        if ((roomObj = rooms.get(i)) == null) {
            rooms.set(i, roomObj = new Room());
        }
        return roomObj;
    }

    AbstractSquare getSquare(int i) {
        return squaresMap.get(i);
    }

    void addSquare(int ID, AbstractSquare square) throws MalformedDataException {
        if (squaresMap.containsKey(ID)) throw new MalformedDataException("Duplicate cell ID " + ID);
        squaresMap.put(ID, square);
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

class JsonSquare {
    private int ID;
    private boolean spawn;
    private int[] coords;
    private int roomID;
    private int[] adjacent;
    private AbstractSquare squareObj;

    private JsonSquare() {
    }

    void build(BoardBuilder boardBuilder) throws MalformedDataException {
        Room room = boardBuilder.getRoom(roomID);
        if (coords == null) throw new MalformedDataException("Missing coordinates in square " + ID);
        if (coords.length != 2)
            throw new MalformedDataException("Cell " + ID + " has " + coords.length + " coordinate");
        squareObj = spawn ?
                new SpawnSquare(room, coords) :
                new AmmoSquare(room, coords);
        boardBuilder.addSquare(ID, squareObj);
    }

    void connect(BoardBuilder boardBuilder) {
        if (adjacent != null) for (int i : adjacent)
            boardBuilder.getSquare(i).connect(squareObj);
    }
}