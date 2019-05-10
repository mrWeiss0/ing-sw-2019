package model.board;

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
        this.rooms = Collections.unmodifiableSet(builder.rooms.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
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
        private final List<Room> rooms = new ArrayList<>();
        private final Map<Integer, AbstractSquare> squaresMap = new HashMap<>();
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

        public Board build() {
            SquareImage[] squareImages = squareImagesSupplier.get();
            for (SquareImage s : squareImages)
                s.build(this);
            for (SquareImage s : squareImages)
                s.connect(this);
            return new Board(this);
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

        int getCapacity() {
            return capacity;
        }

        AbstractSquare getSquare(int i) {
            return squaresMap.get(i);
        }

        void addSquare(int id, AbstractSquare square) {
            if (squaresMap.containsKey(id))
                throw new IllegalArgumentException("Duplicate cell ID " + id);
            squaresMap.put(id, square);
        }
    }
}
