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
    ListView gamesList;
    Button addTeammate;
    public MyViewHolder(View itemView){
        super(itemView);
        gamesList = itemView.findViewById(R.id.gamesListView);
        nameView = itemView.findViewById(R.id.name);
        genderView = itemView.findViewById(R.id.gender);
        locationView = itemView.findViewById(R.id.location);
        addTeammate = itemView.findViewById(R.id.addTeammateButton);
        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when an item is clicked, e.g. start a new activity
                showGameInfo(view,position,id);
            }
        });
    }
        public void showGameInfo(View v,int position,long id){
            Log.d(TAG, "CHECKING FUNCTION CALL!");
        }

}
