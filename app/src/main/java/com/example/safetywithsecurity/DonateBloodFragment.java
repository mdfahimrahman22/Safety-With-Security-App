package com.example.safetywithsecurity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.safetywithsecurity.Models.MyListAdapter;
import com.example.safetywithsecurity.Models.NeedBlood;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DonateBloodFragment extends Fragment {

    DatabaseReference databaseReference;
    View view;
    String donateBloodUserEmail,donateBloodUserName;

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
        Bundle bundle = this.getArguments();
        donateBloodUserEmail = bundle.getString("donateBloodUserEmail");
        donateBloodUserName = bundle.getString("donateBloodUserName");
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
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, y");
        databaseReference = FirebaseDatabase.getInstance().getReference("NeedBlood");
        DatabaseReference deleteData = FirebaseDatabase.getInstance().getReference("NeedBlood");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    needBlood = new ArrayList<>();
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            NeedBlood needBloodObject = dataSnapshot.getValue(NeedBlood.class);
                            String sDate = needBloodObject.getDate();
                            Date needBloodDate = formatter.parse(sDate);
                            Date currentDate = formatter.parse(formatter.format(new Date()));
                            if (currentDate.compareTo(needBloodDate) > 0) {
                                deleteData.child(dataSnapshot.getKey()).removeValue();
                            } else {
                                needBlood.add(needBloodObject);
                            }
                        }

                        adapter = new MyListAdapter(getActivity(),view.getContext(), needBlood, donateBloodUserEmail,donateBloodUserName);
                        needBloodListRecylerView.setHasFixedSize(true);
                        needBloodListRecylerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                        needBloodListRecylerView.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(),
                                "Please try to help others who need blood.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException | ParseException npe) {
//                    Toast.makeText(view.getContext(),
//                            "Can't get data from server.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                if(needBlood.size()==0){
                    Toast.makeText(view.getContext(),
                            "No one needs blood at this moment.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}