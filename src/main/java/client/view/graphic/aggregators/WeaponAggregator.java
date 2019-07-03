package client.view.graphic.aggregators;

import javafx.scene.image.Image;

public class WeaponAggregator {
    private Image weaponPortrait;
    private Image weaponDescription;

    public WeaponAggregator(Image weaponPortrait, Image weaponDescription)
    {
        this.weaponPortrait = weaponPortrait;
        this.weaponDescription = weaponDescription;
    }

    public Image getPortrait() {
        return weaponPortrait;
    }

    public Image getDescription() {
        return weaponDescription;
    }
}
