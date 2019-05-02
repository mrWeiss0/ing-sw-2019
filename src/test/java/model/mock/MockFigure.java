package model.mock;

import model.AmmoCube;
import model.Figure;

public class MockFigure extends Figure {
    public MockFigure() {
        super(12, 3, new AmmoCube(1, 1, 1));
    }
}
