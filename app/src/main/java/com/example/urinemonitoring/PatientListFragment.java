package com.example.urinemonitoring;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.urinemonitoring.helpers.PatientHolder;
import com.example.urinemonitoring.models.Patient;
import com.example.urinemonitoring.models.PatientViewModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PatientListFragment extends Fragment {
    private final String TAG = "PatientListFragment";
    private ListView listView;
    private PatientViewModel patientViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patientlist, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.listView);

        // observe data from db
        patientViewModel = ViewModelProviders.of(this).get(PatientViewModel.class);
        patientViewModel.getPatients().observe(this, new Observer<List<Patient>>() {
            @Override
            public void onChanged(List<Patient> patients) {
                // convert to patient adapter so we can display in list view
                PatientAdapter patientAdapter = new PatientAdapter(PatientListFragment.this.getContext(), patients);
                listView.setAdapter(patientAdapter);
            }
        });

        view.findViewById(R.id.addPatient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // clear the global patient data
                PatientHolder.clearPatient();
                PatientHolder.setSaved(false);
                PatientHolder.clearMutablePatient();

                NavHostFragment.findNavController(PatientListFragment.this)
                        .navigate(R.id.action_PatientList_to_CreatePatient);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Patient patient = (Patient) adapterView.getItemAtPosition(i);
                Log.d(TAG, patient.getName() + patient.getGender());

                // set the clicked patient to the global holder
                PatientHolder.setPatient(patient);
                PatientHolder.setSaved(true);

                // navigate to the create patient fragment
                NavHostFragment.findNavController(PatientListFragment.this)
                        .navigate(R.id.action_PatientList_to_CreatePatient);
            }
        });
    }
}