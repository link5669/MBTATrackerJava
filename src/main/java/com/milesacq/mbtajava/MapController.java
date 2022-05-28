package com.milesacq.mbtajava;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MapController implements Initializable{

    @FXML
    private Circle wonderland;

    @FXML
    private Circle revBeach;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.millis(1000));
        translateTransition.setNode(wonderland);
        System.out.println(wonderland.getLayoutX() - revBeach.getLayoutX());
        translateTransition.setByX((int)revBeach.getLayoutX() - wonderland.getLayoutX());
        translateTransition.setCycleCount(10);
        translateTransition.setAutoReverse(false);
        translateTransition.play();
    }
}
