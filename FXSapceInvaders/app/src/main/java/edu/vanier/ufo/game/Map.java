/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.ufo.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author enyihou
 */
public class Map extends Circle{

    /**
     * The radius of the map
     */
    private static final int MAP_RADIUS = 3000;
    
    /**
     * The number of stars to be generated
     */
    private static final int STAR_COUNT = (int)(MAP_RADIUS/0.5);
    
    /**
     * The color of the map
     */
    private static final Color MAP__COLOR = Color.valueOf("0F193B");
    
    /**
     * The possible colors for the stars
     */
    private static final ArrayList<Color> POSSIBLE_COLORS = new ArrayList<>(
            Arrays.asList(
                    new Color[]{
                        Color.AQUA,
                        Color.AQUAMARINE,
                        Color.BLUEVIOLET,
                        Color.TEAL,
                        Color.WHITESMOKE,
                        Color.BISQUE,
                        Color.DARKGOLDENROD
                    }));

    /**
     * Maximum radius of a star (circle)
     */
    private static final double STAR_MAX_SIZE = 3;

    /**
     * Minimum radius of a star (circle)
     */
    private static final double STAR_MIN_SIZE = 0.5;

    private final Random random = new Random();

    /**
     * 
     * @param root the scene node in which the map and the stars will be generated
     */
    public Map (Pane root) {
        super(MAP_RADIUS,MAP__COLOR);
        generateStars(root);
    }

    /**
     * Generate random stars(circle) at different location of the map with 
     * different colors
     * @param root the scene node in which the stars are generated
     */
    private void generateStars(Pane root) {
        int counter = 0;
        while (counter < STAR_COUNT) {

            double starRadius = random.nextDouble(STAR_MIN_SIZE, STAR_MAX_SIZE);
            Circle star = new Circle(starRadius, POSSIBLE_COLORS.get(random.nextInt(POSSIBLE_COLORS.size())));
            star.setTranslateX(random.nextDouble(-MAP_RADIUS, MAP_RADIUS));
            star.setTranslateY(random.nextDouble(-MAP_RADIUS, MAP_RADIUS));
            root.getChildren().add(star);
           
            counter++;
            if (Shape.intersect(this, star).getBoundsInParent().getWidth() == -1) {
                root.getChildren().remove(star);
                counter--;
            }
        }

    }
    
   
    public static int getMAP_RADIUS() {
        return MAP_RADIUS;
    }


}
