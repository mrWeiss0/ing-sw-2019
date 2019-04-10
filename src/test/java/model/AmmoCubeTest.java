package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AmmoCubeTest {
    @Test
    void TestSimpleSum(){
        AmmoCube a= new AmmoCube(new ArrayList<>(Arrays.asList(1,2,3)));
        AmmoCube b= new AmmoCube(new ArrayList<>(Arrays.asList(2,0,0)));
        AmmoCube c = a.add(b);
        ArrayList<Integer> r= new ArrayList<>(Arrays.asList(3,2,3));
        assertEquals(r,c.getAmmos());
    }
    @Test
    void TestSumLimit(){
        AmmoCube a= new AmmoCube(new ArrayList<>(Arrays.asList(1,2,3)));
        AmmoCube b= new AmmoCube(new ArrayList<>(Arrays.asList(3,0,0)));
        AmmoCube c = a.add(b);
        ArrayList<Integer> r= new ArrayList<>(Arrays.asList(3,2,3));
        assertEquals(r,c.getAmmos());
    }
    @Test
    void TestSimpleSub(){
        AmmoCube a= new AmmoCube(new ArrayList<>(Arrays.asList(1,2,3)));
        AmmoCube b= new AmmoCube(new ArrayList<>(Arrays.asList(1,0,0)));
        AmmoCube c = a.sub(b);
        ArrayList<Integer> r= new ArrayList<>(Arrays.asList(0,2,3));
        assertEquals(r,c.getAmmos());
    }
    @Test
    void TestSubLimit(){
        AmmoCube a= new AmmoCube(new ArrayList<>(Arrays.asList(1,2,3)));
        AmmoCube b= new AmmoCube(new ArrayList<>(Arrays.asList(3,0,0)));
        AmmoCube c = a.sub(b);
        ArrayList<Integer> r= new ArrayList<>(Arrays.asList(0,2,3));
        assertEquals(r,c.getAmmos());
    }

}