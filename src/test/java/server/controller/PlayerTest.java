package server.controller;

import server.connection.ClientRMI;
import server.connection.VirtualClient;
import server.model.board.Figure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    String name = "Pasolini";
    Player player = new Player(name);
    Figure f;
    VirtualClient c;
    GameController g;

    @BeforeEach
    void init() {
        f = new Figure(0, 0, 0, 0, 0);
        c = new ClientRMI(null, null);
        g = new GameController();
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

    @Test
    void testActive() {
        assertTrue(player.isActive());
        player.setInactive();
        assertFalse(player.isActive());
        player.setActive();
        assertTrue(player.isActive());
    }

    @Test
    void testOnline() {
        assertTrue(player.isOnline());
        player.setOffline();
        assertFalse(player.isOnline());
        player.setOnline();
        assertTrue(player.isOnline());
    }
}
