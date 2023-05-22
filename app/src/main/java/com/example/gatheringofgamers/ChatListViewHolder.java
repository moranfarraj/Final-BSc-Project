package com.example.gatheringofgamers;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

public class ChatListViewHolder extends RecyclerView.ViewHolder {
    TextView username;
    ImageView profile_img;
    TextView last_msg;
    ImageButton chat_button;

    public ChatListViewHolder(@NonNull @NotNull View itemView, List<User> users, String currId) {
        super(itemView);
        username = itemView.findViewById(R.id.username);
        profile_img = itemView.findViewById(R.id.profile_image);
        last_msg = itemView.findViewById(R.id.last_message);
        chat_button = itemView.findViewById(R.id.chat_button);

        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    User clickedUser = users.get(position);
                    Intent intent = new Intent(v.getContext(), ChatActivity.class);
                    intent.putExtra("userId", clickedUser.getId());
                    intent.putExtra("MyId",currId);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }
}
