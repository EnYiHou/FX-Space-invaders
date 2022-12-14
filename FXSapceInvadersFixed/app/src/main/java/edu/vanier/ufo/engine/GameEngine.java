package edu.vanier.ufo.engine;

import edu.vanier.ufo.helpers.HomePageController;
import edu.vanier.ufo.helpers.ResourcesManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * This application demonstrates a JavaFX Game Loop. Shown below are the methods
 * which comprise of the fundamentals to a simple game loop in JavaFX:
 * <pre>
 *  <b>initialize()</b> - Initialize the game world.
 *  <b>beginGameLoop()</b> - Creates a JavaFX AnimationTimer object.
 *  <b>updateSprites()</b> - Updates the sprite objects at each frame
 *  <b>handleCollision()</b> - Method will determine objects that collide with each other.
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
     * Root node of the gameSurface.
     */
    private Pane sceneNode;

    /**
     * The AnimationTimer used to create the gameLoop.
     */
    private static AnimationTimer gameLoop;

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

    private boolean finished;
    /*
     * User's score
     */
    protected IntegerProperty gameScore = new SimpleIntegerProperty(0);
    /*
     * How many eliminated Invaders
     */
    protected IntegerProperty gameProgress = new SimpleIntegerProperty(0);

    /*
     * Number of invaders
     */
    protected int numberOfInvaders;

    protected MediaPlayer gameMusic;

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
        finished = false;
        SoundManager.setSoundPoolThread(500);
        // create and set timeline for the game loop
        buildAndSetGameLoop();
    }

    /**
     * Builds and sets the game loop ready to be started.
     */
    protected final void buildAndSetGameLoop() {

        GameEngine engine = this;

        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {

                if (now - lastUpdate >= 1/(float)(framesPerSecond) * 1_000_000_000) {
                    lastUpdate = now;
                    if (!engine.isFinished()) {
                        // update actors
                        updateSprites();
                        // check for collision.
                        handleCollision();
                        // removed dead sprites.
                        cleanupSprites();
                    }
                }
            }
        };

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
        getGameLoop().start();
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

    protected void handleCollision() {
      
    }

    
     /**
     * Handle victory.
     */
    public void victory() {

        {
            finished = true;
            SoundManager.playSound("win");
            finished(true);
        }

    }

    /**
     * Handle defeat.
     */
    public void defeat() {

        finished = true;
        finished(false);

    }

    /**
     * Handle the end of the game based on the result of the game.
     *
     * @param isVictory the result of the game. True if victory and false if
     * defeat.
     */
    private void finished(boolean isVictory) {

        this.gameMusic.stop();
        // The alert in which it will tell the user if he lost or he won.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // If the user won
        if (isVictory) {
            ImageView victoryImageView = new ImageView(ResourcesManager.VICTORY);
            victoryImageView.setFitWidth(100);
            victoryImageView.setPreserveRatio(true);
            alert.setGraphic(victoryImageView);
            alert.setHeaderText("Congratulation!");
            alert.setTitle("Victory!");
            alert.setContentText("You won the game, try another level!");

        // If the user lost
        } else {
            ImageView defeatImageView = new ImageView(ResourcesManager.GAME_OVER);
            defeatImageView.setFitWidth(100);
            defeatImageView.setPreserveRatio(true);
            alert.setGraphic(defeatImageView);
            alert.setHeaderText("Unfortunately You Lost.");
            alert.setTitle("Defeat...");
            alert.setContentText("Try again with another level!");
        }
        Stage primaryStage = (Stage) gameSurface.getWindow();
        primaryStage.setFullScreen(false);
        
        //Handle if the user closes the alert. Go back to home page to let the user
        //choose another level
        alert.setOnCloseRequest((e) -> {
            try {
                this.shutdown();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(("/fxml/homepage.fxml")));
                loader.setController(new HomePageController());
                Pane pane = loader.load();

                primaryStage.setScene(new Scene(pane));
            } catch (IOException ex) {
            }
        });
        alert.show();

    }
    /**
     * When two objects collide this method can handle the passed in sprite
     * objects. By default it returns false, meaning the objects do not collide.
     *
     * @param spriteA - called from checkCollision() method to be compared.
     * @param spriteB - called from checkCollision() method to be compared.
     * @return boolean True if the objects collided, otherwise false.
     */
    protected boolean checkCollision(Sprite spriteA, Sprite spriteB) {
        return true;
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
     * 
     * @return the scene nodes of this game engine containing all the added nodes.
     */
    public Pane getSceneNodes() {
        return this.sceneNode;
    }

    /**
     * set the scene node.
     * @param sceneNode the scene node to set
     */
    public void setSceneNodes(Pane sceneNode) {
        this.sceneNode = sceneNode;
    }

    /**
     * The game loop (AnimationTimer) which is used to update, check collisions,
     * and cleanup sprite objects at every interval (fps).
     *
     * @return AnimationTimer, an animation running indefinitely representing
     * the game loop.
     */
    protected static AnimationTimer getGameLoop() {
        return gameLoop;
    }

    /**
     * The sets the current game loop for this game world.
     *
     * @param gameLoop AnimationTimer object of an animation running
     * indefinitely representing the game loop.
     */
    protected static void setGameLoop(AnimationTimer gameLoop) {
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
     * Get if the game ended
     *
     * @return if the game ended
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Set the game ended.
     *
     * @param finished if the game ended
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
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
     * Returns an integer property of the game score. The score value is stored
     * inside the property.
     *
     * @return the integer property of the game score.
     */
    public IntegerProperty getGameScore() {
        return gameScore;
    }

    /**
     * Set the value of the integer property game score
     * @param gameScore the desired value for game score.
     */
    public void setGameScore(int gameScore) {
        this.gameScore.set(gameScore);
    }

   

    /**
     * Stop threads and stop media from playing. Clear all sprites to allow the
     * user play a new level with new sprites
     */
    public void shutdown() {
        finished = true;
        // Stop the game's animation
        getSpriteManager().clear();
        getGameLoop().stop();
        SoundManager.shutdown();
    }

}
