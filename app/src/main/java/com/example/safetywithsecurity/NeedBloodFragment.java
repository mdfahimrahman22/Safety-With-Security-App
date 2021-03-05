package com.example.safetywithsecurity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.safetywithsecurity.Models.UserProfile;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

public class NeedBloodFragment extends Fragment {
    TextInputEditText dateFormatEditText,timePickerEditText,needBloodContactTextField;
    int year, day, month,hour,minute,amOrPm;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    UserProfile userProfile;
    private ProgressBar progressBar;
    private void getComponentIds(View view) {
        dateFormatEditText = view.findViewById(R.id.date_picker);
        timePickerEditText=view.findViewById(R.id.time_picker);
        needBloodContactTextField=view.findViewById(R.id.needBloodContactTextField);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_need_blood, container, false);
        getComponentIds(root);
        progressBar=root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        auth = FirebaseAuth.getInstance();
        getUserInfo(root);

        calendar = Calendar.getInstance();
        datePickerActivity();
        timePickerActivity();
        return root;
    }

    private void getUserInfo(View view) {
        FirebaseUser user=auth.getCurrentUser();
        String userId=user.getUid();

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
            TextView profileName=view.findViewById(R.id.profileName);
            ImageView profileImage=view.findViewById(R.id.profileImage);
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
                amOrPm=calendar.get(Calendar.AM_PM);
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