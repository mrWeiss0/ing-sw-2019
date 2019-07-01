package server.controller;

import org.junit.jupiter.api.BeforeEach;
import server.model.*;
import server.model.board.BoardBuilderTest;
import server.model.board.Figure;

class GameControllerTest {
    public static Game game;
    private static Figure[] figures;
    private static GameController controller;

    @BeforeEach
    void init() {
        Game.Builder gb = new Game.Builder().nKills(8).maxDamages(12).killDamages(11).maxPowerUps(3).killPoints(new int[]{8, 6, 4, 2})
                .frenzyPoints(new int[]{2, 1, 1, 1}).frenzyOn(true);
        gb.addPlayer(new Player("0"));
        gb.addPlayer(new Player("1"));
        gb.addPlayer(new Player("2"));
        gb.addPlayer(new Player("3"));
        gb.addPlayer(new Player("4"));
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
                .ammoTiles(new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0),
                        new AmmoTileImage(0,false, 1, 0, 0))
                .defaultAmmo(new AmmoCube(1, 1, 1)).maxWeapons(3).maxMarks(3)
                .squares(BoardBuilderTest.squareImages)
                .build();
        figures = game.getBoard().getFigures().toArray(new Figure[0]);
        controller = new GameController(game);
    }

   /* @Test
    void testMove() throws NoSuchFieldException, IllegalAccessException {
        Field f = GameController.class.getDeclaredField("state");
        f.setAccessible(true);
        System.out.println(f.get(controller).getClass().getName());
        assertTrue(f.get(controller).getClass().getName().endsWith("SelectSpawnState"));
        assertEquals(2, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("TurnState"));
        assertEquals(2, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        controller.select(new int[]{1}, "1");
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("MoveState"));
        controller.select(new int[]{10}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("MoveState"));
        controller.select(new int[]{2}, "1");
        assertTrue(f.get(controller).getClass().getName().endsWith("MoveState"));
        controller.select(new int[]{2}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("TurnState"));
        assertEquals(1, controller.getUsersByID().get("0").getFigure().getRemainingActions());
    }

    @Test
    void testGrab() throws NoSuchFieldException, IllegalAccessException {
        Field f = GameController.class.getDeclaredField("state");
        f.setAccessible(true);

        assertTrue(f.get(controller).getClass().getName().endsWith("SelectSpawnState"));
        assertEquals(2, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("TurnState"));
        assertEquals(2, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        controller.select(new int[]{1}, "1");
        controller.select(new int[]{1}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("MoveState"));
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("GrabState"));
    }

    @Test
    void testFrenzyGrab() throws NoSuchFieldException, IllegalAccessException {
        Field f = GameController.class.getDeclaredField("state");
        f.setAccessible(true);
        Field f1 = Game.class.getDeclaredField("remainingKills");
        f1.setAccessible(true);

        assertTrue(f.get(controller).getClass().getName().endsWith("SelectSpawnState"));
        assertEquals(2, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        f1.set(game, 0);
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("TurnState"));
        assertEquals(2, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        controller.select(new int[]{1}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("MoveState"));
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("GrabState"));
        assertEquals(1, controller.getUsersByID().get("0").getFigure().getRemainingActions());
    }

    @Test
    void testFrenzyMove() throws NoSuchFieldException, IllegalAccessException {
        Field f = GameController.class.getDeclaredField("state");
        f.setAccessible(true);
        Field f1 = Game.class.getDeclaredField("remainingKills");
        f1.setAccessible(true);

        assertTrue(f.get(controller).getClass().getName().endsWith("SelectSpawnState"));
        assertEquals(2, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        f1.set(game, 0);
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("TurnState"));
        assertEquals(2, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("MoveState"));
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("TurnState"));
        assertEquals(1, controller.getUsersByID().get("0").getFigure().getRemainingActions());
    }

    @Test
    void testFrenzyAfterFirstPlayer() throws NoSuchFieldException, IllegalAccessException {
        Field f = GameController.class.getDeclaredField("state");
        f.setAccessible(true);
        Field f1 = Game.class.getDeclaredField("remainingKills");
        f1.setAccessible(true);
        f1.set(game, 0);
        game.getPlayers().stream().filter(x -> x != game.currentPlayer()).forEach(x -> x.getFigure().moveTo((AbstractSquare) game.getBoard().getSquares().toArray()[0]));
        assertTrue(f.get(controller).getClass().getName().endsWith("SelectSpawnState"));
        assertEquals(2, controller.getUsersByID().get("1").getFigure().getRemainingActions());
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("TurnState"));
        assertEquals(2, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("MoveState"));
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("TurnState"));
        assertEquals(1, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("MoveState"));
        controller.select(new int[]{0}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("SelectToReloadState"));
        assertEquals(0, controller.getUsersByID().get("0").getFigure().getRemainingActions());
        controller.select(new int[]{}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("TurnState"));
        controller.select(new int[]{1}, "0");
        assertTrue(f.get(controller).getClass().getName().endsWith("TurnState"));
    }
*/

}
