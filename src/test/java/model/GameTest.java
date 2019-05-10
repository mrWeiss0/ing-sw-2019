package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    private Player[] players = new Player[]{
            new Player(),
            new Player(),
            new Player(),
            new Player(),
            new Player()
    };
    private Game.Builder gameBuilder = new Game.Builder().player(players[0]);

    @Test
    void test1Player() {
        Game game = gameBuilder
                .build();
        assertEquals(game.nextPlayer(), players[0]);
    }

    @Test
    void test2Player() {
        Game game = gameBuilder
                .player(players[1])
                .build();
        assertEquals(game.nextPlayer(), players[0]);
        assertEquals(game.nextPlayer(), players[1]);
    }

    @Test
    void testCycling() {
        Game game = gameBuilder
                .player(players[1])
                .build();
        assertEquals(game.nextPlayer(), players[0]);
        assertEquals(game.nextPlayer(), players[1]);
        assertEquals(game.nextPlayer(), players[0]);
        assertEquals(game.nextPlayer(), players[1]);
    }

    @Test
    void test5Players() {
        Game game = gameBuilder
                .player(players[1])
                .player(players[2])
                .player(players[3])
                .player(players[4])
                .build();
        for (int i = 0; i < 10; i++) {
            assertEquals(game.nextPlayer(), players[i % 5]);
        }
    }

    @Test
    void testRemove() {
        Game game = gameBuilder
                .player(players[1])
                .player(players[2])
                .removePlayer(players[1])
                .build();
        assertEquals(game.nextPlayer(), players[0]);
        assertEquals(game.nextPlayer(), players[2]);
        assertEquals(game.nextPlayer(), players[0]);
        assertEquals(game.nextPlayer(), players[2]);
    }
}

