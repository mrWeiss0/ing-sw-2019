package model;

import model.board.Board;
import model.board.Figure;
import model.mock.GameMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {
    private Game game;

    @BeforeEach
    public void init() {
        game = new GameMock().setBoard(new Board());
    }

    @Test
    public void test1Player() {
        Figure p1 = game.newPlayer();
        assertEquals(game.nextPlayer(), p1);
    }

    @Test
    public void test2Player() {
        Figure p1 = game.newPlayer();
        Figure p2 = game.newPlayer();
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
    }

    @Test
    public void testCycling() {
        Figure p1 = game.newPlayer();
        Figure p2 = game.newPlayer();
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
    }

    @Test
    public void test5Players() {
        Figure[] players = new Figure[5];
        for (int i = 0; i < 5; i++) {
            players[i] = game.newPlayer();
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(game.nextPlayer(), players[i % 5]);
        }
    }

    @Test
    public void testRemove() {
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

