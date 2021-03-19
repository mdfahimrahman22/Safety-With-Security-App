package com.example.safetywithsecurity.Models;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safetywithsecurity.R;

import java.util.List;

public class AmbulanceListAdapter extends RecyclerView.Adapter<AmbulanceListAdapter.AmbulanceListViewAdapter> {
    private List<AmbulanceDetails> ambulanceDetailsList;

    public AmbulanceListAdapter(List<AmbulanceDetails> ambulanceDetailsList) {
        this.ambulanceDetailsList = ambulanceDetailsList;
    }

    public AmbulanceListAdapter() {
    }

    @NonNull
    @Override
    public AmbulanceListViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_ambulance_service, parent, false);
        return new AmbulanceListViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmbulanceListViewAdapter holder, int position) {
        holder.ambulanceServiceName.setText(ambulanceDetailsList.get(position).getAmbulanceServiceName());
        holder.currentLocation.setText(ambulanceDetailsList.get(position).getCurrentLocation());
        holder.arrivalTime.setText(ambulanceDetailsList.get(position).getArrivalTime());

        holder.callAmbulanceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ambulanceDetailsList.get(position).getPhoneNum(), null));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ambulanceDetailsList.size();
    }

    public class AmbulanceListViewAdapter extends RecyclerView.ViewHolder {
        TextView ambulanceServiceName, currentLocation, arrivalTime;
        CardView callAmbulanceCardView;

        public AmbulanceListViewAdapter(@NonNull View itemView) {
            super(itemView);
            ambulanceServiceName = itemView.findViewById(R.id.ambulanceServiceName);
            currentLocation = itemView.findViewById(R.id.currentLocation);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
            callAmbulanceCardView = itemView.findViewById(R.id.callAmbulanceCardView);
        }
    }
}
