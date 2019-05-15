package connection.messages.responses;

import connection.messages.ResponseDisplay;

public class TextResponse extends Response {
    private String content;

    public TextResponse(String content) {
        this.content = content;
    }

    @Override
    public String prompt() {
        return content;
    }

    @Override
    public void handle(ResponseDisplay responseDisplay) {
        responseDisplay.handle(this);
    }
}
