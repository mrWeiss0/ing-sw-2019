package connection;

import java.io.Closeable;

public interface VirtualClient extends Closeable {
    void send(String s);

    @Override
    void close();
}
