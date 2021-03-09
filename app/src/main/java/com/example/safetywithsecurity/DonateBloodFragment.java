package com.example.safetywithsecurity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.safetywithsecurity.Models.MyListAdapter;
import com.example.safetywithsecurity.Models.NeedBlood;
import com.example.safetywithsecurity.Models.UserProfile;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class DonateBloodFragment extends Fragment {
    ListView list;
    DatabaseReference databaseReference;
    View view;

    String[] maintitle = {
            "Title 1", "Title 2",
            "Title 3", "Title 4",
            "Title 5",
    };

    String[] subtitle = {
            "Sub Title 1", "Sub Title 2",
            "Sub Title 3", "Sub Title 4",
            "Sub Title 5",
    };

    Integer[] imgid = {
            R.drawable.group_icon, R.drawable.home_icon,
            R.drawable.ic_blood_donation, R.drawable.ic_ambulance_icon,
            R.drawable.ic_blood_help,
    };

    public DonateBloodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_donate_blood, container, false);

        showListOfPeopleWhoNeedBlood();
        return view;
    }

    List<NeedBlood> needBlood;
    MyListAdapter adapter;
    RecyclerView needBloodListRecylerView;

    private void showListOfPeopleWhoNeedBlood() {
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        needBloodListRecylerView = view.findViewById(R.id.needBloodListRecylerView);

        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("NeedBlood");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    needBlood = new ArrayList<>();
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            NeedBlood needBloodObject = dataSnapshot.getValue(NeedBlood.class);
                            needBlood.add(needBloodObject);
                        }

                        adapter = new MyListAdapter(getActivity(),getActivity().getApplicationContext(), needBlood);
                        needBloodListRecylerView.setHasFixedSize(true);
                        needBloodListRecylerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        needBloodListRecylerView.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(),
                                "Please try to help others who need blood.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException npe) {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}