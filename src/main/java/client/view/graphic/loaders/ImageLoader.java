package client.view.graphic.loaders;

import client.view.graphic.aggregators.AmmoAggregator;
import client.view.graphic.aggregators.WeaponAggregator;
import javafx.scene.image.Image;
import server.model.weapon.Weapon;

public class ImageLoader {

    public WeaponAggregator getWeaponImages(String weaponID) {
        return new WeaponAggregator(
                new Image("../../../../client/view/images/weapons" + weaponID + "_PORTRAIT"),
                new Image("../../../../client/view/images/" + weaponID + "_FULL")
        );
    }

    public WeaponAggregator getPowerUpImages(String ppID, String color) {
        return new WeaponAggregator(
                new Image("../../../../client/view/images/powerups" + ppID + "_" + color + "_PORTRAIT"),
                new Image("../../../../client/view/images/powerups" + ppID + "_" + color + "_PORTRAIT")
        );
    }

    public Image getPlayerPotrait(int id) {
        return new Image("../../../../client/view/images/characters" + Integer.toString(id));
    }

    public AmmoAggregator getAmmoCubesImages(int[] ammo) {
        return new AmmoAggregator(
                new Image("../../../../client/view/images/ammocubes/RED"+Integer.toString(ammo[0])),
                new Image("../../../../client/view/images/ammocubes/YELLOW"+Integer.toString(ammo[1])),
                new Image("../../../../client/view/images/ammocubes/BLUE"+Integer.toString(ammo[2]))
        );
    }

    public Image getDamage(int id, int qnty) {
        return new Image("../../../../client/view/images/lifebar/"+Integer.toString(id)+"_"+Integer.toString(qnty));
    }

}
