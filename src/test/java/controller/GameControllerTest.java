package controller;

import model.*;
import model.board.BoardBuilderTest;
import model.board.Figure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    public static Game game;
    private static Figure[] figures;
    private static GameController controller;
    @BeforeAll
    static void init(){
        game= new Game.Builder().nKills(8).maxDamages(12).deathDamage(11).maxPowerUps(3).killPoints(new int[]{8,6,4,2})
                .frenzyPoints(new int[]{2,1,1,1}).frenzyOn(true).
                        player(new Player(null,"0",null)).
                        player(new Player(null,"1",null)).
                        player(new Player(null,"2",null)).
                        player(new Player(null,"3",null)).
                        player(new Player(null,"4",null)).
                        powerUps(new PowerUpImage(1, PowerUpType.NEWTON),
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
                        new PowerUpImage(0, PowerUpType.SCOPE)).
                        defaultAmmo(new AmmoCube(1,1,1)).maxWeapons(3).maxMarks(3)
                        .squares(BoardBuilderTest.squareImages).build();
        figures= game.getBoard().getFigures().toArray(new Figure[0]);
        controller= new GameController(1000,game);
    }

    @Test
    void testTurn(){
        assertEquals(2,controller.getUsersByID().get("0").getFigure().getRemainingActions());
        controller.select(new int[]{0}, "0");
        assertEquals(2,controller.getUsersByID().get("0").getFigure().getRemainingActions());
    }

}