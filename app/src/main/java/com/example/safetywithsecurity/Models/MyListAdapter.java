package com.example.safetywithsecurity.Models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safetywithsecurity.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyListViewAdapter> {

    private Context context;
    List<NeedBlood> needBlood;

    public MyListAdapter(Context context, List<NeedBlood> needBlood) {
        this.context = context;
        this.needBlood = needBlood;
    }
    @NonNull
    @Override
    public MyListViewAdapter onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view= LayoutInflater.from(context).inflate(R.layout.need_blood_listview,parent,false);
        Log.d("donateBlood", "onCreateViewHolder");
        return new MyListViewAdapter(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyListViewAdapter holder,int position){
        NeedBlood needBloodObj=needBlood.get(position);
        holder.name.setText(needBloodObj.getUserName());
        holder.bloodGroup.setText(needBloodObj.getBloodGrp());
        try{
            Picasso.get().load(needBloodObj.getUserImg()).into(holder.profilePicImageView);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("donateBlood", "onBindViewHolder");

    }
    @Override
    public int getItemCount(){
        return needBlood.size();
    }

    public class MyListViewAdapter extends RecyclerView.ViewHolder {
        private TextView name, bloodGroup;
        private ImageView profilePicImageView;

        public MyListViewAdapter(@NonNull View itemView) {
            super(itemView);
            profilePicImageView = itemView.findViewById(R.id.profilePicImageView);
            name=itemView.findViewById(R.id.name);
            bloodGroup=itemView.findViewById(R.id.bloodGroup);

        }
    }

}