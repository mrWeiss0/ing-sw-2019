package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class AbstractSquareSeesTest {
    @Test
    void testSeesSameRoom(){
        Room r1= new Room();
        AbstractSquare square= new SquareSpawn(r1);
        assertTrue(square.sees(r1));
    }
    @Test
    void testSeesConnectedRoom(){
        Room r1= new Room();
        AbstractSquare s1= new SquareSpawn(new Room());
        AbstractSquare s2 = new SquareSpawn(r1);
        s1.connect(s2);
        assertTrue(s1.sees(r1));
    }
    @Test
    void testNotSeesNotConnected(){
        Room r1= new Room();
        AbstractSquare s1= new SquareSpawn(new Room());
        AbstractSquare s2= new SquareSpawn(new Room());
        AbstractSquare s3 = new SquareSpawn(r1);
        s1.connect(s2);
        s2.connect(s3);
        assertFalse(s1.sees(s3));
    }
}
