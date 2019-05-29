package server;

import java.io.IOException;
import java.util.logging.Logger;

public final class Main {
    public static final Logger logger = Logger.getLogger("Server");

    public static void main(String[] args) {
        try (Server server = new Server()) {
            server.run();
        } catch (IOException e) {
            logger.severe(e::toString);
        }
    }
}
