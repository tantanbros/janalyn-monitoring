package com.example.urinemonitoring;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.urinemonitoring.helpers.MonitoringHolder;


public class ConfirmEndFragment extends Fragment {
    private final String TAG = "ConfirmEndFragment";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmend, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // end monitoring
                MonitoringHolder.setEnded(true);
                MonitoringHolder.setStarted(false);
                Toast.makeText(getContext(), "Records Saved", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(ConfirmEndFragment.this)
                        .navigate(R.id.action_ConfirmEnd_to_Monitoring);
            }
        });

    }

}