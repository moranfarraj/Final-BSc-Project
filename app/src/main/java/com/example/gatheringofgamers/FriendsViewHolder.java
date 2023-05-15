package com.example.gatheringofgamers;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class FriendsViewHolder extends RecyclerView.ViewHolder{
    TextView nameView;
    ImageButton accept_btn;
    ImageButton decline_btn;
    ImageButton delete_btn;
    CollectionReference friendsRef;
    FirebaseFirestore db;
    FriendsAdapter adapter;
    int tabPosition;
    String userId;
    TabLayout friendsList_layout;
    private FriendsAdapter.OnItemClickListener mListener;
    RecyclerView friendsList;

    public FriendsViewHolder(View itemView, List<User> users,int tabPosition,String userId,FriendsAdapter adapter) {
        super(itemView);
        db = FirebaseFirestore.getInstance();
        friendsRef = db.collection("friendRequests");
        this.tabPosition = tabPosition;
        this.userId = userId;
        this.adapter = adapter;
        nameView = itemView.findViewById(R.id.friend_name);
        friendsList = itemView.findViewById(R.id.friends_list);
        accept_btn = itemView.findViewById(R.id.accept_button);
        decline_btn = itemView.findViewById(R.id.decline_button);
        delete_btn = itemView.findViewById(R.id.delete_button);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position, users.get(position));
                    }
                }
            }
        });
        if (tabPosition == 0) {
            accept_btn.setVisibility(View.GONE);
            decline_btn.setVisibility(View.GONE);
            delete_btn.setVisibility(View.GONE);
        } else if (tabPosition == 1) {
            accept_btn.setVisibility(View.VISIBLE);
            decline_btn.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.GONE);
        } else if (tabPosition == 2) {
            accept_btn.setVisibility(View.GONE);
            decline_btn.setVisibility(View.GONE);
            delete_btn.setVisibility(View.VISIBLE);
        }
            accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.w(TAG, "This is accept request button:" + users.get(position).getId());
                    friendsRef.whereEqualTo("from",users.get(position).getId()).whereEqualTo("to",userId)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                                        Log.w(TAG,document.getId());
                                        friendsRef.document(document.getId()).update("status", "friends");
                                        Toast.makeText(itemView.getContext(), "Ally Added!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            });

            decline_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.w(TAG, "This is decline request button:" + users.get(position).getId());
                    friendsRef.whereEqualTo("from",users.get(position).getId()).whereEqualTo("to",userId)
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                                        Log.w(TAG,document.getId());
                                        friendsRef.document(document.getId()).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(itemView.getContext(), "Friend Request Declined!", Toast.LENGTH_SHORT).show();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }) .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(itemView.getContext(), "Failed To Delete Friend Request!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            });
                }
            });
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.w(TAG, "This is delete sent request button:" + users.get(position).getId());
                    friendsRef.whereEqualTo("from",userId).whereEqualTo("to",users.get(position).getId())
                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                                        Log.w(TAG,document.getId());
                                        friendsRef.document(document.getId()).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(itemView.getContext(), "Friend Request Deleted!", Toast.LENGTH_SHORT).show();

                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }) .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(itemView.getContext(), "Failed To Delete Friend Request!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            });
                }
            });
//        accept_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = getAdapterPosition();
//                Log.w(TAG,"This is accept button:"+users.get(position).getId());
//            }
//        });
//
//        decline_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = getAdapterPosition();
//                Log.w(TAG,"This is decline button"+users.get(position).getId());
//            }
//        });

        }
    }

