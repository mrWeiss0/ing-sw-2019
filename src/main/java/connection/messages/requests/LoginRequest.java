package connection.messages.requests;

import connection.messages.RequestInterpreter;

public class LoginRequest extends Request {
    private String username;

    public LoginRequest(String username) {
        this.username = username;
    }

    @Override
    public String prompt() {
        return "Request Connection";
    }

    @Override
    public void handle(RequestInterpreter requestInterpreter) {
        requestInterpreter.handle(this);
    }

    public String getUsername() {
        return username;
    }
}
