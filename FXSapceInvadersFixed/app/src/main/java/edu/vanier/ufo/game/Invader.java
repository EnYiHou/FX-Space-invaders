/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.engine.Sprite;
import edu.vanier.ufo.helpers.ResourcesManager;
import java.util.Random;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author enyihou
 */
public class Invader extends Atom{

    
    
    private static Random randomizer = new Random();
    private double speed;
    private int point;
    
    private final Ship spaceShip;

    public Invader(Ship target) {
        
        super(ResourcesManager.INVADER_SCI_FI);
        this.speed = randomizer.nextDouble(3, 5);

        spaceShip = target;
       
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

    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
    

}
