package com.example.safetywithsecurity.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.safetywithsecurity.DashboardMain;
import com.example.safetywithsecurity.R;

public class HomeFragment extends Fragment {

    private CardView myLocationButton, emergencyCallButton,emergencyMessageButton, securityCallButton, policeStationsNearMeButton, bloodDonateButton, needBloodButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        getComponentIds(root);
        policeStationsNearMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                policeStationsNearMe();
            }
        });
        securityCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                securityCall();
            }
        });
        emergencyCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergencyCall();
            }
        });
        emergencyMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmergencyMessage();
            }
        });

        return root;
    }

    private void sendEmergencyMessage() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String ("01615990017"));
        smsIntent.putExtra("sms_body"  , "I am in danger. Please find me in this location: https://www.google.com/maps/?q=24.757656,90.390517");

        try {
            startActivity(smsIntent);
            getActivity().finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    private void policeStationsNearMe() {
        Uri uri = Uri.parse("geo:0, 0?q=Police Stations Near Me");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private void securityCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "999", null));
        startActivity(intent);
    }

    private void emergencyCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "01615990017", null));
        startActivity(intent);
    }

    private void getComponentIds(View view) {
        emergencyMessageButton=view.findViewById(R.id.emergencyMessageButton);
        myLocationButton = view.findViewById(R.id.myLocationButton);
        emergencyCallButton = view.findViewById(R.id.emergencyCallButton);
        securityCallButton = view.findViewById(R.id.securityCallButton);
        policeStationsNearMeButton = view.findViewById(R.id.policeStationsNearMeButton);
        bloodDonateButton = view.findViewById(R.id.bloodDonateButton);
        needBloodButton = view.findViewById(R.id.needBloodButton);
    }
}