package controller;

import model.Figure;
import model.Targettable;
import model.weapon.FireStep;

import java.util.List;

public class FireSequence {
    private Figure shooter;
    private List<FireStep> steps;
    private List<Targettable> lastTargets;

    public FireSequence(List<FireStep> steps){
        this.steps = steps;
    }
}
