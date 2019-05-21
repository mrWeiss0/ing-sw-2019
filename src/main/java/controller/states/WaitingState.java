package controller.states;

import connection.messages.responses.Response;
import connection.messages.responses.TextResponse;
import controller.GameController;
import model.Game;
import model.Player;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WaitingState implements State {
    private Timer countdownTimer;
    private boolean startedCountdown;
    private Game.Builder gameBuilder = new Game.Builder();

    public WaitingState() {
        startedCountdown = false;
    }

    @Override
    public void sendText(GameController controller, String text, String id){
        if (!controller.getUsersByID().keySet().contains(id)) return;
        Response toSend = new TextResponse("LobbyChat>> " + controller.getUsersByID().get(id).getName() + " : " + text);
        for (Player usr : controller.getUsersByID().values())
            usr.getView().handle(toSend);
    }

    @Override
    public void login(GameController controller) {
        if (controller.getUsersByID().size() == 5) {
            resetCountdown();
            controller.setState(new WaitingState());
        } else if (controller.getUsersByID().size() >= 3 && !startedCountdown) {
            startCountdown(controller);
        }
    }

    @Override
    public void logout(GameController controller, String id){
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Logout by " + controller.getUsersByID().get(id).getName()));
        if (controller.getUsersByID().size() < 3) {
            resetCountdown();
        }
    }

    private void startCountdown(GameController controller) {
        startedCountdown = true;
        countdownTimer = new Timer();
        countdownTimer.schedule(new TimerTask() {
            int i = controller.getCountdownDuration();

            @Override
            public void run() {
                if (i > 0) {
                    Response toSend = new TextResponse(Integer.toString(i));
                    for (Player usr : controller.getUsersByID().values())
                        usr.getView().handle(toSend);
                    i--;
                } else {
                    controller.setState(new WaitingState());
                }
            }
        }, new Date(), 1000);
    }

    private void resetCountdown() {
        countdownTimer.cancel();
        countdownTimer.purge();
        startedCountdown = false;
    }
}
