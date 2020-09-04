package com.example.urinemonitoring.models;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class OptionViewModel extends AndroidViewModel {
    private String TAG = "OptionViewModel";
    private OptionDao optionDao;
    private UrineMonitoringRoomDatabase db;
    private LiveData<List<Option>> options;

    public OptionViewModel(Application application) {
        super(application);
        db = UrineMonitoringRoomDatabase.getDatabase(application);
        optionDao = db.optionDao();
        options = optionDao.getOptions();
    }

    public void insert(Option option) {
        new InsertAsyncTask(optionDao).execute(option);
    }

    public LiveData<List<Option>> getOptions() {
        if(options == null) {
            options = db.optionDao().getOptions();
        }
        return options;
    }


    private class InsertAsyncTask extends AsyncTask<Option, Void, Void> {
        OptionDao mOptionDao;

        public InsertAsyncTask(OptionDao mOptionDao) {
            this.mOptionDao = mOptionDao;
        }

        @Override
        protected Void doInBackground(Option... options) {
            mOptionDao.insert(options[0]);
            return null;
        }
    }

}
