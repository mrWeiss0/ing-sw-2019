package client.view.graphic.aggregators;

import javafx.scene.image.Image;

public class AmmoAggregator {

    private Image[] rybImages = new Image[3];

    public AmmoAggregator(Image red, Image yellow, Image blue) {
        rybImages[0] = red;
        rybImages[1] = yellow;
        rybImages[2] = blue;
    }

    public Image[] getRybImages() { return rybImages; }
}
