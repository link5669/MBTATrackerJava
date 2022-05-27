package com.milesacq.mbtajava;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class MBTAApplication extends javafx.application.Application {
    private static SingletonStops stopData = new SingletonStops();
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MBTAApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
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
}