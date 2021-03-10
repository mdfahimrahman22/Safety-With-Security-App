
package com.example.safetywithsecurity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safetywithsecurity.Models.NeedBlood;
import com.example.safetywithsecurity.Models.UserProfile;
import com.example.safetywithsecurity.profile.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

public class DashboardMain extends AppCompatActivity implements LocationListener {

    public static UserProfile userProfile;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    NavController navController;

    double myLatitude, myLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            Drawable drawable = getResources().getDrawable(R.color.theme_color);
            actionBar.setBackgroundDrawable(drawable);
        }

        auth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        navController.navigate(R.id.nav_home);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_security_call:
                        securityCall();
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_emergency_call:
                        emergencyCall();
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_emergency_msg:
                        sendEmergencyMessage();
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_police_stations:
                        policeStationsNearMe();
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_profile:
                        navController.navigate(R.id.nav_profile);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_logout:
                        createLogoutPopupDialog();
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }


    private void sendEmergencyMessage() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");

        SharedPreferences sp;
        sp = PreferenceManager.getDefaultSharedPreferences(DashboardMain.this.getApplicationContext());

        String contactNumber = sp.getString("emergencyContact" , "999");

        String relation = sp.getString("contactRelation" , "Dear");

        smsIntent.putExtra("address", new String(contactNumber));
        smsIntent.putExtra("sms_body", "Hi "+relation+"\nI am in danger. Please find me in this location: https://www.google.com/maps/?q=" + myLatitude + "," + myLongitude);

        try {
            startActivity(smsIntent);
            DashboardMain.this.finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DashboardMain.this.getApplicationContext(),
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DashboardMain.this.getApplicationContext());
        String securityNumber = sharedPreferences.getString("securityNumber" , "999");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", securityNumber, null));
        startActivity(intent);
    }

    private void emergencyCall() {
        SharedPreferences sp;
        sp = PreferenceManager.getDefaultSharedPreferences(DashboardMain.this.getApplicationContext());
        String contactNumber = sp.getString("emergencyContact" , "999");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contactNumber, null));
        startActivity(intent);
    }


    public void createLogoutPopupDialog() {
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        Button cancelBtn, logoutBtn;
        dialogBuilder = new AlertDialog.Builder(DashboardMain.this);
        final View logoutConfirmPopupView = getLayoutInflater().inflate(R.layout.logout_confirm_popup_view, null);
        cancelBtn = logoutConfirmPopupView.findViewById(R.id.cancelBtn);
        logoutBtn = logoutConfirmPopupView.findViewById(R.id.logoutBtn);
        dialogBuilder.setView(logoutConfirmPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
                Intent intent = new Intent(DashboardMain.this, LoginActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                navController.navigate(R.id.nav_settings);
                return true;
            case R.id.action_logout:
                createLogoutPopupDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    int LOCATION_REQUEST_CODE = 100;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(DashboardMain.this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DashboardMain.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
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