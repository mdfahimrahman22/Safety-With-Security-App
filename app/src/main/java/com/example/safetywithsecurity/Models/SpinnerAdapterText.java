package com.example.safetywithsecurity.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.safetywithsecurity.R;

import java.util.ArrayList;

public class SpinnerAdapterText extends ArrayAdapter<String> {
    public SpinnerAdapterText(Context context, ArrayList<String> spinnerList) {
        super(context, 0, spinnerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_layout, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.spinner_layout_text_view);
        textViewName.setTextColor(getContext().getResources().getColor(R.color.textColor));
        String currentItem = getItem(position);
        if (currentItem != null) {
            textViewName.setText(currentItem);

        }
        return convertView;
    }

}
