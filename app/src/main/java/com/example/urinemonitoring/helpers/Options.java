package com.example.urinemonitoring.helpers;

public class Options {
    private int interval;
    private String url;
    private String dataProperty;

    public int getIntervalMs() {
        return intervalMillis[interval];
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataProperty() {
        return dataProperty;
    }

    public void setDataProperty(String dataProperty) {
        this.dataProperty = dataProperty;
    }

    public Options(int interval, String url, String dataProperty) {
        this.interval = interval;
        this.url = url;
        this.dataProperty = dataProperty;
    }

    private final int[] intervalMillis = {
            1 * 1000//,      // 1 second
//            10 * 1000,      // 10 seconds
//            30 * 1000,      // 30 seconds
//            60 * 1000,      // 1 minute
//            5 * 60 * 1000,  // 5 minutes
//            30 * 60 * 1000, // 30 minutes
//            60 * 60 * 1000, // 1 hour
    };
}
