package com.example.urinemonitoring;

import android.os.Bundle;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.urinemonitoring.helpers.MonitoringHolder;
import com.example.urinemonitoring.helpers.PatientHolder;
import com.example.urinemonitoring.models.Patient;
import com.example.urinemonitoring.models.PatientViewModel;

import java.util.UUID;


public class CreatePatientFragment extends Fragment {
    private final String TAG = "CreatePatientFragment";

    EditText editName, editAge, editDevice;
    Spinner spinnerGender, spinnerStatus;

    private PatientViewModel patientViewModel;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_createpatient, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editName = view.findViewById(R.id.editName);
        editAge = view.findViewById(R.id.editAge);
        editDevice = view.findViewById(R.id.editDevice);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);

        patientViewModel = ViewModelProviders.of(this).get(PatientViewModel.class);

        // if global patient is not empty and saved, we are viewing patient info else we get the current values
        Patient patientToView = !PatientHolder.isEmpty() && PatientHolder.isSaved() ? PatientHolder.getPatient() : PatientHolder.getMutablePatient();
        if(patientToView != null) {
            editName.setText(patientToView.getName());
            editAge.setText(Integer.toString(patientToView.getAge()));
            editDevice.setText(patientToView.getDevice());
            spinnerGender.setSelection(((ArrayAdapter)spinnerGender.getAdapter()).getPosition(patientToView.getGender()));
            spinnerStatus.setSelection(((ArrayAdapter)spinnerStatus.getAdapter()).getPosition(patientToView.getStatus()));
        }

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // handle empty fields
                if(hasEmpty()) {
                    Toast.makeText(getContext(), "Please fill up all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Patient patient = getFieldsAsPatient();
                // if not yet saved, we insert, else we update
                if(!PatientHolder.isSaved()) {
                    patientViewModel.insert(patient);
                    Log.d(TAG, "Insert: " + patient.getName());
                }else if(hasChanged()){
                    // get the id of the patient we are updating
                    String id = PatientHolder.getPatient().getId();
                    patient.setId(id);
                    patientViewModel.update(patient);
                    Log.d(TAG, "Update" + patient.getName());
                }

                // set the global patient data
                PatientHolder.setPatient(patient);
                PatientHolder.setSaved(true);

                NavHostFragment.findNavController(CreatePatientFragment.this)
                        .navigate(R.id.action_CreatePatient_to_Saved);
            }
        });

        view.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CreatePatientFragment.this)
                        .navigate(R.id.action_CreatePatient_to_Delete);
            }
        });

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // not saved and empty lahat, can just go back
                if(!PatientHolder.isSaved() && isAllEmpty()) {
                    NavHostFragment.findNavController(CreatePatientFragment.this)
                            .navigate(R.id.action_CreatePatient_to_PatientList);
                }else {
                    tryNavigateTo(R.id.action_CreatePatient_to_PatientList);
                }
                MonitoringHolder.setStarted(false);
                MonitoringHolder.setEnded(true);
            }
        });

        view.findViewById(R.id.btnAccess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryNavigateTo(R.id.action_CreatePatient_to_RecordList);
            }
        });

        view.findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryNavigateTo(R.id.action_CreatePatient_to_Monitoring);
            }
        });
    }

    private void tryNavigateTo(int destination) {
        // Before navigating to anywhere, we need to track the current values in the textfields
        Patient currentValues = getFieldsAsPatient();
        PatientHolder.setMutablePatient(currentValues);

        // New Patient
        if(!PatientHolder.isSaved()) {
            if(!hasEmpty()) {
                // complete fields, we saved the patient to db
                NavHostFragment.findNavController(CreatePatientFragment.this)
                        .navigate(R.id.action_CreatePatient_to_NotYetSaved);
            }
        } else {
            // Patient is already saved, but can edit
            if(!hasEmpty()) {
                if(hasChanged()) {
                    // complete fields but patient info has changed, should we save?
                    NavHostFragment.findNavController(CreatePatientFragment.this)
                            .navigate(R.id.action_CreatePatient_to_NotYetSaved);
                } else {
                    // complete fields and info DID NOT change
                    NavHostFragment.findNavController(CreatePatientFragment.this)
                            .navigate(destination);
                }
            }
        }
    }

    private Patient getFieldsAsPatient() {
        String age = editAge.getText().toString();

        Patient patient = new Patient(
                UUID.randomUUID().toString(),
                editName.getText().toString(),
                Integer.parseInt(TextUtils.isEmpty(age) ? "0" : age),
                spinnerGender.getSelectedItem().toString(),
                spinnerStatus.getSelectedItem().toString(),
                editDevice.getText().toString()
        );
        return patient;
    }

    private boolean isAllEmpty() {
        boolean emptyName = TextUtils.isEmpty(editName.getText().toString());
        boolean emptyAge = TextUtils.isEmpty(editAge.getText().toString());
        boolean emptyDevice = TextUtils.isEmpty(editDevice.getText().toString());
        return emptyName && emptyAge & emptyDevice;
    }

    private boolean hasEmpty() {
        boolean emptyName = TextUtils.isEmpty(editName.getText().toString());
        boolean emptyAge = TextUtils.isEmpty(editAge.getText().toString());
        boolean emptyDevice = TextUtils.isEmpty(editDevice.getText().toString());
        boolean anyEmpty = emptyName || emptyAge || emptyDevice;
        if(anyEmpty){
            Toast.makeText(getContext(), "Please fill up all fields", Toast.LENGTH_SHORT).show();
        }
        return anyEmpty;
    }

    private boolean hasChanged() {
        // check if current edit text fields' values are not equal to values saved.
        boolean nameChanged = !editName.getText().toString().equals(PatientHolder.getPatient().getName());
        boolean ageChanged = !editAge.getText().toString().equals(Integer.toString(PatientHolder.getPatient().getAge()));
        boolean genderChanged = !spinnerGender.getSelectedItem().toString().equals(PatientHolder.getPatient().getGender());
        boolean statusChanged = !spinnerStatus.getSelectedItem().toString().equals(PatientHolder.getPatient().getStatus());
        boolean deviceChanged = !editDevice.getText().toString().equals(PatientHolder.getPatient().getDevice());

        return nameChanged || ageChanged || genderChanged || statusChanged || deviceChanged;
    }
}