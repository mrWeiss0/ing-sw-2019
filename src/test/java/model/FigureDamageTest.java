package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FigureDamageTest {
    static Figure f1;
    static Player p1;
    static Figure f2;
    static Player p2;
    @BeforeAll
    void init(){
        f1= new Figure();
        f2= new Figure();
        p1= new Player();
        p2= new Player();
    }
    @Test
    void testSimpleDamage(){

    }
}
