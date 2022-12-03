/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.SpriteManager;
import edu.vanier.ufo.helpers.ResourcesManager;
import java.util.Random;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author enyihou
 */


public class Invader extends Atom {

    private static final Random randomizer = new Random();
   

    private double speed;
    private int point;
    private InvaderType type;
    private IntegerProperty health;
    private final Ship spaceShip;

    
    public Invader(Ship target, String imagePath, int level) {

        super(imagePath);
        this.getView().setPreserveRatio(true);
        this.speed = randomizer.nextDouble(3*(1+level/3), 5*(1+level/3));
        this.point = 50 * level;
        spaceShip = target;
        health = new SimpleIntegerProperty();
        

    }
    

    @Override
    public void update() {

        double angle = Math.atan2(
                spaceShip.getCenterY() - this.getCenterY(),
                spaceShip.getCenterX() - this.getCenterX());
        this.vX = Math.cos(angle) * speed;
        this.vY = Math.sin(angle) * speed;
        getNode().setLayoutX(getNode().getLayoutX() + vX);
        getNode().setLayoutY(getNode().getLayoutY() + vY);

        getNode().setRotate(angle*180/Math.PI);
    }

    public InvaderType getType() {
        return type;
    }

    public void setType(InvaderType type) {
        this.type = type;
    }

    public IntegerProperty getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health.set(health);
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

}
