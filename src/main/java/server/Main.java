package server;

import java.io.IOException;
import java.util.logging.Logger;

public final class Main {
    public static final Logger logger = Logger.getLogger("Server");

    public static void main(String[] args) {
        // TODO add configuration file
        try (Server server = new Server()) {
            server.start();
            while (System.in.read() != -1) ;
        } catch (IOException e) {
            logger.severe(e::toString);
        }
    }
}
