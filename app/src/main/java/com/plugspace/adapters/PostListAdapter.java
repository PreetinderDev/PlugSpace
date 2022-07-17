package com.plugspace.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.plugspace.R;
import com.plugspace.activities.MusicFavCategoryActivity;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiClient;
import com.plugspace.retrofitApi.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    ArrayList<LoginDataModel> postList;
    MyListener myListener;
    PostPreviewAdapter previewAdapter;
    LoginDataModel loginDataModel;
    ArrayList<MediaModel> mediaList;
    //    String strRank = "";
//    String strNameAge = "";
    public static int USER_IMG = 0;
    public static int USER_DETAILS_IMG_LIST = 1;
    private Interfaces.OnAdapterClickListener onAdapterClickListener;

    public void setOnAdapterClickListener(Interfaces.OnAdapterClickListener onAdapterClickListener) {
        this.onAdapterClickListener = onAdapterClickListener;
    }

    public PostListAdapter(Activity activity, ArrayList<LoginDataModel> postList,
                           LoginDataModel loginDataModel, ArrayList<MediaModel> mediaList, MyListener myListener) {
        this.activity = activity;
        this.postList = postList;
        this.myListener = myListener;
        this.loginDataModel = loginDataModel;
        this.mediaList = mediaList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameAge, tvRank, tvEducation, tvDegree, tvLocation;
        ImageView ivLike, ivBlock;
        ImageView ivDisLike;
        LinearLayout llRealityRank, llLocation;
        RoundedImageView rivPostImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameAge = itemView.findViewById(R.id.tvNameAge);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvEducation = itemView.findViewById(R.id.tvEducation);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDegree = itemView.findViewById(R.id.tvDegree);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivBlock = itemView.findViewById(R.id.ivBlock);
            ivDisLike = itemView.findViewById(R.id.ivDisLike);
            llRealityRank = itemView.findViewById(R.id.llRealityRank);
            llLocation = itemView.findViewById(R.id.llLocation);
            rivPostImg = itemView.findViewById(R.id.rivPostImg);
        }

    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        LinearLayout llMyMusicChoice, llMakeOver;
        TextView tvHeight, tvWeight, tvChildren, tvRelationStatus, tvDesEthnicity, tvSalary,
                tvDressSize, tvEngagement, tvTattoo, tvConsiderMySelf, tvAboutMe;
        RecyclerView rvPreview;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            llMyMusicChoice = itemView.findViewById(R.id.llMyMusicChoice);
            tvHeight = itemView.findViewById(R.id.tvHeight);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvChildren = itemView.findViewById(R.id.tvChildren);
            tvRelationStatus = itemView.findViewById(R.id.tvRelationStatus);
            tvDesEthnicity = itemView.findViewById(R.id.tvDesEthnicity);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvDressSize = itemView.findViewById(R.id.tvDressSize);
            tvEngagement = itemView.findViewById(R.id.tvEngagement);
            tvTattoo = itemView.findViewById(R.id.tvTattoo);
            tvConsiderMySelf = itemView.findViewById(R.id.tvConsiderMySelf);
            tvAboutMe = itemView.findViewById(R.id.tvAboutMe);
            llMakeOver = itemView.findViewById(R.id.llMakeOver);
            rvPreview = itemView.findViewById(R.id.rvPreview);

        }
    }

    public interface MyListener {
        void myClicked(String identify, int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
//        switch (viewType) {
//            case 0:
//                View chatView = inflater.inflate(R.layout.row_post_list, parent, false);
//                return new PostListAdapter.ViewHolder(chatView);
//
//            case 1:
//                View chatView1 = inflater.inflate(R.layout.row_post_details, parent, false);
//                return new PostListAdapter.ViewHolder1(chatView1);
//        }
//        View chatView = inflater.inflate(R.layout.row_post_list, parent, false);
//        return new PostListAdapter.ViewHolder(chatView);

        if (viewType == 1) {
            View chatView1 = inflater.inflate(R.layout.row_post_details, parent, false);
            return new PostListAdapter.ViewHolder1(chatView1);
        } else {
            View chatView = inflater.inflate(R.layout.row_post_list, parent, false);
            return new PostListAdapter.ViewHolder(chatView);

        }
    }

    @Override
    public int getItemViewType(int position) {


        if (position == 0) {
            return USER_IMG;
        } else if (postList.get(position).getTitle() != null
                && postList.get(position).getTitle().equalsIgnoreCase(Constants.MEDIA_LIST)) {
            return USER_DETAILS_IMG_LIST;
        }
        return 121;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                PostListAdapter.ViewHolder viewHolder = (PostListAdapter.ViewHolder) holder;
                LoginDataModel userDetailsModel = postList.get(position);

                if (userDetailsModel != null) {
                    Logger.d("test_is_like", userDetailsModel.getIs_like());

                    if (userDetailsModel.getIs_like().equals(Constants.isLike_1)) {
                        viewHolder.ivLike.setImageResource(R.drawable.ic_like_fill);
                    } else {
                        viewHolder.ivLike.setImageResource(R.drawable.ic_like_normal);
                    }

                    String name = userDetailsModel.getName();
                    String age = userDetailsModel.getAge();

                    String strNameAge = name;
                    if (!Utils.isValidationEmpty(name) && !Utils.isValidationEmpty(age)) {
                        strNameAge = name + ", " + age;
                    } else if (!Utils.isValidationEmpty(name)) {
                        strNameAge = name;
                    } else if (!Utils.isValidationEmpty(age)) {
                        strNameAge = age;
                    }
                    viewHolder.tvNameAge.setText(strNameAge);

                    viewHolder.tvEducation.setText(userDetailsModel.getEducationStatus());
                    viewHolder.tvDegree.setText(userDetailsModel.getJobTitle());
                    viewHolder.tvRank.setText(userDetailsModel.getRank());


                    if (!Utils.isValidationEmpty(userDetailsModel.getLocation())) {
                        String location = userDetailsModel.getLocation().trim();
                        if (location.equals(",")) {
                            location = "";
                        }
                        viewHolder.tvLocation.setText(location);
                    } else {
                        viewHolder.tvLocation.setText("");
                    }
                }

                if (mediaList != null && mediaList.size() > 0) {

                    Utils.setImageBg(activity, mediaList.get(0).getProfile(), viewHolder.rivPostImg);


//                    mediaList.remove(0); // TODO: 12/2/22 Confirm: why this line?
                } else {
                    Utils.setImageBg(activity, "", viewHolder.rivPostImg);
                }

                viewHolder.llRealityRank.setOnClickListener(view -> myListener.myClicked(Constants.RANK, position));

                viewHolder.ivLike.setOnClickListener(view -> myListener.myClicked(Constants.LIKE, position));

                viewHolder.ivDisLike.setOnClickListener(view -> myListener.myClicked(Constants.DISLIKE, position));

                viewHolder.ivBlock.setOnClickListener(view -> {
                    if (onAdapterClickListener != null) {
                        onAdapterClickListener.adapterClick(position, Constants.IS_FROM_BLOCK);
                    }
                });

                break;

            case 1:
                PostListAdapter.ViewHolder1 viewHolder1 = (PostListAdapter.ViewHolder1) holder;
//                LoginDataModel model = loginDataModel;
                userDetailsModel = postList.get(position - 1);
                if (userDetailsModel != null) {
                    Logger.d("test_otherUserId_main", userDetailsModel.getUserId());

                    viewHolder1.tvHeight.setText(userDetailsModel.getHeight());
                    viewHolder1.tvWeight.setText(userDetailsModel.getWeight());
                    viewHolder1.tvChildren.setText(userDetailsModel.getChildren());
                    viewHolder1.tvRelationStatus.setText(userDetailsModel.getRelationshipStatus());
                    viewHolder1.tvDesEthnicity.setText(userDetailsModel.getEthinicity());
                    viewHolder1.tvSalary.setText(userDetailsModel.getMakeOver());
                    viewHolder1.tvDressSize.setText(userDetailsModel.getDressSize());
                    viewHolder1.tvEngagement.setText(userDetailsModel.getTimesOfEngaged());
                    viewHolder1.tvTattoo.setText(userDetailsModel.getYourBodyTatto());
                    viewHolder1.tvConsiderMySelf.setText(userDetailsModel.getMySelfMen());
                    viewHolder1.tvAboutMe.setText(userDetailsModel.getAboutYou());

                    if (Utils.isValidationEmpty(userDetailsModel.getMakeOver())) {
                        viewHolder1.llMakeOver.setVisibility(View.GONE);
                    }
                    ArrayList<MediaModel> mediaModelArrayList = userDetailsModel.getMediaDetail();
                    viewHolder1.rvPreview.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
                    previewAdapter = new PostPreviewAdapter(activity, mediaModelArrayList);
                    viewHolder1.rvPreview.setAdapter(previewAdapter);


                    viewHolder1.llMyMusicChoice.setOnClickListener(view -> {
                        activity.startActivity(new Intent(activity, MusicFavCategoryActivity.class)
                                .putExtra(Constants.INTENT_KEY_MODEL, userDetailsModel)
                                .putExtra(Constants.categoryListFrom, "1"));
                    });
                }


                break;
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


}
