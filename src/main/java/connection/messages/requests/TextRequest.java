package connection.messages.requests;

import connection.messages.RequestInterpreter;

public class TextRequest extends Request {
    private String content;

    public TextRequest(String content) {
        this.content = content;
    }

    @Override
    public String prompt() {
        return content;
    }

    @Override
    public void handle(RequestInterpreter mh) {
        mh.handle(this);
    }
}
