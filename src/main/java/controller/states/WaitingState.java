package controller.states;

import connection.messages.responses.Response;
import connection.messages.responses.TextResponse;
import controller.Controller;
import controller.User;

import java.rmi.RemoteException;

public class WaitingState extends State{
    public WaitingState(Controller controller) {
        super(controller);
    }

    @Override
    public void sendText(String text, String id) throws RemoteException {
        if (!getController().getUsersByID().keySet().contains(id)) return;
        Response toSend = new TextResponse("LobbyChat>> " + getController().getUsersByID().get(id).getName() +" : "+ text);
        for(User usr:getController().getUsersByID().values())
            usr.getView().handle(toSend);
    }

    @Override
    public void login(){
        if(getController().getUsersByID().size()==5){
            getController().setState(new State(getController()));
        }else if(getController().getUsersByID().size()>=3){
            startCountdown();
        }
    }

    @Override
    public void logout(String id) throws RemoteException{
        getController().getUsersByID().get(id).getView().handle(new TextResponse("Logout by "+getController().getUsersByID().get(id).getName()));
        if(getController().getUsersByID().size()<3){
            resetCountdown();
        }
    }

    public void startCountdown(){

    }

    public void resetCountdown(){

    }
}
