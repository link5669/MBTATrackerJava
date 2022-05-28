package com.milesacq.mbtajava;

import java.util.Objects;

public class Stop {
    private String name;
    private Color color;
    private int id;

    public Stop(String name, Color color, int id) {
        this.color = color;
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

    public boolean equals(Object stop) {
        return stop.toString().equals(this.toString());
    }
}
