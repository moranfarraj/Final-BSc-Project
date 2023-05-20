package com.example.gatheringofgamers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    public static int MSG_TYPE_LEFT = 0;
    public static int MSG_TYPE_RIGHT = 1;
    String sender;
    String receiver;

    List<Chat> mChat;
    private Context context;

    public MessagesAdapter(Context context,List<Chat> messages,String sender,String receiver) {
        this.context = context;
        this.mChat =messages;
        this.sender = sender;
        this.receiver = receiver;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            MessageViewHolder messageViewHolder = new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.message_item_layout_right, parent, false));
            return messageViewHolder;
        }
        else{
            MessageViewHolder messageViewHolder = new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.message_item_layout_left, parent, false));
            return messageViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.message.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position){
        if(mChat.get(position).getSender().equals(sender)){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

}
