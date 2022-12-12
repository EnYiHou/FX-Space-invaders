package edu.vanier.ufo.ui;

import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.engine.*;
import edu.vanier.ufo.game.*;
import javafx.scene.Scene;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
    private static final Random randomizer = new Random();

    private static final Timer timer = new Timer();

    /*
    Number of generated Invaders
    Minimum spawn distance from the spaceShip
     */
    private static int SAFETY_REGION_RADIUS = 2000;
    private int level;

    private Stage primaryStage;


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

    public GameWorld(int fps, String title, int level) {
        super(fps, title);
        this.level = level;
        this.numberOfInvaders = 12 + 3 * this.level;

    }

    /**
     * Initialize the game world by adding sprite objects.
     *
     * @param primaryStage The game window or primary stage.
     */
    @Override
    public void initialize(final Stage primaryStage) {

        this.primaryStage = primaryStage;

        // Sets the window title
        primaryStage.setTitle(getWindowTitle());

        //primary stage limit size
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);

        // Create the scene
        setSceneNodes(new Pane());
        setGameSurface(new Scene(getSceneNodes(), 1800, 1800));
//        getSceneNodes().setCache(true);
//        getSceneNodes().setPrefWidth(2000);
//        getSceneNodes().setCacheHint(CacheHint.SPEED);
//       

        // Load css stylesheet
        // Generate invaders of 
        // Generate Camera
        Camera cam = createCamera();
        getGameSurface().setCamera(cam);
        cam.layoutXProperty().bind(spaceShip.getNode().layoutXProperty().subtract(primaryStage.widthProperty().divide(2)).add(spaceShip.getNode().getBoundsInLocal().getWidth() / 2));
        cam.layoutYProperty().bind(spaceShip.getNode().layoutYProperty().subtract(primaryStage.heightProperty().divide(2)).add(spaceShip.getNode().getBoundsInLocal().getHeight() / 2));

        // Create HUD
        createHUD(cam);

        // Change the background of the main scene.
        getGameSurface().setFill(Color.BLACK);
        primaryStage.setScene(getGameSurface());

        // Generate Map
        map = new Map(getSceneNodes());
        getSceneNodes().getChildren().add(0, map);

        // Setup Game input
        setupInput(primaryStage);

        getSpriteManager().cleanupSprites();
        getSpriteManager().addSprites(spaceShip);
        getSceneNodes().getChildren().add(spaceShip.getNode());

        loadSoundSource();

        //set Full screen
        primaryStage.setFullScreen(true);

        this.gameMusic = SoundManager.playGameMusic("level" + String.valueOf(this.level));

    }

    private void loadSoundSource() {
        SoundManager.loadSoundEffects("explosion", getClass().getClassLoader().getResource(ResourcesManager.EXPLOSION));
        SoundManager.loadSoundEffects("laser", getClass().getClassLoader().getResource(ResourcesManager.LASER));
        SoundManager.loadSoundEffects("win", getClass().getClassLoader().getResource(ResourcesManager.WIN));
        SoundManager.loadSoundEffects("rocket", getClass().getClassLoader().getResource(ResourcesManager.ROCKET));

        SoundManager.loadGameMusic("level1", getClass().getClassLoader().getResource(ResourcesManager.LEVEL1));
        SoundManager.loadGameMusic("level2", getClass().getClassLoader().getResource(ResourcesManager.LEVEL2));
        SoundManager.loadGameMusic("level3", getClass().getClassLoader().getResource(ResourcesManager.LEVEL3));
        SoundManager.loadGameMusic("level4", getClass().getClassLoader().getResource(ResourcesManager.LEVEL4));

    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera();
        camera.setFarClip(100000);
        camera.setNearClip(0.0001);

        return camera;
    }

    private void createHUD(Camera cam) {

        //HUD.getStylesheets().add(getClass().getResource("/fontstyle.css").toExternalForm());
        Label gameScoreLabel = new Label();
        Label levelLabel = new Label("Level : ");
        VBox objective = new VBox();
        StackPane health = new StackPane();

        gameScoreLabel.textProperty()
                .bind(gameScore.asString().concat(" Points"));
        fontLabel(gameScoreLabel);

        Label currentProgress = new Label();
        currentProgress.textProperty().bind(gameProgress.asString().concat(" invaders eliminated"));
        Label objectiveLabel = new Label(this.numberOfInvaders + " invaders to kill");

        fontLabel(currentProgress);

        fontLabel(objectiveLabel);

        objective.getChildren().addAll(currentProgress, objectiveLabel);
        fontLabel(levelLabel);

        levelLabel.setText(levelLabel.getText() + level);

        ImageView heartView = new ImageView();
        heartView.setOpacity(0.6);
        Image heartImage = new Image(ResourcesManager.HEART);
        heartView.setImage(heartImage);
        heartView.setFitHeight(50);
        heartView.setPreserveRatio(true);
        Label heartCount = new Label();
        heartCount.setFont(new Font(20));
        heartCount.setTextFill(Color.WHITE);
        heartCount.textProperty().bind(spaceShip.getHealth().asString());

        health.getChildren().addAll(heartView, heartCount);

        HUD.prefWidthProperty().bind(primaryStage.widthProperty());
        HUD.setPrefHeight(50);
        HUD.setAlignment(Pos.CENTER);
        HUD.getChildren()
                .addAll(gameScoreLabel, objective, levelLabel, health);
        HUD.setSpacing(
                30);
        HUD.layoutXProperty()
                .bind(cam.layoutXProperty());
        HUD.layoutYProperty()
                .bind(cam.layoutYProperty());

        getSceneNodes().getChildren().add(HUD);
        HUD.spacingProperty().bind(primaryStage.widthProperty().divide(5));
        HUD.layoutXProperty().bind(cam.layoutXProperty());
        HUD.layoutYProperty().bind(cam.layoutYProperty());

        HUD.prefWidthProperty().bind(primaryStage.widthProperty());
        levelLabel.setRotate(0.000000001);
    }

    private void fontLabel(Label label) {
        label.setFont(
                Font.font("Montserrat", FontWeight.BOLD, 15));

        label.setTextFill(Color.WHITE);
    }

    /**
     * Sets up the mouse input.
     *
     * @param primaryStage The primary stage (app window).
     */
    private void setupInput(Stage primaryStage) {
        GameWorld world = this;

        spaceShip.getNode().rotateProperty().bind(Bindings.createDoubleBinding(() -> {

            return Math.atan2(
                    mouseY.getValue() - (spaceShip.getNode().getLayoutY() + spaceShip.getNode().getBoundsInLocal().getHeight() / 2),
                    mouseX.getValue() - spaceShip.getNode().getLayoutX() - spaceShip.getNode().getBoundsInLocal().getWidth() / 2)
                    * 180 / Math.PI;
        }, mouseX, mouseY, spaceShip.getNode().layoutXProperty(), spaceShip.getNode().layoutYProperty()));

        primaryStage.getScene().setOnMouseMoved((event) -> {

            if (!world.isFinished()) {
                mouseX.set(event.getX());
                mouseY.set(event.getY());
            }

        });

        primaryStage.getScene().setOnMouseDragged((event) -> {
            if (!world.isFinished()) {
                mouseX.set(event.getX());
                mouseY.set(event.getY());
            }
        });

        primaryStage.getScene().setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {

                if (!world.isFinished()) {
                    if (spaceShip.isCanFire()) {

                        for (int i = 0; i < level; i++) {
                            Missile missile = spaceShip.fire(i + 1, level);
                            getSpriteManager().addSprites(missile);
                            getSceneNodes().getChildren().add(missile.getNode());
                            missile.getNode().setLayoutX(spaceShip.getCenterX() - missile.getNode().getBoundsInLocal().getWidth() / 2);
                            missile.getNode().setLayoutY(spaceShip.getCenterY() - missile.getNode().getBoundsInLocal().getHeight() / 2);

                            spaceShip.setCanFire(false);
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    spaceShip.setCanFire(true);
                                }

                            }, (long) (500 / spaceShip.getFireSpeed()));
                        }

                    }
                    spaceShip.getNode().toFront();

                }
            }
        });

        primaryStage.getScene().setOnKeyPressed((var e) -> {

            if (!world.isFinished()) {
                if (e.getCode() == KeyCode.SPACE) {
                    if (spaceShip.isShieldAvailable()) {

                        spaceShip.shieldToggle();
                        spaceShip.setShieldAvailable(false);

                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                spaceShip.setShieldAvailable(true);
                            }
                        }, (long) (spaceShip.getShieldCoolDown()));
                        return;

                    }
                }

                if (e.getCode() == KeyCode.DIGIT1) {
                    spaceShip.changeWeapon(e.getCode());
                }
                if (e.getCode() == KeyCode.DIGIT2) {
                    spaceShip.changeWeapon(e.getCode());
                }
                if (e.getCode() == KeyCode.DIGIT3) {
                    spaceShip.changeWeapon(e.getCode());
                }
                if (e.getCode() == KeyCode.DIGIT4) {
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

    public void spawnInvaders() {

        Invader invader;

        if (level == 4) {
            invader = new Invader(spaceShip, ResourcesManager.BOSS, level);
            invader.setPoint(500);
            invader.setHealth(2000);

        } else if (level == 3) {
            invader = new Invader(spaceShip, ResourcesManager.ENEMY3, level);
            invader.setPoint(100);
            invader.setHealth(1000);

        } else if (level == 2) {
            invader = new Invader(spaceShip, ResourcesManager.ENEMY2, level);
            invader.setPoint(50);
            invader.setHealth(500);

        } else {
            invader = new Invader(spaceShip, ResourcesManager.ENEMY1, level);
            invader.setPoint(20);
            invader.setHealth(200);

        }

        invader.setSpeed(5+level);

        getSpriteManager().addInvader(invader);

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

    /**
     * Each sprite will update it's velocity and bounce off wall borders.
     *
     * @param sprite - An atomic particle (a sphere).
     */
    @Override
    protected void handleUpdate(Sprite sprite) {
        // advance object

        handleOutOfMap(sprite);

        if (!this.isFinished()) {
            sprite.update();
        }
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
