package tools;

import com.google.gson.stream.JsonReader;
import model.AbstractSquare;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParser {


    public List<AbstractSquare> initBoard(String filename) {
        JsonReader json;
        BoardCreator bc;
        try {
            json = new JsonReader(new InputStreamReader(new FileInputStream(new File("src/main/resources/maps/" + filename))));
            bc = new BoardCreator(json);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
        try {
            return bc.readSquares();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

}
