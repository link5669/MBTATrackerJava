package com.milesacq.mbtajava;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MapController implements Initializable{

    @FXML
    private Circle wonderland;

    @FXML
    private Circle revBeach;

    @FXML
    private Circle suffolkDowns;

    @FXML
    private Circle state;

    @FXML
    private Circle bowdoin;

    @FXML
    private Circle beachmont;

    @FXML
    private Circle orient;

    @FXML
    private Circle maverick;

    @FXML
    private Circle airport;

    @FXML
    private Circle woodIsland;

    @FXML
    private Circle aquarium;

    @FXML
    private Circle govtCenter;

    @FXML
    private ImageView train1;

    @FXML
    private ChoiceBox<String> stopPicker;

    @FXML
    private ChoiceBox<String> boundBox;

    @FXML
    protected void onRefreshButtonClick() {
        ArrayList<Train> trainInfo = MBTAApplication.getTrainData(stopPicker, boundBox);
        Stop stop = MBTAApplication.getStopData().getStop(stopPicker.getValue());
        Train train = trainInfo.get(0);
        ArrivalTime time = train.getArrivalTime();
        LocalDateTime now = LocalDateTime.now();
        now.getHour()

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stopPicker.getItems().setAll(MBTAApplication.getStopData().getColorStops(Color.BLUE));
        boundBox.getItems().add("Inbound");
        boundBox.getItems().add("Outbound");
    }

    public void setTrain(double time, Stop startingStop, Stop endingStop) throws FileNotFoundException {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.minutes(5));
        translateTransition.setNode(train1);
        translateTransition.setByX(getLocationDifference(startingStop, endingStop));
        translateTransition.setCycleCount(10);
        translateTransition.setAutoReverse(false);
        translateTransition.play();
    }

    private int getLocationDifference(Stop startingStop, Stop endingStop) {
        Circle startingCircle = getCircle(startingStop);
        Circle endingCircle = getCircle(endingStop);
        return (int) (startingCircle.getLayoutX() - endingCircle.getLayoutX());
    }

    // BUNDLE CIRCLE AS AN ATTRIBUTE OF A TRAIN TO AVOID THIS
    private Circle getCircle(Stop stop) {
        if (stop.toString().contains("Wonderland")) {
            return wonderland;
        } else if (stop.toString().contains("State")) {
            return state;
        } else if (stop.toString().contains("Suffolk Downs")) {
            return suffolkDowns;
        } else if (stop.toString().contains("Bowdoin")) {
            return bowdoin;
        } else if (stop.toString().contains("Beachmont")) {
            return beachmont;
        } else if (stop.toString().contains("Orient")) {
            return orient;
        } else if (stop.toString().contains("Maverick")) {
            return maverick;
        } else if (stop.toString().contains("Airport")) {
            return airport;
        } else if (stop.toString().contains("Wood Island")) {
            return woodIsland;
        } else if (stop.toString().contains("Aquarium")) {
            return aquarium;
        } else if (stop.toString().contains("Government Center")) {
            return govtCenter;
        } else if (stop.toString().contains("Revere Beach")) {
            return revBeach;
        }
        return revBeach;
    }
}
