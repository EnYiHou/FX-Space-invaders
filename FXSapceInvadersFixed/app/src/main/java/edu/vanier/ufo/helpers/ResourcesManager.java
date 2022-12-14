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
    public static final int FRAMES_PER_SECOND = 65;
    private static final String RESOURCES_FOLDER = "";
    private static final String IMAGES_FOLDER = RESOURCES_FOLDER + "images/";
    private static final String SOUNDS_FOLDER = RESOURCES_FOLDER + "sounds/";
    private static final String FXML_FOLDER = RESOURCES_FOLDER + "fxml/";

    private static final String LEVEL_IMAGES = IMAGES_FOLDER + "levels/";
    private static final String ENDSCREEN_IMAGES = IMAGES_FOLDER + "endscreen/";

    private static final String ALLY_IMAGES = IMAGES_FOLDER + "ally/";
    private static final String ALLY_SHIP_IMAGES = ALLY_IMAGES + "ship/";
    private static final String ALLY_MISSILE_IMAGES = ALLY_IMAGES + "missile/";
    private static final String ENEMY_IMAGES = IMAGES_FOLDER + "enemy/";

    // Title image.
    public static final String TITLE = IMAGES_FOLDER + "title.jpg";

    // Level images.
    public static final String ENTRY_LEVEL = LEVEL_IMAGES + "entry.png";
    public static final String BASIC_LEVEL = LEVEL_IMAGES + "basic.png";
    public static final String INTERMEDIATE_LEVEL = LEVEL_IMAGES + "intermediate.png";
    public static final String ADVANCED_LEVEL = LEVEL_IMAGES + "advanced.png";

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

    // End screen images
    public static final String VICTORY = ENDSCREEN_IMAGES + "victory.png";
    public static final String GAME_OVER = ENDSCREEN_IMAGES + "gameover.png";

    // Collision gif
    public static final String ROCKET_EXPLOSION = IMAGES_FOLDER + "explosion.gif";

    //public static final String TRAILER_VIDEO = VIDEOS_FOLDER + "Trailer.mp4";
    public static final String BACKGROUND = IMAGES_FOLDER + "background.jpeg";

    // Sound source
    public static final String EXPLOSION = SOUNDS_FOLDER + "explosion.mp3";
    public static final String WIN = SOUNDS_FOLDER + "win.wav";
    public static final String LASER = SOUNDS_FOLDER + "laser.wav";
    public static final String ROCKET = SOUNDS_FOLDER + "rocket.wav";

    // Game level music
    public static final String LEVEL1 = SOUNDS_FOLDER + "level1.mp3";
    public static final String LEVEL2 = SOUNDS_FOLDER + "level2.mp3";
    public static final String LEVEL3 = SOUNDS_FOLDER + "level3.mp3";
    public static final String LEVEL4 = SOUNDS_FOLDER + "level4.mp3";

    // FXML files
    public static final String HOME_PAGE = FXML_FOLDER + "homgepage.fxml";

    public static final String[] LEVELS_LIST = {ENTRY_LEVEL, BASIC_LEVEL, INTERMEDIATE_LEVEL, ADVANCED_LEVEL};
    public static final String[] SOUND_LEVELS_LIST = {LEVEL1, LEVEL2, LEVEL3, LEVEL4};

}
