package com.milesacq.mbtajava;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class SingletonStops {
    private static final HashSet<Stop> allStops = new HashSet<>();

    private String getStopData() {
        String output = "";
        try {
            URL url = new URL("https://api-v3.mbta.com/stops?sort=name");
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

    private Color getStopColor(String description) {
        Color color = null;
        if (description.contains("Orange")) {
            return  Color.ORANGE;
        } else if (description.contains("Red")) {
            return Color.RED;
        } else if (description.contains("Blue")) {
            return Color.BLUE;
        } else {
            return Color.GREEN;
        }
    }

    public SingletonStops() {
        String output = getStopData();
        JSONObject allData = (JSONObject) JSONValue.parse(output);
        JSONArray array = (JSONArray) allData.get("data");
        for (Object o : array) {
            JSONObject jsonDocument = (JSONObject) o;
            JSONObject relationship = (JSONObject) jsonDocument.get("relationships");
            if (!((String) jsonDocument.get("id")).matches("-?\\d+")) {
                continue;
            }
            int idNum = Integer.parseInt((String) jsonDocument.get("id"));
            JSONObject zone = (JSONObject) relationship.get("zone");
            JSONObject data = (JSONObject) zone.get("data");
            if (data == null) {
                continue;
            }
            String id = (String) data.get("id");
            JSONObject attributes = (JSONObject) jsonDocument.get("attributes");
            String description = (String) attributes.get("description");
            if (description == null) {
                continue;
            }
            if (description.contains("Exit")) {
                continue;
            }
            description = description.trim();
            if (description.contains("Line - ")) {
                int index = description.indexOf("Line - ");
                description = description.substring(0, index);
                description = description + "Line";
            }
            if (id.equals("RapidTransit")) {
                Color color = getStopColor(description);
                Stop stop = new Stop(description, color, idNum);
                allStops.add(stop);
            }
        }
    }

    public ArrayList<String> getColorStops(Color color) {
        ArrayList<String> redStops = new ArrayList<>();
        for (Stop stop : allStops) {
            if (stop.getColor() == color) {
                redStops.add(stop.toString());
            }
        }
        return redStops;
    }

    public String toString() {
        return allStops.toString();
    }

    public String getStopID(String stopIn) {
        for (Stop stop : allStops) {
            if (stop.toString().equals(stopIn)) {
                return stop.getId();
            }
        }
        return null;
    }

    public Stop getStop(String stopIn) {
        for (Stop stop : allStops) {
            if (stop.toString().equals(stopIn)) {
                return stop;
            }
        }
        return null;
    }
}
