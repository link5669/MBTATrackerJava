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

    public SingletonStops() {
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
            description = description.trim();
            if (description.contains("Track")) {
                int index = description.indexOf(" - Track");
                description = description.substring(0, index);
            }
            if (description.contains("Line - ")) {
                int index = description.indexOf("Line - ");
                description = description.substring(0, index);
                description = description + "Line";
            }
            if (description.contains("Exit")) {
                continue;
            }
            if (id.equals("RapidTransit")) {
                Color color = null;
                if (description.contains("Orange")) {
                    color = Color.ORANGE;
                } else if (description.contains("Red")) {
                    color = Color.RED;
                } else if (description.contains("Blue")) {
                    color = Color.BLUE;
                } else if (description.contains("Green")) {
                    color = Color.GREEN;
                }
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
}
