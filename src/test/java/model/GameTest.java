package model;

import model.board.Board;
import model.board.Figure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    private Game game;

    @BeforeEach
    void init() {
        game = new GameMock().setBoard(new Board());
    }

    @Test
    void test1Player() {
        final Figure p1 = game.newPlayer();
        assertEquals(game.nextPlayer(), p1);
    }

    @Test
    void test2Player() {
        final Figure p1 = game.newPlayer();
        final Figure p2 = game.newPlayer();
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
    }

    @Test
    void testCycling() {
        final Figure p1 = game.newPlayer();
        final Figure p2 = game.newPlayer();
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
    }

    @Test
    void test5Players() {
        final Figure[] players = new Figure[5];
        for (int i = 0; i < 5; i++) {
            players[i] = game.newPlayer();
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(game.nextPlayer(), players[i % 5]);
        }
    }

    @Test
    void testRemove() {
        final Figure p1 = game.newPlayer();
        final Figure p2 = game.newPlayer();
        final Figure p3 = game.newPlayer();
        game.removePlayer(p2);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p3);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p3);
    }
}

