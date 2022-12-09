/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.ufo.helpers;

import edu.vanier.ufo.ui.GameWorld;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

/**
 *
 * @author enyihou
 */
public class HomePageController {

    private IntegerProperty currentChosenLevel = new SimpleIntegerProperty(1);

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
    ImageView background;

    @FXML
    HBox hbox;

    @FXML
    StackPane stackpane;

    @FXML
    void initialize() {
        setImageView();
        setPlayBtn();
        setLeftBtn();
        setRightBtn();
        setLevelLabel();
        checkButtons();

    }

    private void setImageView() {

        Image image = new Image(ResourcesManager.LEVELS_LIST[currentChosenLevel.get() - 1]);
        imageview.setImage(image);

    }

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

    private void setLevelLabel() {
        level.textProperty().bind(Bindings.createStringBinding(() -> {
            return "Level : ".concat(currentChosenLevel.getValue().toString());
        }, currentChosenLevel));
    }

    private void setLeftBtn() {

        leftBtn.setOnMouseEntered((e) -> {
            leftBtn.setTextFill(Color.DARKORCHID);
        });
        leftBtn.setOnMouseExited((e) -> {
            leftBtn.setTextFill(Color.BLACK);
        });
        leftBtn.setBackground(Background.fill(Color.TRANSPARENT));
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
        rightBtn.setBackground(Background.fill(Color.TRANSPARENT));

        rightBtn.setOnAction((e) -> {
            int i = currentChosenLevel.get();
            currentChosenLevel.set(i + 1);

            setImageView();

            checkButtons();
        });
    }

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
