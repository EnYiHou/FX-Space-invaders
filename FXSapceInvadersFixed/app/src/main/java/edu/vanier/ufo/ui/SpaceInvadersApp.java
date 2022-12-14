package edu.vanier.ufo.ui;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.helpers.HomePageController;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The main driver of the game.
 * @author EnYi
 */
public class SpaceInvadersApp extends Application {

    /**
     * The game engine
     */
    GameEngine gameWorld;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the homepage fxml file into the scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource(("/fxml/homepage.fxml")));
        loader.setController(new HomePageController());
        Pane pane = loader.load();
        
        primaryStage.setOnCloseRequest((e) -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.setScene(new Scene(pane));
        
        primaryStage.show();
    }

   

    @Override
    public void stop() throws Exception {
        Platform.exit();

        if (gameWorld != null) {
            gameWorld.shutdown();
        }
    }

}
