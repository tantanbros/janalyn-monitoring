package com.example.urinemonitoring.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Patient.class, Record.class, Option.class}, version = 11, exportSchema = false)
public abstract class UrineMonitoringRoomDatabase extends RoomDatabase {

    public abstract PatientDao patientDao();
    public abstract RecordDao recordDao();
    public abstract OptionDao optionDao();

    private static volatile UrineMonitoringRoomDatabase roomInstance;

    static UrineMonitoringRoomDatabase getDatabase(final Context context) {
        if(roomInstance == null){
            synchronized (UrineMonitoringRoomDatabase.class) {
                if(roomInstance == null) {
                    roomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            UrineMonitoringRoomDatabase.class, "urineMonitoring_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return roomInstance;
    }
}
