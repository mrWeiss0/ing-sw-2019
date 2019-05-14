package connection.messages.requests;

import connection.messages.RequestInterpreter;

public class ReconnectRequest extends Request {
    @Override
    public String prompt() {
        return "Request Connection";
    }

    @Override
    public void handle(RequestInterpreter requestInterpreter) {
        requestInterpreter.handle(this);
    }
}
