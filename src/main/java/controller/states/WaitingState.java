package controller.states;

import connection.messages.responses.Response;
import connection.messages.responses.TextResponse;
import controller.Controller;
import model.Player;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WaitingState extends State{
    private Timer countdownTimer;
    private boolean startedCountdown;
    public WaitingState(Controller controller) {
        super(controller);
        startedCountdown= false;
    }

    @Override
    public void sendText(String text, String id) throws RemoteException {
        if (!getController().getUsersByID().keySet().contains(id)) return;
        Response toSend = new TextResponse("LobbyChat>> " + getController().getUsersByID().get(id).getName() +" : "+ text);
        for(Player usr:getController().getUsersByID().values())
            usr.getView().handle(toSend);
    }

    @Override
    public void login(){
        if(getController().getUsersByID().size()==5){
            resetCountdown();
            getController().setState(new State(getController()));
        }else if(getController().getUsersByID().size()>=3 && !startedCountdown){
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

    private void startCountdown(){
        startedCountdown= true;
        countdownTimer = new Timer();
        countdownTimer.schedule(new TimerTask() {
            int i = getController().getCountdownDuration();
            @Override
            public void run() {
                if(i>0){
                    try {
                        Response toSend = new TextResponse(Integer.toString(i));
                        for (Player usr : getController().getUsersByID().values())
                            usr.getView().handle(toSend);
                        i--;
                    }catch(RemoteException e){
                        e.printStackTrace();
                    }
                }else{
                    getController().setState(new State(getController()));
                }
            }
        }, new Date(), 1000);
    }

    private void resetCountdown(){
        countdownTimer.cancel();
        countdownTimer.purge();
        startedCountdown=false;
    }
}
