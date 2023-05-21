package com.example.gatheringofgamers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class ChatListViewHolder extends RecyclerView.ViewHolder {
    TextView username;
    ImageView profile_img;


    public ChatListViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.username);
        profile_img = itemView.findViewById(R.id.profile_image);
    }
}
