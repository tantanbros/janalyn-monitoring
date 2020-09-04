package com.example.urinemonitoring.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "patients")
public class Patient {
    @NonNull
    @PrimaryKey
    private String Id;

    @NonNull
    @ColumnInfo(name = "name")
    private String Name;

    @ColumnInfo(name = "age")
    private int Age;

    @NonNull
    @ColumnInfo(name = "gender")
    private String Gender;

    @NonNull
    @ColumnInfo(name = "status")
    private String Status;

    @NonNull
    @ColumnInfo(name = "device")
    private String Device;

    public Patient() {}

    public Patient(@NonNull String id, @NonNull String name, int age, @NonNull String gender, @NonNull String status, @NonNull String device) {
        Id = id;
        Name = name;
        Age = age;
        Gender = gender;
        Status = status;
        Device = device;
    }

    @NonNull
    public String getId() {
        return Id;
    }

    @NonNull
    public String getName() {
        return Name;
    }

    public int getAge() {
        return Age;
    }

    @NonNull
    public String getGender() {
        return Gender;
    }

    @NonNull
    public String getStatus() {
        return Status;
    }

    @NonNull
    public String getDevice() {
        return Device;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    public void setName(@NonNull String name) {
        Name = name;
    }

    public void setAge(int age) {
        Age = age;
    }

    public void setGender(@NonNull String gender) {
        Gender = gender;
    }

    public void setStatus(@NonNull String status) {
        Status = status;
    }

    public void setDevice(@NonNull String device) {
        Device = device;
    }
}
