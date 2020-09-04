package com.example.urinemonitoring.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PatientDao {

    @Insert
    void insert(Patient patient);

    @Query("SELECT * FROM patients")
    LiveData<List<Patient>> getPatients();

    @Update
    void update(Patient patient);

    @Delete
    int delete(Patient patient);
}
