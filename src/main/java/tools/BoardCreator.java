package tools;

import com.google.gson.stream.JsonReader;
import model.AbstractSquare;
import model.AmmoSquare;
import model.Room;
import model.SpawnSquare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BoardCreator {
    private JsonReader reader;
    private List<AbstractSquare> board;

    BoardCreator(JsonReader reader) {
        this.reader = reader;
        board = new ArrayList<>();
    }

    List<AbstractSquare> readSquares() throws IOException {
        reader.beginArray();
        List<Room> rooms = new ArrayList<>();
        while (reader.hasNext()) {
            board.add(createSquareFromJson(reader, rooms));
        }
        reader.endArray();
        return board;
    }

    private AbstractSquare createSquareFromJson(JsonReader reader, List<Room> rooms) throws IOException {
        AbstractSquare toAdd;
        boolean isSpawn = false;
        int[] coordinates = new int[2];
        int roomIndex = 0;
        List<Integer> adjacentIndexes = new ArrayList<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "spawnpoint":
                    isSpawn = reader.nextBoolean();
                    break;
                case "coordinates":
                    reader.beginArray();
                    coordinates[0] = reader.nextInt();
                    coordinates[1] = reader.nextInt();
                    reader.endArray();
                    break;
                case "room":
                    roomIndex = reader.nextInt();
                    break;
                case "adjacent":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        adjacentIndexes.add(reader.nextInt());
                    }
                    reader.endArray();
                    break;
                default:
                    throw new IOException();
            }
        }
        reader.endObject();
        if (roomIndex >= rooms.size())
            rooms.add(new Room());
        if (isSpawn) {
            toAdd = new SpawnSquare(rooms.get(roomIndex), coordinates);
        } else {
            toAdd = new AmmoSquare(rooms.get(roomIndex), coordinates);
        }
        for (int i : adjacentIndexes) {
            toAdd.connect(board.get(i));
        }
        return toAdd;
    }
}
