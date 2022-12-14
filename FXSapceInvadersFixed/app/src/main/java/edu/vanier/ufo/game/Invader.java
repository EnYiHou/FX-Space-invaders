/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.ufo.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.effect.ColorAdjust;

/**
 *
 * @author enyihou
 */
public class Invader extends Atom {

    

    /**
     * Optimize speed, avoid computing rotation angle at each frame. This tells
     * how many frame to wait to compute the rotation angle and the velocities
     */
    private static final int FRAME_COOLDOWN_ROTATION_UPDATE = 2;
    private static int COUNTER_ROTATION_SPEED = 0;

    /**
     * Integer property for the health of the invader
     */
    private final IntegerProperty health;
    private final double speed;
    private int point;
    private final Ship spaceShip;

    /**
     *
     * Invader instance that takes a ship as a target and follows it.
     *
     * @param target the targeted ship to follow
     * @param imagePath the path of the image of the invader
     * @param level the level of the game, its points and speed will be adjusted
     * according to it
     */
    public Invader(Ship target, String imagePath, int level) {

        super(imagePath);
        // Generate image with random colors
        this.getView().setEffect(new ColorAdjust(Math.random(), Math.random(), Math.random() - 0.5, 0.5));
        this.point = 50 * level;
        this.spaceShip = target;
        this.health = new SimpleIntegerProperty();
        this.speed = (5 + level);

    }

    /**
     * Update the invader according to the spaceship's position. Its rotation
     * will make the space ship direct toward the spaceship. Update the
     * velocityX and velocityY of the invader
     */
    @Override
    public void update() {
        COUNTER_ROTATION_SPEED++;
        if (COUNTER_ROTATION_SPEED == FRAME_COOLDOWN_ROTATION_UPDATE) {
            double angle = Math.atan2(
                    spaceShip.getCenterY() - this.getCenterY(),
                    spaceShip.getCenterX() - this.getCenterX());
            this.vX = Math.cos(angle) * speed;
            this.vY = Math.sin(angle) * speed;

            getNode().setRotate((int) (angle * 180 / Math.PI));
            COUNTER_ROTATION_SPEED = 0;
        }
        getNode().setTranslateX(getNode().getTranslateX() + vX);
        getNode().setTranslateY(getNode().getTranslateY() + vY);
    }

    /**
     *
     * @return the integer property of the health of the invader
     */
    public IntegerProperty getHealth() {
        return health;
    }

    /**
     *
     * @param health the value of the health to set.
     */
    public void setHealth(int health) {
        this.health.set(health);
    }

    /**
     *
     * @return the worth of this invader in points. The stronger the invader,
     * the more points it makes
     */
    public int getPoint() {
        return point;
    }

    /**
     * Set the worth points of this invader
     *
     * @param point the desired value of the worth points of this invader
     */
    public void setPoint(int point) {
        this.point = point;
    }

    /**
     *
     * @return the speed of the invader
     */
    public double getSpeed() {
        return speed;
    }

    /**
     *
     * @return if this invader is dead
     */
    public boolean isIsDead() {
        return isDead;
    }

    /**
     * Set if this invader is dead
     *
     * @param isDead the desired value to set if this invader is dead
     */
    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

}
