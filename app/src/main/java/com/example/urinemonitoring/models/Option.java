package com.example.urinemonitoring.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.urinemonitoring.helpers.Constants;

@Entity(tableName = "options")
public class Option {
    @NonNull
    @PrimaryKey
    private String Id;

    @NonNull
    @ColumnInfo(name = "interval")
    private int Interval;

    @NonNull
    @ColumnInfo(name = "url")
    private String Url;

    @NonNull
    @ColumnInfo(name = "dataProperty")
    private String DataProperty;

    public Option() {}

    public Option(@NonNull String id, int interval, @NonNull String url, @NonNull String dataProperty) {
        Id = id;
        Interval = interval;
        Url = url;
        DataProperty = dataProperty;
    }

    public int getIntervalMs() {
        return Constants.intervalMillis[Interval];
    }

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    public int getInterval() {
        return Interval;
    }

    public void setInterval(int interval) {
        Interval = interval;
    }

    @NonNull
    public String getUrl() {
        return Url;
    }

    public void setUrl(@NonNull String url) {
        Url = url;
    }

    @NonNull
    public String getDataProperty() {
        return DataProperty;
    }

    public void setDataProperty(@NonNull String dataProperty) {
        DataProperty = dataProperty;
    }

}
