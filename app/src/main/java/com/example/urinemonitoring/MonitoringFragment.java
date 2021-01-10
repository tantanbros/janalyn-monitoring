package com.example.urinemonitoring;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.urinemonitoring.helpers.Constants;
import com.example.urinemonitoring.helpers.MonitoringHolder;
import com.example.urinemonitoring.helpers.PatientHolder;
import com.example.urinemonitoring.models.Option;
import com.example.urinemonitoring.models.OptionViewModel;
import com.example.urinemonitoring.models.Patient;
import com.example.urinemonitoring.models.Record;
import com.example.urinemonitoring.models.RecordViewModel;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class MonitoringFragment extends Fragment {
    private final String TAG = "MonitoringFragment";
    private final String RequestTag = "Request";
    private EditText editDevice;
    private TextView textData;
    private Patient patient;
    private RecordViewModel recordViewModel;
    private ArrayList<Record> records = new ArrayList<>();
    private MutableLiveData<List<Record>> liveRecords = new MutableLiveData<>();
    private Timer dataGetter;
    private Option option;
    private OptionViewModel optionViewModel;
    private RequestQueue queue;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monitoring, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patient = PatientHolder.getPatient();
        MonitoringHolder.setEnded(false);

        editDevice = view.findViewById(R.id.editDevice);
        editDevice.setText(patient.getDevice());

        recordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        optionViewModel = ViewModelProviders.of(this).get(OptionViewModel.class);

        queue = Volley.newRequestQueue(MonitoringFragment.this.getContext());

        Toast.makeText(getContext(), "Monitoring Started", Toast.LENGTH_SHORT).show();
        MonitoringHolder.setEnded(false);
        MonitoringHolder.setStarted(true);
//
//        view.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resetMonitoring();
//                Toast.makeText(getContext(), "Reset Monitoring", Toast.LENGTH_SHORT).show();
//            }
//        });

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // v1.1
                // Future: In Case need maview ung previous textData, dito mo kunin
//                MonitoringHolder.setTextData(textData.getText().toString());

                goBack();

                // v1
                // can't go back when monitoring is running
//                if(!MonitoringHolder.hasEnded()) {
//                    Toast.makeText(getContext(), "Monitoring is still running, end or save it first to go back", Toast.LENGTH_SHORT).show();
//                } else {
//                    NavHostFragment.findNavController(MonitoringFragment.this)
//                            .navigate(R.id.action_Monitoring_to_CreatePatient);
//                }
            }
        });

//        v1.1 - removed
//        view.findViewById(R.id.btnEnd).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MonitoringHolder.setEnded(true);
//                MonitoringHolder.setStarted(false);
//                Log.d(TAG, "Monitoring Ended");
//                Toast.makeText(getContext(), "Monitoring Ended", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // if hasn't ended, we confirm if they want to end it now and save it
//                if(!MonitoringHolder.hasEnded()) {
//                    saveRecords(false, false);
//                    NavHostFragment.findNavController(MonitoringFragment.this)
//                            .navigate(R.id.action_Monitoring_to_ConfirmEnd);
//                }else {
//                    // monitoring ended, save the records in memory to the database
//                    saveRecords(true, true);
//                }
//            }
//        });

        view.findViewById(R.id.btnOptions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptions();
            }
        });

        // display the records in here
        textData = view.findViewById(R.id.textData);

        // observe the live data in memory and display them
        liveRecords.observe(getViewLifecycleOwner(), new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                // display lang pag nag start na yung monitoring
                if(MonitoringHolder.hasStarted()) {
                    int tailCount = 5;
                    List<Record> tail = records.subList(Math.max(records.size() - tailCount, 0), records.size());
                    String recordsString = "";
                    for(Record r: tail) {
                        recordsString += r.toString();
                    }
                    textData.setText(recordsString);
//                    Log.d(TAG, recordsString);
                }
            }
        });
    }

    private void goBack() {
        // keep the last item on the record and reset monitoring
        if(records.size() > 0) {
            Record lastRecord = records.get(records.size() - 1);
            Log.d(TAG, "GO BACK: Last Record");
            Log.d(TAG, lastRecord.toString());
            MonitoringHolder.setLastRecord(lastRecord);
        }
        resetMonitoring();


        NavHostFragment.findNavController(MonitoringFragment.this)
                .navigate(R.id.action_Monitoring_to_CreatePatient);
    }

//    private void computeFlowrate(Record finalRecord) {
//        // not yet computable
//        if(records.size() < 2){
//            return;
//        }
//
//
//        Record initialRecord = null;
//
//        float highestVolume = Float.MIN_VALUE;
//
//        for(int i = 0; i < records.size() - 1; i++) {
//            Record prev = records.get(i);
//            Record cur = records.get(i + 1);
//
//            if(isStringZero(prev.getVolume()) && !isStringZero(cur.getVolume())) {
//                // need lang yung time ng initial record para macompute yung sa flowrate
//                initialRecord = cur;
//                break;
//            }
//        }
//
//        if(initialRecord == null) {
//            return;
//        }
//
//        // compute na ung flowrate
//        float finalVol = Float.parseFloat(finalRecord.getVolume());
//
//        float flowRate = finalVol / 10; // kunyare 10 seconds sya, ml/s yung flowrate
//
//        Log.d(TAG, "ETO NA YUNG FLOWRATE");
//        Log.d(TAG, String.valueOf(flowRate));
//
//
//        finalRecord.setFlowRate(String.valueOf(flowRate));
//        finalRecord.setId(UUID.randomUUID().toString());
//        recordViewModel.insert(finalRecord);
//        Toast.makeText(getContext(), "Flowrate Computed.. See Records", Toast.LENGTH_SHORT).show();
//    }
//
//    private void checkFlowrateComputable(Record previous, Record current) {
//        // make sure not null
//        if(previous == null || current == null) {
//            return;
//        }
//
//        float prevVol = Float.parseFloat(current.getVolume());
//        float curVol = Float.parseFloat(previous.getVolume());
//
//        // pag pareho silang 0, hindi pa computable
//        if(isZero(prevVol) || isZero(curVol)) {
//            return;
//        }
//
//        // get the difference
//        int difference = (int) Math.floor(Math.abs(curVol - prevVol));
//
//
//        // both volumes are not zero, and their difference is 0, means that they are equal, so compute na yung flowrate
//        if(difference == 0) {
//            Log.d(TAG, "FLOWRATE IS COMPUTABLE NA");
//            Log.d(TAG, previous.toFinalString());
//            computeFlowrate(previous);
//        }
//    }
//
//    Record prevRecord, newRecord;

    private void resetMonitoring() {
        baseRecordIndex = 0;
        finalRecordIndex = 0;
        records.clear();
    }

    private boolean isStringZero(String str) {
        return isZero(toFloat(str));
    }

    private boolean isZero(float val) {
        return ((int) Math.floor(val)) == 0;
    }

    private float toFloat(String str) {
        if(str == null || str.isEmpty()) {
            return 0.0f;
        }
        try {
            str = str.replaceAll("[^\\d.]", "");
            return Float.parseFloat(str);
        }catch(Exception e) {
            return 0.0f;
        }
    }

    private float getVolumeDifference(Record a, Record b) {
        float volA  = toFloat(a.getVolume());
        float volB  = toFloat(b.getVolume());
        return volB - volA;
    }

    private float computeFlowrate(Record initialRecord, Record latestRecord) {
        try {
            // flowrate = change in volume / change in time
            float volDiff = toFloat(latestRecord.getVolume()) - toFloat(initialRecord.getVolume());

            // get record times in ms
            long initialTime = new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss", Locale.ENGLISH).parse(initialRecord.getRecordTime()).getTime();
            long latestTime = new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss", Locale.ENGLISH).parse(latestRecord.getRecordTime()).getTime();

            // compute time difference in ms and convert to seconds
            long timeDiff = Math.abs(latestTime - initialTime) / 1000;

            // return the computed flowrate
            return volDiff / timeDiff;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // returns the final record with computed flowrate
    private Record getFinalRecord(Record latestRecord, float flowrate) {
        Record finalRecord = new Record(UUID.randomUUID().toString(),
                PatientHolder.getPatient().getId(),
                PatientHolder.getPatient().getDevice(),
                latestRecord.getRecordTime(),
                String.valueOf(flowrate),
                latestRecord.getVolume(),
                latestRecord.getTurbidity(),
                latestRecord.getColor()
        );
        return finalRecord;
    }

    int baseRecordIndex = 0;
    int finalRecordIndex = 0;
    private void doFlowrateAlgorithm() {
        Log.d(TAG, "doFlowrateAlgorithm: Start");
        if(records.size() < 2) {
            return;
        }

        Log.d(TAG, "doFlowrateAlgorithm: For");

        for(int i = baseRecordIndex; i < records.size() - 1; i ++){
            int prevIndex = i;
            int curIndex = i + 1;
            Record prev = records.get(prevIndex);
            Record cur = records.get(curIndex);

            float volDiff = getVolumeDifference(prev, cur);

            // volume didnt change
            if(isZero(volDiff)){
                Log.d(TAG, "doFlowrateAlgorithm: volDiff = 0");
                Log.d(TAG, String.format("doFlowrateAlgorithm: initial: %d - latest: %d", baseRecordIndex, finalRecordIndex));

                // final volume has repeated already and we can now compute the flowrate
                if(finalRecordIndex > baseRecordIndex) {
                    Record initialRecord = records.get(baseRecordIndex);
                    Record latestRecord = records.get(prevIndex);

                    float flowrate = computeFlowrate(initialRecord, latestRecord);
                    Log.d(TAG, String.format("doFlowrateAlgorithm: Flowrate: %f", flowrate));

                    // get the final record with the flowrate and save it to db
                    Record finalRecord = getFinalRecord(latestRecord, flowrate);
                    recordViewModel.insert(finalRecord);
                    Log.d(TAG, "doFlowrateAlgorithm: finalRecord");
                    Log.d(TAG, finalRecord.toFinalString());
                    Toast.makeText(getContext(), "Flowrate Computed.. See Records", Toast.LENGTH_SHORT).show();
                }

                //update the base record index
                baseRecordIndex = curIndex;
            } else if(volDiff > 0) {
                Log.d(TAG, "doFlowrateAlgorithm: volDiff > 0");
                // volume is increasing, just update the final record with the higher volume
                finalRecordIndex = curIndex;
            }

        }
    }

    private void addLocalRecord(Record record) {
        records.add(record);
        addToTextView(record.toString());

        // Check if the volume changed
        doFlowrateAlgorithm();
    }

    private void addRecord(Record record) {
        // v1.1
        addLocalRecord(record);
        recordViewModel.insert(record);

//        prevRecord = newRecord;
//        newRecord = record;
//        checkFlowrateComputable(prevRecord, newRecord);

        // v1
//        records.add(record);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                liveRecords.setValue(records);
//            }
//        });
    }

    private void addLastRecord() {
        Record lastRecord = MonitoringHolder.getLastRecord();
        if(lastRecord != null) {
            addLocalRecord(lastRecord);
        }
    }


    private void addToTextView(String recordString) {
        // prepend to text view
//        textData.append(recordString);

        // prepend to text view
        textData.setText(recordString + textData.getText());
    }

    // v1.1
//    private void saveRecords(boolean showToast, boolean goToPatient) {
//        // save the records in memory to the database
//        for(Record r: records){
//            recordViewModel.insert(r);
//        }
//        Log.d(TAG, "Records Saved");
//        if(showToast) {
//            Toast.makeText(getContext(), "Records Saved", Toast.LENGTH_SHORT).show();
//        }
//        MonitoringHolder.setStarted(false);
//        MonitoringHolder.setEnded(true);
//        if(goToPatient) {
//            NavHostFragment.findNavController(MonitoringFragment.this)
//                    .navigate(R.id.action_Monitoring_to_CreatePatient);
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        loadOptions();
        addLastRecord();
        // v1.1: Future
//        if(MonitoringHolder.hasStarted()){
//            loadData();
//        }
//        getData();
    }

    @Override
    public void onPause() {
        super.onPause();
        dataGetter.cancel();
    }

    private void loadData() {
        Log.d(TAG, "LOAD DATA");
        recordViewModel.getRecordsByPatient(PatientHolder.getPatient().getId()).observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                for(Record r: records) {
                    addToTextView(r.toString());
                }
            }
        });
    }

    private void getData() {
        // create the scheduler and run the Request for data, every interval
        dataGetter = new Timer();
        Log.d(TAG, "Data Getter Initialized");
        dataGetter.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!MonitoringHolder.hasEnded()) {
                    if(!MonitoringHolder.hasStarted()) {
                        MonitoringFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Monitoring Started", Toast.LENGTH_SHORT).show();
                            }
                        });
                        MonitoringHolder.setStarted(true);
                    }

                    // create the request
                    queue.add(createRequest());
                    Log.d(TAG, "Request added to queue");
                }
                else {
                    Log.d(TAG, "Monitoring Cancelled");
                    cancel();
                }
            }

            @Override
            public boolean cancel() {
                queue.cancelAll(RequestTag);
                Log.d(TAG, "Requests cancelled");
                return super.cancel();
            }
        }, 0, option.getIntervalMs());
    }

    private JsonObjectRequest createRequest() {
        Log.d(TAG, "GET - " + option.getUrl());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, option.getUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Record record = jsonToRecord(response);
                if(record != null) {
                    Log.d(TAG, "Record is not null");
                    addRecord(record);
                } else {
                    Log.d(TAG, "Record is null");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "There was a problem retrieving the data", error);
                Toast.makeText(MonitoringFragment.this.getContext(), "There was a problem retrieving the data\nPlease recheck your options and connection", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(RequestTag);
        return request;
    }

    // TODELETE
    int cc = 0;
    String[] testVolumes = new String[] {"0", "0", "0", "10", "20", "30", "30", "30", "30", "30", "60", "70", "90", "120", "120", "120"};

    private Record jsonToRecord(JSONObject response) {
        Log.d(TAG, "Data Obtained...");
        Log.d(TAG, response.toString());

        String recordTime = new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss", Locale.ENGLISH).format(Calendar.getInstance().getTime());

        // parse the contents
        String[] data = response.optString(option.getDataProperty()).split(",");
        try {
            // v1
//            String flowRate = data[0];
            // v1.1
            String flowRate = Constants.noFlowRate;

            // TODELETE: Pag nagbago yung volume
//            String volume = cc < testVolumes.length ? testVolumes[cc++] : data[1];
            String volume = data[1];
            String turbidity = data[2];
            String R = data[3];
            String G = data[4];
            String B = data[5];
            // String color = String.format("R: %s G: %s B: %s", R, G, B);
            String color = data[6];
            Record record = new Record(UUID.randomUUID().toString(),
                    PatientHolder.getPatient().getId(),
                    PatientHolder.getPatient().getDevice(),
                    recordTime,
                    flowRate,
                    volume,
                    turbidity,
                    color
            );
            Log.d(TAG, record.toString());
            return record;
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "The data received was in an invalid format");
            Toast.makeText(MonitoringFragment.this.getContext(), "The data received was in an invalid format\nValid: FlowRate,Volume,Turbidity,R,G,B", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private void loadOptions() {
        Log.d(TAG, "Loading Options");
        optionViewModel.getOptions().observe(MonitoringFragment.this,
                new Observer<List<Option>>() {
                    @Override
                    public void onChanged(List<Option> options) {
                        Log.d(TAG, "Options Changed");

                        // Get the data if the options have been loaded
                        if(options.size() > 0) {
                            option = options.get(0);
                            getData();
                        }
                    }
                }
        );
    }

    private void showOptions() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_options, null);
        final EditText editUrl = view.findViewById(R.id.editUrl);
        final Spinner spinnerInterval = view.findViewById(R.id.spinnerInterval);

        // set the initial values
        editUrl.setText(option.getUrl());
        spinnerInterval.setSelection(option.getInterval());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Monitoring Options")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // update the options
                        option.setUrl(editUrl.getText().toString());
                        option.setInterval(spinnerInterval.getSelectedItemPosition());

                        // cancel getting the data, then update the option
                        if(dataGetter != null){
                            dataGetter.cancel();
                        }
                        optionViewModel.insert(option);

                        Log.d(TAG, "Monitoring Options Saved");

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "Monitoring Options Cancelled");
                        dialog.cancel();
                    }
                });
        builder.create().show();

    }
}
