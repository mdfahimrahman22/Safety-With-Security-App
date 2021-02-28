package com.example.safetywithsecurity.ui;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.safetywithsecurity.DashboardMain;
import com.example.safetywithsecurity.R;

public class HomeFragment extends Fragment implements LocationListener {

    private CardView myLocationButton, emergencyCallButton, emergencyMessageButton, securityCallButton, policeStationsNearMeButton, callAmbulanceButton, bloodDonateButton, needBloodButton;
    LocationManager locationManager;
    double myLatitude, myLongitude;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        getComponentIds(root);
        checkLocationPermission();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
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
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_my_location);
            }
        });
        callAmbulanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAmbulance();
            }
        });

        return root;
    }

    private void callAmbulance() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "999", null));
        startActivity(intent);
    }

    private void sendEmergencyMessage() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", new String("01615990017"));
        smsIntent.putExtra("sms_body", "I am in danger. Please find me in this location: https://www.google.com/maps/?q=" + myLatitude + "," + myLongitude);

        try {
            startActivity(smsIntent);
            getActivity().finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }


    int LOCATION_REQUEST_CODE = 100;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                //permission not granted
            }
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
        callAmbulanceButton = view.findViewById(R.id.callAmbulanceButton);
        emergencyMessageButton = view.findViewById(R.id.emergencyMessageButton);
        myLocationButton = view.findViewById(R.id.myLocationButton);
        emergencyCallButton = view.findViewById(R.id.emergencyCallButton);
        securityCallButton = view.findViewById(R.id.securityCallButton);
        policeStationsNearMeButton = view.findViewById(R.id.policeStationsNearMeButton);
        bloodDonateButton = view.findViewById(R.id.bloodDonateButton);
        needBloodButton = view.findViewById(R.id.needBloodButton);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}