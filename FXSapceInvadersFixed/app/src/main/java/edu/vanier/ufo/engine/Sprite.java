package edu.vanier.ufo.engine;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

/**
 * A class used to represent a sprite of any type on the scene.
 */
public abstract class Sprite {

    // The JavaFX node that holds the sprite graphic.
    protected Node node;
    private Image image;
    protected double vX;
    protected double vY;
    private double width;
    private double height;
    public boolean isDead = false;

    protected Circle collidingNode;
    protected Circle originalCollidingNode;

    public Sprite() {
        vX = 0;
        vY = 0;
    }

    public void setImage(Image inImage) {
        image = inImage;
        width = inImage.getWidth();
        height = inImage.getHeight();
    }

    public void setImage(String filename) {
        Image image = new Image(filename);
        setImage(image);
    }

    public void setVelocity(double x, double y) {
        vX = x;
        vY = y;
    }

    public void addVelocity(double x, double y) {
        vX += x;
        vY += y;
    }

    
      public double getCenterX() {
        return getNode().getLayoutX() + ( getNode().getBoundsInLocal().getWidth() / 2);
    }

    /**
     * The center Y coordinate of the current visible image. See
     * <code>getCurrentShipImage()</code> method.
     *
     * @return The scene or screen Y coordinate.
     */
    public double getCenterY() {
        return getNode().getLayoutY() + ( getNode().getBoundsInLocal().getHeight() / 2);
    }
    /**
     * Did this sprite collide into the other sprite?
     *
     * @param other - The other sprite.
     * @return boolean - Whether this or the other sprite collided, otherwise
     * false.
     */
    public boolean collide(Sprite other) {
        return collidingNode.getBoundsInParent().intersects(other.node.getBoundsInParent());

    }

    public abstract void update();

    public boolean intersects(Sprite s) {
        //return s.getBoundary().intersects(this.getBoundary());        
        Bounds sBounds = s.getNode().localToScene(s.getNode().getBoundsInLocal());
        return node.intersects(sBounds);

    }

    public Image getImage() {
        return image;
    }

    public double getVelocityX() {
        return vX;
    }

    public void setVelocityX(double velocityX) {
        this.vX = velocityX;
    }

    public double getVelocityY() {
        return vY;
    }

    public void setVelocityY(double velocityY) {
        this.vY = velocityY;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

  
    public void handleDeath(GameEngine gameWorld) {
        gameWorld.getSpriteManager().addSpritesToBeRemoved(this);
    }

    public Circle getOriginalCollidingNode() {
        return originalCollidingNode;
    }

    public void setOriginalCollidingNode(Circle originalCollidingNode) {
        this.originalCollidingNode = originalCollidingNode;
    }

    public Circle getCollidingNode() {
        return collidingNode;
    }

    public void setCollidingNode(Circle collidingNode) {
        this.collidingNode = collidingNode;
    }
    
    
}
