package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.engine.Sprite;
import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

/**
 * A spherical looking object (Atom) with a random radius, color, and velocity.
 * When two atoms collide each will fade and become removed from the scene. The
 * method called implode() implements a fade transition effect.
 *
 * @author cdea
 */
public class Atom extends Sprite {

    private static int LIFE_EXPECTENCY = 500;

    private Group flipBook = new Group();
    private int durationCounter = 0;
    private ImageView view; 
    Circle hitBounds;

    /**
     * Constructor will create a optionally create a gradient fill circle shape.
     * This sprite will contain a JavaFX Circle node.
     *
     * @param imagePath the path of the image to be embedded in the node object.
     */
    public Atom(String imagePath) {
        ImageView newAtom = new ImageView();
        Image shipImage = new Image(imagePath);
        newAtom.setImage(shipImage);
        this.view = newAtom;

        flipBook.getChildren().add(newAtom);
        setNode(flipBook);
        double hZoneCenterX = flipBook.getBoundsInLocal().getWidth() / 2;
        double hZoneCenterY = flipBook.getBoundsInLocal().getHeight() / 2;

        hitBounds = new Circle();
        hitBounds.setCenterX(hZoneCenterX);
        hitBounds.setCenterY(hZoneCenterY);
        hitBounds.setRadius(Math.max(flipBook.getBoundsInLocal().getWidth()/2, flipBook.getBoundsInLocal().getHeight()/2));
        hitBounds.setOpacity(0.5);
        flipBook.getChildren().add(hitBounds);
        setCollidingNode(hitBounds);
        setOriginalCollidingNode(hitBounds);
    }

    /**
     * Change the velocity of the current atom particle.
     */
    @Override
    public void update() {
        durationCounter++;
        getNode().setLayoutX(getNode().getLayoutX() + vX);
        getNode().setLayoutY(getNode().getLayoutY() + vY);
    }

    /**
     * Returns a node casted as a JavaFX Circle shape.
     *
     * @return Circle shape representing JavaFX node for convenience.
     */
    public ImageView getImageViewNode() {
        return (ImageView) ((Group) getNode()).getChildren().get(0);
    }

    /**
     * Animate an implosion. Once done remove from the game world
     *
     * @param gameWorld - game world
     */
    public void implode(final GameEngine gameWorld, double xCoord, double yCoord) {
        vX = vY = 0;
        Node currentNode = getNode();
        isDead = true;

        Image explosionImage = new Image(ResourcesManager.ROCKET_EXPLOSION, 150d, 150d, true, true);
        ImageView explosionAnimation = new ImageView(explosionImage);
        Group group = new Group(explosionAnimation);
        
        
        gameWorld.getSceneNodes().getChildren().remove(currentNode);
        gameWorld.getSceneNodes().getChildren().add(group);
        group.setLayoutX(xCoord- explosionImage.getWidth() / 2);
        group.setLayoutY(yCoord - explosionImage.getHeight() / 2);
       

    }

  
    public static int getLIFE_EXPECTENCY() {
        return LIFE_EXPECTENCY;
    }

    public static void setLIFE_EXPECTENCY(int LIFE_EXPECTENCY) {
        Atom.LIFE_EXPECTENCY = LIFE_EXPECTENCY;
    }

    public int getDurationCounter() {
        return durationCounter;
    }

    public ImageView getView() {
        return view;
    }

    public void setView(Image view) {
        this.view.setImage(view);
    }

    public Circle getHitBounds() {
        return hitBounds;
    }

    public void setHitBounds(Circle hitBounds) {
        this.hitBounds = hitBounds;
    }

    
    
    public void setDurationCounter(int durationCounter) {
        this.durationCounter = durationCounter;
    }
}


