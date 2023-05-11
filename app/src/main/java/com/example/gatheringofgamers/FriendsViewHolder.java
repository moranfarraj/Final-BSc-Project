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

public class FriendsViewHolder extends RecyclerView.ViewHolder{
    TextView nameView;
    TabLayout friends_tablayout;


    public FriendsViewHolder(View itemView){
        super(itemView);
        nameView = itemView.findViewById(R.id.friend_name);
        friends_tablayout = itemView.findViewById(R.id.friendList_layout);
    }
}
