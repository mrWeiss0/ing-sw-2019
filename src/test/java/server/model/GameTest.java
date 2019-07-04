package server.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.controller.Player;
import server.model.board.BoardBuilderTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    private final Player[] players = new Player[]{
            new Player(null),
            new Player(null),
            new Player(null),
            new Player(null),
            new Player(null)
    };
    private Game.Builder gameBuilder;

    @BeforeEach
    void init() {
        gameBuilder = new Game.Builder()
                .squares(BoardBuilderTest.squareImages)
                .ammoTiles(new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0),
                        new AmmoTileImage(1, false, 0))
                .powerUps(
                        new PowerUpImage(1, PowerUpType.NEWTON),
                        new PowerUpImage(1, PowerUpType.TAGBACK),
                        new PowerUpImage(1, PowerUpType.TELEPORTER),
                        new PowerUpImage(1, PowerUpType.SCOPE),
                        new PowerUpImage(2, PowerUpType.NEWTON),
                        new PowerUpImage(2, PowerUpType.TAGBACK),
                        new PowerUpImage(2, PowerUpType.TELEPORTER),
                        new PowerUpImage(2, PowerUpType.SCOPE),
                        new PowerUpImage(0, PowerUpType.NEWTON),
                        new PowerUpImage(0, PowerUpType.TAGBACK),
                        new PowerUpImage(0, PowerUpType.TELEPORTER),
                        new PowerUpImage(0, PowerUpType.SCOPE)
                );
        gameBuilder.addPlayer(players[0]);
    }

    @Test
    void test1Player() {
        Game game = gameBuilder
                .build();
        assertEquals(game.nextPlayer(), players[0]);
    }

    @Test
    void test2Player() {
        gameBuilder.addPlayer(players[1]);
        Game game = gameBuilder.build();
        assertEquals(game.nextPlayer(), players[0]);
        assertEquals(game.nextPlayer(), players[1]);
    }

    @Test
    void testCycling() {
        gameBuilder.addPlayer(players[1]);
        Game game = gameBuilder.build();
        assertEquals(game.nextPlayer(), players[0]);
        assertEquals(game.nextPlayer(), players[1]);
        assertEquals(game.nextPlayer(), players[0]);
        assertEquals(game.nextPlayer(), players[1]);
    }

    @Test
    void test5Players() {
        gameBuilder.addPlayer(players[1]);
        gameBuilder.addPlayer(players[2]);
        gameBuilder.addPlayer(players[3]);
        gameBuilder.addPlayer(players[4]);
        Game game = gameBuilder.build();
        assertEquals(Arrays.asList(players), game.getPlayers());
        for (int i = 0; i < 10; i++) {
            assertEquals(game.nextPlayer(), players[i % 5]);
        }
    }

    @Test
    void testRemove() {
        gameBuilder.addPlayer(players[1]);
        gameBuilder.addPlayer(players[2]);
        gameBuilder.removePlayer(players[1]);
        Game game = gameBuilder.build();
        assertEquals(Arrays.asList(players[0], players[2]), game.getPlayers());
        assertEquals(game.nextPlayer(), players[0]);
        assertEquals(game.nextPlayer(), players[2]);
        assertEquals(game.nextPlayer(), players[0]);
        assertEquals(game.nextPlayer(), players[2]);
    }
}

