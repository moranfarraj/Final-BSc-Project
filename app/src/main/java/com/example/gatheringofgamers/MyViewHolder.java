package com.example.gatheringofgamers;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import org.checkerframework.checker.nullness.qual.NonNull;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView nameView;
    TextView genderView;
    TextView locationView;
    RecyclerView gamesList;
    Button addTeammate;
    TextView recommendationScore;
    public MyViewHolder(View itemView){
        super(itemView);
        gamesList = itemView.findViewById(R.id.user_games_recycler);
        nameView = itemView.findViewById(R.id.name);
        genderView = itemView.findViewById(R.id.gender);
        locationView = itemView.findViewById(R.id.location);
        addTeammate = itemView.findViewById(R.id.addTeammateButton);
        recommendationScore = itemView.findViewById(R.id.recommendation_score);
    }

}
