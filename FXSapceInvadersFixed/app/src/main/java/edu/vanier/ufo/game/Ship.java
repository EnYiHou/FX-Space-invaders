package edu.vanier.ufo.game;

import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.engine.Sprite;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import javafx.scene.image.ImageView;

/**
 * A spaceship with 32 directions When two atoms collide each will fade and
 * become removed from the scene. The method called implode() implements a fade
 * transition effect.
 *
 * @author cdea
 */
public class Ship extends Sprite {

    private final static double DECCELERATION_CONSTANT = 0.04;
    private final static double ACCELERATION_CONSTANT = 0.1;

    private final static float MISSILE_THRUST_AMOUNT = 20.3F;

    /**
     * A group contain all of the ship image view nodes.
     */
    private final Group flipBook = new Group();

    /**
     * A key code will be used for weapon selection.
     */
    private KeyCode keyCode;

    /**
     * Turn shields on
     */
    private boolean shieldOn;

    /**
     * Green shield to be used as collision bounds.
     */
    private Circle shield;

    /**
     * A fade effect while the shields are up momentarily
     */
    FadeTransition shieldFade;

    /**
     * The collision bounding region for the ship
     */
    private Circle hitBounds;
    
    
    private int health = 3;

    private boolean wPressed, aPressed, sPressed, dPressed = false;

    public Ship() {

        // Load one image.
        ImageView shipImageView = new ImageView(ResourcesManager.SPACE_STAR_SHIP);
        flipBook.getChildren().add(shipImageView);

        // set javafx node to an imagefirstShip.setVisible(true);
        setNode(flipBook);
        initHitZone();
        flipBook.setLayoutX(350);
        flipBook.setLayoutY(450);
        flipBook.setCache(true);
        flipBook.setCacheHint(CacheHint.SPEED);
        flipBook.setManaged(false);
        flipBook.setAutoSizeChildren(false);

    }

    /**
     * Change the ship imageView
     *
     * @param newShip new image of ship
     */
    public void changeShip(String newShip) {
        double currentX = flipBook.getLayoutX();
        double currentY = flipBook.getLayoutY();

        flipBook.getChildren().clear();

        Image shipImage;
        shipImage = new Image(newShip, true);
        ImageView shipImageView = new ImageView(newShip);
        flipBook.getChildren().add(shipImageView);
        setNode(flipBook);
        flipBook.setLayoutX(currentX);
        flipBook.setLayoutY(currentY);
        flipBook.setCache(true);
        flipBook.setCacheHint(CacheHint.SPEED);
        flipBook.setManaged(false);
        flipBook.setAutoSizeChildren(false);
        initHitZone();
    }

    /**
     * Initialize the collision region for the space ship. It's just an
     * inscribed circle.
     */
    public void initHitZone() {
        // build hit zone
        if (hitBounds == null) {
            //RotatedShipImage firstShip = directionalShips.get(0);
            
            hitBounds = new Circle();
            hitBounds.setCenterX(getCenterX());
            hitBounds.setCenterY(getCenterY());
            hitBounds.setOpacity(0);
            hitBounds.setRadius(50);
             flipBook.getChildren().add(hitBounds);
            setCollidingNode(hitBounds);
            setOriginalCollidingNode(hitBounds);
        }
    }

    /**
     * Change the velocity of the atom particle.
     */
    @Override
    public void update() {

        accelerate();
        deccelerate();

         getNode().setLayoutX( getNode().getLayoutX() + vX);
         getNode().setLayoutY( getNode().getLayoutY() - vY);

    }

    private void deccelerate() {
        if (vX > 0) {
            this.vX = Math.max(this.vX - DECCELERATION_CONSTANT, 0);
        } else if (vX < 0) {
            this.vX = Math.min(this.vX + DECCELERATION_CONSTANT, 0);
        }

        if (vY > 0) {
            this.vY = Math.max(this.vY - DECCELERATION_CONSTANT, 0);
        } else if (vY < 0) {
            this.vY = Math.min(this.vY + DECCELERATION_CONSTANT, 0);
        }

    }

    private void accelerate() {
        double x = 0;
        double y = 0;

        if (wPressed) {
            y += 1;
        }
        if (aPressed) {
            x -= 1;
        }
        if (sPressed) {
            y -= 1;
        }
        if (dPressed) {
            x += 1;
        }

        if ((wPressed || aPressed || sPressed || dPressed)) {

            if ((wPressed && sPressed && !aPressed && !dPressed) || (aPressed && dPressed && !wPressed && !sPressed)) {

            } else {
                double movementAngle = Math.atan2(y, x);

                this.vX += Math.cos(movementAngle) * ACCELERATION_CONSTANT;
                this.vY += Math.sin(movementAngle) * ACCELERATION_CONSTANT;

            }

        }
    }

    /**
     * The center X coordinate of the current visible image. See
     * <code>getCurrentShipImage()</code> method.
     *
     * @return The scene or screen X coordinate.
     */
  

    public Missile fire() {
        Missile fireMissile;
        float slowDownAmt = 0;
        if (KeyCode.DIGIT2 == keyCode) {
            fireMissile = new Missile(ResourcesManager.ROCKET_FIRE);
            slowDownAmt = 1f;
        } else {
            fireMissile = new Missile(ResourcesManager.ROCKET_SMALL);
            slowDownAmt = 2.3f;
        }

        fireMissile.setVelocityX(this.vX + Math.cos(Math.toRadians(this.getNode().getRotate())) * (MISSILE_THRUST_AMOUNT - slowDownAmt));
        fireMissile.setVelocityY(-this.vY + Math.sin(Math.toRadians(this.getNode().getRotate())) * (MISSILE_THRUST_AMOUNT - slowDownAmt));

        fireMissile.getNode().setRotate(getNode().getRotate() + 90);

        return fireMissile;
    }

    public void changeWeapon(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    public void shieldToggle() {
        if (shield == null) {

            double x = getNode().getBoundsInLocal().getWidth() / 2;
            double y = getNode().getBoundsInLocal().getHeight() / 2;

            // add shield
            shield = new Circle();
            shield.setRadius(60);
            shield.setStrokeWidth(5);
            shield.setStroke(Color.LIMEGREEN);
            shield.setCenterX(x);
            shield.setCenterY(y);
            shield.setOpacity(.70);
            setCollidingNode(shield);
            //--
            shieldFade = new FadeTransition();
            shieldFade.setFromValue(1);
            shieldFade.setToValue(.40);
            shieldFade.setDuration(Duration.millis(1000));
            shieldFade.setCycleCount(12);
            shieldFade.setAutoReverse(true);
            shieldFade.setNode(shield);
            shieldFade.setOnFinished((ActionEvent actionEvent) -> {
                shieldOn = false;
                flipBook.getChildren().remove(shield);
                shieldFade.stop();
                setCollidingNode(hitBounds);
            });
            shieldFade.playFromStart();

        }
        shieldOn = !shieldOn;
        if (shieldOn) {
            setCollidingNode(shield);
            flipBook.getChildren().add(0, shield);
            shieldFade.playFromStart();
        } else {
            flipBook.getChildren().remove(shield);
            shieldFade.stop();
            setCollidingNode(hitBounds);
        }
    }
    
    public void damaged(){
        this.health--;
    }

    public boolean iswPressed() {
        return wPressed;
    }

    public void setwPressed(boolean wPressed) {
        this.wPressed = wPressed;
    }

    public boolean isaPressed() {
        return aPressed;
    }

    public void setaPressed(boolean aPressed) {
        this.aPressed = aPressed;
    }

    public boolean issPressed() {
        return sPressed;
    }

    public void setsPressed(boolean sPressed) {
        this.sPressed = sPressed;
    }

    public boolean isdPressed() {
        return dPressed;
    }

    public void setdPressed(boolean dPressed) {
        this.dPressed = dPressed;
    }

}
