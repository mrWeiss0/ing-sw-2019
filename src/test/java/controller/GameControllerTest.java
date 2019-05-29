package controller;

import model.AmmoCube;
import model.Game;
import org.junit.jupiter.api.BeforeAll;


import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {


    @BeforeAll
    void init(){
        Game game= new Game.Builder().nKills(8).maxDamages(12).deathDamage(11).maxPowerUps(3).killPoints(new int[]{8,6,4,2})
                .frenzyPoints(new int[]{2,1,1,1}).frenzyOn(true).defaultAmmo(new AmmoCube(1,1,1)).maxWeapons(3).maxMarks(3).build();
    }

}