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

public class MessageViewHolder extends RecyclerView.ViewHolder {
    TextView message;

    public MessageViewHolder(View itemView){
        super(itemView);
        message= itemView.findViewById(R.id.messageText);
    }
}
