package connection.messages;

import connection.messages.responses.ControllerProposalResponse;
import connection.messages.responses.TextResponse;

public interface ResponseDisplay {
    void handle(ControllerProposalResponse m);

    void handle(TextResponse m);
}
