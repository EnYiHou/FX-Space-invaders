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
 * particles colliding with each other. The user will navigate his/her ship with
 * WASD keys to thrust and use the primary mouse click to fire. The user
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
    private static final int SAFETY_REGION_RADIUS = 2000;
    private final int level;

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

    private final Ship spaceShip = new Ship();
    private Map map;

    /**
     * The HUD Hbox that displays the game statistic at the top of the screen.
     */
    private final HBox HUD = new HBox();

    /**
     *
     * @param fps the fps at which the game refreshes
     * @param title
     * @param level
     */
    public GameWorld(int fps, String title, int level) {
        super(fps, title);
        this.level = level;
        this.numberOfInvaders = 12 + 3 * this.level;

        setDefaultRocket();

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

    /**
     * Create a perspective camera that follows the position of the spaceship
     *
     * @return the created camera
     */
    private Camera createCamera() {
        Camera camera = new PerspectiveCamera();
        camera.setFarClip(10000);
        camera.setNearClip(0.0001);

        camera.translateXProperty().bind(
                spaceShip.getNode().translateXProperty().subtract(
                        primaryStage.widthProperty().divide(2))
                        .add(spaceShip.getNode().getBoundsInLocal().getWidth() / 2));
        camera.translateYProperty().bind(
                spaceShip.getNode().translateYProperty().subtract(
                        primaryStage.heightProperty().divide(2))
                        .add(spaceShip.getNode().getBoundsInLocal().getHeight() / 2));

        return camera;
    }

    /**
     * Set the default rocket of the spaceship depending on the current level
     */
    private void setDefaultRocket() {
        if (this.level == 1) {
            this.spaceShip.setKeyCode(KeyCode.NUMPAD1);
        }
        if (this.level == 2) {
            this.spaceShip.setKeyCode(KeyCode.NUMPAD2);
        }
        if (this.level == 3) {
            this.spaceShip.setKeyCode(KeyCode.NUMPAD3);
        }
        if (this.level == 4) {
            this.spaceShip.setKeyCode(KeyCode.NUMPAD4);
        }

    }

    /**
     * Creates a HUD at the top of the screen by binding its position to the
     * relative position of the camera.
     *
     * @param cam the camera with which the HUD binds its position.
     */
    private void createHUD(Camera cam) {

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
        HUD.translateXProperty()
                .bind(cam.translateXProperty());
        HUD.translateYProperty()
                .bind(cam.translateYProperty());

        getSceneNodes().getChildren().add(HUD);
        HUD.spacingProperty().bind(primaryStage.widthProperty().divide(5));
        HUD.translateXProperty().bind(cam.translateXProperty());
        HUD.translateYProperty().bind(cam.translateYProperty());

        HUD.prefWidthProperty().bind(primaryStage.widthProperty());
        levelLabel.setRotate(0.000000001);
    }

    /**
     * This method sets fonts for specific labels
     *
     * @param label the label to set the font
     */
    private void fontLabel(Label label) {
        label.setFont(
                Font.font("Montserrat", FontWeight.BOLD, 15));

        label.setTextFill(Color.WHITE);
    }

    /**
     * Sets up all inputs of the user. Set the spaceship rotation to follow the
     * mouse. Set the mouse inputs for shooting Set the keyboard inputs for
     * changing weapons, activating shield, and handle ship movement.
     *
     * @param primaryStage The primary stage (app window).
     */
    private void setupInput(Stage primaryStage) {
        GameWorld world = this;

        //Set the rotation of the ship to follow the mouse
        spaceShip.getNode().rotateProperty().bind(Bindings.createDoubleBinding(() -> {

            return Math.atan2(
                    mouseY.getValue() - (spaceShip.getNode().getTranslateY() + spaceShip.getNode().getBoundsInLocal().getHeight() / 2),
                    mouseX.getValue() - spaceShip.getNode().getTranslateX() - spaceShip.getNode().getBoundsInLocal().getWidth() / 2)
                    * 180 / Math.PI;
        }, mouseX, mouseY, spaceShip.getNode().translateXProperty(), spaceShip.getNode().translateYProperty()));

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

        //Set the mouse event to handle shooting
        primaryStage.getScene().setOnMouseClicked((e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {

                if (!world.isFinished()) {
                    if (spaceShip.isCanFire()) {

                        for (int i = 0; i < level; i++) {
                            Missile missile = spaceShip.fire(i + 1, level);
                            getSpriteManager().addSprites(missile);
                            getSceneNodes().getChildren().add(missile.getNode());
                            missile.getNode().setTranslateX(spaceShip.getCenterX() - missile.getNode().getBoundsInLocal().getWidth() / 2);
                            missile.getNode().setTranslateY(spaceShip.getCenterY() - missile.getNode().getBoundsInLocal().getHeight() / 2);

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

            //set the keyboard event to activate shield
            if (!world.isFinished()) {
                if (e.getCode() == KeyCode.SPACE) {
                    if (spaceShip.isShieldAvailable()) {

                        spaceShip.activateShield();
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

                //set the keyboard event to change weapons
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

                // Set the keyboard event to handle space movement
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

        // Handle when user release a key
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

    /**
     * This method generate an invader according to the level and set its
     * location to a random position that is outside the safe region, which is
     * the region where the ship spawns.
     */
    public void spawnInvaders() {

        Invader invader;

        switch (level) {
            case 4 -> {
                invader = new Invader(spaceShip, ResourcesManager.BOSS, level);
                invader.setPoint(500);
                invader.setHealth(2000);
            }
            case 3 -> {
                invader = new Invader(spaceShip, ResourcesManager.ENEMY3, level);
                invader.setPoint(100);
                invader.setHealth(1000);
            }
            case 2 -> {
                invader = new Invader(spaceShip, ResourcesManager.ENEMY2, level);
                invader.setPoint(50);
                invader.setHealth(500);
            }
            default -> {
                invader = new Invader(spaceShip, ResourcesManager.ENEMY1, level);
                invader.setPoint(20);
                invader.setHealth(200);
            }
        }

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

        invader.getNode().setTranslateX(randomXPos);
        invader.getNode().setTranslateY(randomYPos);
        getSceneNodes().getChildren().add(invader.getNode());
        getSpriteManager().addSprites(invader);

    }

    /**
     * Remove Missiles after their life expectancy is over. Handle cursor
     * according to the camera translation Handle the spaceship or missile if it
     * is getting out of the map.
     *
     * @param sprite - The handled sprite
     */
    @Override
    protected void handleUpdate(Sprite sprite) {

        
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

    /**
     * Handle a specific sprite to prevent it from going out of the map
     * 
     * if the sprite is a missile or an invader, it implodes.
     * if the sprite is the spaceship, it will set its velocity's direction 
     * toward the center of the map
     *
     * @param sprite the sprite that is getting handled
     */
    private void handleOutOfMap(Sprite sprite) {

        Circle collidingNode = sprite.getCollidingNode();
        Bounds bound = collidingNode.getBoundsInLocal();

        if (!approxEqual(bound.getHeight(), Shape.intersect(collidingNode, map).getBoundsInLocal().getHeight())
                || !approxEqual(bound.getWidth(), Shape.intersect(collidingNode, map).getBoundsInLocal().getWidth())) {

            if (sprite instanceof Missile) {
                Missile missile = (Missile) (sprite);
                missile.implode(this, missile.getCenterX(), missile.getCenterY());
                missile.handleDeath(this);
            }

            if (sprite instanceof Ship) {
                double angle = Math.atan2(sprite.getNode().getTranslateY(), sprite.getNode().getTranslateX());
                sprite.setVelocity(-Math.cos(angle), Math.sin(angle));
            }
            if (sprite instanceof Invader invader) {
                invader.implode(this, invader.getCenterX(), invader.getCenterY());
                this.getSpriteManager().addSpritesToBeRemoved(invader);
                this.getSpriteManager().removeInvader(invader);
            }
        }
    }

    /**
     * Update the mouse position according to the spaceship velocity
     */
    private void handleCursor() {
        mouseX.set(mouseX.get() + spaceShip.getVelocityX());
        mouseY.set(mouseY.get() - spaceShip.getVelocityY());
    }

    /**
     * Remove a specific missile if its life expectancy is over.
     *
     * @param missile the missile that is handled.
     */
    private void removeMissiles(Missile missile) {
        if (missile.getDurationCounter() > Atom.getLIFE_EXPECTENCY()) {
            getSpriteManager().addSpritesToBeRemoved(missile);
            missile.implode(this, missile.getCenterX(), missile.getCenterY());
        }
    }

    /**
     * Overridden method that checks the collision between two sprites
     *
     * @param spriteA the first sprite to be checked
     * @param spriteB the second sprite to be checked
     * @return if the two checked sprites collide with each other
     */
    @Override
    protected boolean checkCollision(Sprite spriteA, Sprite spriteB) {
        //TODO: implement collision detection here.
        Shape shape = Shape.intersect(spriteA.getCollidingNode(), spriteB.getCollidingNode());
        return shape.getBoundsInLocal().getWidth() > -1;
    }

    
    /**
     * Overridden method that handles the collision between two sprites. 
     * If an invader collides with a missile, it loses health, and it implodes 
     * if its health is lower than 0.
     * 
     * If an invader collides with the spaceship, the spaceship loses 1 heart
     * and the invader implodes.
     * 
     * If the number of invaders on the field added with the eliminated number 
     * of invaders is less than the total number of invaders of the level, spawn
     * a new invader. 
     */
    @Override
    protected void handleCollision() {

        getSpriteManager().resetCollisionsToCheck();
        for (Sprite spriteA : getSpriteManager().getCollisionsToCheck()) {
            for (Sprite spriteB : getSpriteManager().getAllSprites()) {
                if (checkCollision(spriteA, spriteB)) {

                    Shape intersect = Shape.intersect(spriteA.getCollidingNode(), spriteB.getCollidingNode());
                    if (spriteA instanceof Missile && (spriteB instanceof Invader)) {

                        Missile missile = ((Missile) spriteA);
                        Invader invader = ((Invader) spriteB);
                        missile.implode(this, intersect.getBoundsInParent().getCenterX(), intersect.getBoundsInParent().getCenterY());

                        if (!invader.isIsDead()) {
                            invader.setHealth(invader.getHealth().get() - missile.getDamage());

                            if (invader.getHealth().get() < 0) {
                                getSpriteManager().removeInvader(invader);
                                invader.implode(this, invader.getCenterX(), invader.getCenterY());
                                getSpriteManager().addSpritesToBeRemoved(invader);
                                gameScore.set(gameScore.get() + invader.getPoint());
                                gameProgress.set(gameProgress.get() + 1);
                                invader.setIsDead(true);

                            }
                        }
                        getSpriteManager().addSpritesToBeRemoved(missile);

                    } else if (spriteA instanceof Ship ship) {
                        if ((spriteB instanceof Invader invader)) {
                             
                            if (!ship.isShieldOn()) {
                                ship.damaged();
                            } else {
                                ship.setShieldOn(false);
                                ship.getShieldFade().jumpTo(ship.getShieldFade().getDuration());
                                ship.getCollidingNode().setOpacity(0);

                            }
                            invader.implode(this, intersect.getBoundsInParent().getCenterX(), intersect.getBoundsInParent().getCenterY());
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
        if (gameProgress.get() + getSpriteManager().getInvaders().size() < numberOfInvaders && (getSpriteManager().getInvaders().size() < 9)) {
            this.spawnInvaders();

        }
        if (getSpriteManager().getInvaders().isEmpty()) {
            victory();
        }
    }
}
