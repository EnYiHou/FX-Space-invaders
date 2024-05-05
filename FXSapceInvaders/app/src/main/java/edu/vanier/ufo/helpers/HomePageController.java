
package edu.vanier.ufo.helpers;

import edu.vanier.ufo.ui.GameWorld;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author enyihou
 */
public class HomePageController {

    private final IntegerProperty currentChosenLevel = new SimpleIntegerProperty(1);

    @FXML
    Button leftBtn;

    @FXML
    Button rightBtn;

    @FXML
    Label level;

    @FXML
    Button playBtn;

    @FXML
    ImageView imageview;

   

    @FXML
    void initialize() {
        setImageView();
        setPlayBtn();
        setLeftBtn();
        setRightBtn();
        setLevelLabel();
        checkButtons();

    }

    /**
     * Set the image view according to the level
     */
    private void setImageView() {

        String path = ResourcesManager.LEVELS_LIST[currentChosenLevel.get() - 1];
        System.out.println("Current path: " + path);
        Image image = new Image(getClass().getResource(path).toExternalForm());
        imageview.setImage(image);

    }

    public static void main(String[] args) {
        
        String path = ResourcesManager.LEVELS_LIST[0];
        System.out.println("Path: " + path);
        Image image = new Image(HomePageController.class.getResource(path).toExternalForm());
        System.out.println("Image: " + image);
    

    }

    /**
     * Handle the button.
     * The button starts the game with the selected level
     */
    private void setPlayBtn() {

        playBtn.setOnAction((e) -> {
            Stage primaryStage = (Stage) playBtn.getScene().getWindow();
            GameWorld gameWorld = new GameWorld(ResourcesManager.FRAMES_PER_SECOND, "JavaFX Space Invaders", currentChosenLevel.get());
            // Setup title, scene, stats, controls, and actors.
            gameWorld.initialize(primaryStage);
            // kick off the game loop
            gameWorld.beginGameLoop();
            // display window
        });
    }

    /**
     * Binds the textPropert of the title label to the current chosen level
     */
    private void setLevelLabel() {
        level.textProperty().bind(Bindings.createStringBinding(() -> {
            return "Level : ".concat(currentChosenLevel.getValue().toString());
        }, currentChosenLevel));
    }

    /**
     * Set arrows button to allow the user switch between levels.
     */
    private void setLeftBtn() {

        leftBtn.setOnMouseEntered((e) -> {
            leftBtn.setTextFill(Color.DARKORCHID);
        });
        leftBtn.setOnMouseExited((e) -> {
            leftBtn.setTextFill(Color.BLACK);
        });
        leftBtn.setBackground(Background.EMPTY);
        leftBtn.setOnAction((e) -> {

            int i = currentChosenLevel.get();
            System.out.println(i);
            currentChosenLevel.set(i - 1);

            setImageView();

            checkButtons();

        });
    }

    private void setRightBtn() {
        rightBtn.setOnMouseEntered((e) -> {
            rightBtn.setTextFill(Color.DARKORCHID);
        });
        rightBtn.setOnMouseExited((e) -> {

            rightBtn.setTextFill(Color.BLACK);
        });
        rightBtn.setBackground(Background.EMPTY);

        rightBtn.setOnAction((e) -> {
            int i = currentChosenLevel.get();
            currentChosenLevel.set(i + 1);

            setImageView();

            checkButtons();
        });
    }

    /**
     * verify if the buttons can be clicked. The user cannot choose a level less 
     * than 1 or more than 4
     */
    private void checkButtons() {

        int i = currentChosenLevel.get();
        if (i == 1) {
            leftBtn.setDisable(true);
        } else {
            leftBtn.setDisable(false);

        }
        if (i == 4) {
            rightBtn.setDisable(true);
        } else {
            rightBtn.setDisable(false);

        }

    }

}
