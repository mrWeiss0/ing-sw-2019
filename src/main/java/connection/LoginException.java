package connection;

public class LoginException extends BadRequestException {
    public LoginException() {
        super();
    }

    public LoginException(String s) {
        super(s);
    }
}
