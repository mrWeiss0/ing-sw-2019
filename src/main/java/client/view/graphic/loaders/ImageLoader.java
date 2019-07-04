package client.view.graphic.loaders;

import client.view.graphic.aggregators.AmmoAggregator;
import client.view.graphic.aggregators.WeaponAggregator;
import javafx.scene.image.Image;
import server.model.weapon.Weapon;

public class ImageLoader {

    public WeaponAggregator getWeaponImages(int weaponID) {
        return new WeaponAggregator(
                new Image("../../../../client/view/images/weapons" + weaponID + "_PORTRAIT"),
                new Image("../../../../client/view/images/" + weaponID + "_FULL")
        );
    }

    public WeaponAggregator getPowerUpImages(int ppID, int color) {
        return new WeaponAggregator(
                new Image("../../../../client/view/images/powerups" + ppID + "_" + color + "_PORTRAIT"),
                new Image("../../../../client/view/images/powerups" + ppID + "_" + color + "_PORTRAIT")
        );
    }

    public Image getPlayerPotrait(int id) {
        return new Image("../../../../client/view/images/characters" + id);
    }

    public AmmoAggregator getAmmoCubesImages(int[] ammo) {
        return new AmmoAggregator(
                new Image("../../../../client/view/images/ammocubes/RED"+ammo[0]),
                new Image("../../../../client/view/images/ammocubes/YELLOW"+ ammo[1]),
                new Image("../../../../client/view/images/ammocubes/BLUE"+ ammo[2])
        );
    }

    public Image getDamage(int id, int qnty) {
        return new Image("../../../../client/view/images/lifebar/"+ id +"_"+ qnty);
    }

    public Image getKillTrack(int maxKill) {
        return new Image("../../../../client/view/images/lifebar/killtrack_" + maxKill);
    }

    public Image getMap(int mapID) {
        return new Image("../../../../client/view/images/maps/map" + mapID);
    }

}
