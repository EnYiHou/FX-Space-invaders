package edu.vanier.ufo.helpers;

import java.util.HashMap;

/**
 * A resource manager providing useful resource definitions used in this game.
 *
 * @author Sleiman
 */
public class ResourcesManager {

    /**
     * Used to control the speed of the game.
     */
    public static final int FRAMES_PER_SECOND = 85;
    private static final String RESOURCES_FOLDER = "";
    private static final String IMAGES_FOLDER = RESOURCES_FOLDER + "images/";
    private static final String SOUNDS_FOLDER = RESOURCES_FOLDER + "sounds/";
    private static final String VIDEOS_FOLDER = RESOURCES_FOLDER + "videos/";
    private static final String STYLESHEET_FOLDER = RESOURCES_FOLDER + "stylesheet/";
    private static final String ALLY_IMAGES = IMAGES_FOLDER + "ally/";
    private static final String ALLY_SHIP_IMAGES = ALLY_IMAGES + "ship/";
    private static final String ALLY_MISSILE_IMAGES = ALLY_IMAGES + "missile/";
    private static final String ENEMY_IMAGES = IMAGES_FOLDER + "enemy/";

    // Title image.
    public static final String TITLE = IMAGES_FOLDER + "title.jpg";

    // Ally images.
    public static final String SPACE_SHIP1 = ALLY_SHIP_IMAGES + "level1.png";
    public static final String SPACE_SHIP2 = ALLY_SHIP_IMAGES + "level2.png";
    public static final String SPACE_SHIP3 = ALLY_SHIP_IMAGES + "level3.png";
    public static final String HEART = ALLY_IMAGES + "heart.png";

    // Enemy Images.
    public static final String BOSS = ENEMY_IMAGES + "boss.png";
    public static final String ENEMY1 = ENEMY_IMAGES + "enemy1.png";
    public static final String ENEMY2 = ENEMY_IMAGES + "enemy2.png";
    public static final String ENEMY3 = ENEMY_IMAGES + "enemy3.png";

    // Rocket images
    public static final String ROCKET_HUGE = ALLY_MISSILE_IMAGES + "huge.png";
    public static final String ROCKET_MEDIUM = ALLY_MISSILE_IMAGES + "medium.png";
    public static final String ROCKET_NORMAL = ALLY_MISSILE_IMAGES + "normal.png";
    public static final String ROCKET_ULTIMATE = ALLY_MISSILE_IMAGES + "ultimate.png";

    // Collision gif
    public static final String ROCKET_EXPLOSION = IMAGES_FOLDER + "explosion.gif";
    // Trailer Video
    public static final String TRAILER_VIDEO = VIDEOS_FOLDER + "Trailer.mp4";

    // Sound source
    public static final String EXPLOSION_SOUND = SOUNDS_FOLDER + "explosion.mp3";

    // Style sheet
    public static final String STYLESHEET = STYLESHEET_FOLDER + "stylesheet.css";

    public static final String[] INADER_SPRITES_PATH = {
        BOSS, ENEMY1, ENEMY2, ENEMY3
    };

}
