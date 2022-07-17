package com.plugspace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.LikeAdapter;
import com.plugspace.common.Constants;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.SavedProfileModel;

import java.util.ArrayList;

public class ChatLikedFragment extends BaseFragment {
    Activity activity;
    RecyclerView rvChatLiked;
    LikeAdapter viewsAdapter;
    private int positionTab = 0;
    ArrayList<LoginDataModel> likeList = new ArrayList<>();
    private ArrayList<SavedProfileModel> lstSavedProfile=  new ArrayList<>();;

    public static ChatLikedFragment newInstance(ArrayList<LoginDataModel> likeViewList,ArrayList<SavedProfileModel> lstSavedProfile, int position) {
        ChatLikedFragment fragmentFirst = new ChatLikedFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.INTENT_KEY_LIST, likeViewList);
        args.putSerializable(Constants.INTENT_KEY_SAVE_LIST, lstSavedProfile);
        args.putInt(Constants.INTENT_KEY_POSITION_TAB, position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        likeList = (getArguments() != null ? (ArrayList<LoginDataModel>) getArguments().getSerializable(Constants.INTENT_KEY_LIST) : null);
        lstSavedProfile = (getArguments() != null ? (ArrayList<SavedProfileModel>) getArguments().getSerializable(Constants.INTENT_KEY_SAVE_LIST) : null);
        positionTab = getArguments().getInt(Constants.INTENT_KEY_POSITION_TAB);
        Log.e("positionTab", String.valueOf(+positionTab));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_liked, container, false);
        activity = getActivity();
        initView(view);
        return view;
    }

    private void initView(View view) {
        //    TextView tvNoDataFound;
        LinearLayout llEmptyList = view.findViewById(R.id.llEmptyList);

        TextView tvEmptyList = view.findViewById(R.id.tvEmptyList);
        if (positionTab == 0) {
            tvEmptyList.setText(activity.getResources().getString(R.string.empty_list_liked));
        } else if (positionTab == 1) {
            tvEmptyList.setText(activity.getResources().getString(R.string.empty_list_views));
        }else if (positionTab == 3) {
            tvEmptyList.setText(activity.getResources().getString(R.string.empty_list_profiles));
        }

        rvChatLiked = view.findViewById(R.id.rvChatLiked);
        rvChatLiked.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        if (likeList != null && likeList.size() > 0 ||lstSavedProfile != null && lstSavedProfile.size() > 0) {
            llEmptyList.setVisibility(View.GONE);
            rvChatLiked.setVisibility(View.VISIBLE);


            viewsAdapter = new LikeAdapter(activity, likeList,lstSavedProfile, positionTab);
            rvChatLiked.setAdapter(viewsAdapter);
        } else {
            llEmptyList.setVisibility(View.VISIBLE);
            rvChatLiked.setVisibility(View.GONE);
        }
    }

}
