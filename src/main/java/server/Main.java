package server;

import tools.FileParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public final class Main {
    public static final Logger LOGGER = Logger.getLogger("Server");

    public static void main(String[] args) {
        try (Server server = new Server(FileParser.readServerConfig(
                new FileReader("src/main/resources/server_config.json")))) {
            server.start();
            while (System.in.read() != -1) ;
        } catch (IOException e) {
            LOGGER.severe(e::toString);
        }
    }
}
