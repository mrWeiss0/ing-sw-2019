package server.connection;

public class BadRequestException extends Exception {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String s) {
        super(s);
    }
}
