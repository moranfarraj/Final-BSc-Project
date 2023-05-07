package com.example.gatheringofgamers;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import org.checkerframework.checker.nullness.qual.NonNull;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView nameView;
    TextView genderView;
    TextView locationView;
    public MyViewHolder(View itemView){
        super(itemView);
        nameView = itemView.findViewById(R.id.name);
        genderView = itemView.findViewById(R.id.gender);
        locationView = itemView.findViewById(R.id.location);
    }
}
