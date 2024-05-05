module edu.vanier.ufo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;

    opens edu.vanier.ufo.helpers to javafx.fxml;
    exports edu.vanier.ufo.engine;
    exports edu.vanier.ufo.game;
    exports edu.vanier.ufo.helpers;
    exports edu.vanier.ufo.ui;



}
