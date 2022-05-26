package com.milesacq.mbtajava;

public class ArrivalTime {
    private int hour;
    private int minute;
    private String meridiem = "PM";

    public ArrivalTime(String timeString) {
        String[] arrivalArr = timeString.split("");
        int hr = Integer.parseInt(arrivalArr[11] + arrivalArr[12]);
        int min = Integer.parseInt(arrivalArr[14] + arrivalArr[15]);
        boolean am = true;
        if (hr > 12) {
            hr = hr - 12;
            am = false;
        }
        if (am) {
            meridiem = "AM";
        }
        minute = min;
        hour = hr;
    }

    public String toString() {
        String opZero = "";
        if (minute < 10) {
            opZero = "0";
        }
        return hour + ":" + opZero + minute + " " + meridiem;
    }
}
