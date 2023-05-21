package com.example.gatheringofgamers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.tasks.*;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.*;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class ChatFragment extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    FirebaseFirestore db;
    DatabaseReference reference ;
    RecyclerView recyclerView;
    ChatListAdapter adapter;
    CollectionReference usersRef;
    List<User> users;
    List<User> finalUsers;
    String userId;
    long count;


    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String userId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.chatsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        users = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://gathering-of-gamers-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                count = 0;
                long childrenCount = dataSnapshot.getChildrenCount();
                users.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getSender().equals(userId)){
                        usersRef.document(chat.getReceiver()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User user = new User(documentSnapshot.getId(),documentSnapshot.get("username").toString(),documentSnapshot.get("gender").toString(),documentSnapshot.get("country").toString());
                                users.add(user);
                                count++;

                                if(count == childrenCount){
                                    readChats(users);
                                }
                            }
                        });
                    }
                    else if(chat.getReceiver().equals(userId)){
                        usersRef.document(chat.getSender()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User user = new User(documentSnapshot.getId(),documentSnapshot.get("username").toString(),documentSnapshot.get("gender").toString(),documentSnapshot.get("country").toString());
                                users.add(user);
                                count++;
                                if(count == childrenCount){
                                    readChats(users);
                                }
                            }
                        });
                    }
                    else{
                        count++;
                        if(count == childrenCount){
                            readChats(users);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }

    private void readChats(List<User> mUsers){
        finalUsers = new ArrayList<>();
        for(User user : mUsers){
            if(!finalUsers.contains(user)){
                finalUsers.add(user);
            }
        }
        adapter = new ChatListAdapter(this.getContext(),userId,finalUsers);
        recyclerView.setAdapter(adapter);
    }

}
