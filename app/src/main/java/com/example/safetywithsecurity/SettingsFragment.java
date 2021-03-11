package com.example.safetywithsecurity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        ListPreference listPreference=findPreference("appTheme");
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                switch (newValue.toString()){
                    case "light":
                        listPreference.setValue("light");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case "dark":
                        listPreference.setValue("dark");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                    case "system_default":
                        listPreference.setValue("system_default");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        break;
                }
                return false;
            }
        });

    }

//    private void setSettings() {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
//        sp.edit().putString("contactRelation",userSettings.getEmergencyContactRelation());
//        sp.edit().putString("emergencyContact",userSettings.getEmergencyContact());
//        sp.edit().putString("securityNumber",userSettings.getNationalSecurityNumber());
//        sp.edit().putString("ambulanceNumber",userSettings.getAmbulanceNumber());
//        sp.edit().putBoolean("bloodNeedNotify",userSettings.isBloodNeedNotification());
//    }
//
//    private void loadSettings() {
//        FirebaseUser user = auth.getCurrentUser();
//        String userId = user.getUid();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + userId + "/UserSettings");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userSettings = snapshot.getValue(UserSettings.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

}