package client;


import tools.FileParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public final class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Client client = new Client(FileParser.readConfig(new FileReader("src/main/resources/client_config.json")));
        client.start();
    }
}
