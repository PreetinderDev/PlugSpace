package com.plugspace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private Activity activity;
    private List<LoginDataModel> lstModel;
    private String otherUserId;
    private Interfaces.OnPlayMusicClickListener onPlayMusicClickListener;

    public void setOnPlayMusicClickListener(Interfaces.OnPlayMusicClickListener onPlayMusicClickListener) {
        this.onPlayMusicClickListener = onPlayMusicClickListener;
    }

    private Interfaces.OnLikeClickListener onLikeClickListener;

    public void setOnLikeClickListener(Interfaces.OnLikeClickListener onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
    }

    //    boolean isPausePlay = true;
//    MyListener myListener;
//    String strMusicFavFrom;
//    String strFrom;

//    public MusicListAdapter(Activity activity, ArrayList<PreviewModel> songList, String strFrom, String strMusicFavFrom, MyListener myListener) {
//        this.activity = activity;
//        this.songList = songList;
//        this.strMusicFavFrom = strMusicFavFrom;
//        this.strFrom = strFrom;
//        this.myListener = myListener;
//    }


    public MusicListAdapter(Activity activity, List<LoginDataModel> lstModel, String otherUserId) {
        this.activity = activity;
        this.lstModel = lstModel;
        this.otherUserId = otherUserId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSongName, tvSingerName;
        RelativeLayout rlPlayMusic;
        RoundedImageView rivChatProfileImg, rivShadow;
        ImageView ivPausePlay, ivFavSong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            tvSingerName = itemView.findViewById(R.id.tvSingerName);
            rivChatProfileImg = itemView.findViewById(R.id.rivChatProfileImg);
            rlPlayMusic = itemView.findViewById(R.id.rlPlayMusic);
            ivPausePlay = itemView.findViewById(R.id.ivPausePlay);
            ivFavSong = itemView.findViewById(R.id.ivFavSong);
            rivShadow = itemView.findViewById(R.id.rivShadow);
        }
    }

//    public interface MyListener {
//        void myFavSong(int position, ImageView imageView);
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.row_music_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoginDataModel model = lstModel.get(position);
        if (model != null) {
//            holder.tvSongName.setText(model.getTitle());
//            holder.tvSingerName.setText(model.getArtistsName());

            holder.tvSongName.setText(model.getSubtitle());
            holder.tvSingerName.setText(model.getHeader_desc());

//            Glide.with(activity)
//                    .asBitmap()
//                    .placeholder(R.drawable.bg_place_holder_photo)
//                    .error(R.drawable.ic_profile_placeholder)
//                    .load(model.getImageUrl())
//                    .into(holder.rivChatProfileImg);

            Utils.setImageProfile(activity,model.getImageUrl(),holder.rivChatProfileImg);

            holder.rlPlayMusic.setOnClickListener(view -> {
//                if (isPausePlay) {
//                    holder.ivPausePlay.setVisibility(View.GONE);
//                    holder.rivShadow.setVisibility(View.GONE);
//                    isPausePlay = false;
//                } else {
//                    holder.ivPausePlay.setVisibility(View.VISIBLE);
//                    holder.rivShadow.setVisibility(View.VISIBLE);
//                    isPausePlay = true;
//                }

                if (onPlayMusicClickListener != null) {
                    onPlayMusicClickListener.playMusicClick(position);
                }
            });

            holder.ivFavSong.setOnClickListener(view -> {
                if (onLikeClickListener != null) {
                    onLikeClickListener.likeClick(position);
                }
//                myListener.myFavSong(position, holder.ivFavSong);
            });

//            if (strMusicFavFrom.equalsIgnoreCase(Constants.ODD_NUM)) {
//                holder.ivFavSong.setVisibility(View.INVISIBLE);
//            } else {
//                holder.ivFavSong.setVisibility(View.VISIBLE);
//            }

            if (Utils.isValidationEmpty(otherUserId)) {

                if (model.getIsFavorite().equals(Constants.IS_FAVOURITE_1)) {
                    holder.ivFavSong.setImageResource(R.drawable.ic_heart_fill);
                } else {
                    holder.ivFavSong.setImageResource(R.drawable.ic_heart_un_fill);
                }
            } else if (Preferences.getStringName(Preferences.keyUserId).equals(otherUserId)) {
                holder.ivFavSong.setImageResource(R.drawable.ic_heart_fill);
            } else {
                holder.ivFavSong.setVisibility(View.GONE);
            }

            if (model.isPlay()) {
                holder.ivPausePlay.setImageResource(R.drawable.ic_pause);
            } else {
                holder.ivPausePlay.setImageResource(R.drawable.ic_play);
            }
        }
    }

    @Override
    public int getItemCount() {
        return lstModel.size();
    }

}
