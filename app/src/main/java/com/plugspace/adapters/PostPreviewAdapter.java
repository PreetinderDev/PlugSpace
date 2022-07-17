package com.plugspace.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.activities.BaseActivity;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.MediaModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiClient;
import com.plugspace.retrofitApi.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostPreviewAdapter extends RecyclerView.Adapter<PostPreviewAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<MediaModel> previewList;


    public PostPreviewAdapter(Activity activity, ArrayList<MediaModel> previewList) {
        this.activity = activity;
        this.previewList = previewList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPreviewDes;
        ImageView ivPreviewImg, ivReport;
        private VideoView videoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPreviewDes = itemView.findViewById(R.id.tvPreviewDes);
            ivPreviewImg = itemView.findViewById(R.id.ivPreviewImg);
            ivReport = itemView.findViewById(R.id.ivReport);
            videoView = itemView.findViewById(R.id.videoView);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_post_list_preview, parent, false);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaModel model = previewList.get(position);

        if (model != null) {
//            if (model.getType() != null) {
//                if (model.getType().equalsIgnoreCase(Constants.PROFILE)) {
//
//                    Utils.setImageBg(activity, model.getProfile(), holder.ivPreviewImg);
//                } else if (model.getType().equalsIgnoreCase(Constants.FEED)) {
//
//                    Utils.setImageBg(activity, model.getFeedImage(), holder.ivPreviewImg);
//                    holder.tvPreviewDes.setVisibility(View.VISIBLE);
//                    holder.tvPreviewDes.setText(model.getDescription());
//                }
//            }

            // : 19/2/22 Pending: above below.

            if (model.getMediaType().equals(Constants.MEDIA_TYPE_VIDEO)) {
                holder.ivPreviewImg.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.VISIBLE);

                String pathVideo = "";
                if (model.getType().equalsIgnoreCase(Constants.PROFILE)) {
                    pathVideo = model.getProfile();
                } else if (model.getType().equalsIgnoreCase(Constants.FEED)) {
                    pathVideo = model.getFeedImage();
                }

                holder.videoView.setVideoURI(Uri.parse(pathVideo));
                holder.videoView.start();

            } else {
                holder.ivPreviewImg.setVisibility(View.VISIBLE);
                holder.videoView.setVisibility(View.GONE);

                String image = "";

                if (model.getType().equalsIgnoreCase(Constants.PROFILE)) {
                    image = model.getProfile();
                } else if (model.getType().equalsIgnoreCase(Constants.FEED)) {
                    image = model.getFeedImage();
                }
                Utils.setImageBg(activity,image,holder.ivPreviewImg);
            }

            holder.ivReport.setOnClickListener(v -> {

                dialogReport(model);
            });

        }

    }

    @Override
    public int getItemCount() {
        return previewList.size();
    }

    private void dialogReport(MediaModel mediaModel) {

        Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_report);
        dialog.setTitle("");

        EditText etReason = dialog.findViewById(R.id.etReason);

        dialog.findViewById(R.id.btnSubmit).setOnClickListener(view -> {
            String reason = etReason.getText().toString().trim();
            if (Utils.isValidationEmpty(reason)) {
                Utils.showAlert(activity, activity.getResources().getString(R.string.valid_empty_reason));
            } else {
                Utils.hideKeyBoard(activity, etReason);
//                dialog.dismiss(); // TODO: Confirm: loader show to comment this line
                doAPIProfileReportByUser(reason, dialog, mediaModel);
            }

        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(view -> dialog.dismiss());


        dialog.show();

    }

    private void doAPIProfileReportByUser(String reason, Dialog dialog, MediaModel mediaModel) {
        if (Utils.isNetworkAvailable(activity, true, false)) {

            BaseActivity.showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiProfileReportByUser(
                    Preferences.getStringName(Preferences.keyUserId),
                    mediaModel.getUserId(),
                    mediaModel.getMediaId(),
                    reason,
                    mediaModel.getType(),
                    Constants.deviceType,
                    Constants.token
            );
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
//                    hideProgressDialog(activity);

                    if (response.isSuccessful() && response.body() != null) {
                        ObjectResponseModel model = response.body();

                        if (model.getResponseCode() == 1) {
                            BaseActivity.hideProgressDialog(activity);
                            Utils.showToast(activity, model.getResponseMsg());

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                        } else {
                            BaseActivity.hideProgressDialog(activity);
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                                        model.getResponseMsg());
                            }
                        }

                    } else {
                        BaseActivity.hideProgressDialog(activity);
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
                    BaseActivity.hideProgressDialog(activity);

                    if (!Utils.isNetworkAvailable(activity, true, false)) {
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                                activity.getResources().getString(R.string.error_network));
                    } else {
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                                activity.getResources().getString(R.string.technical_problem));
                    }
                    t.printStackTrace();
                }
            });
        }
    }
}
