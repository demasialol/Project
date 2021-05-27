package com.androidapp.youjigom;

import android.app.Activity;
import android.app.Person;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

    private ArrayList<UserInfo_item> listData = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), CameraActivity.class);
                view.getContext().startActivity(intent);

            }
        });

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(UserInfo_item data){
        listData.add(data);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        protected ImageView profileImage;
        protected TextView name;
        protected TextView country;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.name);
            country = itemView.findViewById(R.id.country);


        }

        public void onBind(UserInfo_item data) {
            profileImage.setImageResource(data.getProfileImage());
            name.setText(data.getName());
            country.setText(data.getCountry());
        }
    }
}
