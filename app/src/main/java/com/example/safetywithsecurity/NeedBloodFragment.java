package com.example.safetywithsecurity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.safetywithsecurity.Models.NeedBlood;
import com.example.safetywithsecurity.Models.UserProfile;
import com.example.safetywithsecurity.Models.SpinnerAdapterText;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NeedBloodFragment extends Fragment {
    private TextInputEditText needBloodLocationTextField,dateFormatEditText,timePickerEditText,needBloodContactTextField,needBloodShortNoteTextField;
    TextInputLayout bloodGroup;
    private int year, day, month,hour,minute;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private UserProfile userProfile;
    private ProgressBar progressBar;
    private Button postNeedBlood;
    private TextView profileName;
    private ImageView profileImage;
    private SpinnerAdapterText adapter;
    private AutoCompleteTextView bloodGroupSpinner;
    String userId;
    View view;
    private void getComponentIds() {
        dateFormatEditText = view.findViewById(R.id.date_picker);
        timePickerEditText=view.findViewById(R.id.time_picker);
        needBloodContactTextField=view.findViewById(R.id.needBloodContactTextField);
        postNeedBlood=view.findViewById(R.id.postNeedBlood);
        needBloodShortNoteTextField=view.findViewById(R.id.needBloodShortNoteTextField);
        bloodGroup=view.findViewById(R.id.bloodGroup);
        needBloodLocationTextField=view.findViewById(R.id.needBloodLocationTextField);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_need_blood, container, false);
        getComponentIds();
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        initializeBloodGroupSpinner();
        auth = FirebaseAuth.getInstance();
        getUserInfo();

        calendar = Calendar.getInstance();
        datePickerActivity();
        timePickerActivity();
        postNeedBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needBloodActivity();
            }
        });

        return view;
    }

    private void needBloodActivity() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("NeedBlood/" + userId);
        String userName=userProfile.getFullName();
        String userEmail=userProfile.getEmail();
        String userImg=userProfile.getProfilePic();
        String msg=needBloodShortNoteTextField.getText().toString();
        String bloodGrp=bloodGroup.getEditText().getText().toString();
        String date=dateFormatEditText.getText().toString();
        String time=timePickerEditText.getText().toString();
        String phoneNum=needBloodContactTextField.getText().toString();
        String location=needBloodLocationTextField.getText().toString();
        NeedBlood needBlood=new NeedBlood(userName,userEmail,bloodGrp,phoneNum,userImg,msg,time,date,location);
        databaseReference.setValue(needBlood);
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity().getApplicationContext(),
                "Your Post has been Uploaded. Stay calm. Someone will help you soon.", Toast.LENGTH_LONG).show();

    }


    private void initializeBloodGroupSpinner() {
        ArrayList<String> bloodGroup = new ArrayList<>();
        bloodGroup.add("A+");
        bloodGroup.add("A-");
        bloodGroup.add("B+");
        bloodGroup.add("B-");
        bloodGroup.add("AB+");
        bloodGroup.add("AB-");
        bloodGroup.add("O+");
        bloodGroup.add("O-");
        adapter = new SpinnerAdapterText(getActivity().getApplicationContext(), bloodGroup);
        bloodGroupSpinner = view.findViewById(R.id.filledExposedDropdown);
        bloodGroupSpinner.setAdapter(adapter);
    }
    private void getUserInfo() {
        FirebaseUser user=auth.getCurrentUser();
        userId=user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    userProfile=dataSnapshot.getValue(UserProfile.class);
                    progressBar.setVisibility(View.INVISIBLE);
                    setProfileInfo(view);
                } catch (NullPointerException e) {
                    userProfile=new UserProfile("User's Full Name","user@gmail.com","017********","https://www.flaticon.com/svg/vstatic/svg/1077/1077012.svg?token=exp=1614958650~hmac=96876d86c5add1816a45a5f8788fd528","B+");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setProfileInfo(View view) {
        try {
            profileName=view.findViewById(R.id.profileName);
            profileImage=view.findViewById(R.id.profileImage);
            String userProfileBloodGruop = userProfile.getBloodGruop();
            if (userProfileBloodGruop != null) {
                int spinnerPosition = adapter.getPosition(userProfileBloodGruop);
                try {
                    bloodGroupSpinner.setText(bloodGroupSpinner.getAdapter().getItem(spinnerPosition).toString(), false);
                }catch (Exception e){
                }
            }
            needBloodContactTextField.setText(userProfile.getPhone());
            String nameAndBloodGroup=userProfile.getFullName();
            if(userProfile.getBloodGruop()!=null){
                nameAndBloodGroup+="\n"+userProfile.getBloodGruop();
            }
            needBloodContactTextField.setText(userProfile.getPhone());
            profileName.setText(nameAndBloodGroup);
            Picasso.get().load(userProfile.getProfilePic()).into(profileImage);

        }catch (NullPointerException e){
            Toast.makeText(getActivity().getApplicationContext(),
                    "Can't fetch user data properly.", Toast.LENGTH_SHORT).show();
        }

    }

    private void datePickerActivity() {
        dateFormatEditText.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
        dateFormatEditText.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    SimpleDateFormat myDateFormat = new SimpleDateFormat("MMM d, y");
                    year-=1900;
                    Date date=new Date(year,monthOfYear,dayOfMonth);
                    dateFormatEditText.setText(myDateFormat.format(date));
                }
            }, year, month, day);
            datePickerDialog.show();
        }
    });
    }

    private void timePickerActivity() {
        timePickerEditText.setText(SimpleDateFormat.getTimeInstance().format(calendar.getTime()));
        timePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour=calendar.get(Calendar.HOUR_OF_DAY);
                minute=calendar.get(Calendar.MINUTE);
                timePickerDialog=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM ;
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            hourOfDay=hourOfDay%12;
                            AM_PM = "PM";
                        }

                        timePickerEditText.setText(hourOfDay+":"+minute+":00 "+AM_PM);
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });

    }


}