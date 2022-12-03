package edu.vanier.ufo.engine;

import edu.vanier.ufo.game.Atom;
import edu.vanier.ufo.game.Invader;
import edu.vanier.ufo.game.Missile;
import edu.vanier.ufo.game.Ship;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This application demonstrates a JavaFX Game Loop. Shown below are the methods
 * which comprise of the fundamentals to a simple game loop in JavaFX:
 * <pre>
 *  <b>initialize()</b> - Initialize the game world.
 *  <b>beginGameLoop()</b> - Creates a JavaFX Timeline object containing the game life cycle.
 *  <b>updateSprites()</b> - Updates the sprite objects each period (per frame)
 *  <b>checkCollisions()</b> - Method will determine objects that collide with each other.
 *  <b>cleanupSprites()</b> - Any sprite objects needing to be removed from play.
 * </pre>
 *
 * @author cdea
 */
public abstract class GameEngine {

    /**
     * The JavaFX Scene as the game surface
     */
    private Scene gameSurface;
    /**
     * All nodes to be displayed in the game window.
     */
    private Group sceneNodes;
    /**
     * The game loop using JavaFX's <code>Timeline</code> API.
     */
    private static Timeline gameLoop;

    /**
     * Number of frames per second.
     */
    private final int framesPerSecond;

    /**
     * Title in the application window.
     */
    private final String windowTitle;

    /**
     * The sprite manager.
     */
    private final SpriteManager spriteManager;

    private final SoundManager soundManager;

    /*
    * User's score
     */
    private IntegerProperty gameScore = new SimpleIntegerProperty(0);

    /**
     * Constructor that is called by the derived class. This will set the frames
     * per second, title, and setup the game loop.
     *
     * @param fps - Frames per second.
     * @param title - Title of the application window.
     */
    public GameEngine(final int fps, final String title) {
        framesPerSecond = fps;
        windowTitle = title;
        spriteManager = new SpriteManager();
        soundManager = new SoundManager(10);
        // create and set timeline for the game loop
        buildAndSetGameLoop();
    }

    /**
     * Builds and sets the game loop ready to be started.
     */
    protected final void buildAndSetGameLoop() {

        final Duration frameDuration = Duration.millis(1000 / (float) getFramesPerSecond());
        EventHandler<ActionEvent> onFinished = (event) -> {
            // update actors
            updateSprites();
            // check for collision.
            checkCollisions();
            // removed dead sprites.
            cleanupSprites();

            detectVictory();

        };
        final KeyFrame gameFrame = new KeyFrame(frameDuration, onFinished);
        // sets the game world's game loop (Timeline)
        Timeline gameLoop = new Timeline(gameFrame);
        gameLoop.setCycleCount(Animation.INDEFINITE);
        setGameLoop(gameLoop);
    }

    /**
     * Initialize the game world by update the JavaFX Stage.
     *
     * @param primaryStage The main window containing the JavaFX Scene.
     */
    public abstract void initialize(final Stage primaryStage);

    /**
     * Kicks off (plays) the Timeline objects containing one key frame that
     * simply runs indefinitely with each frame invoking a method to update
     * sprite objects, check for collisions, and cleanup sprite objects.
     */
    public void beginGameLoop() {
        getGameLoop().play();
    }

    /**
     * Updates each game sprite in the game world. This method will loop through
     * each sprite and passing it to the handleUpdate() method. The derived
     * class should override handleUpdate() method.
     */
    protected void updateSprites() {
        for (Sprite sprite : spriteManager.getAllSprites()) {
            handleUpdate(sprite);
        }
    }

    /**
     * Updates the sprite object's information to position on the game surface.
     *
     * @param sprite - The sprite to update.
     */
    protected void handleUpdate(Sprite sprite) {
    }

    protected void checkCollisions() {
        //FIXME: handle collision with the spaceship.
        // check other sprite's collisions
        spriteManager.resetCollisionsToCheck();
        // check each sprite against other sprite objects.
        for (Sprite spriteA : spriteManager.getCollisionsToCheck()) {
            for (Sprite spriteB : spriteManager.getAllSprites()) {
                if (handleCollision(spriteA, spriteB)) {

                    Shape intersect = Shape.intersect(spriteA.getCollidingNode(), spriteB.getCollidingNode());
                    if (spriteA instanceof Missile) {
                        if ((spriteB instanceof Invader)) {

                            Missile missile = ((Missile) spriteA);
                            Invader invader = ((Invader) spriteB);
                            missile.implode(this, intersect.getBoundsInParent().getCenterX(), intersect.getBoundsInParent().getCenterY());
                            invader.setHealth(invader.getHealth().get() - missile.getDamage());

                            if (invader.getHealth().get() < 0) {
                                getSpriteManager().removeInvader(invader);
                                invader.implode(this, invader.getCenterX(), invader.getCenterY());
                                getSpriteManager().addSpritesToBeRemoved(invader);
                                gameScore.set(gameScore.get() + invader.getPoint());

                            }
                            getSpriteManager().addSpritesToBeRemoved(missile);
                        }
                    }

                    if (spriteA instanceof Ship) {
                        if ((spriteB instanceof Invader)) {
                            Ship ship = ((Ship) spriteA);
                            ship.damaged();
                            ((Invader) spriteB).implode(this, intersect.getBoundsInParent().getCenterX(), intersect.getBoundsInParent().getCenterY());
                            getSpriteManager().addSpritesToBeRemoved(spriteB);
                            getSpriteManager().removeInvader((Invader) spriteB);

                            if (ship.getHealth().get() == 0) {
                                ship.isDead = true;
                                defeat();
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * When two objects collide this method can handle the passed in sprite
     * objects. By default it returns false, meaning the objects do not collide.
     *
     * @param spriteA - called from checkCollision() method to be compared.
     * @param spriteB - called from checkCollision() method to be compared.
     * @return boolean True if the objects collided, otherwise false.
     */
    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
        Shape shape = Shape.intersect(spriteA.collidingNode, spriteB.collidingNode);
        return shape.getBoundsInLocal().getWidth() > -1;
    }

    /**
     * Sprites to be cleaned up.
     */
    protected void cleanupSprites() {
        spriteManager.cleanupSprites();
    }

    /**
     * Returns the frames per second.
     *
     * @return int The frames per second.
     */
    protected int getFramesPerSecond() {
        return framesPerSecond;
    }

    /**
     * Returns the game's window title.
     *
     * @return String The game's window title.
     */
    public String getWindowTitle() {
        return windowTitle;
    }

    /**
     * The game loop (Timeline) which is used to update, check collisions, and
     * cleanup sprite objects at every interval (fps).
     *
     * @return Timeline An animation running indefinitely representing the game
     * loop.
     */
    protected static Timeline getGameLoop() {
        return gameLoop;
    }

    /**
     * The sets the current game loop for this game world.
     *
     * @param gameLoop Timeline object of an animation running indefinitely
     * representing the game loop.
     */
    protected static void setGameLoop(Timeline gameLoop) {
        GameEngine.gameLoop = gameLoop;
    }

    /**
     * Returns the sprite manager containing the sprite objects to manipulate in
     * the game.
     *
     * @return SpriteManager The sprite manager.
     */
    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    /**
     * Returns the JavaFX Scene. This is called the game surface to allow the
     * developer to add JavaFX Node objects onto the Scene.
     *
     * @return Scene The JavaFX scene graph.
     */
    public Scene getGameSurface() {
        return gameSurface;
    }

    /**
     * Sets the JavaFX Scene. This is called the game surface to allow the
     * developer to add JavaFX Node objects onto the Scene.
     *
     * @param gameSurface The main game surface (JavaFX Scene).
     */
    protected void setGameSurface(Scene gameSurface) {
        this.gameSurface = gameSurface;
    }

    /**
     * All JavaFX nodes which are rendered onto the game surface(Scene) is a
     * JavaFX Group object.
     *
     * @return Group The root containing many child nodes to be displayed into
     * the Scene area.
     */
    public Group getSceneNodes() {
        return sceneNodes;
    }

    /**
     * Sets the JavaFX Group that will hold all JavaFX nodes which are rendered
     * onto the game surface(Scene) is a JavaFX Group object.
     *
     * @param sceneNodes The root container having many children nodes to be
     * displayed into the Scene area.
     */
    protected void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }

    protected SoundManager getSoundManager() {
        return soundManager;
    }

    public IntegerProperty getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore.set(gameScore);
    }

    private void detectVictory() {
        if (SpriteManager.getInvaders().isEmpty()) {
            victory();
        }

    }

    private void victory() {
        System.out.println("Victory!");
        this.shutdown();
    }

    private void defeat() {
        System.out.println("Defeated!");
        this.shutdown();

    }

    /**
     * Stop threads and stop media from playing.
     */
    public void shutdown() {
        // Stop the game's animation.
        getGameLoop().stop();
        getSoundManager().shutdown();
    }
}
