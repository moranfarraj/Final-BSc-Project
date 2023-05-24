package com.example.gatheringofgamers;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ProfilePagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 2;

    private String userId;
    List<userGames> userGamesList;
    public ProfilePagerAdapter(FragmentManager fm, String userId,List<userGames>userGamesList) {
        super(fm);
        this.userId = userId;
        this.userGamesList=userGamesList;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProfileInfoFragment.newInstance(userId);
            case 1:
                return MyGamesFragment.newInstance(userId,userGamesList);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Profile";
            case 1:
                return "My Games";
            default:
                return null;
        }
    }
}
