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
        game = new GameMock().setBoard(new Board.Builder().build());
    }

    @Test
    void test1Player() {
        Figure p1 = game.newPlayer();
        assertEquals(game.nextPlayer(), p1);
    }

    @Test
    void test2Player() {
        Figure p1 = game.newPlayer();
        Figure p2 = game.newPlayer();
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
    }

    @Test
    void testCycling() {
        Figure p1 = game.newPlayer();
        Figure p2 = game.newPlayer();
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
    }

    @Test
    void test5Players() {
        Figure[] players = new Figure[5];
        for (int i = 0; i < 5; i++) {
            players[i] = game.newPlayer();
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(game.nextPlayer(), players[i % 5]);
        }
    }

    @Test
    void testRemove() {
        Figure p1 = game.newPlayer();
        Figure p2 = game.newPlayer();
        Figure p3 = game.newPlayer();
        game.removePlayer(p2);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p3);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p3);
    }
}

