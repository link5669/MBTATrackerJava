package com.milesacq.mbtajava;

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

    private void setStopPicker() {

    }

    @FXML
    protected void onRefreshButtonClick() {
        StringBuilder output = new StringBuilder();
        String idString;
        if (boundBox.getValue().equals("Inbound")) {
            int idInt = Integer.parseInt(MBTAApplication.getStopData().getStopID(stopPicker.getValue()));
            idInt++;
            idString = String.valueOf(idInt);
        } else {
            idString = MBTAApplication.getStopData().getStopID(stopPicker.getValue());
        }
        JSONObject jsonDocument = (JSONObject) JSONValue.parse(getData(idString));
        JSONArray jsonArr = (JSONArray) jsonDocument.get("data");
        for (Object o : jsonArr) {
            JSONObject curr = (JSONObject) o;
            Object relationship = curr.get("relationships");
            JSONObject rel = (JSONObject) relationship;
            JSONObject routeData = (JSONObject) rel.get("route");
            JSONObject data = (JSONObject) routeData.get("data");
            String id = (String) data.get("id");
            JSONObject attributes = (JSONObject) curr.get("attributes");
            String arrivalTimeStr = (String) attributes.get("arrival_time");
            Long directionId = (Long) attributes.get("direction_id");
            boolean direction;
            direction = directionId == 1;
            Train train;
            if (id.equals("Green")) {
                train = new GreenTrain(id, arrivalTimeStr, direction);
            } else {
                train = new Train(id, arrivalTimeStr, direction);
            }
            output.append(train).append("\n");
        }
        welcomeText.setText(output.toString());
    }


    private String getData(String id) {
        String output = "";
        try {
            URL url = new URL("https://api-v3.mbta.com/predictions?filter[stop]=" + id + "&sort=arrival_time");
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