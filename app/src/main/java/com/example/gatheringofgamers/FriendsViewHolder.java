package com.example.gatheringofgamers;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class FriendsViewHolder extends RecyclerView.ViewHolder{
    TextView nameView;
    TabLayout friendsList_layout;
    private FriendsAdapter.OnItemClickListener mListener;
    RecyclerView friendsList;

    public FriendsViewHolder(View itemView, List<User> users) {
        super(itemView);
        nameView = itemView.findViewById(R.id.friend_name);
        friendsList = itemView.findViewById(R.id.friends_list);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position,users.get(position));
                    }
                }
            }
        });
    }

}

