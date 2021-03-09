package com.example.safetywithsecurity.Models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safetywithsecurity.DashboardMain;
import com.example.safetywithsecurity.R;
import com.example.safetywithsecurity.profile.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyListViewAdapter> {

    private Context context;
    private Activity activity;
    private List<NeedBlood> needBloodList;
    ViewGroup parent;

    public MyListAdapter(Activity activity,Context context, List<NeedBlood> needBlood) {
        this.activity=activity;
        this.context = context;
        this.needBloodList = needBlood;
    }
    @NonNull
    @Override
    public MyListViewAdapter onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.need_blood_listview,parent,false);
        this.parent=parent;
        return new MyListViewAdapter(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyListViewAdapter holder,int position){
        NeedBlood needBloodObj=needBloodList.get(position);
        holder.name.setText(needBloodObj.getUserName());
        holder.bloodGroup.setText(needBloodObj.getBloodGrp());
        try{
            Picasso.get().load(needBloodObj.getUserImg()).into(holder.profilePicImageView);
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNeedBloodHelpInfo(holder.linearLayout,needBloodObj);
            }
        });

    }

    public void showNeedBloodHelpInfo(LinearLayout linearLayout,NeedBlood needBloodObj){
        ImageView profileImage;
        TextView profileName;
        MaterialTextView bloodGroup,needBloodDate,needBloodTime,needBloodLocation,needBloodMsg;
        CardView callCardView,messageCardView,emailCardView;
        AlertDialog.Builder dialogBuilder;
        AlertDialog dialog;
        String userEmail,userPhoneNumber;
        dialogBuilder = new AlertDialog.Builder(linearLayout.getContext());
        final View needBloodInfoPopupView = LayoutInflater.from(parent.getContext()).inflate(R.layout.need_blood_info_popup_view,parent, false);

        profileName = needBloodInfoPopupView.findViewById(R.id.profileName);
        bloodGroup = needBloodInfoPopupView.findViewById(R.id.bloodGroup);
        needBloodDate = needBloodInfoPopupView.findViewById(R.id.needBloodDate);
        needBloodTime = needBloodInfoPopupView.findViewById(R.id.needBloodTime);
        needBloodLocation = needBloodInfoPopupView.findViewById(R.id.needBloodLocation);
        needBloodMsg = needBloodInfoPopupView.findViewById(R.id.needBloodMsg);
        profileImage = needBloodInfoPopupView.findViewById(R.id.profileImage);
        callCardView = needBloodInfoPopupView.findViewById(R.id.callCardView);
        messageCardView = needBloodInfoPopupView.findViewById(R.id.messageCardView);
        emailCardView = needBloodInfoPopupView.findViewById(R.id.emailCardView);

        profileName.setText(needBloodObj.getUserName());
        bloodGroup.setText(needBloodObj.getBloodGrp());
        needBloodDate.setText(needBloodObj.getDate());
        needBloodTime.setText(needBloodObj.getTime());
        needBloodLocation.setText(needBloodObj.getLocation());
        needBloodMsg.setText(needBloodObj.getMessage());
        Picasso.get().load(needBloodObj.getUserImg()).into(profileImage);
        userPhoneNumber=needBloodObj.getPhoneNumber();
        userEmail=needBloodObj.getUserEmail();

        dialogBuilder.setView(needBloodInfoPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        callCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateBloodCall(userPhoneNumber,v);
                dialog.dismiss();
            }
        });
        messageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donateBloodSendMessage(userPhoneNumber,v);
                dialog.dismiss();
            }
        });
        emailCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void donateBloodCall(String phoneNumber,View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
        v.getContext().startActivity(intent);
    }

    private void donateBloodSendMessage(String phoneNumber,View v) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");

        smsIntent.putExtra("address", new String(phoneNumber));
        smsIntent.putExtra("sms_body", "Hi Dear,"+"\nI am able to donate blood. You can contact me.");

        try {
            v.getContext().startActivity(smsIntent);
//            v.getContext().finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(v.getContext(),
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount(){
        return needBloodList.size();
    }

    public class MyListViewAdapter extends RecyclerView.ViewHolder {
        private TextView name, bloodGroup;
        private ImageView profilePicImageView;
        private LinearLayout linearLayout;

        public MyListViewAdapter(@NonNull View itemView) {
            super(itemView);
            profilePicImageView = itemView.findViewById(R.id.profilePicImageView);
            name=itemView.findViewById(R.id.name);
            bloodGroup=itemView.findViewById(R.id.bloodGroup);
            linearLayout=itemView.findViewById(R.id.linearLayout);

        }
    }

}