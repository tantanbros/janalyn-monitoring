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


public class DeleteFragment extends Fragment {
    private final String TAG = "DeleteFragment";
    private PatientViewModel patientViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patientViewModel = ViewModelProviders.of(this).get(PatientViewModel.class);

        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PatientHolder.isSaved()) {
                    Patient patientToDelete = PatientHolder.getPatient();
                    patientViewModel.delete(patientToDelete);
                    Log.d(TAG, "Deleted: " + patientToDelete.getName());
                } else {
                    Log.d(TAG, "Nothing to delete");
                }

                NavHostFragment.findNavController(DeleteFragment.this)
                        .navigate(R.id.action_Delete_to_Deleted);
            }
        });

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(DeleteFragment.this)
                        .navigate(R.id.action_Delete_to_CreatePatient);
            }
        });
    }
}