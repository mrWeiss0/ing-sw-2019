package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {
    private Game game;

    @BeforeEach
    public void init(){
        game = new Game();
    }

    @Test
    public void test1Player() {
        Player p1 = new Player();
        game.addPlayer(p1);
        assertEquals(game.nextPlayer(), p1);
    }

    @Test
    public void test2Player() {
        Player p1 = new Player();
        Player p2 = new Player();
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
    }

    @Test
    public void testCycling() {
        Player p1 = new Player();
        Player p2 = new Player();
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p2);
    }

    @Test
    public void test5Players() {
        Player[] players = new Player[5];
        for (int i = 0; i < 5; i++) {
            players[i] = new Player();
            game.addPlayer(players[i]);
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(game.nextPlayer(), players[i % 5]);
        }
    }

    @Test
    public void testRemove(){
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.removePlayer(p2);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p3);
        assertEquals(game.nextPlayer(), p1);
        assertEquals(game.nextPlayer(), p3);
    }
}

