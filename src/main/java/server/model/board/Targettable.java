package server.model.board;

/**
 * The <code>Targettable</code> interface ensures that when targeting a
 * <code>Targettable</code> area the correct damages and marks are dealt to
 * the targettables within it.
 * <p>
 * It provides two methods that delegate damage dealing from the target to
 * contained collections of targettables until a damageable or markable class
 * is reached, the class must then take care of the damages and marks
 * dealt to it.
 */
public interface Targettable {

    /**
     * Delegates damage dealing from this target to all smaller targets
     * contained within this.
     *
     * @param dealer the figure that has dealt the damage
     * @param n      the amount of damage given
     */
    void damageFrom(Figure dealer, int n);

    /**
     * Delegates marks assigning from this target to all smaller targets
     * contained within this.
     *
     * @param dealer the figure that has dealt the damage
     * @param n      the amount of damage given
     */
    void markFrom(Figure dealer, int n);
}
