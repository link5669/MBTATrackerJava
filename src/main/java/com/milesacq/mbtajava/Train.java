package com.milesacq.mbtajava;

import org.json.simple.JSONObject;

public class Train {
    private Color color;
    private Route route;
    private boolean direction;
    private ArrivalTime arrivalTime;

    public Train(JSONObject jsonObject) {
        Object relationship = jsonObject.get("relationships");
        JSONObject rel = (JSONObject) relationship;
        JSONObject routeData = (JSONObject) rel.get("route");
        JSONObject data = (JSONObject) routeData.get("data");
        String id = (String) data.get("id");
        if (id.contains("Green")) {
            color = Color.GREEN;
            switch (id.charAt(6)) {
                case 'A' -> route = Route.A;
                case 'B' -> route = Route.B;
                case 'C' -> route = Route.C;
                case 'D' -> route = Route.D;
                case 'E' -> route = Route.E;
            }
        }
        switch (id) {
            case "Blue" -> color = Color.BLUE;
            case "Red" -> color = Color.RED;
            case "Orange" -> color = Color.ORANGE;
        }
        JSONObject attributes = (JSONObject) jsonObject.get("attributes");
        String arrivalTimeStr = (String) attributes.get("arrival_time");
        arrivalTime = new ArrivalTime(arrivalTimeStr);
    }

    public String toString() {
        return color + " arrives at Hynes at " + arrivalTime;
    }
}
