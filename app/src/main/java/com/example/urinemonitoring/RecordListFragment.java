package com.example.urinemonitoring;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.urinemonitoring.helpers.PatientHolder;
import com.example.urinemonitoring.models.Patient;
import com.example.urinemonitoring.models.Record;
import com.example.urinemonitoring.models.RecordViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RecordListFragment extends Fragment {
    private final String TAG = "RecordListFragment";
    private ListView listView;
    private EditText editDevice;
    private RecordViewModel recordViewModel;
    private Patient patient;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recordlist, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.recordList);

        // get the patient whose records belongs to
        patient = PatientHolder.getPatient();

        // update the patient's device number
        editDevice = view.findViewById(R.id.recordEditDevice);
        editDevice.setText(patient.getDevice());

        // observe data from db
        recordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        recordViewModel.getRecordsByPatient(patient.getId()).observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                // convert to record adapter
                RecordAdapter recordAdapter = new RecordAdapter(RecordListFragment.this.getContext(), records);
                listView.setAdapter(recordAdapter);
            }
        });

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RecordListFragment.this)
                        .navigate(R.id.action_RecordList_to_CreatePatient);
            }
        });

        view.findViewById(R.id.btnDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(RecordListFragment.this);
            }
        });
    }

    interface Downloadable {
        void run();
    }

    private void showDialog(final LifecycleOwner owner) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filename, null);
        TextView txtFileName = view.findViewById(R.id.txtFileName);

        // create the parent directories for each device
        final File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), String.format("Device_%s", patient.getDevice()));
        if(!path.exists()) {
            path.mkdirs();
        }
        txtFileName.setText(path.getPath());
        Log.d(TAG, "Download Path: " + path.getAbsolutePath());

        final Downloadable d = new Downloadable() {
            @Override
            public void run() {
                // Get the records from db and write to files
                recordViewModel.getRecordsByPatient(patient.getId()).observe(owner, new Observer<List<Record>>() {
                    @Override
                    public void onChanged(List<Record> records) {
                        Log.d("DownloadDialog", "Download: " + records.size() + " records");
                        try {
                            int i = 0;
                            for(Record r: records) {
                                // create the file per record
                                String filename = String.format("Record %d_%s.txt", i, r.getRecordTime());
                                File file = new File(path, filename);
                                if(!file.getParentFile().exists()) {
                                    file.getParentFile().mkdirs();
                                }
                                if(!file.exists()) {
                                    file.createNewFile();
                                    Log.d(TAG, "Download Record: " + filename);

                                    FileOutputStream stream = new FileOutputStream(file);
                                    stream.write(r.toString().getBytes());
                                    stream.close();
                                }
                                i++;
                            }
                            Toast.makeText(getContext(), "Records Downloaded", Toast.LENGTH_SHORT).show();
                        }
                        catch (IOException e) {
                            Log.e("Exception", "File write failed: " + e.toString());
                        }
                        recordViewModel.getRecordsByPatient(patient.getId()).removeObserver(this);
                    }
                });
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Download Records")
                .setView(view)
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Runtime permissions
                        ActivityResultLauncher<String> requestPermissionLauncher =
                                getActivity().registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                                    @Override
                                    public void onActivityResult(Boolean isGranted) {
                                        if (isGranted) {
                                            // Permission is granted. Continue the action or workflow in your
                                            // app.
                                            Log.d(TAG, "requestPermissionLauncher - PERMISSION_GRANTED - Downloading Records");
                                            d.run();
                                        } else {
                                            // Explain to the user that the feature is unavailable because the
                                            // features requires a permission that the user has denied. At the
                                            // same time, respect the user's decision. Don't link to system
                                            // settings in an effort to convince the user to change their
                                            // decision.
                                            Log.d(TAG, "requestPermissionLauncher - PERMISSION_DENIED - Feature Unavailable");
                                            Toast.makeText(RecordListFragment.this.getContext(), "The feature unavailable because you denied it", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

//                        // check for permissions
                        if (ContextCompat.checkSelfPermission(
                                RecordListFragment.this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            // You can use the API that requires the permission.
                            Log.d(TAG, "checkSelfPermission - PERMISSION_GRANTED - Downloading Records");
                            d.run();
                        }  else {
                            // You can directly ask for the permission.
                            // The registered ActivityResultCallback gets the result of this request.
                            Log.d(TAG, "checkSelfPermission - PERMISSION_DENIED - Asking for Permission");
                            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("DownloadDialog", "Cancel");
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }
}