package com.example.gatheringofgamers;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class FriendsFragment extends Fragment {

    private static final String ARG_USER_ID = "user_id";

    private ViewPager viewPager;
    private ProfilePagerAdapter pagerAdapter;
    private FirebaseFirestore db;
    private String username;
    RecyclerView.Adapter adapter;
    private String userId;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance(String userId) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendslayout, container, false);
        TabLayout tabLayout = view.findViewById(R.id.friendList_layout);
//        Query query;
//        query = db.collection("friendRequests")
//                .whereEqualTo("from",userId);
        //i dont think we need those up there.
        db.collectionGroup("friendRequests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<FriendRequest> friendRequestsSent =new ArrayList<>();
                    List<FriendRequest> friendRequestsReceived = new ArrayList<>();
                    List<FriendRequest> friends = new ArrayList<>();
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    for(DocumentSnapshot document : documents){
                        FriendRequest friendRequest = new FriendRequest(document.get("from").toString(), document.get("to").toString(), document.get("status").toString());
                        if(document.get("status").equals("pending")) {
                            if (document.get("from").toString().equals(userId)) {
                                friendRequestsSent.add(friendRequest);
                            } else if (document.get("to").toString().equals(userId)) {
                                friendRequestsReceived.add(friendRequest);
                            }
                        }
                        else{
                            friends.add(friendRequest);
                        }
                    }
                    RecyclerView recyclerView = view.findViewById(R.id.friends_list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
                    adapter = new FriendsAdapter(view.getContext(),friends,userId,0);
                    recyclerView.setAdapter(adapter);

                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            // Check which tab is selected and pass the appropriate list to the adapter
                            if (tab.getPosition() == 0) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
                                adapter = new FriendsAdapter(view.getContext(),friends,userId,tab.getPosition());
                                recyclerView.setAdapter(adapter);
                            } else if (tab.getPosition() == 1) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
                                adapter = new FriendsAdapter(view.getContext(),friendRequestsReceived,userId,tab.getPosition());
                                recyclerView.setAdapter(adapter);

                            } else if (tab.getPosition() == 2) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
                                adapter = new FriendsAdapter(view.getContext(),friendRequestsSent,userId,tab.getPosition());
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }

                    });
                }
            }
        });
        return view;
    }
}
