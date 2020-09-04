package com.example.urinemonitoring.models;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PatientViewModel extends AndroidViewModel {
    private String TAG = "PatientViewModel";
    private PatientDao patientDao;
    private UrineMonitoringRoomDatabase db;
    private LiveData<List<Patient>> patients;

    public PatientViewModel(Application application) {
        super(application);
        db = UrineMonitoringRoomDatabase.getDatabase(application);
        patientDao = db.patientDao();
        patients = patientDao.getPatients();
    }

    public void insert(Patient patient) {
        new InsertAsyncTask(patientDao).execute(patient);
    }

    public LiveData<List<Patient>> getPatients() {
        return patients;
    }

    public void update(Patient patient) {
        new UpdateAsyncTask(patientDao).execute(patient);
    }

    public void delete(Patient patient) {
        new DeleteAsyncTask(patientDao).execute(patient);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "ViewModel Destroyed");
    }

    private class InsertAsyncTask extends AsyncTask<Patient, Void, Void> {
        PatientDao mPatientDao;

        public InsertAsyncTask(PatientDao mPatientDao) {
            this.mPatientDao = mPatientDao;
        }

        @Override
        protected Void doInBackground(Patient... patients) {
            mPatientDao.insert(patients[0]);
            return null;
        }
    }

    private class UpdateAsyncTask extends AsyncTask<Patient, Void, Void> {
        PatientDao mPatientDao;

        public UpdateAsyncTask(PatientDao mPatientDao) {
            this.mPatientDao = mPatientDao;
        }

        @Override
        protected Void doInBackground(Patient... patients) {
            mPatientDao.update(patients[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Patient, Void, Void> {
        PatientDao mPatientDao;

        public DeleteAsyncTask(PatientDao mPatientDao) {
            this.mPatientDao = mPatientDao;
        }

        @Override
        protected Void doInBackground(Patient... patients) {
            mPatientDao.delete(patients[0]);
            return null;
        }
    }
}
