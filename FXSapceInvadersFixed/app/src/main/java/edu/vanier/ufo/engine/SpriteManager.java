package edu.vanier.ufo.engine;

import edu.vanier.ufo.game.Invader;
import java.util.*;

/**
 * Sprite manager is responsible for holding all sprite objects, and cleaning up
 * sprite objects to be removed. All collections are used by the JavaFX
 * application thread. During each cycle (animation frame) sprite management
 * occurs. This assists the user of the API to not have to create lists to later
 * be garbage collected. Should provide some performance gain.
 *
 * @author cdea
 */
public class SpriteManager {

    /**
     * All the sprite objects currently in play
     */
    private final static List<Sprite> sprites = new ArrayList<>();

    /**
     * A global single threaded list used to check collision against other
     * sprite objects.
     */
    private final static List<Sprite> collisionList = new ArrayList<>();

    /**
     * A global single threaded set used to cleanup or remove sprite objects in
     * play.
     */
    private final static Set<Sprite> spritesToBeRemoved = new HashSet<>();

    private final static List<Invader> invaders = new ArrayList<>();

    /**
     * Get the list of sprites.
     *
     * @return a list of sprites.
     */
    public List<Sprite> getAllSprites() {
        return sprites;
    }

    /**
     * VarArgs of sprite objects to be added to the game.
     *
     * @param inSprites
     */
    public void addSprites(Sprite... inSprites) {
        sprites.addAll(Arrays.asList(inSprites));
    }

    /**
     * VarArgs of sprite objects to be removed from the game.
     *
     * @param inSprites
     */
    public void removeSprites(Sprite... inSprites) {
        sprites.removeAll(Arrays.asList(inSprites));
    }

    /**
     * Returns a set of sprite objects to be removed from the GAME_ACTORS.
     *
     * @return CLEAN_UP_SPRITES
     */
    public Set<Sprite> getSpritesToBeRemoved() {
        return spritesToBeRemoved;
    }

    /**
     * Adds sprite objects to be removed
     *
     * @param sprites varargs of sprite objects.
     */
    public void addSpritesToBeRemoved(Sprite... sprites) {
        if (sprites.length > 1) {
            spritesToBeRemoved.addAll(Arrays.asList((Sprite[]) sprites));
        } else {
            spritesToBeRemoved.add(sprites[0]);
        }
    }

    /**
     * Returns a list of sprite objects to assist in collision checks. This is a
     * temporary and is a copy of all current sprite objects (copy of
     * GAME_ACTORS).
     *
     * @return CHECK_COLLISION_LIST
     */
    public List<Sprite> getCollisionsToCheck() {
        return collisionList;
    }

    /**
     * Clears the list of sprite objects in the collision check collection
     * (CHECK_COLLISION_LIST).
     */
    public void resetCollisionsToCheck() {
        collisionList.clear();
        collisionList.addAll(sprites);
    }

    /**
     * Removes sprite objects and nodes from all temporary collections such as:
     * CLEAN_UP_SPRITES. The sprite to be removed will also be removed from the
     * list of all sprite objects called (GAME_ACTORS).
     */
    public void cleanupSprites() {

        // remove from actors list
        sprites.removeAll(spritesToBeRemoved);

        // reset the clean up sprites
        spritesToBeRemoved.clear();
    }

    public void addInvader(Invader a) {
        invaders.add(a);
    }

    public void removeInvader(Invader a) {
        invaders.remove(a);
    }

    public static List<Invader> getInvaders() {
        return invaders;
    }


}
