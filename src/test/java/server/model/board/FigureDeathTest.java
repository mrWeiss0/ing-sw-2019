package server.model.board;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.controller.Player;
import server.model.Game;
import server.model.PowerUpImage;
import server.model.PowerUpType;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FigureDeathTest {
    private static AbstractSquare[] squares;
    private static Game game;
    private Figure[] figures;

    @BeforeAll
    static void init() {
        Room room = new Room();
        squares = new AbstractSquare[]{
                new SpawnSquare(room, new int[]{}, 1),
                new SpawnSquare(room, new int[]{}, 1)
        };
    }

    @BeforeEach
    void initCase() {
        Game.Builder gb = new Game.Builder().killPoints(new int[]{8, 6, 4, 2}).maxDamages(12).deathDamage(11).nKills(8).maxMarks(3);
        gb.addPlayer(new Player(null));
        gb.addPlayer(new Player(null));
        gb.addPlayer(new Player(null));
        gb.addPlayer(new Player(null));
        gb.addPlayer(new Player(null));
        game = gb.powerUps(new PowerUpImage(1, PowerUpType.NEWTON),
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
                new PowerUpImage(0, PowerUpType.SCOPE))
                .squares(BoardBuilderTest.squareImages)
                .build();
        figures = game.getBoard().getFigures().toArray(new Figure[0]);

        figures[0].moveTo(squares[0]);
        figures[1].moveTo(squares[0]);
        figures[2].moveTo(squares[0]);
        figures[3].moveTo(squares[1]);
        figures[4].moveTo(squares[1]);
    }

    @Test
    void killedByOne() {
        figures[0].damageFrom(figures[1], 11);
        figures[0].resolveDeath(game);
        assertEquals(9, figures[1].getPoints());
    }

    @Test
    void killedTwiceByOne() {
        figures[0].damageFrom(figures[1], 11);
        figures[0].resolveDeath(game);
        figures[0].damageFrom(figures[1], 11);
        figures[0].resolveDeath(game);
        assertEquals(16, figures[1].getPoints());
    }

    @Test
    void killedByTwo() {
        figures[0].damageFrom(figures[1], 6);
        figures[0].damageFrom(figures[2], 6);
        figures[0].resolveDeath(game);
        assertEquals(9, figures[1].getPoints());
        assertEquals(6, figures[2].getPoints());
    }

    @Test
    void killedByThree() {
        figures[0].damageFrom(figures[1], 4);
        figures[0].damageFrom(figures[3], 4);
        figures[0].damageFrom(figures[2], 4);

        figures[0].resolveDeath(game);
        assertEquals(9, figures[1].getPoints());
        assertEquals(6, figures[3].getPoints());
        assertEquals(4, figures[2].getPoints());
    }

    @Test
    void killedByAll() {
        figures[0].damageFrom(figures[1], 3);
        figures[0].damageFrom(figures[2], 3);
        figures[0].damageFrom(figures[3], 3);
        figures[0].damageFrom(figures[4], 3);
        figures[0].resolveDeath(game);
        assertEquals(9, figures[1].getPoints());
        assertEquals(6, figures[2].getPoints());
        assertEquals(4, figures[3].getPoints());
        assertEquals(2, figures[4].getPoints());

        figures[0].damageFrom(figures[1], 3);
        figures[0].damageFrom(figures[2], 3);
        figures[0].damageFrom(figures[3], 3);
        figures[0].damageFrom(figures[4], 3);
        figures[0].resolveDeath(game);
        assertEquals(16, figures[1].getPoints());
        assertEquals(10, figures[2].getPoints());
        assertEquals(6, figures[3].getPoints());
        assertEquals(3, figures[4].getPoints());
    }

    @Test
    void killedByDifferentAmounts() {
        figures[0].damageFrom(figures[1], 5);
        figures[0].damageFrom(figures[2], 6);
        figures[0].resolveDeath(game);
        assertEquals(7, figures[1].getPoints());
        assertEquals(8, figures[2].getPoints());
    }

    @Test
    void killedByDifferentAmounts2() {
        figures[0].damageFrom(figures[1], 6);
        figures[0].damageFrom(figures[2], 5);
        figures[0].resolveDeath(game);
        assertEquals(9, figures[1].getPoints());
        assertEquals(6, figures[2].getPoints());
        assertEquals(Collections.singletonList(figures[2]), game.getKillCount());
        assertEquals(7, game.getRemainingKills());
    }

    @Test
    void testRevengeMarkAndGameProperties() {
        figures[0].damageFrom(figures[1], 12);
        figures[0].resolveDeath(game);
        figures[1].applyMarks();
        figures[1].damageFrom(figures[0], 1);
        assertEquals(2, figures[1].getDamages().size());
        assertEquals(Arrays.asList(figures[1], figures[1]), game.getKillCount());
        assertEquals(7, game.getRemainingKills());
    }

    @Test
    void notDead() {
        figures[0].damageFrom(figures[1], 10);
        figures[0].resolveDeath(game);
        assertEquals(0, figures[0].getPoints());
    }

    @Test
    void testAllCondition() {
        figures[0].damageFrom(figures[1], 4);
        figures[0].damageFrom(figures[2], 7);
        figures[0].resolveDeath(game);
        assertEquals(7, figures[1].getPoints());
        assertEquals(8, figures[2].getPoints());

        figures[0].damageFrom(figures[1], 7);
        figures[0].damageFrom(figures[2], 4);
        figures[0].resolveDeath(game);
        assertEquals(14, figures[1].getPoints());
        assertEquals(12, figures[2].getPoints());

        figures[0].damageFrom(figures[1], 6);
        figures[0].damageFrom(figures[2], 6);
        figures[0].resolveDeath(game);
        assertEquals(19, figures[1].getPoints());
        assertEquals(14, figures[2].getPoints());

        figures[0].damageFrom(figures[2], 6);
        figures[0].damageFrom(figures[1], 6);
        figures[0].resolveDeath(game);
        assertEquals(20, figures[1].getPoints());
        assertEquals(17, figures[2].getPoints());
    }

}
