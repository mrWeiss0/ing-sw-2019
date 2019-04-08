package model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {
    private Game game;
    @Test
    public void test1Player(){
        game= new Game(8);
        Player p1= new Player();
        game.addPlayer(p1);
        assertEquals(game.nextPlayer(),p1);
    }
    @Test
    public void test2Player(){
        game= new Game(8);
        Player p1= new Player();
        Player p2= new Player();
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertEquals(game.nextPlayer(),p1);
        assertEquals(game.nextPlayer(),p2);
    }
    @Test
    public void testCycling(){
        game= new Game(8);
        Player p1= new Player();
        Player p2= new Player();
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertEquals(game.nextPlayer(),p1);
        assertEquals(game.nextPlayer(),p2);
        assertEquals(game.nextPlayer(),p1);
        assertEquals(game.nextPlayer(),p2);
        assertEquals(game.nextPlayer(),p1);
        assertEquals(game.nextPlayer(),p2);
        assertEquals(game.nextPlayer(),p1);
        assertEquals(game.nextPlayer(),p2);
    }
    @Test
    public void test5Players(){
        game= new Game(8);
        Player[] players= new Player[5];
        for(int i=0;i<5;i++){
            players[i]= new Player();
            game.addPlayer(players[i]);
        }
        for(int i=0;i<10;i++){
            assertEquals(game.nextPlayer(),players[i%5]);
        }
    }

}

