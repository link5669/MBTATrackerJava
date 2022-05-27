package com.milesacq.mbtajava;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private ChoiceBox<String> linePicker;

    @FXML
    private ChoiceBox<String> stopPicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Color[] possibleValues = Color.RED.getDeclaringClass().getEnumConstants();
        for (Color possibleValue : possibleValues) {
            linePicker.getItems().add(possibleValue.toString());
        }
        SingletonStops stops = new SingletonStops();
        System.out.println(stops);
        linePicker.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                checkStopPicker(Color.values()[t1.intValue()]);
            }
        });
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

    private void setStopPicker() {

    }

    @FXML
    protected void onRefreshButtonClick() {
        StringBuilder output = new StringBuilder();
        JSONObject jsonDocument = (JSONObject) JSONValue.parse(getData());
        JSONArray jsonArr = (JSONArray) jsonDocument.get("data");
        for (Object o : jsonArr) {
            JSONObject curr = (JSONObject) o;
            Train train = new Train(curr);
            output.append(train).append("\n");
        }
        welcomeText.setText(output.toString());
    }


    private String getData() {
        String output = "";
        try {
            URL url = new URL("https://api-v3.mbta.com/predictions?filter[stop]=70153&sort=arrival_time");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            output = br.readLine();
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        return output;
    }
}