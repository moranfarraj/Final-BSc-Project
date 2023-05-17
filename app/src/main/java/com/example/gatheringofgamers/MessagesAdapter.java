package com.example.gatheringofgamers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    List<String> messages;
    Context context;

    public MessagesAdapter(List<String> messages,Context context) {
        this.context = context;
        this.messages=messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MessageViewHolder messageViewHolder=  new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.message_item_layout,parent,false));
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageViewHolder holder, int position) {
        holder.message.setText(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

}
