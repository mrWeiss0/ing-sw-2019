package controller.states;

import connection.messages.responses.Response;
import connection.messages.responses.TextResponse;
import controller.Controller;
import model.Player;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WaitingState extends State {
    private Timer countdownTimer;
    private boolean startedCountdown;

    public WaitingState() {
        startedCountdown = false;
    }

    @Override
    public void sendText(Controller controller, String text, String id) throws RemoteException {
        if (!controller.getUsersByID().keySet().contains(id)) return;
        Response toSend = new TextResponse("LobbyChat>> " + controller.getUsersByID().get(id).getName() + " : " + text);
        for (Player usr : controller.getUsersByID().values())
            usr.getView().handle(toSend);
    }

    @Override
    public void login(Controller controller) {
        if (controller.getUsersByID().size() == 5) {
            resetCountdown();
            controller.setState(new State());
        } else if (controller.getUsersByID().size() >= 3 && !startedCountdown) {
            startCountdown(controller);
        }
    }

    @Override
    public void logout(Controller controller, String id) throws RemoteException {
        controller.getUsersByID().get(id).getView().handle(new TextResponse("Logout by " + controller.getUsersByID().get(id).getName()));
        if (controller.getUsersByID().size() < 3) {
            resetCountdown();
        }
    }

    private void startCountdown(Controller controller) {
        startedCountdown = true;
        countdownTimer = new Timer();
        countdownTimer.schedule(new TimerTask() {
            int i = controller.getCountdownDuration();

            @Override
            public void run() {
                if (i > 0) {
                    try {
                        Response toSend = new TextResponse(Integer.toString(i));
                        for (Player usr : controller.getUsersByID().values())
                            usr.getView().handle(toSend);
                        i--;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    controller.setState(new State());
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
