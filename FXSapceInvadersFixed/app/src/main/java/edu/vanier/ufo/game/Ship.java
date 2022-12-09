package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.engine.SoundManager;
import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.engine.Sprite;
import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

    private float fireSpeed;

    private boolean canFire;

    /**
     * Turn shields on
     */
    private boolean shieldOn;

    private boolean shieldAvailable = true;

    private FadeTransition shieldFade;

    /**
     * The collision bounding region for the ship
     */
    private Circle hitBounds;

    private IntegerProperty health = new SimpleIntegerProperty(3);

    private boolean wPressed, aPressed, sPressed, dPressed = false;

    public Ship() {

        canFire = true;
        // Load one image.
        ImageView shipImageView = new ImageView(ResourcesManager.SPACE_SHIP1);
        flipBook.getChildren().add(shipImageView);
        shipImageView.setCache(true);
        shipImageView.setCacheHint(CacheHint.SPEED);

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
            hitBounds.setStroke(Color.AQUA);
            hitBounds.setFill(Color.CHARTREUSE);
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

        getNode().setLayoutX(getNode().getLayoutX() + vX);
        getNode().setLayoutY(getNode().getLayoutY() - vY);

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
        if (KeyCode.DIGIT4 == keyCode) {
            fireMissile = new Missile(ResourcesManager.ROCKET_ULTIMATE);
            slowDownAmt = 3f;
            fireMissile.setDamage(500);
            SoundManager.playSound("rocket");

        } else if (KeyCode.DIGIT2 == keyCode) {
            fireMissile = new Missile(ResourcesManager.ROCKET_MEDIUM);
            slowDownAmt = 2.3f;
            fireMissile.setDamage(200);
            SoundManager.playSound("rocket");
        } else if (KeyCode.DIGIT3 == keyCode) {
            fireMissile = new Missile(ResourcesManager.ROCKET_HUGE);
            slowDownAmt = 5f;
            fireMissile.setDamage(300);
            SoundManager.playSound("rocket");
        } else {
            fireMissile = new Missile(ResourcesManager.ROCKET_NORMAL);
            slowDownAmt = 3f;
            fireMissile.setDamage(100);
            SoundManager.playSound("laser");
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
        if (collidingNode.getOpacity() == 0) {

            // add shield
            this.collidingNode.setOpacity(0.5);

            shieldFade = new FadeTransition(Duration.seconds(5), this.collidingNode);
            shieldFade.setByValue(-0.4);
            shieldFade.setOnFinished((ActionEvent actionEvent) -> {
                shieldOn = false;
                collidingNode.setOpacity(0);
            });
            shieldFade.playFromStart();
            shieldFade.setCycleCount(7);
            shieldOn = true;

        }
    }

    @Override
    public void handleDeath(GameEngine gameWorld) {

    }

    public IntegerProperty getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health.set(health);
    }

    public void damaged() {
        this.health.set(this.health.get() - 1);
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

    public boolean isShieldOn() {
        return shieldOn;
    }

    public void setShieldOn(boolean shieldOn) {
        this.shieldOn = shieldOn;
    }

    public FadeTransition getShieldFade() {
        return shieldFade;
    }

    public void setShieldFade(FadeTransition shieldFade) {
        this.shieldFade = shieldFade;
    }

    public float getFireSpeed() {
        return fireSpeed;
    }

    public void setFireSpeed(float fireSpeed) {
        this.fireSpeed = fireSpeed;
    }

    public boolean isShieldAvailable() {
        return shieldAvailable;
    }

    public void setShieldAvailable(boolean shieldAvailable) {
        this.shieldAvailable = shieldAvailable;
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

}
