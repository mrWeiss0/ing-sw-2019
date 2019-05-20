package controller.states;

import connection.messages.responses.TextResponse;
import controller.GameController;
import model.AmmoCube;
import model.weapon.FireMode;
import model.weapon.Weapon;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FireModeSelectionState implements State{
    private Weapon currWeapon;
    FireModeSelectionState(Weapon w){
        currWeapon = w;
    }
    @Override
    public void onEnter(GameController controller) throws RemoteException {
        controller.getGame().currentPlayer().getView().handle(new TextResponse("possible firemodes"));
    }
    @Override
    public void select(int[] selection, GameController controller, String id) throws RemoteException{
        if(Arrays.stream(selection).distinct().anyMatch(x->x<0 || x>= currWeapon.getFireModes().size())) return;
        List<FireMode> selectedFireModes = Arrays.stream(selection).mapToObj(x->currWeapon.getFireModes().get(x)).collect(Collectors.toList());
        if(currWeapon.validateFireModes(selectedFireModes) && id.equals(controller.getGame().currentPlayer().getId())){
            controller.setState(new PayAmmoState(selectedFireModes.stream().map(FireMode::getCost).reduce(AmmoCube::add).orElseGet(AmmoCube::new),new FireState(selectedFireModes)));
        }
    }
}
