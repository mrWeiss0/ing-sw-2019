package server.model;

import org.junit.jupiter.api.Test;
import server.model.board.Room;
import server.model.board.SpawnSquare;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PowerUpTest {
    PowerUp discard;

    private void discard(PowerUp p) {
        discard = p;
    }

    @Test
    void test() {
        PowerUp powerUp = new PowerUp(null, new AmmoCube(1), new SpawnSquare(new Room(), null, 0), this::discard);
        powerUp.discard();
        assertEquals(powerUp, discard);
    }

    @Test
    void testColor() {
        PowerUp powerUp = new PowerUp(PowerUpType.NEWTON
                , new AmmoCube(1)
                , new SpawnSquare(new Room()
                , null
                , 0)
                , this::discard);

        assertEquals(0, powerUp.getColor());

        powerUp = new PowerUp(PowerUpType.NEWTON
                , new AmmoCube(0, 1)
                , new SpawnSquare(new Room()
                , null
                , 0)
                , this::discard);

        assertEquals(1, powerUp.getColor());

        powerUp = new PowerUp(PowerUpType.NEWTON
                , new AmmoCube(0, 0, 1)
                , new SpawnSquare(new Room()
                , null
                , 0)
                , this::discard);

        assertEquals(2, powerUp.getColor());
    }
}
