package com.example.urinemonitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.urinemonitoring.models.Record;

import java.util.List;

public class RecordAdapter extends ArrayAdapter<Record> {
    public RecordAdapter(Context context, List<Record> records){
        super(context, 0, records);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Record record = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView text1 = convertView.findViewById(android.R.id.text1);
        String recordName = String.format("Record %d_%s", position, record.getRecordTime());
        text1.setText(recordName);
        return convertView;
    }
}
