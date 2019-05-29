package server.model.board;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <code>Board</code> represents the game board and its square.
 * <p>
 * It provides methods to allow refilling using the visitor pattern.
 */
public class Board {
    private final Set<Room> rooms;
    private final Set<AbstractSquare> squares;
    private final Set<Figure> figures;

    private Board(Builder builder) {
        this.rooms = Collections.unmodifiableSet(new HashSet<>(builder.roomsMap.values()));
        this.squares = Collections.unmodifiableSet(new HashSet<>(builder.squaresMap.values()));
        this.figures = Collections.unmodifiableSet(builder.figures);
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public Set<AbstractSquare> getSquares() {
        return squares;
    }

    public Set<Figure> getFigures() {
        return figures;
    }

    public Set<Figure> getDamaged() {
        return figures.stream().filter(Figure::isDamaged).collect(Collectors.toSet());
    }

    public void clearDamaged() {
        figures.forEach(Figure::clearDamaged);
    }

    public void applyMarks() {
        figures.forEach(Figure::applyMarks);
    }

    public static class Builder {
        private final Map<Integer, Room> roomsMap = new HashMap<>();
        private final Map<Integer, AbstractSquare> squaresMap = new HashMap<>();
        private final Map<Integer, SpawnSquare> spawnColorMap = new HashMap<>();
        private final Set<Figure> figures = new HashSet<>();
        private Supplier<SquareImage[]> squareImagesSupplier = () -> new SquareImage[]{};
        private int capacity = 1;

        public Builder squares(SquareImage... squareImages) {
            return squares(() -> squareImages);
        }

        public Builder squares(Supplier<SquareImage[]> supplier) {
            squareImagesSupplier = supplier;
            return this;
        }

        public Builder spawnCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder figures(Figure... figures) {
            return figures(Arrays.asList(figures));
        }

        public Builder figures(Collection<Figure> figures) {
            this.figures.addAll(figures);
            return this;
        }

        public SpawnSquare getSpawnByColor(int color) {
            if (!spawnColorMap.containsKey(color))
                throw new IllegalArgumentException("No spawn has color " + color);
            return spawnColorMap.get(color);
        }

        public Board build() {
            SquareImage[] squareImages = squareImagesSupplier.get();
            for (SquareImage s : squareImages) {
                if (s.coords == null)
                    throw new IllegalArgumentException("Missing coordinates in square " + s.id);
                if (s.coords.length != 2)
                    throw new IllegalArgumentException("Cell " + s.id + " has " + s.coords.length + " coordinate");
                addSquare(s.id, createSquare(s, getRoom(s.roomId)));
            }
            for (SquareImage s : squareImages)
                for (int i : s.adjacent)
                    squaresMap.get(s.id).connect(squaresMap.get(i));
            return new Board(this);
        }

        private AbstractSquare createSquare(SquareImage s, Room room) {
            AbstractSquare squareObj;
            if (s.spawn) {
                squareObj = new SpawnSquare(room, s.coords, capacity);
                if (spawnColorMap.containsKey(s.color))
                    throw new IllegalArgumentException("Two spawns have same color " + s.color);
                spawnColorMap.put(s.color, (SpawnSquare) squareObj);
            } else
                squareObj = new AmmoSquare(room, s.coords);
            return squareObj;
        }

        private Room getRoom(int i) {
            if (!roomsMap.containsKey(i))
                roomsMap.put(i, new Room());
            return roomsMap.get(i);
        }

        private void addSquare(int id, AbstractSquare square) {
            if (squaresMap.containsKey(id))
                throw new IllegalArgumentException("Duplicate cell ID " + id);
            squaresMap.put(id, square);
        }
    }
}
