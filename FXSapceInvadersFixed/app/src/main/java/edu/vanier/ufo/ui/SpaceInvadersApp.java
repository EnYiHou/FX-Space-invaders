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
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(("/fxml/homepage.fxml")));
        loader.setController(new HomePageController());
        Pane pane = loader.load();
        
        primaryStage.setScene(new Scene(pane));
        setStage(primaryStage);
        primaryStage.show();
    }

    private void setStage(Stage stage){
        stage.setMinHeight(500);
        stage.setMinWidth(650);
    }
    
    @Override
    public void stop() throws Exception {
        Platform.exit();

        if (gameWorld != null) {
            gameWorld.shutdown();
        }
    }

}
