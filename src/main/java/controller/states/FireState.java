package controller.states;

import connection.messages.responses.TextResponse;
import controller.GameController;
import model.board.Targettable;
import model.weapon.FireMode;
import model.weapon.FireSequence;
import model.weapon.FireStep;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FireState implements State{
    private List<FireMode> fireModes;
    private FireSequence fireSequence;
    private List<Targettable> choices;
    public FireState(List<FireMode> fireModes){
        this.fireModes = fireModes;
    }

    @Override
    public void onEnter(GameController controller) throws RemoteException{
        fireSequence= new FireSequence(controller.getGame().currentPlayer().getFigure(),controller.getGame().getBoard(),
                FireMode.flatSteps(fireModes));
        controller.getGame().currentPlayer().getView().handle(new TextResponse("possibili targets"));
        choices = new ArrayList<>(fireSequence.getTargets());
    }

    @Override
    public void select(int[] selection, GameController controller, String id) throws RemoteException{
        if(Arrays.stream(selection).distinct().anyMatch(x->x<0 || x>= choices.size())) return;
        Set<Targettable> selectedTargets = Arrays.stream(selection).mapToObj(x->choices.get(x)).collect(Collectors.toSet());
        if(fireSequence.validateTargets(selectedTargets) && id.equals(controller.getGame().currentPlayer().getId())){
            fireSequence.run(selectedTargets);
            controller.getGame().currentPlayer().getView().handle(new TextResponse("possibili targets"));
            choices = new ArrayList<>(fireSequence.getTargets());
            //TODO CHECK DAMAGED AND ASK FOR TAGBACK
            //TODO CHECK CURRENT PLAYER FOR SCOPE AND ASK
            if(!fireSequence.hasNext()) controller.setState(new TurnState());
        }
    }




}
