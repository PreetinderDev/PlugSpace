package com.plugspace.common;

import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;

public class Interfaces {

    public interface OnGoBackClickListener {
        void gobackClick(int position);
    }

    public interface OnEditProfileSaveClickListener {
        void editProfileSaveClick();
    }

    public interface OnNextStoryClickListener {
        void nextStoryClick(int position);
    }

    public interface OnPlayMusicClickListener {
        void playMusicClick(int position);
    }

    public interface OnLikeClickListener {
        void likeClick(int position);
    }

    public interface OnGetReadCountClickListener {
        void getReadCountClick(int readCount, int totalUserCount);
    }

    public interface OnPlayback {
        void onPlayback(int playback);
    }

    public interface OnNotificationRefreshClickListener {
        void notificationRefreshClick();
    }

    public interface OnMatchProfileClickListener {
        void matchProfileClick(LoginDataModel model);
    }

    public interface OnAdapterClickListener {
        void adapterClick(int position,String isFrom);
    }
//    public interface OnReportClickListener {
//        void reportClick(MediaModel mediaModel);
//    }

}
