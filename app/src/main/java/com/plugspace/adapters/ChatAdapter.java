package com.plugspace.adapters;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.plugspace.fragment.ChatLikedFragment;
import com.plugspace.fragment.ChatListFragment;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.SavedProfileModel;

import java.util.ArrayList;


public class ChatAdapter extends FragmentStatePagerAdapter {
//public class ChatAdapter extends BaseFragmentPagerAdapter {
    int totalTabs;
    ArrayList<LoginDataModel> likesList;
    ArrayList<LoginDataModel> viewsList;
    ArrayList<SavedProfileModel> savedProfileList;
  //  ArrayList<Message> savedProfileList;
//    ArrayList<LoginDataModel> chatList;


    public ChatAdapter(FragmentManager fm, int totalTabs, ArrayList<LoginDataModel> likesList,
                       ArrayList<LoginDataModel> viewsList/*, ArrayList<LoginDataModel> chatList*/, ArrayList<SavedProfileModel> savedProfileList) {
        super(fm);
        this.totalTabs = totalTabs;
        this.likesList = likesList;
        this.viewsList = viewsList;
        this.savedProfileList = savedProfileList;
//        this.chatList = chatList;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ChatLikedFragment.newInstance(likesList,savedProfileList, position);
            case 1:
                return ChatLikedFragment.newInstance(viewsList,savedProfileList, position);
            case 2:
                return ChatListFragment.newInstance(/*chatList,*/ position);
            case 3:
                return ChatLikedFragment.newInstance(likesList,savedProfileList, position);


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}