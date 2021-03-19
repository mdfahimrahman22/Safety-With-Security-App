package com.example.safetywithsecurity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
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
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safetywithsecurity.DashboardMain;
import com.example.safetywithsecurity.Models.AmbulanceDetails;
import com.example.safetywithsecurity.Models.AmbulanceListAdapter;
import com.example.safetywithsecurity.Models.UserProfile;
import com.example.safetywithsecurity.R;
import com.example.safetywithsecurity.SettingsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements LocationListener {
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private CardView myLocationButton, hospitalButton, emergencyCallButton, emergencyMessageButton, securityCallButton, policeStationsNearMeButton, callAmbulanceButton, bloodDonateButton, needBloodButton;
    LocationManager locationManager;
    double myLatitude, myLongitude;
    Location location;
    boolean locationPermissionGranted;
    UserProfile userProfile;
    ProgressBar progressBar;
    FirebaseUser user;
    String userId;
    View view;

    private void getComponentIds(View view) {
        bloodDonateButton = view.findViewById(R.id.bloodDonateButton);
        needBloodButton = view.findViewById(R.id.needBloodButton);
        callAmbulanceButton = view.findViewById(R.id.callAmbulanceButton);
        emergencyMessageButton = view.findViewById(R.id.emergencyMessageButton);
        myLocationButton = view.findViewById(R.id.myLocationButton);
        emergencyCallButton = view.findViewById(R.id.emergencyCallButton);
        securityCallButton = view.findViewById(R.id.securityCallButton);
        policeStationsNearMeButton = view.findViewById(R.id.policeStationsNearMeButton);
        bloodDonateButton = view.findViewById(R.id.bloodDonateButton);
        needBloodButton = view.findViewById(R.id.needBloodButton);
        hospitalButton = view.findViewById(R.id.myHospitalButton);
        progressBar = view.findViewById(R.id.progressBar);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        getComponentIds(view);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
        getUserInfo();
        FirebaseUser user = auth.getCurrentUser();
        checkLocationPermission();
        if (locationPermissionGranted) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        }
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
        hospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospitalsNearMe();
            }
        });
        needBloodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_need_blood);
            }
        });
        bloodDonateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userProfile != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("donateBloodUserEmail", userProfile.getEmail());
                    bundle.putString("donateBloodUserName", userProfile.getFullName());
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.nav_donate_blood, bundle);
                }
            }
        });

        return view;
    }

    private void getUserInfo() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(UserProfile.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void hospitalsNearMe() {
        Uri uri = Uri.parse("google.navigation:q=Hospitals Near Me");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    RecyclerView ambulanceListRecylerView;
    AmbulanceListAdapter ambulanceListAdapter;
    List<AmbulanceDetails> ambulanceDetailsList = new ArrayList<>();
    String jsonString="[{\"ambulanceServiceName\":\"Ambulance 1\",\"currentLocation\":\"Location 1\",\"arrivalTime\":\"45min\",\"phoneNum\":\"01790273456\"},{\"ambulanceServiceName\":\"Ambulance 2\",\"currentLocation\":\"Location 2\",\"arrivalTime\":\"1hour\",\"phoneNum\":\"01990223456\"}]";

    private void callAmbulance() {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View ambulanceListPopupView = getLayoutInflater().inflate(R.layout.ambulance_list_popup_view, null);
        dialogBuilder.setView(ambulanceListPopupView);
        dialog = dialogBuilder.create();
        progressBar = ambulanceListPopupView.findViewById(R.id.progressBar);
        ambulanceListRecylerView = ambulanceListPopupView.findViewById(R.id.ambulanceListRecylerView);

        Gson gson = new Gson();
        Type type = new TypeToken<List<AmbulanceDetails>>(){}.getType();
        ambulanceDetailsList = gson.fromJson(jsonString, type);
//        ambulanceDetailsList.add(new AmbulanceDetails("Name1", "Location1", "45min", "01790273456"));
//        ambulanceDetailsList.add(new AmbulanceDetails("Name2", "Location2", "1hour", "01790273456"));

        ambulanceListAdapter = new AmbulanceListAdapter(ambulanceDetailsList);
        ambulanceListRecylerView.setHasFixedSize(true);
        ambulanceListRecylerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        ambulanceListRecylerView.setAdapter(ambulanceListAdapter);

        dialog.show();
    }

    private void sendEmergencyMessage() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");

        SharedPreferences sp;
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String contactNumber = sp.getString("emergencyContact", "999");

        String relation = sp.getString("contactRelation", "Dear");

        smsIntent.putExtra("address", new String(contactNumber));
        smsIntent.putExtra("sms_body", "Hi " + relation + "\nI am in danger. Please find me in this location: https://www.google.com/maps/?q=" + myLatitude + "," + myLongitude);

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
        } else {
            locationPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                return;
            } else {
                locationPermissionGranted = false;
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String securityNumber = sharedPreferences.getString("securityNumber", "999");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", securityNumber, null));
        startActivity(intent);
    }

    private void emergencyCall() {
        SharedPreferences sp;
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String contactNumber = sp.getString("emergencyContact", "999");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contactNumber, null));
        startActivity(intent);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            if (locationPermissionGranted) {
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
            } else {
                checkLocationPermission();
            }
        } catch (NullPointerException npe) {

        }

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