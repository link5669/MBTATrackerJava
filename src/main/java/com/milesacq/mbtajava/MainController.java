package com.milesacq.mbtajava;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.URL;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws FileNotFoundException {
        writeFile();
        StringBuilder output = new StringBuilder();
        JSONObject jsonDocument = (JSONObject) JSONValue.parse(new FileReader("MBTASTATUS.txt"));
        JSONArray jsonArr = (JSONArray) jsonDocument.get("data");
        for (Object o : jsonArr) {
            JSONObject curr = (JSONObject) o;
            Train train = new Train(curr);
            output.append(train).append("\n");
        }
        welcomeText.setText(output.toString());
    }

    private void writeFile() {
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
    }
}