package com.example.urinemonitoring.helpers;

public class MonitoringHolder {
    private static boolean ended;
    private static boolean started;

    public static boolean hasEnded() {
        return ended;
    }

    public static void setEnded(boolean ended) {
        MonitoringHolder.ended = ended;
    }

    public static boolean hasStarted() {
        return started;
    }

    public static void setStarted(boolean started) {
        MonitoringHolder.started = started;
    }

}
