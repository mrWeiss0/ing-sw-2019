package client.view.graphic.loaders;

import client.view.graphic.aggregators.AmmoAggregator;
import client.view.graphic.aggregators.WeaponAggregator;
import javafx.scene.image.Image;
import server.model.weapon.Weapon;

import java.io.FileInputStream;

public class ImageLoader {

    public WeaponAggregator getWeaponImages(int weaponID) {
        return new WeaponAggregator(
                new Image(getClass().getResourceAsStream("src/main/resources/client/view/images/weapons/" + weaponID + "_PORTRAIT" + ".png")),
                new Image(getClass().getResourceAsStream("src/main/resources/client/view/images/weapons/" + weaponID + "_FULL"+ ".png"))
        );
    }

    public WeaponAggregator getPowerUpImages(int ppID, int color) {
        return new WeaponAggregator(
                new Image(getClass().getResourceAsStream("src/main/resources/client/view/images/powerups/" + ppID + "_" + color + "_PORTRAIT" + ".png")),
                new Image(getClass().getResourceAsStream("src/main/resources/client/view/images/powerups/" + ppID + "_" + color + "_PORTRAIT" + ".png"))
        );
    }

    public Image getPlayerPortrait(int id) {
           return new Image(getClass().getResourceAsStream("../../../../client/view/images/characters/"+id+".png"));
    }

    public Image getAction(int id) {
        //handle -1
        return new Image(getClass().getResourceAsStream("../../../../client/view/images/actions/"+id+".png"));
    }

    public AmmoAggregator getAmmoCubesImages(int[] ammo) {
        return new AmmoAggregator(
                new Image(getClass().getResourceAsStream("src/main/resources/client/view/images/ammocubes/RED"+ammo[0] +".png")),
                new Image(getClass().getResourceAsStream("src/main/resources/client/view/images/ammocubes/YELLOW"+ ammo[1] +".png")),
                new Image(getClass().getResourceAsStream("src/main/resources/client/view/images/ammocubes/BLUE"+ ammo[2] +".png"))
        );
    }

    public Image getDamage(int id, int qnty) {
        return new Image(getClass().getResourceAsStream("src/main/resources/client/view/images/lifebar/"+ id +"_"+ qnty +".png"));
    }

    public Image getKillTrack(int maxKill) {
        return new Image(getClass().getResourceAsStream("src/main/resources/client/view/images/lifebar/killtrack_" + maxKill +".png"));
    }

    public Image getMap(int mapID) {
        return new Image(getClass().getResourceAsStream("src/main/resources/client/view/images/maps/map" + mapID +".png"));
    }

}
