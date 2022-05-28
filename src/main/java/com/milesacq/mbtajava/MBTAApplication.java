package com.milesacq.mbtajava;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MBTAApplication extends javafx.application.Application {
    private static SingletonStops stopData = new SingletonStops();
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MBTAApplication.class.getResource("listview.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1820, 640);
        stage.setTitle("MBTA Tracker");
        stage.setScene(scene);
        stage.show();
    }

    public static SingletonStops getStopData() {
        return stopData;
    }

    public static void main(String[] args) {
        launch();
    }

    public static String getData(String id) {
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

    public static StringBuilder getTrainData(ChoiceBox<String> stopPicker, ChoiceBox<String> boundBox) {
        StringBuilder output = new StringBuilder();
        String idString;
        if (boundBox.getValue().equals("Inbound")) {
            int idInt = Integer.parseInt(MBTAApplication.getStopData().getStopID(stopPicker.getValue()));
            idInt--;
            idString = String.valueOf(idInt);
        } else {
            idString = MBTAApplication.getStopData().getStopID(stopPicker.getValue());
        }
        JSONObject jsonDocument = (JSONObject) JSONValue.parse(MBTAApplication.getData(idString));
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
        return output;
    }
}