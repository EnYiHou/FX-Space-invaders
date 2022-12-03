package edu.vanier.ufo.game;


/**
 * A missile projectile without the radial gradient.
 */
public class Missile extends Atom {
    

    public Missile(String imagePath) {
        super(imagePath);
        
    }


    public double getX() {

        return getNode().getLayoutY() + getNode().getBoundsInParent().getWidth() / 2;

    }

    public double getY() {

        return getNode().getLayoutY() + getNode().getBoundsInParent().getWidth() / 2;
    }

}
