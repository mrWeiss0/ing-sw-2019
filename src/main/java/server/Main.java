package server;

import java.io.IOException;
import java.util.logging.Logger;

public final class Main {
    public static void main(String[] args) {
        try (Server server = new Server()) {
            server.start();
        } catch (IOException e) {
            Logger.getLogger("Server").severe(e::toString);
        }
    }
}
