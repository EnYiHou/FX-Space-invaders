package edu.vanier.ufo.game;

/**
 * A missile projectile without the radial gradient.
 */
public class Missile extends Atom {

    /**
     * The damage that a missile does to an invader.
     */
    private int damage;

    public Missile(String imagePath) {
        super(imagePath);

    }

    /**
     *
     * @return the damage of the missile
     */
    public int getDamage() {
        return damage;
    }

    /**
     * set the damage of the missile
     * @param damage the value of the desired damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

}
