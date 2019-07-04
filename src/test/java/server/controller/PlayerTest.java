package server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.connection.VirtualClient;
import server.model.Game;
import server.model.board.Figure;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {
    String name = "Pasolini";
    Player player = new Player(name);
    Figure f;
    VirtualClient c;
    GameController g;

    @BeforeEach
    void init() {
        f = new Figure(0, 0, 0, 0, 0, 0);
        c = null;
        g = new GameController(new Game.Builder().build());
        player.setFigure(f);
        player.setClient(c);
        player.setGame(g);
    }

    @Test
    void test() {
        assertEquals(name, player.getName());
        assertEquals(f, player.getFigure());
        assertEquals(c, player.getClient());
        assertEquals(g, player.getGame());
    }
}
