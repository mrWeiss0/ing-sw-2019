package connection.messages.requests;

import connection.messages.RequestInterpreter;

public class LogoutRequest extends Request {

    @Override
    public String prompt() {
        return "Logout Request";
    }

    @Override
    public void handle(RequestInterpreter requestInterpreter) {
        requestInterpreter.handle(this);
    }
}
