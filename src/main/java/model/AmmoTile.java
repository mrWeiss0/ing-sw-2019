package model;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AmmoTile implements Grabbable {
    private final AmmoCube ammo;
    private final Supplier<PowerUp> powerUp;
    private final Consumer<AmmoTile> discard;

    public AmmoTile(AmmoCube ammo, Supplier<PowerUp> powerUp, Consumer<AmmoTile> discard) {
        this.ammo = ammo;
        this.powerUp = powerUp;
        this.discard = discard;
    }

    public Optional<PowerUp> getPowerUp() {
        return Optional.ofNullable(powerUp.get());
    }

    public AmmoCube getAmmo() {
        return ammo;
    }

    public void discard() {
        discard.accept(this);
    }
}
