package com.plugspace.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.plugspace.R;
import com.plugspace.activities.ChattingMsgActivity;
import com.plugspace.adapters.MyChatListAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatListFragment extends BaseFragment implements MyChatListAdapter.MyListener {
    Activity activity;
    private LinearLayout llEmptyList;
    RecyclerView rvModel;
    //    TextView tvNoDataFound;
    MyChatListAdapter myChatListAdapter;
    //    String strReceiveUserId = "";
    ArrayList<LoginDataModel> chatList = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReferenceChatList;
    //    FirebaseDatabase firebaseDatabase;
    LoginDataModel dataModelLogin;
    private String userIdLogin = "";
    public static Interfaces.OnGetReadCountClickListener onGetReadCountClickListener;

    public static void setOnGetReadCountClickListener(Interfaces.OnGetReadCountClickListener onGetReadCountClickListener) {
        ChatListFragment.onGetReadCountClickListener = onGetReadCountClickListener;
    }

    public static ChatListFragment newInstance(/*ArrayList<LoginDataModel> chatList,*/ int position) {
        ChatListFragment fragmentFirst = new ChatListFragment();
        Bundle args = new Bundle();
//        args.putSerializable(Constants.ARRAY_LIST, chatList);
        args.putInt(Constants.position, position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        chatList = (getArguments() != null ? (ArrayList<LoginDataModel>) getArguments().getSerializable(Constants.ARRAY_LIST) : null);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        activity = getActivity();
//        for (int i = 0; i < chatList.size(); i++) {
//            strReceiveUserId = chatList.get(i).getUserId();
//        }
        getPreviousData();
        initView(view);
        return view;
    }

    private void getPreviousData() {
        dataModelLogin = Preferences.GetLoginDetails();
        if (dataModelLogin != null) {
            userIdLogin = dataModelLogin.getUserId();
        }
    }

    private void initView(View view) {
//        tvNoDataFound = view.findViewById(R.id.tvNoDataFound);
        llEmptyList = view.findViewById(R.id.llEmptyList);
        TextView tvEmptyList = view.findViewById(R.id.tvEmptyList);
        tvEmptyList.setText(activity.getResources().getString(R.string.empty_list_match_chat));

        rvModel = view.findViewById(R.id.rvModel);
        rvModel.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        if (chatList != null && chatList.size() > 0) {
            llEmptyList.setVisibility(View.GONE);
            rvModel.setVisibility(View.VISIBLE);
//            myChatListAdapter = new MyChatListAdapter(activity, chatList, ChatListFragment.this);
//            rvModel.setAdapter(myChatListAdapter);
        } else {
            llEmptyList.setVisibility(View.VISIBLE);
            rvModel.setVisibility(View.GONE);
        }


        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
//        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceChatList = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_FOLDER_CHAT_LIST).child(userIdLogin);
//        // : 19/1/22 Test: above below
//        chattingDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_FOLDER_CHAT_LIST).child("6");
//        chattingDatabaseRef.addValueEventListener(listener);

//        chattingDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                showProgressDialog(activity);
//
//                if (chatList == null) {
//                    chatList = new ArrayList<>();
//                }
//                chatList.clear();
//
//                Logger.d("test_chat_list_snapshot", dataSnapshot.toString());
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                    if (snapshot != null) {
//                        LoginDataModel model = new LoginDataModel();
//                        HashMap<String, String> mMap = (HashMap<String, String>) snapshot.getValue();
//
//                        if (mMap != null) {
//                            for (String key : mMap.keySet()) {
//                                String value = mMap.get(key);
//
//
//                                if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_READ_COUNT)) {
//                                    model.setRead_count(value);
//                                } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_MESSAGE)) {
//                                    model.setMessage(value);
//                                } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_NAME)) {
//                                    model.setName(value);
//                                } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_TIME)) {
//                                    model.setTime(value);
//                                } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_USER_ID)) {
//                                    model.setUserId(value);
//                                } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_PROFILE)) {
//                                    model.setProfile(value);
//                                } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_DEVICE_TOKEN)) {
//                                    model.setDevice_token(value);
//                                } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_DEVICE_TYPE)) {
//                                    model.setDevice_type(value);
//                                }
//
//                            }
//                            chatList.add(model);
//                        }
//
//                    }
//                }
//
//                if (myChatListAdapter != null) {
//                    myChatListAdapter.notifyDataSetChanged();
//                } else {
//                    myChatListAdapter = new MyChatListAdapter(activity, chatList, ChatListFragment.this);
//                    rvModel.setAdapter(myChatListAdapter);
//                }
//
//                if (chatList != null && chatList.size() > 0) {
//                    tvNoDataFound.setVisibility(View.GONE);
//                    rvModel.setVisibility(View.VISIBLE);
//                } else {
//                    tvNoDataFound.setVisibility(View.VISIBLE);
//                    rvModel.setVisibility(View.GONE);
//                }
//
//                if (chatList != null && chatList.size() > 0) {
//                    rvModel.smoothScrollToPosition(chatList.size() - 1);
//                }
//
//                hideProgressDialog(activity);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Logger.d("test_chat_list_snapshot", error.toString());
//            }
//        });

        databaseReferenceChatList.addValueEventListener(valueEventListenerChatList);

    }

    ValueEventListener valueEventListenerChatList = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            showProgressDialog(activity);

            if (chatList == null) {
                chatList = new ArrayList<>();
            }
            chatList.clear();
            int readCount = 0;
            Logger.d("test_chat_list_snapshot", dataSnapshot.toString());

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                if (snapshot != null) {
                    LoginDataModel model = new LoginDataModel();
                    HashMap<String, String> mMap = (HashMap<String, String>) snapshot.getValue();

                    if (mMap != null) {
                        for (String key : mMap.keySet()) {
                            Object object = mMap.get(key);
                            String value = String.valueOf(object);

                            if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_READ_COUNT)) {
                                model.setRead_count(value);

                                if (!Utils.isValidationEmpty(value)) {
                                    readCount += Integer.parseInt(value);
                                }
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_MESSAGE)) {
                                model.setMessage(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_NAME)) {
                                model.setName(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_TIME)) {
                                model.setTime(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_USER_ID)) {
                                model.setUserId(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_PROFILE)) {
                                model.setProfile(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_DEVICE_TOKEN)) {
                                model.setDevice_token(value);
                            } else if (key.equalsIgnoreCase(Constants.FIREBASE_KEY_DEVICE_TYPE)) {
                                model.setDevice_type(value);
                            }

                        }
                        chatList.add(model);
                    }

                }
            }

            if (myChatListAdapter != null) {
                myChatListAdapter.notifyDataSetChanged();
            } else {
                myChatListAdapter = new MyChatListAdapter(activity, chatList, ChatListFragment.this);
                rvModel.setAdapter(myChatListAdapter);
            }

            if (chatList != null && chatList.size() > 0) {
                llEmptyList.setVisibility(View.GONE);
                rvModel.setVisibility(View.VISIBLE);
            } else {
                llEmptyList.setVisibility(View.VISIBLE);
                rvModel.setVisibility(View.GONE);
            }

            if (chatList != null && chatList.size() > 0) {
                rvModel.smoothScrollToPosition(chatList.size() - 1);
            }

            if (onGetReadCountClickListener != null) {
                int totalUserCount = chatList == null ? 0 : chatList.size();
                onGetReadCountClickListener.getReadCountClick(readCount, totalUserCount);
            }

            hideProgressDialog(activity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Logger.d("test_chat_list_snapshot", error.toString());
        }
    };

//    ValueEventListener listener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            chatList.clear();
//
//            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                if (snapshot != null) {
//                    Logger.d("test_chat_list_snapshot", snapshot.toString());
//                    Logger.d("test_chat_list_snapshot_key", snapshot.getKey());
//                    Logger.d("test_chat_list_snapshot_value", snapshot.getValue());
//                }
//
//////                    Log.e("Chatting", "snapshot-------->" + snapshot);
//////                    Log.e("Chatting", "Key-------->" + snapshot.getKey());
//////                    Log.e("Chatting", "Value-------->" + snapshot.getValue());
////                HashMap<String, String> meMap = (HashMap<String, String>) snapshot.getValue();
////
////                Iterator stringIterator = meMap.keySet().iterator();
////                ChattingMsgModel chatModel = new ChattingMsgModel();
////                while (stringIterator.hasNext()) {
////                    String key = (String) stringIterator.next();
////                    String value = (String) meMap.get(key);
////
////                    if (key.equalsIgnoreCase(Constants.SENDER_TIME)) {
////                        chatModel.setTime(value);
////                    } else if (key.equalsIgnoreCase(Constants.SENDER_MSG)) {
////                        chatModel.setTextMsg(value);
////                    } else if (key.equalsIgnoreCase(Constants.SENDER_USER_ID) || key.equalsIgnoreCase("userId")) {
////                        chatModel.setUserId(value);
////                    } else if (key.equalsIgnoreCase(Constants.SENDER_NAME)) {
////                        chatModel.setName(value);
////                    } else if (key.equalsIgnoreCase(Constants.SENDER_PROFILE)) {
////                        chatModel.setImage(value);
////                    } else if (key.equalsIgnoreCase(Constants.SENDER_MESSAGE_STATUS)) {
////                        chatModel.setMsgStatus(value);
////                    }
////
//////                        Log.e("Chatting", "KEY_&_VALUE-----chatting------>" + key + "," + value);
//////                        Log.e("Chatting", "STATUS -> " + chatModel.getMsgStatus());
//////                        Log.e("Chatting", "USER_ID -> " + chatModel.getUserId());
////                    chatModel.setKey(snapshot.getKey());
////
////                }
////                if (snapshot.getKey() != null && chatModel.getUserId() != null && chatModel.getUserId().equalsIgnoreCase(strReceiveUserId)
////                        && chatModel.getMsgStatus() != null && chatModel.getMsgStatus().equalsIgnoreCase("1")) {
////                    chattingDatabaseRef.child(snapshot.getKey()).child(Constants.SENDER_MESSAGE_STATUS).setValue("2");
////                }
////                chatList.add(chatModel);
//            }
//
//            if (chatList != null && chatList.size() > 0) {
//                tvNoDataFound.setVisibility(View.GONE);
//                rvModel.setVisibility(View.VISIBLE);
//                myChatListAdapter = new MyChatListAdapter(activity, chatList, ChatListFragment.this);
//                rvModel.setAdapter(myChatListAdapter);
//            } else {
//                tvNoDataFound.setVisibility(View.VISIBLE);
//                rvModel.setVisibility(View.GONE);
//            }
//
//            if (chatList != null && chatList.size() > 0) {
//                rvModel.smoothScrollToPosition(chatList.size() - 1);
//            }
//
//
//        }
//
//        @Override
//        public void onCancelled(@NotNull DatabaseError databaseError) {
//            Log.e("Chatting", "Chatting---------error---------->" + databaseError);
//        }
//    };

    @Override
    public void myChatClicked(int position) {
        LoginDataModel model = chatList.get(position);
        startActivity(new Intent(activity, ChattingMsgActivity.class)
                .putExtra(Constants.DATA_MODEL, model));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReferenceChatList.removeEventListener(valueEventListenerChatList);
    }
}
