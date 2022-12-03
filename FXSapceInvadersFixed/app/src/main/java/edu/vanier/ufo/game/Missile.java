package edu.vanier.ufo.game;


/**
 * A missile projectile without the radial gradient.
 */
public class Missile extends Atom {
    

    private int damage;
    
    public Missile(String imagePath) {
        super(imagePath);
        
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }



}
