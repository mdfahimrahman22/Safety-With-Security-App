package com.example.safetywithsecurity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.safetywithsecurity.Models.UserProfile;
import com.example.safetywithsecurity.Models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends PreferenceFragmentCompat {
    EditTextPreference emergencyNumber;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    UserSettings userSettings;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        auth=FirebaseAuth.getInstance();
//        loadSettings();
//        setSettings();
    }

    private void setSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        sp.edit().putString("contactRelation",userSettings.getEmergencyContactRelation());
        sp.edit().putString("emergencyContact",userSettings.getEmergencyContact());
        sp.edit().putString("securityNumber",userSettings.getNationalSecurityNumber());
        sp.edit().putString("ambulanceNumber",userSettings.getAmbulanceNumber());
        sp.edit().putBoolean("bloodNeedNotify",userSettings.isBloodNeedNotification());
    }

    private void loadSettings() {
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + userId + "/UserSettings");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userSettings = snapshot.getValue(UserSettings.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}