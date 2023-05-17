package com.example.gatheringofgamers;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class FriendsFragment extends Fragment implements FriendsAdapter.ViewHolderCallback {
    @Override
    public void onFunctionCall(int position) {
        if(view !=null) {
            handleTabSelection(2, view);
        }
    }

    private static final String ARG_USER_ID = "user_id";

    private ViewPager viewPager;
    private View view;
    private ProfilePagerAdapter pagerAdapter;
    private FirebaseFirestore db;
    private String username;
    private TabLayout tabLayout;
    RecyclerView.Adapter adapter;
    private SharedPreferences preferences;

    CollectionReference usersRef;
    List<User> users;
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
        users = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        usersRef = db.collection("users");
        view = inflater.inflate(R.layout.friendslayout, container, false);
        setView(view);
        tabLayout = view.findViewById(R.id.friendList_layout);
        handleTabSelection(0,view);

//        Query query;
//        query = db.collection("friendRequests")
//                .whereEqualTo("from",userId);
        //i dont think we need those up there.

                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            // Check which tab is selected and pass the appropriate list to the adapter
                            //fix tab 0 which shows the users that are friends.
                            if (tab.getPosition() == 0) {
                                handleTabSelection(tab.getPosition(),view);
                            } else if (tab.getPosition() == 1) {
                                handleTabSelection(tab.getPosition(),view);
                            } else if (tab.getPosition() == 2) {
                                handleTabSelection(tab.getPosition(),view);
                            }
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }

                    });
        return view;
    }

    private void handleTabSelection(int position,View view) {
        // Perform actions based on the selected tab position
        if (position == 0) {
            db.collectionGroup("friendRequests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<FriendRequest> friends = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            FriendRequest friendRequest = new FriendRequest(document.get("from").toString(), document.get("to").toString(), document.get("status").toString());
                            if(document.get("status").equals("friends")) {
                                if (document.get("to").toString().equals(userId) || document.get("from").toString().equals(userId)) {
                                    friends.add(friendRequest);
                                }
                            }
                        }
                        int count = 0;
                        users = new ArrayList<>();
                        Query lastQuery = null;
                        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                        for (FriendRequest request : friends) {
                            if(request.getFrom().equals(userId)){
                                Task<DocumentSnapshot> task1 = usersRef.document(request.getTo()).get();
                                tasks.add(task1);
                                task1.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task1.getResult();
                                            if (document.exists()) {
                                                User user = new User(document.getId(), document.get("username").toString(), document.get("gender").toString(), document.get("country").toString());
                                                users.add(user);
                                                Log.w(TAG, "this is the current user:" + user.getName());
                                            }
                                        }
                                    }
                                });
                            }
                            else{
                                Task<DocumentSnapshot> task1 = usersRef.document(request.getFrom()).get();
                                tasks.add(task1);
                                task1.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot document = task1.getResult();
                                            if (document.exists()) {
                                                User user = new User(document.getId(), document.get("username").toString(), document.get("gender").toString(), document.get("country").toString());
                                                users.add(user);
                                                Log.w(TAG, "this is the current user:" + user.getName());
                                            }
                                        }
                                    }
                                });
                            }

                        }

                        Tasks.whenAllSuccess(tasks).addOnCompleteListener(new OnCompleteListener<List<Object>>() {
                            @Override
                            public void onComplete(@NonNull Task<List<Object>> task) {
                                RecyclerView recyclerView = view.findViewById(R.id.friends_list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
                                adapter = new FriendsAdapter(view.getContext(), users, userId, position, new FriendsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position,User user) {
                                        String ID = user.getId();
                                        Log.w(TAG,"go to profile:"+ID);
                                        Intent intent = new Intent(view.getContext(),UserProfileActivity.class);
                                        intent.putExtra("userID",ID);
                                        view.getContext().startActivity(intent);
                                    }
                                },FriendsFragment.this);
                                Log.w(TAG, "friends list size:" + users.size());
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        });

                    }
                }
            });

        } else if (position == 1) {
            db.collectionGroup("friendRequests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<FriendRequest> friendRequestsReceived = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            FriendRequest friendRequest = new FriendRequest(document.get("from").toString(), document.get("to").toString(), document.get("status").toString());
                            if (document.get("status").equals("pending")) {
                                if (document.get("to").toString().equals(userId)) {
                                    friendRequestsReceived.add(friendRequest);
                                }
                            }
                        }
                        int count = 0;
                        users = new ArrayList<>();
                        Query lastQuery = null;
                        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                        for (FriendRequest request : friendRequestsReceived) {
                            Task<DocumentSnapshot> task1 = usersRef.document(request.getFrom()).get();
                            tasks.add(task1);
                            task1.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        if (document.exists()) {
                                            User user = new User(document.getId(), document.get("username").toString(), document.get("gender").toString(), document.get("country").toString());
                                            users.add(user);
                                            Log.w(TAG, "this is the current user:" + user.getName());
                                        }
                                    }
                                }
                            });
                        }

                        Tasks.whenAllSuccess(tasks).addOnCompleteListener(new OnCompleteListener<List<Object>>() {
                            @Override
                            public void onComplete(@NonNull Task<List<Object>> task) {
                                RecyclerView recyclerView = view.findViewById(R.id.friends_list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
                                adapter = new FriendsAdapter(view.getContext(), users, userId,position, new FriendsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position,User user) {
                                        String ID = user.getId();
                                        Log.w(TAG,"go to profile:"+ID);
                                        Intent intent = new Intent(view.getContext(),UserProfileActivity.class);
                                        intent.putExtra("userID",ID);
                                        view.getContext().startActivity(intent);
                                    }
                                },FriendsFragment.this);
                                Log.w(TAG, "friend requests received list size:" + users.size());
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        });
                    }
                }
            });

        } else if (position == 2) {
            db.collectionGroup("friendRequests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<FriendRequest> friendRequestsSent = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            FriendRequest friendRequest = new FriendRequest(document.get("from").toString(), document.get("to").toString(), document.get("status").toString());
                            if (document.get("status").equals("pending")) {
                                if (document.get("from").toString().equals(userId)) {
                                    friendRequestsSent.add(friendRequest);
                                }
                            }
                        }
                        int count = 0;
                        users = new ArrayList<>();
                        Query lastQuery = null;
                        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                        for (FriendRequest request : friendRequestsSent) {
                            Task<DocumentSnapshot> task1 = usersRef.document(request.getTo()).get();
                            tasks.add(task1);
                            task1.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        if (document.exists()) {
                                            User user = new User(document.getId(), document.get("username").toString(), document.get("gender").toString(), document.get("country").toString());
                                            users.add(user);
                                            Log.w(TAG, "this is the current user:" + user.getName());
                                        }
                                    }
                                }
                            });
                        }

                        Tasks.whenAllSuccess(tasks).addOnCompleteListener(new OnCompleteListener<List<Object>>() {
                            @Override
                            public void onComplete(@NonNull Task<List<Object>> task) {
                                RecyclerView recyclerView = view.findViewById(R.id.friends_list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
                                adapter = new FriendsAdapter(view.getContext(), users, userId, position, new FriendsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position,User user) {
                                        String ID = user.getId();
                                        Log.w(TAG,"go to profile:"+ID);
                                        Intent intent = new Intent(view.getContext(),UserProfileActivity.class);
                                        intent.putExtra("userID",ID);
                                        view.getContext().startActivity(intent);
                                    }
                                },FriendsFragment.this);
                                Log.w(TAG, "friend requests sent list size:" + users.size());
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        });
                    }
                }
            });

        }
    }
    public View getView(){
        return view;
    }
    public void setView(View v){
        view = v;
    }
}
