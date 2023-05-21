package com.example.gatheringofgamers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListViewHolder>{
    private Context context;
    private String currId;
    private List<User> users;
    String theLastMessage;

    public ChatListAdapter(Context context, String currId, List<User> users) {
        this.context = context;
        this.currId = currId;
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
        if (user.getImg() != null) {
            // Decode the Base64 string and convert it into a Bitmap
            byte[] decodedString = Base64.decode(users.get(position).getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            // Set the Bitmap in the ImageView
            holder.profile_img.setImageBitmap(decodedByte);
        }
        lastMessage(users.get(position).getId(), holder.last_msg);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    private void lastMessage(String userid, TextView last_msg){
        theLastMessage = "default";
       DatabaseReference reference = FirebaseDatabase.getInstance("https://gathering-of-gamers-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
               for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   Chat chat = snapshot.getValue(Chat.class);
                   if(chat.getReceiver().equals(currId) && chat.getSender().equals(userid) ||
                           chat.getReceiver().equals(userid) && chat.getSender().equals(currId)){
                       theLastMessage = chat.getMessage();
                   }
               }
               switch(theLastMessage){
                   case "default":
                       last_msg.setText("No Message");
                       break;
                   default:
                       last_msg.setText(theLastMessage);
                       break;
               }
               theLastMessage = "default";
           }

           @Override
           public void onCancelled(@NonNull @NotNull DatabaseError error) {

           }
       });

    }
}
