package com.example.urinemonitoring;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.urinemonitoring.helpers.PatientHolder;
import com.example.urinemonitoring.models.Patient;
import com.example.urinemonitoring.models.PatientViewModel;

public class NotYetSavedFragment extends Fragment {
    private final String TAG = "NotYetSavedFragment";
    private PatientViewModel patientViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notyetsaved, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patientViewModel = ViewModelProviders.of(this).get(PatientViewModel.class);


        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save the current values then clear it
                Patient mutablePatient = PatientHolder.getMutablePatient();

                // if not yet saved, we insert, else we update
                if(!PatientHolder.isSaved()) {
                    patientViewModel.insert(mutablePatient);
                    Log.d(TAG, "Insert: " + mutablePatient.getName());
                }else {
                    // get the id of the patient we are updating
                    String id = PatientHolder.getPatient().getId();
                    mutablePatient.setId(id);
                    patientViewModel.update(mutablePatient);
                    Log.d(TAG, "Update" + mutablePatient.getName());
                }

                // set the global patient data
                PatientHolder.setPatient(mutablePatient);
                PatientHolder.setSaved(true);
                PatientHolder.clearMutablePatient();

                NavHostFragment.findNavController(NotYetSavedFragment.this)
                        .navigate(R.id.action_NotYetSaved_to_Saved);
            }
        });

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NotYetSavedFragment.this)
                        .navigate(R.id.action_NotYetSaved_to_CreatePatient);
            }
        });
    }
}
