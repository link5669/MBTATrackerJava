package com.milesacq.mbtajava;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private ChoiceBox<String> linePicker;

    @FXML
    private ChoiceBox<String> stopPicker;

    @FXML
    private ChoiceBox<String> boundBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Color[] possibleValues = Color.RED.getDeclaringClass().getEnumConstants();
        for (Color possibleValue : possibleValues) {
            linePicker.getItems().add(possibleValue.toString());
        }
        boundBox.getItems().add("Inbound");
        boundBox.getItems().add("Outbound");
        SingletonStops stops = new SingletonStops();
        linePicker.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> checkStopPicker(Color.values()[t1.intValue()]));
    }

    private void checkStopPicker(Color color) {
        if (color != null) {
            if (color == Color.RED) {
                stopPicker.getItems().setAll(MBTAApplication.getStopData().getColorStops(Color.RED));
            } else if (color == Color.BLUE) {
                stopPicker.getItems().setAll(MBTAApplication.getStopData().getColorStops(Color.BLUE));
            } else if (color == Color.ORANGE) {
                stopPicker.getItems().setAll(MBTAApplication.getStopData().getColorStops(Color.ORANGE));
            } else if (color == Color.GREEN) {
                stopPicker.getItems().setAll(MBTAApplication.getStopData().getColorStops(Color.GREEN));
            }
        }
    }

    @FXML
    protected void onRefreshButtonClick() {
        welcomeText.setText(MBTAApplication.getTrainData(stopPicker, boundBox).toString());
    }



}