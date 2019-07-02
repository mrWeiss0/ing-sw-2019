package client.view.graphic.loaders;

import client.view.graphic.Weapon;
import javafx.scene.image.Image;

public class ImageLoader {
    public Weapon getWeaponImages(String weaponID) {
        return new Weapon(new Image("../../../../client/view/images/" + weaponID + "_PORTRAIT"), new Image("../../../../client/view/images/" + weaponID + "_FULL"));
    }

}
