package com.milesacq.mbtajava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException, ParseException {
        try (BufferedInputStream in = new BufferedInputStream(new URL("https://api-v3.mbta.com/predictions?filter[stop]=70153&sort=arrival_time").openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("MBTASTATUS.txt")) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.out.println("Can't find data!");
        }
        JSONObject jsonDocument = (JSONObject) JSONValue.parse(new FileReader(new File("MBTASTATUS.txt")));
        JSONArray jsonArr = (JSONArray) jsonDocument.get("data");
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject curr = (JSONObject) jsonArr.get(i);
            Object relationship = curr.get("relationships");
            JSONObject rel = (JSONObject) relationship;
            JSONObject route = (JSONObject) rel.get("route");
            JSONObject data = (JSONObject) route.get("data");
            String id = (String) data.get("id");
            JSONObject attributes = (JSONObject) curr.get("attributes");
            String arrivalTimeStr = (String) attributes.get("arrival_time");
            String[] arrivalArr = arrivalTimeStr.split("");
            int hr = Integer.parseInt(arrivalArr[11] + arrivalArr[12]);
            int min = Integer.parseInt(arrivalArr[14] + arrivalArr[15]);
            String opZero = "";
            if (arrivalArr[14].equals("0")) {
                opZero = "0";
            }
            boolean am = true;
            if (hr > 12) {
                hr = hr - 12;
                am = false;
            }

            String meridian = "PM";
            if (am) {
                meridian = "AM";
            }
            System.out.println(id + " arrives at Hynes at " + hr + ":" + opZero + min + " " + meridian);
        }
        launch();
    }
}