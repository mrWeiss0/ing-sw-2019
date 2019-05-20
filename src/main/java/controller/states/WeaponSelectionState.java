package controller.states;

import connection.messages.responses.TextResponse;
import controller.GameController;
import model.weapon.Weapon;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

public class WeaponSelectionState implements State {
    private List<Weapon> choices;
    public void onEnter(GameController controller) throws RemoteException {
        choices = controller.getGame().currentPlayer().getFigure().getWeapons().stream().filter(Weapon::isLoaded).collect(Collectors.toList());
        controller.getGame().currentPlayer().getView().handle(new TextResponse("player's weapons list"));
    }
    public void select(int[] selection, GameController controller, String id) throws RemoteException{
        if(selection.length==1 && selection[0]>=0 &&
                selection[0]<choices.size() &&
                id.equals(controller.getGame().currentPlayer().getId())){
            controller.setState(new FireModeSelectionState(choices.get(selection[0])));
        }
    }
}
