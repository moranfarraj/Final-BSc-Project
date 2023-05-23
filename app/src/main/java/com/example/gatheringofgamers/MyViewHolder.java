package com.example.gatheringofgamers;

import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import org.checkerframework.checker.nullness.qual.NonNull;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView nameView;
    TextView genderView;
    TextView locationView;
    RecyclerView gamesList;
    Button addTeammate;
    Button reportUser;
    TextView recommendationScore;
    ImageView profile_img;
    public MyViewHolder(View itemView){
        super(itemView);
        profile_img = itemView.findViewById(R.id.profile_picture);
        gamesList = itemView.findViewById(R.id.user_games_recycler);
        nameView = itemView.findViewById(R.id.name);
        reportUser = itemView.findViewById(R.id.reportButton);
        genderView = itemView.findViewById(R.id.gender);
        locationView = itemView.findViewById(R.id.location);
        addTeammate = itemView.findViewById(R.id.addTeammateButton);
        recommendationScore = itemView.findViewById(R.id.recommendation_score);
    }

}
