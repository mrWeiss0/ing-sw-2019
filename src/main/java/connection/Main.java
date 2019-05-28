package connection;

import java.io.IOException;

public final class Main {
    public static void main(String[] args) {
        try (Server server = new Server()) {
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
