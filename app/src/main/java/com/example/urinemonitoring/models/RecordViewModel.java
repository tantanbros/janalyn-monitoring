package com.example.urinemonitoring.models;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.urinemonitoring.helpers.Constants;

import java.util.List;

public class RecordViewModel extends AndroidViewModel {
    private String TAG = "RecordViewModel";
    private RecordDao recordDao;
    private UrineMonitoringRoomDatabase db;
    private LiveData<List<Record>> records;

    public RecordViewModel(Application application) {
        super(application);
        db = UrineMonitoringRoomDatabase.getDatabase(application);
        recordDao = db.recordDao();
        records = recordDao.getRecords();
    }

    public void insert(Record record) {
        new RecordViewModel.InsertAsyncTask(recordDao).execute(record);
    }

    public LiveData<List<Record>> getRecords() {
        return records;
    }

    public LiveData<List<Record>> getRecordsByPatient(String patientId) {
        return recordDao.getRecordsByPatient(patientId, Constants.noFlowRate);
    }

    private class InsertAsyncTask extends AsyncTask<Record, Void, Void> {
        RecordDao mRecordDao;

        public InsertAsyncTask(RecordDao mRecordDao) {
            this.mRecordDao = mRecordDao;
        }

        @Override
        protected Void doInBackground(Record... records) {
            mRecordDao.insert(records[0]);
            return null;
        }
    }
}
