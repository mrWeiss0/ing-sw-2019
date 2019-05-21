package controller.states;

import connection.messages.responses.TextResponse;
import controller.GameController;
import model.AmmoCube;
import model.PowerUp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class PayAmmoState implements State {
    private AmmoCube cost;
    private State nextState;
    private List<PowerUp> powerUpList;

    public PayAmmoState(AmmoCube cost, State nextState){
        this.cost=cost;
        this.nextState= nextState;
    }

    @Override
    public void onEnter(GameController controller){
        powerUpList = new ArrayList<>(controller.getGame().currentPlayer().getFigure().getPowerUps());
        controller.getGame().currentPlayer().getView().handle(new TextResponse("possible powerup to discard"));
    }

    @Override
    public void select(int[] selection, GameController controller, String id){
        if(Arrays.stream(selection).distinct().anyMatch(x->x<0 || x>= powerUpList.size())) return;
        List<PowerUp> toDiscard = Arrays.stream(selection).distinct().mapToObj(x->powerUpList.get(x)).collect(Collectors.toList());
        if(id.equals(controller.getGame().currentPlayer().getId()) &&
                (toDiscard.stream().map(PowerUp::getAmmo).
                        reduce(controller.getGame().currentPlayer().getFigure().getAmmo(),AmmoCube::add)).greaterEqThan(cost)){
            controller.getGame().currentPlayer().getFigure().getPowerUps().removeAll(toDiscard);
            toDiscard.forEach(PowerUp::discard);
            controller.setState(nextState);
        }
    }



}
