package server.model;

import server.model.board.Room;
import server.model.board.SpawnSquare;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PowerUpTest {
    PowerUp discard;

    private void discard(PowerUp p) {
        discard = p;
    }

    @Test
    void test() {
        PowerUp powerUp = new PowerUp(new AmmoCube(1), new SpawnSquare(new Room(), null, 0), this::discard);
        powerUp.discard();
        assertEquals(powerUp, discard);
    }
}
