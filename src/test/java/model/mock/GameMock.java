package model.mock;

import model.AmmoCube;
import model.Game;

public class GameMock extends Game {
    public GameMock() {
        super(8, 12, 3, new AmmoCube(1, 1, 1), 3);
    }
}
