package com.milesacq.mbtajava;

public class GreenTrain extends Train {
    private Route route;

    public GreenTrain(String id, String arrivalTimeStr, boolean directionId) {
        super(id, arrivalTimeStr, directionId);
        switch (id.charAt(6)) {
            case 'A' -> route = Route.A;
            case 'B' -> route = Route.B;
            case 'C' -> route = Route.C;
            case 'D' -> route = Route.D;
            case 'E' -> route = Route.E;
        }
    }

    public String toString() {
        return super.toString() + route;
    }
}
