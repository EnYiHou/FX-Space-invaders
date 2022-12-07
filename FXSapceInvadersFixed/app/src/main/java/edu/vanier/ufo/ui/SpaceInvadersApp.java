package edu.vanier.ufo.ui;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The main driver of the game.
 *
 * @author cdea
 */
public class SpaceInvadersApp extends Application {
    
    GameEngine gameWorld;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        Media media = new Media(getClass().getResource("/videos/Trailer.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(Integer.MAX_VALUE);
        MediaView trailerMP4 = new MediaView(mediaPlayer);
        trailerMP4.setSmooth(true);
        trailerMP4.setCache(true);
        trailerMP4.setCacheHint(CacheHint.QUALITY);
        VBox optionsBox = new VBox();
        optionsBox.prefWidthProperty().bind(primaryStage.widthProperty());
        optionsBox.prefHeightProperty().bind(primaryStage.heightProperty());
        
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setSpacing(50);
        
        StackPane root = new StackPane(trailerMP4, optionsBox);
        Scene scene = new Scene(root, 1200, 900);
        primaryStage.setScene(scene);
        
        ImageView image = new ImageView(new Image(ResourcesManager.TITLE));
        
        
        
        Rectangle title = new Rectangle();
        title.widthProperty().bind(primaryStage.widthProperty());
         title.heightProperty().bind(primaryStage.heightProperty());
        title.setClip(image);
        Button playBtn = new Button("Play");
        playBtn.setBackground(Background.fill(Color.CYAN));
        playBtn.prefWidthProperty().bind(primaryStage.widthProperty().divide(3));
        
        playBtn.setBorder(Border.stroke(Color.SKYBLUE));
        playBtn.setFont(new Font(STYLESHEET_MODENA, 30));
        
        Button shopBtn = new Button("Shop");
        shopBtn.setBackground(Background.fill(Color.CYAN));
        shopBtn.prefWidthProperty().bind(primaryStage.widthProperty().divide(3));
        shopBtn.setBorder(Border.stroke(Color.SKYBLUE));
        shopBtn.setFont(new Font(STYLESHEET_MODENA, 30));
        
        optionsBox.getChildren().addAll( playBtn, shopBtn);
        
        
        primaryStage.show();
        mediaPlayer.play();
        
        playBtn.setOnAction((e) -> {
            
            gameWorld = new GameWorld(ResourcesManager.FRAMES_PER_SECOND, "JavaFX Space Invaders", 1);
            // Setup title, scene, stats, controls, and actors.
            gameWorld.initialize(primaryStage);
            // kick off the game loop
            gameWorld.beginGameLoop();
            // display window
        });
    }
    
    @Override
    public void stop() throws Exception {
        Platform.exit();
        
        if (gameWorld != null) {
            gameWorld.shutdown();
        }
    }
    
}
