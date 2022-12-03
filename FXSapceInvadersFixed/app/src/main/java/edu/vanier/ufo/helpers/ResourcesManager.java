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
      // Title image.
    public static final String TITLE = IMAGES_FOLDER + "title.jpg";
    
    // Ship images.
    public static final String SPACE_SHIP = IMAGES_FOLDER + "spiked ship.png";
    public static final String SPACE_STAR_SHIP = IMAGES_FOLDER + "starship.png";
    public static final String SPACE_TANK = IMAGES_FOLDER + "tank.png";
    // Rocket images
    public static final String ROCKET_SMALL = IMAGES_FOLDER + "rocket.png";
    public static final String ROCKET_FIRE = IMAGES_FOLDER + "missile.png";
    // Collision gif
    public static final String ROCKET_EXPLOSION = IMAGES_FOLDER + "explosion.gif";
    // Trailer Video
    public static final String TRAILER_VIDEO = VIDEOS_FOLDER + "Trailer.mp4";

    // Invader sprites.
    public static final String INVADER_LARGE_SHIP = IMAGES_FOLDER + "large_invader_b.png";
    public static final String INVADER_SMALL_SHIP = IMAGES_FOLDER + "small_invader_b.png";
    public static final String INVADER_UFO = IMAGES_FOLDER + "ufo.png";
    public static final String INVADER_CHICKEN = IMAGES_FOLDER + "rounded-chicken.png";
    public static final String INVADER_BEE = IMAGES_FOLDER + "small-bee.png";
    public static final String INVADER_SCI_FI = IMAGES_FOLDER + "sci-fi.png";

    // Sound effect files
    public static final String SOUND_LASER = SOUNDS_FOLDER + "laser_2.mp3";
    //public static final String SOUND_LASER = SOUNDS_FOLDER + "alienMove2.wav";    

    public static final String[] INADER_SPRITES_PATH = {
        INVADER_UFO, INVADER_CHICKEN, INVADER_BEE, INVADER_SCI_FI
    };

//    public static final String ROCKET_SMALL = IMAGES_FOLDER + "rocket.png";
    public static HashMap<Integer, String> getInvaderSprites() {
        HashMap<Integer, String> invaders = new HashMap<Integer, String>();
        invaders.put(1, ResourcesManager.IMAGES_FOLDER + "large_invader_b.png");
        invaders.put(2, ResourcesManager.IMAGES_FOLDER + "small_invader_b.png");
        return invaders;
    }

}
