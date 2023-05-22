package com.example.gatheringofgamers;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

public class UserGameViewHolder extends RecyclerView.ViewHolder{
    TextView gameText;
    View competitiveView1,competitiveView2,competitiveView3,competitiveView4,competitiveView5;
    View skillView1,skillView2,skillView3,skillView4,skillView5;
    View communicationView1,communicationView2,communicationView3,communicationView4,communicationView5;


    public UserGameViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        gameText = itemView.findViewById(R.id.game_text);

        competitiveView1 = itemView.findViewById(R.id.competitivebox1);
        competitiveView2 = itemView.findViewById(R.id.competitivebox2);
        competitiveView3 = itemView.findViewById(R.id.competitivebox3);
        competitiveView4 = itemView.findViewById(R.id.competitivebox4);
        competitiveView5 = itemView.findViewById(R.id.competitivebox5);

        skillView1 = itemView.findViewById(R.id.skillbox1);
        skillView2 = itemView.findViewById(R.id.skillbox2);
        skillView3 = itemView.findViewById(R.id.skillbox3);
        skillView4 = itemView.findViewById(R.id.skillbox4);
        skillView5 = itemView.findViewById(R.id.skillbox5);

        communicationView1 = itemView.findViewById(R.id.communicationbox1);
        communicationView2 = itemView.findViewById(R.id.communicationbox2);
        communicationView3 = itemView.findViewById(R.id.communicationbox3);
        communicationView4 = itemView.findViewById(R.id.communicationbox4);
        communicationView5 = itemView.findViewById(R.id.communicationbox5);
    }
}
