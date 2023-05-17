package com.example.gatheringofgamers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.util.Collection;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder> {
    Context context;
    List<FriendRequest> friendRequestsSent;
    List<FriendRequest> friendRequestsReceived;
    List<FriendRequest> friends;
    List<User>friendsList;
    private ViewHolderCallback callback;
    List<User> users;
    private OnItemClickListener mListener;

    int tabPosition;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef;
    String currUser;
    String userId;
    CollectionReference friendsRef;

    public FriendsAdapter(Context context, List<User> friendsList, String user, int tabPosition, OnItemClickListener listener, ViewHolderCallback callBack){
        this.callback=callBack;
        this.context = context;
        this.friendsList = friendsList;
        this.currUser = user;
        this.tabPosition = tabPosition;
        this.mListener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        FriendsViewHolder friendsViewHolder=  new FriendsViewHolder(LayoutInflater.from(context).inflate(R.layout.friendlist_view,parent,false),friendsList,tabPosition,currUser,this,callback);
        return friendsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendsViewHolder holder,int position) {

        Log.w(TAG, "this is the list in adapter:" + friendsList.size());
        friendsRef = db.collection("friendRequests");
        usersRef = db.collection("users");
        Query query;
        holder.nameView.setText(friendsList.get(position).getName());
        userId = friendsList.get(position).getId();
        final User currentUser = friendsList.get(position);
        int position1 = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(position1,friendsList.get(position1));
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position,User user);
    }
    public interface ViewHolderCallback {
        void onFunctionCall(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



}
