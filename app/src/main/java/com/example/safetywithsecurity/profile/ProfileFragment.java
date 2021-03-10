package com.example.safetywithsecurity.profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safetywithsecurity.Models.UserProfile;
import com.example.safetywithsecurity.R;
import com.example.safetywithsecurity.Models.SpinnerAdapterText;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private UserProfile userProfile;
    private View view;
    private Button updateProfileBtn;
    private TextInputLayout profileNameEditText, bloodGroupEditText, phoneNumberEditText;
    private ImageView profilePicImageView;
    private TextView emailTextView;
    private CardView addProfileImage;
    private Uri imageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String profilePicImageUrl;
    SpinnerAdapterText adapter;
    private AutoCompleteTextView bloodGroupSpinner;

    private void getComponentIds() {
        profileNameEditText = view.findViewById(R.id.profileFullName);
        bloodGroupEditText = view.findViewById(R.id.bloodGroup);
        phoneNumberEditText = view.findViewById(R.id.phoneNumber);
        profilePicImageView = view.findViewById(R.id.profilePicImageView);
        emailTextView = view.findViewById(R.id.emailTextView);
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);
        addProfileImage = view.findViewById(R.id.addProfileImageCardView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getComponentIds();
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        initializeBloodGroupSpinner();
        getUserInfo();
//        initializeBloodGroupSpinner();
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadProfilePicture();
                } else {
                    updateProfileActivity();
                }
            }
        });
        addProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        return view;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profilePicImageView.setImageURI(imageUri);
        }
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadProfilePicture() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();
        StorageReference profilePicRef = storageReference.child("profile_pics");
        String imageDest = System.currentTimeMillis() + "." + getFileExtention(imageUri);
        profilePicRef.child(imageDest).putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setProgress(0);
                            }
                        }, 5000);

                        profilePicRef.child(imageDest).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                Log.d("profile","Profile pic link:"+uri.toString());
                                updateProfileActivity(uri.toString());
                            }
                        });
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Progress: " + (int) progressPercent + " %");
            }
        });
    }

    String fullName, bloodGroup, phoneNumber, profilePic, userEmail;

    private void getUpdatedValues() {
        fullName = profileNameEditText.getEditText().getText().toString();
        bloodGroup = bloodGroupEditText.getEditText().getText().toString();
        phoneNumber = phoneNumberEditText.getEditText().getText().toString();
        profilePic = userProfile.getProfilePic();
        userEmail = userProfile.getEmail();
    }

    private void updateProfileActivity() {
        progressBar.setVisibility(View.VISIBLE);
        getUpdatedValues();
        UserProfile updatedProfile = new UserProfile(fullName, userEmail, phoneNumber, profilePic, bloodGroup);
        databaseReference.setValue(updatedProfile);
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity().getApplicationContext(), "User Profile Updated", Toast.LENGTH_SHORT).show();
    }

    private void updateProfileActivity(String imageUrl) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            getUpdatedValues();
            UserProfile updatedProfile = new UserProfile(fullName, userEmail, phoneNumber, imageUrl, bloodGroup);
            databaseReference.setValue(updatedProfile);

            Toast.makeText(getActivity().getApplicationContext(), "User Profile Updated", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {

        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void getUserInfo() {
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(UserProfile.class);
                setProfileInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setProfileInfo() {
        try {
            profileNameEditText.getEditText().setText(userProfile.getFullName());
            String userProfileBloodGruop = userProfile.getBloodGruop();
            if (userProfileBloodGruop != null) {
                int spinnerPosition = adapter.getPosition(userProfileBloodGruop);
                try {
                    bloodGroupSpinner.setText(bloodGroupSpinner.getAdapter().getItem(spinnerPosition).toString(), false);
                } catch (Exception e) {
                }
            }
            phoneNumberEditText.getEditText().setText(userProfile.getPhone());
            Picasso.get().load(userProfile.getProfilePic()).into(profilePicImageView);
            emailTextView.setText(userProfile.getEmail());
            progressBar.setVisibility(View.INVISIBLE);
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "Null Pointer Exception.", Toast.LENGTH_SHORT).show();
        }

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}