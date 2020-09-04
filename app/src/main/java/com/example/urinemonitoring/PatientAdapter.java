package com.example.urinemonitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.urinemonitoring.models.Patient;

import java.util.List;

public class PatientAdapter extends ArrayAdapter<Patient> {
    public PatientAdapter(Context context, List<Patient> patients){
        super(context, 0, patients);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Patient patient = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView text1 = convertView.findViewById(android.R.id.text1);
        text1.setText(patient.getName());
        return convertView;
    }
}
