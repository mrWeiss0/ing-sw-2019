package model;

/**
 * The <code>Targettable</code> interface ensures that the correct damages and
 * marks are dealt when targeting areas, using the chain of responsibility
 * pattern.
 * <p>
 * It provides two methods that delegate damage dealing from this target to all
 * its smaller collections of targettables until a damageable or markable class
 * is reached. The individual must then take care of the damages and marks
 * dealt to him.
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

    void applyMarks();
}
