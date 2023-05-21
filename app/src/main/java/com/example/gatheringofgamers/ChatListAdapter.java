package com.example.gatheringofgamers;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder>{
    private Context context;
    private String userId;
    private List<User> users;

    public ChatListAdapter(Context context, String userId, List<User> users) {
        this.context = context;
        this.userId = userId;
        this.users = users;
    }

    @NonNull
    @NotNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ChatListViewHolder chatViewHolder = new ChatListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat,parent,false));
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChatListViewHolder holder, int position) {
        User user = users.get(position);
        holder.username.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
