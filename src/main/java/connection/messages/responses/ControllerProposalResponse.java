package connection.messages.responses;


import connection.messages.ResponseDisplay;

public class ControllerProposalResponse extends Response {
    private String controllerDescriptions;
    private String id;

    public ControllerProposalResponse(String controllerDescriptions, String id) {
        this.id = id;
        this.controllerDescriptions = controllerDescriptions;
    }


    public String getId() {
        return id;
    }

    @Override
    public String prompt() {
        return controllerDescriptions;
    }

    @Override
    public void handle(ResponseDisplay mh) {
        mh.handle(this);
    }
}
