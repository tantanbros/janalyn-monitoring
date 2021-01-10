package com.example.urinemonitoring.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.urinemonitoring.helpers.Constants;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecordDao {

    @Insert
    void insert(Record record);

    @Query("SELECT * FROM records")
    LiveData<List<Record>> getRecords();

    @Query("SELECT * FROM records WHERE patientId=:patientId AND flowRate!=:flowRate")
    LiveData<List<Record>> getRecordsByPatient(String patientId, String flowRate);
}
