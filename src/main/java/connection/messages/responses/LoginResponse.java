package connection.messages.responses;


import connection.messages.ResponseDisplay;

public class LoginResponse extends Response {
    private String id;

    public LoginResponse(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    @Override
    public String prompt() {
        return ">> Connected";
    }

    @Override
    public void handle(ResponseDisplay mh) {
        mh.handle(this);
    }
}
