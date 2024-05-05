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
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import javafx.scene.image.ImageView;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 *
 * The spaceship object is the user-controlled character of the game.
 *
 * @author enyi
 */
public class Ship extends Sprite {

    /**
     * Deceleration of the spaceship. It must be lower than the acceleration
     * constant
     */
    private final static double DECELERATION_CONSTANT = 0.08;

    /**
     * Acceleration of the spaceship. It must be higher than the deceleration
     * constant
     */
    private final static double ACCELERATION_CONSTANT = 0.2;

    /**
     * Speed of the missile.
     */
    private final static float MISSILE_THRUST_AMOUNT = 40.3F;

    /**
     * Speed of shooting. The cool down of fire is inversely proportional to
     * this property
     */
    private float fireSpeed = 1f;

    /**
     * Maximum Angle of shooting bullets. When the spaceship shoots more than 1
     * missile, each missile will have an angle slightly deviated from the
     * center. This variable determines the maximum angle of deviation from the
     * one end to another end
     */
    private int maxAngleShooting = 30;

    /**
     * Boolean of if the spaceship can fire or not.
     */
    private boolean canFire;

    /**
     * Cooldown of the shield after it is activated in milliseconds
     */
    private float shieldCoolDown = 15000;

    /**
     * Boolean of whether the spaceship has a shield on.
     */
    private boolean shieldOn;

    /**
     * Boolean of whether the user can turn on the shield.
     */
    private boolean shieldAvailable = true;

    /**
     * Fade Transition of the shield to make it looks like it is slowly
     * disappearing with time.
     */
    private FadeTransition shieldFade;

    /**
     * A group contain all of the ship image view nodes.
     */
    private final Group flipBook = new Group();

    /**
     * A key code will be used for weapon selection.
     */
    private KeyCode keyCode;

    /**
     * The collision bounding region for the ship
     */
    private Circle hitBounds;

    /**
     * The remaining health of the spaceship
     */
    private IntegerProperty health = new SimpleIntegerProperty(3);

    /**
     * Booleans to tell which direction the spaceship is commanded to go.
     */
    private boolean wPressed, aPressed, sPressed, dPressed = false;

    /**
     * Constructor of a ship instance.
     */
    public Ship() {

        canFire = true;
        ImageView shipImageView = null;
        shipImageView = new ImageView(getClass().getResource(ResourcesManager.SPACE_SHIP1).toExternalForm());

        flipBook.getChildren().add(shipImageView);

        setNode(flipBook);
        initHitZone();
        flipBook.setTranslateX(350);
        flipBook.setTranslateY(450);
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
        double currentX = flipBook.getTranslateX();
        double currentY = flipBook.getTranslateY();

        flipBook.getChildren().clear();

        ImageView shipImageView = new ImageView(getClass().getResource(newShip).toExternalForm());
        flipBook.getChildren().add(shipImageView);
        setNode(flipBook);
        flipBook.setTranslateX(currentX);
        flipBook.setTranslateY(currentY);
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
        }
    }

    /**
     * Change the velocity of the atom particle.
     */
    @Override
    public void update() {

        deccelerate();
        accelerate();

        getNode().setTranslateX(getNode().getTranslateX() + vX);
        getNode().setTranslateY(getNode().getTranslateY() - vY);

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

    private void deccelerate() {
        if (vX > 0) {
            this.vX = Math.max(this.vX - DECELERATION_CONSTANT, 0);
        } else if (vX < 0) {
            this.vX = Math.min(this.vX + DECELERATION_CONSTANT, 0);
        }

        if (vY > 0) {
            this.vY = Math.max(this.vY - DECELERATION_CONSTANT, 0);
        } else if (vY < 0) {
            this.vY = Math.min(this.vY + DECELERATION_CONSTANT, 0);
        }

    }

    /**
     * Creates a corresponding missile that starts at the spaceship's location
     * and travels at a constant speed to the direction of the rotated ship.
     *
     * @param i the order of this missile in all the missiles that the spaceship
     * shoots. If the spaceship shoots more than 1 missile, each missile's
     * trajectory is deviated to a certain angle. This parameter tells its order
     * in the total number of shot missiles.
     * @param level the level of the game, this parameters how many levels there
     * are in total to allow the calculations of deviated angle.
     * @return
     */
    public Missile fire(int i, int level) {
        Missile fireMissile;
        float slowDownAmt;
        if (null == keyCode) {
            fireMissile = new Missile(ResourcesManager.ROCKET_NORMAL);
            slowDownAmt = 3f;
            fireMissile.setDamage(100);
            SoundManager.playSound("laser");
        } else {
            switch (keyCode) {
                case DIGIT4:
                    fireMissile = new Missile(ResourcesManager.ROCKET_ULTIMATE);
                    slowDownAmt = 3f;
                    fireMissile.setDamage(500);
                    SoundManager.playSound("rocket");
                    break;
                case DIGIT2:
                    fireMissile = new Missile(ResourcesManager.ROCKET_MEDIUM);
                    slowDownAmt = 2.3f;
                    fireMissile.setDamage(200);
                    SoundManager.playSound("laser");
                    break;
                case DIGIT3:
                    fireMissile = new Missile(ResourcesManager.ROCKET_HUGE);
                    slowDownAmt = 5f;
                    fireMissile.setDamage(300);
                    SoundManager.playSound("rocket");
                    break;
                default:
                    fireMissile = new Missile(ResourcesManager.ROCKET_NORMAL);
                    slowDownAmt = 3f;
                    fireMissile.setDamage(100);
                    SoundManager.playSound("laser");
                    break;
            }
        }

        fireMissile.setVelocityX(this.vX + Math.cos(Math.toRadians(this.getNode().getRotate() - (this.maxAngleShooting / 2) + (i * ((this.maxAngleShooting / (level + 1)))))) * (MISSILE_THRUST_AMOUNT - slowDownAmt));
        fireMissile.setVelocityY(-this.vY + Math.sin(Math.toRadians(this.getNode().getRotate() - (this.maxAngleShooting / 2) + (i * ((this.maxAngleShooting / (level + 1)))))) * (MISSILE_THRUST_AMOUNT - slowDownAmt));

        fireMissile.getNode().setRotate(this.getNode().getRotate() - (this.maxAngleShooting / 2) + (i * ((this.maxAngleShooting / (level + 1)))) + 90);

        return fireMissile;
    }

    /**
     * Change the current weapon to the weapon with corresponding key code
     *
     * @param keyCode the key code of the weapon to replace
     */
    public void changeWeapon(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Activates the spaceship's shield. The spaceship cannot be damaged while
     * being shielded, but collision will remove the shield and make the
     * spaceship vulnerable once again.
     */
    public void activateShield() {
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

    /**
     * @return the integer property of the health of the spaceship
     */
    public IntegerProperty getHealth() {
        return health;
    }

    /**
     * Set the value of the integer property of health to a desired value
     *
     * @param health the health to set
     */
    public void setHealth(int health) {
        this.health.set(health);
    }

    /**
     * Removes one heart of the spaceship.
     */
    public void damaged() {
        this.health.set(this.health.get() - 1);
    }

    //Used to calculate the direction of acceleration and the acceleration
    //of the spaceship
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

    /**
     *
     * @return if the spaceship has the shield on
     */
    public boolean isShieldOn() {
        return shieldOn;
    }

    /**
     * set the shield to the desired value
     *
     * @param shieldOn if the spaceship has the shield one
     */
    public void setShieldOn(boolean shieldOn) {
        this.shieldOn = shieldOn;
    }

    /**
     *
     * @return the fade transition of the shield
     */
    public FadeTransition getShieldFade() {
        return shieldFade;
    }

    /**
     *
     * @return the spaceship fire speed
     */
    public float getFireSpeed() {
        return fireSpeed;
    }

    /**
     *
     * @return the spaceship cool down for shield
     */
    public float getShieldCoolDown() {
        return shieldCoolDown;
    }

    /**
     *
     * @return if the cool down to active a shield is done
     */
    public boolean isShieldAvailable() {
        return shieldAvailable;
    }

    /**
     * set if the spaceship can be shielded
     * @param shieldAvailable if the spaceship can be shielded
     */
    public void setShieldAvailable(boolean shieldAvailable) {
        this.shieldAvailable = shieldAvailable;
    }

    /**
     * 
     * @return if the spaceship's cool down to fire is done.
     */
    public boolean isCanFire() {
        return canFire;
    }

    /**
     * set if the spaceship is in cool down to fire
     * @param canFire if the space ship can fire
     */
    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    /**
     * 
     * @return the maximum angle of shooting.
     */
    public int getMaxAngleShooting() {
        return maxAngleShooting;
    }

    /**
     * 
     * @return the chosen weapon's keycode
     */
    public KeyCode getKeyCode() {
        return keyCode;
    }

    /**
     * set the key code to the desired weapon's key code
     * @param keyCode the desired weapon's key code
     */
    public void setKeyCode(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

}
