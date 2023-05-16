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
import com.google.firebase.firestore.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class ChatFragment extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    FirebaseFirestore db;
    String userId;


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
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment_layout, container, false);

        return view;
    }
}
