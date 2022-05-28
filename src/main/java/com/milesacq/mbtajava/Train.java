package com.milesacq.mbtajava;

import javafx.scene.shape.Circle;

public class Train {
    private Color color;
    //TRUE is inbound, FALSE is outbound
    private boolean direction;
    private ArrivalTime arrivalTime;
    private Circle circle;

    public Train(String id,String arrivalTimeStr, boolean directionId) {
        direction = directionId;
        if (id.contains("Green")) {
            color = Color.GREEN;
        } else {
            switch (id) {
                case "Blue" -> color = Color.BLUE;
                case "Red" -> color = Color.RED;
                case "Orange" -> color = Color.ORANGE;
            }
        }
        arrivalTime = new ArrivalTime(arrivalTimeStr);
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public String toString() {
        String dir = direction ? "Inbound" : "Outbound";
        String colorStr = null;
        if (color == Color.GREEN) {
            colorStr = "Green";
        } else if (color == Color.BLUE) {
            colorStr = "Blue";
        } else if (color == Color.ORANGE) {
            colorStr = "Orange";
        } else if (color == Color.RED) {
            colorStr = "Red";
        }
        return dir + " " + colorStr + " arrives at " + arrivalTime;
    }
}