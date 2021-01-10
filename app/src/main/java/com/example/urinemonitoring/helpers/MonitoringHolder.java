package com.example.urinemonitoring.helpers;

import com.example.urinemonitoring.models.Record;

public class MonitoringHolder {
    private static boolean ended;
    private static boolean started;
    private static String textData;
    private static Record lastRecord;


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


    public static String getTextData() {
        return textData;
    }

    public static void setTextData(String textData) {
        MonitoringHolder.textData = textData;
    }

    public static Record getLastRecord() {
        return lastRecord;
    }

    public static void setLastRecord(Record lastRecord) {
        MonitoringHolder.lastRecord = lastRecord;
    }
}
