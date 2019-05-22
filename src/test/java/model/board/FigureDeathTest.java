package model.board;

import model.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class FigureDeathTest {
    private static Room room;
    private static AbstractSquare[] squares;
    private static Game game;
    private Figure[] figures;

    @BeforeAll
    static void init() {
        room = new Room();
        squares = new AbstractSquare[]{
                new SpawnSquare(room, new int[]{}, 1),
                new SpawnSquare(room, new int[]{}, 1)
        };
        game = new Game.Builder().killPoints(new int[]{8,6,4,2}).maxDamages(12).deathDamage(11).build();
    }

    @BeforeEach
    void initCase() {
        figures = new Figure[]{
                new Figure(12, 3, 3, 3, 3, 11),
                new Figure(12, 3, 3, 3, 3, 11),
                new Figure(12, 3, 3, 3, 3, 11),
                new Figure(12, 3, 3, 3, 3, 11),
                new Figure(12, 3, 3, 3, 3, 11)
        };
        figures[0].moveTo(squares[0]);
        figures[1].moveTo(squares[0]);
        figures[2].moveTo(squares[0]);
        figures[3].moveTo(squares[1]);
        figures[4].moveTo(squares[1]);
    }

    @Test
    void killedByOne(){
        figures[0].damageFrom(figures[1],11);
        figures[0].resolveDeath(game);
        assertEquals(9,figures[1].getPoints());
    }

    @Test
    void killedTwiceByOne(){
        figures[0].damageFrom(figures[1], 11);
        figures[0].resolveDeath(game);
        figures[0].damageFrom(figures[1],11);
        figures[0].resolveDeath(game);
        assertEquals(16,figures[1].getPoints());
    }

    @Test
    void killedByTwo(){
        figures[0].damageFrom(figures[1],6);
        figures[0].damageFrom(figures[2],6);
        figures[0].resolveDeath(game);
        assertEquals(9,figures[1].getPoints());
        assertEquals(6,figures[2].getPoints());
    }
    @Test
    void killedByThree(){
        figures[0].damageFrom(figures[1],4);
        figures[0].damageFrom(figures[3],4);
        figures[0].damageFrom(figures[2],4);

        figures[0].resolveDeath(game);
        assertEquals(9,figures[1].getPoints());
        assertEquals(6,figures[3].getPoints());
        assertEquals(4,figures[2].getPoints());
    }

    @Test
    void killedByAll(){
        figures[0].damageFrom(figures[1],3);
        figures[0].damageFrom(figures[2],3);
        figures[0].damageFrom(figures[3],3);
        figures[0].damageFrom(figures[4],3);
        figures[0].resolveDeath(game);
        assertEquals(9,figures[1].getPoints());
        assertEquals(6,figures[2].getPoints());
        assertEquals(4,figures[3].getPoints());
        assertEquals(2,figures[4].getPoints());

        figures[0].damageFrom(figures[1],3);
        figures[0].damageFrom(figures[2],3);
        figures[0].damageFrom(figures[3],3);
        figures[0].damageFrom(figures[4],3);
        figures[0].resolveDeath(game);
        assertEquals(16,figures[1].getPoints());
        assertEquals(10,figures[2].getPoints());
        assertEquals(6,figures[3].getPoints());
        assertEquals(3,figures[4].getPoints());
    }
    @Test
    void killedByDifferentAmounts(){
        figures[0].damageFrom(figures[1],5);
        figures[0].damageFrom(figures[2],6);
        figures[0].resolveDeath(game);
        assertEquals(7,figures[1].getPoints());
        assertEquals(8,figures[2].getPoints());
    }
    @Test
    void killedByDifferentAmounts2(){
        figures[0].damageFrom(figures[1],6);
        figures[0].damageFrom(figures[2],5);
        figures[0].resolveDeath(game);
        assertEquals(9,figures[1].getPoints());
        assertEquals(6,figures[2].getPoints());
    }
    @Test
    void testRevengeMark(){
        figures[0].damageFrom(figures[1],12);
        figures[0].resolveDeath(game);
        figures[1].applyMarks();
        figures[1].damageFrom(figures[0],1);
        assertEquals(2,figures[1].getDamages().size());
    }

}
