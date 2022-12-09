/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.ufo.helpers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author enyihou
 */
public class EndPageController {
    
    

    @FXML
    Button continuBtn;

    @FXML
    ImageView imageView;

    @FXML
    void initialize() {

    }

    private void setContinuBtn() {
        continuBtn.setOnAction((e) -> {

            try {
                Stage stage = (Stage) continuBtn.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(("/fxml/homepage.fxml")));
                loader.setController(new HomePageController());
                Pane pane = loader.load();

                stage.setScene(new Scene(pane));
            } catch (IOException ex) {
            }

        });
    }

    private void setImageView() {
        
        
        

    }
}
