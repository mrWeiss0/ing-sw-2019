package connection.messages.requests;

import connection.messages.RequestInterpreter;

public class ControllerSelectionRequest extends Request {
    private int selectedControllerIndex;
    private String username;

    public ControllerSelectionRequest(int selectedControllerIndex, String username) {
        this.username = username;
        this.selectedControllerIndex = selectedControllerIndex;
    }


    @Override
    public String prompt() {
        return "User has selectedControllerIndex : " + selectedControllerIndex;
    }

    @Override
    public void handle(RequestInterpreter mh) {
        mh.handle(this);
    }

    public int getSelectedControllerIndex() {
        return selectedControllerIndex;
    }

    public String getUsername() {
        return username;
    }
}
