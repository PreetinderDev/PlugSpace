package com.plugspace.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.plugspace.R;
import com.plugspace.adapters.ChattingMsgAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.ChattingMsgModel;
import com.plugspace.model.LoginDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ChattingMsgActivity extends BaseActivity /*implements View.OnClickListener*/ {
    Activity activity;
    //    ImageView ivBack/*, ivSendMsg*/;
    RoundedImageView rivProfileImage;
    LoginDataModel model;
    String strReceiveName = "", strReceiveProfile = "", strReceiveUserId = "", strReceiveDeviceToken = "", strReceiveDeviceType = "",
            strSenderId = "", strSenderProfile = "", strSenderName = "", strSenderDeviceToken = "";
    TextView tvName, tvOnlineUser;
    RecyclerView rvChattingMsg;
    EditText etWriteMsg;
    ChattingMsgAdapter chattingMsgAdapter;
    ArrayList<LoginDataModel> chattingMsgList = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference chattingDatabaseRef;
    DatabaseReference databaseReferenceActiveUser;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_msg);
        activity = ChattingMsgActivity.this;
        LoginDataModel loginDataModel = Preferences.GetLoginDetails();
        if (loginDataModel != null) {
            strSenderId = loginDataModel.getUserId();
            strSenderName = loginDataModel.getName();
            strSenderProfile = loginDataModel.getProfile();
            strSenderDeviceToken = loginDataModel.getDevice_token();
        }
        GetIntentData();
        initView();
        initClick();
    }

    private void GetIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.DATA_MODEL)) {
            model = (LoginDataModel) intent.getSerializableExtra(Constants.DATA_MODEL);

            if (model != null) {
                strReceiveName = model.getName();
                strReceiveUserId = model.getUserId();
                strReceiveProfile = model.getProfile();
                strReceiveDeviceToken = model.getDevice_token();
                strReceiveDeviceType = model.getDevice_type();
            }
        }
    }

    private void initClick() {
//        ivBack.setOnClickListener(this);
//        ivSendMsg.setOnClickListener(this);

        findViewById(R.id.ivBack).setOnClickListener(v -> onBackPressed());

        findViewById(R.id.ivSendMsg).setOnClickListener(v -> {
            if (Utils.isValidationEmpty(etWriteMsg.getText().toString().trim())) {
                Toast.makeText(activity, activity.getResources().getString(R.string.valid_empty_message), Toast.LENGTH_SHORT).show();
            } else {

//                String key = Utils.TimeStamp();
//                chattingDatabaseRef.child(key).child(Constants.SENDER_TIME).setValue(Utils.CurrentChatTime(""));
//                chattingDatabaseRef.child(key).child(Constants.SENDER_MSG).setValue(etWriteMsg.getText().toString());
//                chattingDatabaseRef.child(key).child(Constants.SENDER_USER_ID).setValue(strSenderId);
//                chattingDatabaseRef.child(key).child(Constants.SENDER_NAME).setValue(strSenderName);
//                chattingDatabaseRef.child(key).child(Constants.SENDER_PROFILE).setValue(strSenderProfile);
//                chattingDatabaseRef.child(key).child(Constants.SENDER_MESSAGE_STATUS).setValue("1"); //1 for send , 2 for read
//                etWriteMsg.getText().clear();


                showProgressDialog(activity);

                String message = etWriteMsg.getText().toString().trim();
                long currentTime = Utils.currentTimeInMilliSeconds();

// CHATTING folder
//                String key = Utils.TimeStamp();
                String key = chattingDatabaseRef.push().getKey();
                if (key == null || Utils.isValidationEmpty(key)) {
                    key = Utils.TimeStamp();
                }
                DatabaseReference databaseReferenceKey = chattingDatabaseRef.child(key);

                databaseReferenceKey.child(Constants.FIREBASE_KEY_MESSAGE_STATUS).setValue(Constants.FIREBASE_VALUE_MESSAGE_STATUS_1);
                databaseReferenceKey.child(Constants.FIREBASE_KEY_MESSAGE).setValue(message);
                databaseReferenceKey.child(Constants.FIREBASE_KEY_NAME).setValue(strSenderName);
                databaseReferenceKey.child(Constants.FIREBASE_KEY_TIME).setValue(currentTime);
                databaseReferenceKey.child(Constants.FIREBASE_KEY_USER_ID).setValue(strSenderId);
                databaseReferenceKey.child(Constants.FIREBASE_KEY_DEVICE_TOKEN).setValue(strSenderDeviceToken);
                databaseReferenceKey.child(Constants.FIREBASE_KEY_DEVICE_TYPE).setValue(Constants.deviceType);

// CHAT_LIST folder
                DatabaseReference databaseReferenceChatList = FirebaseDatabase.getInstance().getReference()
                        .child(Constants.FIREBASE_FOLDER_CHAT_LIST);

                // sender info
                DatabaseReference databaseReferenceReceiver = databaseReferenceChatList.child(strReceiveUserId);
                DatabaseReference databaseReferenceSender = databaseReferenceReceiver.child(strSenderId);

//                databaseReferenceSender.child(Constants.FIREBASE_KEY_READ_COUNT).setValue(Constants.FIREBASE_VALUE_READ_COUNT_0);
                databaseReferenceSender.child(Constants.FIREBASE_KEY_MESSAGE).setValue(message);
                databaseReferenceSender.child(Constants.FIREBASE_KEY_NAME).setValue(strSenderName);
                databaseReferenceSender.child(Constants.FIREBASE_KEY_TIME).setValue(currentTime);
                databaseReferenceSender.child(Constants.FIREBASE_KEY_USER_ID).setValue(strSenderId);
                databaseReferenceSender.child(Constants.FIREBASE_KEY_PROFILE).setValue(strSenderProfile);
                databaseReferenceSender.child(Constants.FIREBASE_KEY_DEVICE_TOKEN).setValue(strSenderDeviceToken);
                databaseReferenceSender.child(Constants.FIREBASE_KEY_DEVICE_TYPE).setValue(Constants.deviceType);

                databaseReferenceSender.child(Constants.FIREBASE_KEY_READ_COUNT).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Logger.d("test_read_count_snapshot", snapshot.toString());
//                        String key = snapshot.getKey();
//                        Logger.d("test_read_count_key", key);
//                        Logger.d("test_read_count_value_", snapshot.getValue(String.class));
//                        Logger.d("test_read_count_value", snapshot.getValue());
                        snapshot.hasChild(Constants.FIREBASE_KEY_READ_COUNT);
                        int readCount = 1;
                        if (snapshot.getValue() != null) {

                            String value = snapshot.getValue(String.class);
                            if (value != null && !Utils.isValidationEmpty(value)) {
                                readCount = (Integer.parseInt(value) + 1);
                            }
                        }
                        databaseReferenceSender.child(Constants.FIREBASE_KEY_READ_COUNT).setValue(String.valueOf(readCount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                // receiver info
                DatabaseReference databaseReferenceSender1 = databaseReferenceChatList.child(strSenderId);
                DatabaseReference databaseReferenceReceiver1 = databaseReferenceSender1.child(strReceiveUserId);

                databaseReferenceReceiver1.child(Constants.FIREBASE_KEY_MESSAGE).setValue(message);
                databaseReferenceReceiver1.child(Constants.FIREBASE_KEY_NAME).setValue(strReceiveName);
                databaseReferenceReceiver1.child(Constants.FIREBASE_KEY_TIME).setValue(currentTime);
                databaseReferenceReceiver1.child(Constants.FIREBASE_KEY_USER_ID).setValue(strReceiveUserId);
                databaseReferenceReceiver1.child(Constants.FIREBASE_KEY_PROFILE).setValue(strReceiveProfile);
                databaseReferenceReceiver1.child(Constants.FIREBASE_KEY_DEVICE_TOKEN).setValue(strReceiveDeviceToken);
                databaseReferenceReceiver1.child(Constants.FIREBASE_KEY_DEVICE_TYPE).setValue(strReceiveDeviceType);
                databaseReferenceReceiver1.child(Constants.FIREBASE_KEY_READ_COUNT).setValue(Constants.FIREBASE_VALUE_READ_COUNT_0);


                etWriteMsg.getText().clear();
                hideProgressDialog(activity);
            }
        });

    }

    private void initView() {
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        tvOnlineUser = findViewById(R.id.tvOnlineUser);
        rvChattingMsg = findViewById(R.id.rvChattingMsg);
        rvChattingMsg.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

//        ivSendMsg = findViewById(R.id.ivSendMsg);
        etWriteMsg = findViewById(R.id.etWriteMsg);
//        ivBack = findViewById(R.id.ivBack);
        rivProfileImage = findViewById(R.id.rivProfileImage);
        tvName = findViewById(R.id.tvName);

//        Glide.with(activity)
//                .asBitmap()
//                .load(strReceiveProfile)
//                .into(rivProfileImage);
        Utils.setImageProfile(activity, strReceiveProfile, rivProfileImage);

        tvName.setText(strReceiveName);

        firebaseDatabase = FirebaseDatabase.getInstance();

//        firebaseDatabase.getReference().child(Constants.FIREBASE_FOLDER_ACTIVE_USER).child(strReceiveUserId).setValue("1");


        if (!Utils.isValidationEmpty(strSenderId) && !Utils.isValidationEmpty(strReceiveUserId) && Integer.parseInt(strSenderId) > Integer.parseInt(strReceiveUserId)) {
            chattingDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_FOLDER_CHATTING).child(strReceiveUserId + "_" + strSenderId);
        } else {
            chattingDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_FOLDER_CHATTING).child(strSenderId + "_" + strReceiveUserId);
        }


        chattingDatabaseRef.addValueEventListener(listener);

        databaseReferenceActiveUser = FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_FOLDER_ACTIVE_USER)
                .child(strReceiveUserId);
        databaseReferenceActiveUser.addValueEventListener(valueEventListenerActiveUser);
    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
            if (chattingMsgList == null) {
                chattingMsgList = new ArrayList<>();
            }
            chattingMsgList.clear();

//            Logger.d("test_chatting_onDataChange", dataSnapshot.toString());
//            Logger.d("test_chatting_onDataChange_getChildren", dataSnapshot.getValue());

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                if (snapshot != null) {
//                    Logger.d("test_chatting_onDataChange_getChildren_snapshot", snapshot.getValue());

                    HashMap<String, String> mMap = (HashMap<String, String>) snapshot.getValue();
                    if (mMap != null) {
                        LoginDataModel model = new LoginDataModel();
                        for (String key : mMap.keySet()) {
                            Object object = mMap.get(key);
                            String value = String.valueOf(object);

                            if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_MESSAGE_STATUS)) {
                                model.setMessage_status(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_MESSAGE)) {
                                model.setMessage(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_NAME)) {
                                model.setName(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_TIME)) {
                                model.setTime(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_USER_ID)) {
                                model.setUserId(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_DEVICE_TOKEN)) {
                                model.setDevice_token(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_DEVICE_TYPE)) {
                                model.setDevice_type(value);
                            }
                        }

                        if (snapshot.getKey() != null && model.getUserId() != null && model.getUserId().equalsIgnoreCase(strReceiveUserId)
                                && model.getMessage_status() != null && !model.getMessage_status().equals(Constants.FIREBASE_VALUE_MESSAGE_STATUS_2)) {
                            chattingDatabaseRef.child(snapshot.getKey()).child(Constants.FIREBASE_KEY_MESSAGE_STATUS).setValue(Constants.FIREBASE_VALUE_MESSAGE_STATUS_2);
                        }

                        chattingMsgList.add(model);
                    }
                }

            }


//            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                HashMap<String, String> meMap = (HashMap<String, String>) snapshot.getValue();
//
//                Iterator stringIterator = meMap.keySet().iterator();
//                ChattingMsgModel chatModel = new ChattingMsgModel();
//                while (stringIterator.hasNext()) {
//                    String key = (String) stringIterator.next();
//                    String value = meMap.get(key);
//
//                    if (key.equalsIgnoreCase(Constants.SENDER_TIME)) {
//                        chatModel.setTime(value);
//                    } else if (key.equalsIgnoreCase(Constants.SENDER_MSG)) {
//                        chatModel.setTextMsg(value);
//                    } else if (key.equalsIgnoreCase(Constants.SENDER_USER_ID) || key.equalsIgnoreCase("userId")) {
//                        chatModel.setUserId(value);
//                    } else if (key.equalsIgnoreCase(Constants.SENDER_NAME)) {
//                        chatModel.setName(value);
//                    } else if (key.equalsIgnoreCase(Constants.SENDER_PROFILE)) {
//                        chatModel.setImage(value);
//                    } else if (key.equalsIgnoreCase(Constants.SENDER_MESSAGE_STATUS)) {
//                        chatModel.setMsgStatus(value);
//                    }
//                    chatModel.setKey(snapshot.getKey());
//
//                }
//                if (snapshot.getKey() != null && chatModel.getUserId() != null && chatModel.getUserId().equalsIgnoreCase(strReceiveUserId)
//                        && chatModel.getMsgStatus() != null && chatModel.getMsgStatus().equalsIgnoreCase("1")) {
//                    chattingDatabaseRef.child(snapshot.getKey()).child(Constants.SENDER_MESSAGE_STATUS).setValue("2");
//                }
////                chattingMsgList.add(chatModel);
//            }


//            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                if (snapshot != null) {
//
//                    HashMap<String, String> mMap = (HashMap<String, String>) snapshot.getValue();
//
//                    if (mMap != null) {
//                        LoginDataModel model = new LoginDataModel();
//                        for (String key : mMap.keySet()) {
//                            String value = mMap.get(key);
//
//
//                            if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_MESSAGE_STATUS)) {
//                                model.setMessage_status(value);
//                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_MESSAGE)) {
//                                model.setMessage(value);
//                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_NAME)) {
//                                model.setName(value);
//                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_TIME)) {
//                                model.setTime(value);
//                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_USER_ID)) {
//                                model.setUserId(value);
//                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_DEVICE_TOKEN)) {
//                                model.setDevice_token(value);
//                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_DEVICE_TYPE)) {
//                                model.setDevice_type(value);
//                            }
//
//                        }
//                        chattingMsgList.add(model);
//                    }
//
//                }
//            }

            FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_FOLDER_CHAT_LIST).child(strSenderId).child(strReceiveUserId).child(Constants.FIREBASE_KEY_READ_COUNT).setValue(Constants.FIREBASE_VALUE_READ_COUNT_0);

            if (chattingMsgAdapter == null) {
                chattingMsgAdapter = new ChattingMsgAdapter(activity, chattingMsgList, strSenderId);
                rvChattingMsg.setAdapter(chattingMsgAdapter);
            } else {
                chattingMsgAdapter.notifyDataSetChanged();

            }
            if (chattingMsgList != null && chattingMsgList.size() > 0) {
                rvChattingMsg.smoothScrollToPosition(chattingMsgList.size() - 1);
            }


        }

        @Override
        public void onCancelled(@NotNull DatabaseError databaseError) {
            Log.e("Chatting", "Chatting---------error---------->" + databaseError);
        }
    };

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ivBack:
//                onBackPressed();
//                break;
//
////            case R.id.ivSendMsg:
////                if (Utils.isValidationEmpty(etWriteMsg.getText().toString().trim())) {
////                    Toast.makeText(activity, activity.getResources().getString(R.string.valid_empty_message), Toast.LENGTH_SHORT).show();
////                } else {
////                    String key = Utils.TimeStamp();
////                    chattingDatabaseRef.child(key).child(Constants.SENDER_TIME).setValue(Utils.CurrentChatTime(""));
////                    chattingDatabaseRef.child(key).child(Constants.SENDER_MSG).setValue(etWriteMsg.getText().toString());
////                    chattingDatabaseRef.child(key).child(Constants.SENDER_USER_ID).setValue(strSenderId);
////                    chattingDatabaseRef.child(key).child(Constants.SENDER_NAME).setValue(strSenderName);
////                    chattingDatabaseRef.child(key).child(Constants.SENDER_PROFILE).setValue(strSenderProfile);
////                    chattingDatabaseRef.child(key).child(Constants.SENDER_MESSAGE_STATUS).setValue("1"); //1 for send , 2 for read
////                    etWriteMsg.getText().clear();
////                }
//        }
//    }

    public boolean isValid() {
        if (Utils.isValidationEmpty(etWriteMsg.getText().toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_write_msg));
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        chattingDatabaseRef.removeEventListener(listener);
        databaseReferenceActiveUser.removeEventListener(valueEventListenerActiveUser);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.setActiveUser(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.setActiveUser(false);
        chattingDatabaseRef.removeEventListener(listener);
        databaseReferenceActiveUser.removeEventListener(valueEventListenerActiveUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.setActiveUser(true);
    }


    ValueEventListener valueEventListenerActiveUser = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.getValue() != null) {
                String value = snapshot.getValue(String.class);
                if (value != null && !Utils.isValidationEmpty(value) && value.equals(Constants.FIREBASE_VALUE_ACTIVE_USER_1)) {
                    tvOnlineUser.setText(activity.getResources().getString(R.string.active_now));
                } else {
                    tvOnlineUser.setText(activity.getResources().getString(R.string.active_user_offline));
                }
            } else {
                tvOnlineUser.setText(activity.getResources().getString(R.string.active_user_offline));
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Logger.d("test_firebase_onCancelled", error.getMessage());
            tvOnlineUser.setText(activity.getResources().getString(R.string.active_user_offline));
        }
    };
}
