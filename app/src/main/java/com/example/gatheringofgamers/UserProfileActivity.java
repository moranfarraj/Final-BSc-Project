package com.example.gatheringofgamers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

public class UserProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private FirebaseFirestore db;
    private ViewPager viewPager;
    private TextView name ;
    private String userID;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        setContentView(R.layout.userprofile);
        name = findViewById(R.id.profile_name);
        userID = getIntent().getStringExtra("userID");
        Task<DocumentSnapshot> task = usersRef.document(userID).get();
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = new User(document.getId(), document.get("username").toString(), document.get("gender").toString(), document.get("country").toString());
                        name.setText(user.getName());
                    }
                }
            }
        });
        tabLayout = findViewById(R.id.user_tab_layout);
        viewPager = findViewById(R.id.user_view_pager);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(UserProfileFragment.newInstance(userID),"Profile");
        adapter.addFragment(UserGamesFragment.newInstance(userID),"Games");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public class TabAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

}
