package client.view.graphic;

import javafx.scene.image.Image;

public class Weapon {
    private Image weaponPortrait;
    private Image weaponDescription;

    public Weapon(Image weaponPortrait, Image weaponDescription)
    {
        this.weaponPortrait = weaponPortrait;
        this.weaponDescription = weaponDescription;
    }
}
