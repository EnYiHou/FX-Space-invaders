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

    // The JavaFX Shape Circle that corresponds to the collision node of the sprite
    protected Circle collidingNode;
    

    
    public Sprite() {
        vX = 0;
        vY = 0;
    }

    
    /**
     * set the sprite image
     * @param inImage the image to set
     */
    public void setImage(Image inImage) {
        image = inImage;
        width = inImage.getWidth();
        height = inImage.getHeight();
    }

    /**
     * set the sprite image
     * @param filename the file path of the image to set
     */
    public void setImage(String filename) {
        Image image = new Image(filename);
        setImage(image);
    }

    /**
     * Set the velocity of the sprite
     * @param x the x-axis velocity
     * @param y  the y-axis velocity
     */
    public void setVelocity(double x, double y) {
        vX = x;
        vY = y;
    }

    /**
     * Add the velocity to the velocity of the sprite
     * @param x the x-axis velocity
     * @param y  the y-axis velocity
     */
    public void addVelocity(double x, double y) {
        vX += x;
        vY += y;
    }

     /**
     * The center X coordinate of the current visible image. See
     * <code>getCurrentShipImage()</code> method.
     *
     * @return The scene or screen X coordinate.
     */
      public double getCenterX() {
        return getNode().getTranslateX() + ( getNode().getBoundsInLocal().getWidth() / 2);
    }

    /**
     * The center Y coordinate of the current visible image. See
     * <code>getCurrentShipImage()</code> method.
     *
     * @return The scene or screen Y coordinate.
     */
    public double getCenterY() {
        return getNode().getTranslateY() + ( getNode().getBoundsInLocal().getHeight() / 2);
    }
  
    /**
     * detect whether this sprite collide with another sprite
     * 
     * @param other another sprite to check collision with
     * @return a boolean of whether this sprite collide with the other sprite
     */
    public boolean collide(Sprite other) {
        return collidingNode.getBoundsInParent().intersects(other.node.getBoundsInParent());

    }

    /**
     * This method determines how this sprit update at each frame
     */
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

    

    public Circle getCollidingNode() {
        return collidingNode;
    }

    public void setCollidingNode(Circle collidingNode) {
        this.collidingNode = collidingNode;
    }
    
    
}
