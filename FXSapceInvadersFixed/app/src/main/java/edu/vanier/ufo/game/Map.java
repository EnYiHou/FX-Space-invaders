/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.ufo.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author enyihou
 */
public class Map extends Circle{

    private static final int MAP_RADIUS = 3000;
    private static final int STAR_COUNT = (int)(MAP_RADIUS/0.5);
    private static final Color MAP__COLOR = Color.valueOf("0F193B");
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

    private static final double STAR_MAX_SIZE = 3;

    private static final double STAR_MIN_SIZE = 0.5;

    private final Random random = new Random();

    public Map (Pane root) {
        super(MAP_RADIUS,MAP__COLOR);
        generateStars(root);
    }

    private void generateStars(Pane root) {
        int counter = 0;
        while (counter < STAR_COUNT) {

            double starRadius = random.nextDouble(STAR_MIN_SIZE, STAR_MAX_SIZE);
            Circle star = new Circle(starRadius, POSSIBLE_COLORS.get(random.nextInt(POSSIBLE_COLORS.size())));
            star.setLayoutX(random.nextDouble(-MAP_RADIUS, MAP_RADIUS));
            star.setLayoutY(random.nextDouble(-MAP_RADIUS, MAP_RADIUS));
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
