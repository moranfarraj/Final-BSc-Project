package com.example.gatheringofgamers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
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
    List<FriendRequest>friendsList;
    int tabPosition;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users;
    String currUser;
    CollectionReference friendsRef;

    public FriendsAdapter(Context context, List<FriendRequest> friendsList, String user,int tabPosition){
        this.context = context;
        this.friendsList = friendsList;
        this.currUser = user;
        this.tabPosition = tabPosition;

    }

    @NonNull
    @NotNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        FriendsViewHolder friendsViewHolder=  new FriendsViewHolder(LayoutInflater.from(context).inflate(R.layout.friendlist_view,parent,false));
        return friendsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendsViewHolder holder, int position) {
        friendsRef = db.collection("friendRequests");
        users = db.collection("users");
        Query query;
        //FIX if tabPosition==0 which are friends its not right!
        if(tabPosition==0){
            query = users.whereEqualTo(FieldPath.documentId(),friendsList.get(position).getFrom());
        }
        else if(tabPosition==1){
            query = users.whereEqualTo(FieldPath.documentId(),friendsList.get(position).getFrom());
        }
        else if(tabPosition==2){
            query = users.whereEqualTo(FieldPath.documentId(),friendsList.get(position).getTo());
        }
        else{
            query = users.whereEqualTo("none", "none");
        }

        //CHECK HERER!!!!!!!
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot.getDocuments().isEmpty()){
                        holder.nameView.setText("Empty List");
                    }
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    holder.nameView.setText(document.get("username").toString());

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }
}
