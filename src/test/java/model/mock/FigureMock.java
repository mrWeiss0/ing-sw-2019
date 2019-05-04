package model.mock;

import model.AmmoCube;
import model.Figure;

public class FigureMock extends Figure {
    public FigureMock() {
        super(12, 3, new AmmoCube(1, 1, 1));
    }
}
