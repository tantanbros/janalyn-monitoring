package com.example.urinemonitoring.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "records",
        foreignKeys = @ForeignKey(
                entity = Patient.class,
                parentColumns = "Id",
                childColumns = "patientId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Record {
    @NonNull
    @PrimaryKey
    private String Id;

    @NonNull
    @ColumnInfo(name = "patientId")
    private String PatientId;

    @NonNull
    @ColumnInfo(name = "device")
    private String Device;

    @NonNull
    @ColumnInfo(name = "recordTime")
    private String RecordTime;

    @NonNull
    @ColumnInfo(name = "flowRate")
    private String FlowRate;

    @NonNull
    @ColumnInfo(name = "volume")
    private String Volume;

    @NonNull
    @ColumnInfo(name = "turbidity")
    private String Turbidity;

    @NonNull
    @ColumnInfo(name = "color")
    private String Color;

    public Record() {}

    public Record(@NonNull String id, @NonNull String patientId, @NonNull String device, @NonNull String recordTime, @NonNull String flowRate, @NonNull String volume, @NonNull String turbidity, @NonNull String color) {
        Id = id;
        PatientId = patientId;
        Device = device;
        RecordTime = recordTime;
        FlowRate = flowRate;
        Volume = volume;
        Turbidity = turbidity;
        Color = color;
    }

    @NonNull
    @Override
    public String toString() {
        String str = "";
        str += String.format("%s\n", RecordTime);
        str += String.format("Flowrate: %s ml/s Volume: %s ml\n", FlowRate, Volume);
        str += String.format("Turbidity Eq. V: %s V\n", Turbidity);
        str += String.format("Color Eq: %s \n", Color);
        str += "\n\n";
        return str;
    }

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    @NonNull
    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(@NonNull String patientId) {
        PatientId = patientId;
    }

    @NonNull
    public String getDevice() {
        return Device;
    }

    public void setDevice(@NonNull String device) {
        Device = device;
    }

    @NonNull
    public String getRecordTime() {
        return RecordTime;
    }

    public void setRecordTime(@NonNull String recordTime) {
        RecordTime = recordTime;
    }

    @NonNull
    public String getFlowRate() {
        return FlowRate;
    }

    public void setFlowRate(@NonNull String flowRate) {
        FlowRate = flowRate;
    }

    @NonNull
    public String getVolume() {
        return Volume;
    }

    public void setVolume(@NonNull String volume) {
        Volume = volume;
    }

    @NonNull
    public String getTurbidity() {
        return Turbidity;
    }

    public void setTurbidity(@NonNull String turbidity) {
        Turbidity = turbidity;
    }

    @NonNull
    public String getColor() {
        return Color;
    }

    public void setColor(@NonNull String color) {
        Color = color;
    }
}
