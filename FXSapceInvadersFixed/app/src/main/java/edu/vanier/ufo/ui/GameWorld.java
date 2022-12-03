package edu.vanier.ufo.ui;

import com.sun.javafx.scene.traversal.Hueristic2D;
import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.engine.*;
import edu.vanier.ufo.game.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Random;
import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_MODENA;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 * This is a simple game world simulating a bunch of spheres looking like atomic
 * particles colliding with each other. When the game loop begins the user will
 * notice random spheres (atomic particles) floating and colliding. The user
 * will navigate his/her ship by right clicking the mouse to thrust forward and
 * left click to fire weapon to atoms.
 *
 * @author cdea
 */
public class GameWorld extends GameEngine {

    // Randomizer for later use
    private static Random randomizer = new Random();

    /*
    Number of generated Invaders
    Minimum spawn distance from the spaceShip
     */
    private static int SAFETY_REGION_RADIUS = 2000;
    private int level;
    private int numberOfInvaders;


    /* 
    Returns the boolean of approximative equal. 
    The interval of accepted error is TOLERANCE.
     */
    private static final double TOLERANCE = 0.1;

    public static boolean approxEqual(final double d1, final double d2) {
        return Math.abs(d1 - d2) < TOLERANCE;
    }

    /* 
    Stores the relative value of mouseX and mouseY to the current Screen. 
    It is updated at each SpaceShip Movement and Mouse Movement.
     */
    private final DoubleProperty mouseX = new SimpleDoubleProperty();
    private final DoubleProperty mouseY = new SimpleDoubleProperty();

    private Ship spaceShip = new Ship();

    private Map map;
    private HBox HUD = new HBox();
    Label gameScoreLabel = new Label();
    Label levelLabel = new Label("Level : ");

    public GameWorld(int fps, String title, int level) {
        super(fps, title);
        this.level = level;
        this.numberOfInvaders = 10 * this.level;
        gameScoreLabel.textProperty().bind(this.getGameScore().asString().concat(" Points"));
        this.levelLabel.setText(this.levelLabel.getText() + this.level);
        HUD.getChildren().addAll(gameScoreLabel, levelLabel);

    }

    /**
     * Initialize the game world by adding sprite objects.
     *
     * @param primaryStage The game window or primary stage.
     */
    @Override
    public void initialize(final Stage primaryStage) {

        // Sets the window title
        primaryStage.setTitle(getWindowTitle());

        //primary stage limit size
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);
        //set Full screen
        primaryStage.setFullScreen(true);

        // Create the scene
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), 1400, 900));

        // Generate invaders of 
        generateInvaders();

        // Generate Camera
        Camera cam = createCamera();
        getGameSurface().setCamera(cam);
        cam.layoutXProperty().bind(spaceShip.getNode().layoutXProperty().subtract(primaryStage.widthProperty().divide(2)).add(spaceShip.getNode().getBoundsInLocal().getWidth() / 2));
        cam.layoutYProperty().bind(spaceShip.getNode().layoutYProperty().subtract(primaryStage.heightProperty().divide(2)).add(spaceShip.getNode().getBoundsInLocal().getHeight() / 2));

        // Create HUD
        gameScoreLabel.setFont(new Font(Application.STYLESHEET_CASPIAN, 20));
        gameScoreLabel.setTextFill(Color.WHEAT);
        levelLabel.setFont(new Font(Application.STYLESHEET_CASPIAN, 20));
        levelLabel.setTextFill(Color.WHEAT);

        HUD.setSpacing(30);
        getSceneNodes().getChildren().add(HUD);
        HUD.layoutXProperty().bind(cam.layoutXProperty());
        HUD.layoutYProperty().bind(cam.layoutYProperty());

        // Change the background of the main scene.
        getGameSurface().setFill(Color.BLACK);
        primaryStage.setScene(getGameSurface());

        // Generate Map
        map = new Map(getSceneNodes());
        getSceneNodes().getChildren().add(0, map);

        // Setup Game input
        setupInput(primaryStage);

        getSpriteManager().addSprites(spaceShip);
        getSceneNodes().getChildren().add(spaceShip.getNode());

        // load sound files
        getSoundManager().loadSoundEffects("laser", getClass().getClassLoader().getResource(ResourcesManager.SOUND_LASER));

    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera();
        camera.setFarClip(100000);
        camera.setNearClip(0.0001);

        return camera;
    }

    /**
     * Sets up the mouse input.
     *
     * @param primaryStage The primary stage (app window).
     */
    private void setupInput(Stage primaryStage) {

        spaceShip.getNode().rotateProperty().bind(Bindings.createDoubleBinding(() -> {

            return Math.atan2(
                    mouseY.getValue() - (spaceShip.getNode().getLayoutY() + spaceShip.getNode().getBoundsInLocal().getHeight() / 2),
                    mouseX.getValue() - spaceShip.getNode().getLayoutX() - spaceShip.getNode().getBoundsInLocal().getWidth() / 2)
                    * 180 / Math.PI;
        }, mouseX, mouseY, spaceShip.getNode().layoutXProperty(), spaceShip.getNode().layoutYProperty()));

        primaryStage.getScene().setOnMouseMoved((event) -> {
            mouseX.set(event.getX());
            mouseY.set(event.getY());

        });

        primaryStage.getScene().setOnMouseDragged((event) -> {
            mouseX.set(event.getX());
            mouseY.set(event.getY());

        });

        primaryStage.getScene().setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {

                Missile missile = spaceShip.fire();
                getSpriteManager().addSprites(missile);
                getSoundManager().playSound("laser");
                getSceneNodes().getChildren().add(missile.getNode());
                missile.getNode().setLayoutX(spaceShip.getCenterX() - missile.getNode().getBoundsInLocal().getWidth() / 2);
                missile.getNode().setLayoutY(spaceShip.getCenterY() - missile.getNode().getBoundsInLocal().getHeight() / 2);

                spaceShip.getNode().toFront();

            }
        });

        primaryStage.getScene().setOnKeyPressed((var e) -> {
            if (e.getCode() == KeyCode.SPACE) {
                spaceShip.shieldToggle();
                return;
            }

            if (e.getCode() == KeyCode.DIGIT1) {
                spaceShip.changeWeapon(e.getCode());
            }
            if (e.getCode() == KeyCode.DIGIT2) {
                spaceShip.changeWeapon(e.getCode());
            }

            if (e.getCode() == KeyCode.W) {
                spaceShip.setwPressed(true);

            }
            if (e.getCode() == KeyCode.A) {
                spaceShip.setaPressed(true);
            }
            if (e.getCode() == KeyCode.S) {
                spaceShip.setsPressed(true);
            }
            if (e.getCode() == KeyCode.D) {
                spaceShip.setdPressed(true);
            }

        });

        primaryStage.getScene().setOnKeyReleased((var e) -> {

            if (e.getCode() == KeyCode.W) {
                spaceShip.setwPressed(false);
            }
            if (e.getCode() == KeyCode.A) {
                spaceShip.setaPressed(false);
            }
            if (e.getCode() == KeyCode.S) {
                spaceShip.setsPressed(false);
            }
            if (e.getCode() == KeyCode.D) {
                spaceShip.setdPressed(false);
            }

        });

    }

    private void generateInvaders() {
        for (int i = 0; i < numberOfInvaders; i++) {
            Invader invader = new Invader(spaceShip);

            int rangeOfSpawn = (Map.getMAP_RADIUS());

            int randomXPos;
            int randomYPos;
            double distanceX;
            double distanceY;
            do {
                randomXPos = randomizer.nextInt(-rangeOfSpawn, rangeOfSpawn);
                randomYPos = randomizer.nextInt(-rangeOfSpawn, rangeOfSpawn);

                distanceX = randomXPos - spaceShip.getCenterX();
                distanceY = randomYPos - spaceShip.getCenterY();

            } while (randomXPos * randomXPos + randomYPos * randomYPos > Map.getMAP_RADIUS() * Map.getMAP_RADIUS()
                    || distanceX * distanceX + distanceY * distanceY < SAFETY_REGION_RADIUS * SAFETY_REGION_RADIUS);

            invader.getNode().setLayoutX(randomXPos);
            invader.getNode().setLayoutY(randomYPos);
            getSceneNodes().getChildren().add(invader.getNode());
            getSpriteManager().addSprites(invader);

        }

    }

    /**
     * Each sprite will update it's velocity and bounce off wall borders.
     *
     * @param sprite - An atomic particle (a sphere).
     */
    @Override
    protected void handleUpdate(Sprite sprite) {
        // advance object

        handleOutOfMap(sprite);

        sprite.update();
        if (sprite instanceof Missile) {
            removeMissiles((Missile) sprite);
        }
        if (sprite instanceof Ship) {
            handleCursor();
        }

    }

    private void handleOutOfMap(Sprite sprite) {

        Circle collidingNode = sprite.getOriginalCollidingNode();
        Bounds bound = collidingNode.getBoundsInLocal();

        if (!approxEqual(bound.getHeight(), Shape.intersect(collidingNode, map).getBoundsInLocal().getHeight())
                || !approxEqual(bound.getWidth(), Shape.intersect(collidingNode, map).getBoundsInLocal().getWidth())) {

            if (sprite instanceof Missile) {
                Missile missile = (Missile) (sprite);
                missile.implode(this, missile.getCenterX(), missile.getCenterY());
                missile.handleDeath(this);
            }

            if (sprite instanceof Ship) {
                double angle = Math.atan2(sprite.getNode().getLayoutY(), sprite.getNode().getLayoutX());
                sprite.setVelocity(-Math.cos(angle), Math.sin(angle));
            }
        }
    }

    private void handleCursor() {
        mouseX.set(mouseX.get() + spaceShip.getVelocityX());
        mouseY.set(mouseY.get() - spaceShip.getVelocityY());
    }

    private void removeMissiles(Missile missile) {
        if (missile.getDurationCounter() > Atom.getLIFE_EXPECTENCY()) {
            getSpriteManager().addSpritesToBeRemoved(missile);
            missile.implode(this, missile.getCenterX(), missile.getCenterY());
        }
    }

    /**
     * How to handle the collision of two sprite objects. Stops the particle by
     * zeroing out the velocity if a collision occurred. /** How to handle the
     * collision of two sprite objects. Stops the particle by
     *
     *
     * @param spriteA Sprite from the first list.
     * @param spriteB Sprite from the second list.
     * @return boolean returns a true if the two sprites have collided otherwise
     * false.
     */
    @Override
    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
        //TODO: implement collision detection here.
        Shape shape = Shape.intersect(spriteA.getCollidingNode(), spriteB.getCollidingNode());
        return shape.getBoundsInLocal().getWidth() > -1;
    }
}