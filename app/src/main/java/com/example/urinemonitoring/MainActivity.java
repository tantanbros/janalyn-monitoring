package com.example.urinemonitoring;

import android.os.Bundle;

import com.example.urinemonitoring.helpers.Constants;
import com.example.urinemonitoring.models.Option;
import com.example.urinemonitoring.models.OptionViewModel;
import com.example.urinemonitoring.models.Patient;
import com.example.urinemonitoring.models.PatientViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private OptionViewModel optionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        optionViewModel = ViewModelProviders.of(this).get(OptionViewModel.class);

        optionViewModel.getOptions().observe(this,
                new Observer<List<Option>>() {
                    @Override
                    public void onChanged(List<Option> options) {
                        if(options.size() <= 0) {
                            Option defaultOption = Constants.defaultOption;
                            optionViewModel.insert(defaultOption);
                            Log.d(TAG, "No Options Yet, setting default");
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}