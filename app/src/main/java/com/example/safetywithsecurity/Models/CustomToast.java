package com.example.safetywithsecurity.Models;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safetywithsecurity.R;
import com.squareup.picasso.Picasso;

public class CustomToast {
    public static Toast currentToast;

    public static void customToast(Context context, String title, String message, int uri) {
        // Avoid creating a queue of toasts
        if (currentToast != null) {
            // Dismiss the current showing Toast
            currentToast.cancel();
        }
        //Retrieve the layout Inflater
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Assign the custom layout to view
        View layout = inflater.inflate(R.layout.custom_toast, null);
        //Return the application context
        currentToast = new Toast(context.getApplicationContext());
        //Set toast gravity to center
        currentToast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
        //Set toast duration
        currentToast.setDuration(Toast.LENGTH_LONG);
        //Set the custom layout to Toast
        currentToast.setView(layout);

        TextView toastTitle = (TextView) layout.findViewById(R.id.toastTitle);
        toastTitle.setText(title);
        TextView toastMessage = (TextView) layout.findViewById(R.id.toastMessage);
        toastMessage.setText(message);
        ImageView toastImg=layout.findViewById(R.id.toastImg);
//        toastImg.setImageResource(uri);
        Picasso.get().load(uri).placeholder(uri).into(toastImg);


        //Display toast
        currentToast.show();
        // Check if the layout is visible - just to be sure
        if (layout != null) {
            // Touch listener for the layout
            // This will listen for any touch event on the screen
            layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // No need to check the event, just dismiss the toast if it is showing
                    if (currentToast != null) {
                        currentToast.cancel();
                        // we return True if the listener has consumed the event
                        return true;
                    }
                    return false;
                }
            });
        }
    }
    public static void customToast(Context context, String title, String message) {

        if (currentToast != null) {
            currentToast.cancel();
        }
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);
        currentToast = new Toast(context.getApplicationContext());
        currentToast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
        currentToast.setDuration(Toast.LENGTH_LONG);
        currentToast.setView(layout);

        TextView toastTitle = (TextView) layout.findViewById(R.id.toastTitle);
        toastTitle.setText(title);
        TextView toastMessage = (TextView) layout.findViewById(R.id.toastMessage);
        toastMessage.setText(message);
        ImageView toastImg=layout.findViewById(R.id.toastImg);
        toastImg.setVisibility(View.GONE);


        currentToast.show();
        if (layout != null) {
            layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (currentToast != null) {
                        currentToast.cancel();
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
