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
    List<User> users;
    private OnItemClickListener mListener;

    int tabPosition;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef;
    String currUser;
    String userId;
    CollectionReference friendsRef;

    public FriendsAdapter(Context context, List<User> friendsList, String user,int tabPosition,OnItemClickListener listener){
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
        FriendsViewHolder friendsViewHolder=  new FriendsViewHolder(LayoutInflater.from(context).inflate(R.layout.friendlist_view,parent,false),friendsList);
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
//        //FIX if tabPosition==0 which are friends its not right!
//        if(tabPosition==0){
//            query = usersRef.whereEqualTo(FieldPath.documentId(),friendsList.get(position).getFrom());
//        }
//        else if(tabPosition==1){
//            query = usersRef.whereEqualTo(FieldPath.documentId(),friendsList.get(position).getFrom());
//        }
//        else if(tabPosition==2){
//            query = usersRef.whereEqualTo(FieldPath.documentId(),friendsList.get(position).getTo());
//        }
//        else{
//            query = usersRef.whereEqualTo("none", "none");
//        }
//
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    QuerySnapshot querySnapshot = task.getResult();
//                    if(querySnapshot.getDocuments().isEmpty()){
//                        holder.nameView.setText("Empty List");
//                    }
//                    else{
//                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
//                            User user = new User(document.getId(),document.get("username").toString(),document.get("gender").toString(),document.get("location").toString());
//                            users.add(user);
//                    }
////                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
////                    userId = document.getId();
////                    holder.nameView.setText(document.get("username").toString());
//
//                }
//            }
//        });
//        if(!users.isEmpty()) {
//            holder.nameView.setText(users.get(position).getName());
//        }
//        holder.nameView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = holder.nameView.getText().toString();
//                // Call function with the username parameter
//                goToUserProfile(userId);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
//    public void goToUserProfile(String ID){
//        Log.w(TAG,"go to profile:"+ID);
//        Intent intent = new Intent(context,UserProfileActivity.class);
//        intent.putExtra("userID",ID);
//        context.startActivity(intent);
//    }
    public interface OnItemClickListener {
        void onItemClick(int position,User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



}
