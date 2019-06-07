package server.model.board;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * The <code>Board</code> class represents the game board, its rooms, squares
 * and figures; these are <code>final</code> as to reflect a Weapon's static
 * nature in the game.
 * <p>
 * The class also provides a <code>Builder</code> to allow the board's setup
 * and creation. A <code>Board</code> may only be instantiated through the
 * <code>Board.Builder</code> class.
 * <p>
 * It provides methods to keep track of damaged figures and applying marks to
 * them.
 */
public class Board {
    private final List<Room> rooms;
    private final List<AbstractSquare> squares;
    private final List<Figure> figures;

    private Board(Builder builder) {
        this.rooms = builder.roomsMap.values().stream().sorted(Comparator.comparingInt(Object::hashCode)).collect(Collectors.toList());
        this.squares = builder.squaresMap.values().stream().sorted(Comparator.comparingInt(Object::hashCode)).collect(Collectors.toList());
        this.figures = builder.figures.stream().sorted(Comparator.comparingInt(Object::hashCode)).collect(Collectors.toList());
    }

    /**
     * Returns this board's rooms.
     *
     * @return this board's rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Returns this board's squares.
     *
     * @return this board's squares
     */
    public List<AbstractSquare> getSquares() {
        return squares;
    }

    /**
     * Returns this board's figures,
     *
     * @return this board's figures
     */
    public List<Figure> getFigures() {
        return figures;
    }

    /**
     * Returns the set of damaged figures.
     *
     * @return the set of damaged figures
     */
    public Set<Figure> getDamaged() {
        return figures.stream().filter(Figure::isDamaged).collect(Collectors.toSet());
    }

    /**
     * Applies to each figure's their newly acquired marks
     */
    public void applyMarks() {
        figures.forEach(Figure::applyMarks);
    }

    /**
     * The <code>Board.Builder</code> class allows the construction of a new
     * <code>Board</code>.
     */
    public static class Builder {
        private final Map<Integer, Room> roomsMap = new HashMap<>();
        private final Map<Integer, AbstractSquare> squaresMap = new HashMap<>();
        private final Map<Integer, SpawnSquare> spawnColorMap = new HashMap<>();
        private final List<Figure> figures = new ArrayList<>();
        private Supplier<SquareImage[]> squareImagesSupplier = () -> new SquareImage[]{};
        private int capacity = 1;

        /**
         * Returns this builder with the specified <code>SquareImages</code>
         * set as its supplier.
         *
         * @param squareImages the images to set as a supplier
         * @return this builder
         */
        public Builder squares(SquareImage... squareImages) {
            return squares(() -> squareImages);
        }

        /**
         * Returns this builder with the specified <code>SquareImage</code>
         * supplier set.
         *
         * @param supplier the supplier to be set
         * @return this builder
         */
        public Builder squares(Supplier<SquareImage[]> supplier) {
            squareImagesSupplier = supplier;
            return this;
        }

        /**
         * Sets the spawns' weapon capacity as the one given.
         *
         * @param capacity the weapon capacity to be set
         * @return this builder
         */
        public Builder spawnCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        /**
         * Sets the specified figures as this builder's and returns it.
         *
         * @param figures the figures to be set
         * @return this builder
         */
        public Builder figures(Figure... figures) {
            return figures(Arrays.asList(figures));
        }

        /**
         * Returns this builder with the specified collection of figures added
         * to its list.
         *
         * @param figures the figures to be added
         * @return this builder
         */
        public Builder figures(Collection<Figure> figures) {
            this.figures.addAll(figures);
            return this;
        }

        /**
         * Returns the spawn with the corresponding color.
         *
         * @param color the color of the spawn to be returned
         * @return the spawn with the corresponding color
         */
        public SpawnSquare getSpawnByColor(int color) {
            if (!spawnColorMap.containsKey(color))
                throw new IllegalArgumentException("No spawn has color " + color);
            return spawnColorMap.get(color);
        }

        /**
         * Returns an instance of <code>Board</code> created from the fields
         * set on this builder.
         *
         * @return the board instantiated
         * @throws IllegalArgumentException if the <code>SquareImages</code>
         *                                  provided don't have coordinates or their format is wrong.
         */
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
