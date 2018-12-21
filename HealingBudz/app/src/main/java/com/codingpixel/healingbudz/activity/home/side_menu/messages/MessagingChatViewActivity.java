package com.codingpixel.healingbudz.activity.home.side_menu.messages;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.MessagesChatMsgsDataModel;
import com.codingpixel.healingbudz.DataModel.MessagesDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.CreateGroupSuccessfully;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupSettingActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.MessagingChatViewRecylerAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomEditText;
import com.codingpixel.healingbudz.customeUI.ProgressDialogVideoProcessing;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotationVideo;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.send_message;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.send_message_after_media;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.send_message_meida;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.send_msg_video;
import static com.codingpixel.healingbudz.network.model.URL.get_detail_chat;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.videos_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class MessagingChatViewActivity extends AppCompatActivity implements View.OnClickListener, MessagingChatViewRecylerAdapter.ItemClickListener, APIResponseListner {
    ImageView Back, Home;
    RecyclerView chat_recyler_view;
    SwipeRefreshLayout refreshLayout;
    ImageView Send_Msg_Button;
    CustomEditText Msg_Text;
    TextView User_Name;
    JSONObject json_object;
    RelativeLayout Attachment;
    ImageView User_img, profile_img_topi;
    long totalSize = 0;
    ProgressDialogVideoProcessing video_processing;
    ImageView Attatchment_Image, Attachment_video_icon, Attachment_cross;
    public static MessagesDataModel chat_message_data_modal;
    LinearLayoutManager linearLayoutManager;
    MessagingChatViewRecylerAdapter recyler_adapter;
    ImageView Msg_attachment;
    int dummy_msg_reply_number = 0;
    String Attached_video_path = "";
    String Attached_image_path = "";
    Drawable Attached_image = null;
    LinearLayout user_profile;
    Drawable Attached_Video_thumbnil = null;
    ArrayList<MessagesChatMsgsDataModel> data = new ArrayList<>();
    private Socket mSocket;
    private RelativeLayout enter_msg;
    boolean isUrlSend = false;

    {
        try {
            mSocket = IO.socket(URL.socket_url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getNotify()) {
            if (!this.isDestroyed()) {
                finish();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    void callRefresh() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (user.getUser_id() == chat_message_data_modal.getSender_id()) {
                jsonObject.put("user_id", chat_message_data_modal.getReceiver_id());
            } else {
                jsonObject.put("user_id", chat_message_data_modal.getSender_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new VollyAPICall(MessagingChatViewActivity.this, false, get_detail_chat, jsonObject, user.getSession_key(), Request.Method.POST, MessagingChatViewActivity.this, APIActions.ApiActions.get_detail_chat);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging_chat_view);
        mSocket.connect();
        mSocket.on("message_send", onNewMessage);
        ChangeStatusBarColor(MessagingChatViewActivity.this, "#171717");
        HideKeyboard(MessagingChatViewActivity.this);
        Msg_Text = (CustomEditText) findViewById(R.id.msg_text);
        user_profile = findViewById(R.id.user_profile);
        User_Name = (TextView) findViewById(R.id.user_name);
        User_img = (ImageView) findViewById(R.id.user_img);
        profile_img_topi = (ImageView) findViewById(R.id.profile_img_topi);
        Msg_attachment = (ImageView) findViewById(R.id.attach_ment);
        Send_Msg_Button = (ImageView) findViewById(R.id.send_buuton);
        Back = (ImageView) findViewById(R.id.back_btn);
        Home = (ImageView) findViewById(R.id.home_btn);
        Back.setOnClickListener(this);
        Home.setOnClickListener(this);
        enter_msg = (RelativeLayout) findViewById(R.id.enter_msg);
        Msg_Text.setKeyImeChangeListener(new CustomEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
//                Msg_Text.clearFocus();
//                enter_msg.setBackgroundColor(Color.parseColor("#5c5c5c"));
            }
        });
        Msg_Text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            //            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    enter_msg.setBackgroundColor(Color.parseColor("#FFFFFF"));
//
//                } else {
//                    enter_msg.setBackgroundColor(Color.parseColor("#5c5c5c"));
//                }
            }
        });
        Attatchment_Image = (ImageView) findViewById(R.id.attachment_img);
        Attachment = (RelativeLayout) findViewById(R.id.attachment);
        Attachment_video_icon = (ImageView) findViewById(R.id.attachment_video_icon);
        Attachment_cross = (ImageView) findViewById(R.id.attachment_cross);
        chat_recyler_view = (RecyclerView) findViewById(R.id.group_recyler_view);
        linearLayoutManager = new LinearLayoutManager(MessagingChatViewActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        chat_recyler_view.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemAnimator animator = chat_recyler_view.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyler_adapter = new MessagingChatViewRecylerAdapter(this, mSocket, data);
        chat_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(MessagingChatViewActivity.this);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#86887c"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                callRefresh();
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    if (user.getUser_id() == chat_message_data_modal.getSender_id()) {
//                        jsonObject.put("user_id", chat_message_data_modal.getReceiver_id());
//                    } else {
//                        jsonObject.put("user_id", chat_message_data_modal.getSender_id());
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                new VollyAPICall(MessagingChatViewActivity.this, false, get_detail_chat, jsonObject, user.getSession_key(), Request.Method.POST, MessagingChatViewActivity.this, APIActions.ApiActions.get_detail_chat);

            }
        });
        chat_recyler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(chat_recyler_view.getLayoutManager());
                int lastVisible = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (lastVisible < 1) {
                    refreshLayout.setEnabled(true);
                }
            }
        });

        Attachment_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attachment.setVisibility(View.GONE);
                Attached_video_path = "";
                Attached_image_path = "";
                Attached_image = null;
                Attatchment_Image.setBackground(null);
            }
        });

        Send_Msg_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Attached_image == null && Attached_video_path.length() == 0) {
                    if (Msg_Text.getText().toString().trim().length() > 0) {
                        JSONObject object = new JSONObject();
                        List<String> urlsTest = Utility.extractURL(Msg_Text.getText().toString());
                        if (urlsTest.size() > 0) {
                            isUrlSend = true;
                        } else {
                            isUrlSend = false;
                            try {
                                if (chat_message_data_modal.getSender_id() == user.getUser_id()) {
                                    object.put("user_id", chat_message_data_modal.getReceiver_id());
                                } else {
                                    object.put("user_id", chat_message_data_modal.getSender_id());
                                }
                                object.put("other_id", user.getUser_id());
                                object.put("other_name", user.getFirst_name());
                                object.put("photo", "");
                                object.put("text", Msg_Text.getText().toString());
                                object.put("file", "");
                                object.put("file_type", "");
                                object.put("file_poster", "");
                                object.put("images_base", "");
                                object.put("video_base", "");
                                object.put("site_url", "");
                                object.put("site_content", "");
                                object.put("site_extracted_url", "");
                                object.put("site_title", "");
                                object.put("site_image", "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            boolean isSocketConnected = mSocket.connected();
                            if (isSocketConnected) {
                                Log.d("soket", "connectedddd");
                                mSocket.emit("message_get", object);
                            } else {

                                Log.d("soket", "not connecteddddddd");
                                mSocket.emit("message_get", object);
                            }
                        }


                        data.add(new MessagesChatMsgsDataModel(Msg_Text.getText().toString(), R.drawable.ic_profile_gray, null, false, false, false, getCurrentDate(), false));
                        recyler_adapter.notifyItemInserted(data.size());
                        chat_recyler_view.scrollToPosition(recyler_adapter.getItemCount() - 1);
                        linearLayoutManager.scrollToPosition(recyler_adapter.getItemCount() - 1);

                        JSONObject json_object = new JSONObject();
                        try {
                            json_object.put("message", Msg_Text.getText().toString());
                            List<String> urls = Utility.extractURL(Msg_Text.getText().toString());
                            if (urls.size() > 0) {
                                json_object.put("url", urls.get(0));
                            } else {
                                json_object.put("url", "");
                            }
                            if (chat_message_data_modal.getSender_id() == user.getUser_id()) {
                                json_object.put("receiver_id", chat_message_data_modal.getReceiver_id());
                            } else {
                                json_object.put("receiver_id", chat_message_data_modal.getSender_id());
                            }
                            json_object.put("image", "");
                            json_object.put("video", "");
                            new VollyAPICall(MessagingChatViewActivity.this, false, URL.send_message, json_object, user.getSession_key(), Request.Method.POST, MessagingChatViewActivity.this, send_message);
                            Msg_Text.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (Attached_image != null) {
                        MessagesChatMsgsDataModel dataModel = new MessagesChatMsgsDataModel();
                        dataModel.setMsg_TExt(Msg_Text.getText().toString());
                        dataModel.setImageMsg(true);
                        dataModel.setVideoMsg(true);
                        dataModel.setTimeItem(false);
                        dataModel.setReceiver(false);
                        dataModel.setUploadinStart(true);
                        dataModel.setLoacal_image_drawable(Attached_image);
                        dataModel.setLocal_video_thumbnil(null);
                        dataModel.setLocal_image_path(Attached_image_path);
                        dataModel.setLocal_video_path(Attached_video_path);
                        dataModel.setImage_Path("");
                        if (chat_message_data_modal.getSender_id() == user.getUser_id()) {
                            dataModel.setReceiver_id(chat_message_data_modal.getReceiver_id());
                        } else {
                            dataModel.setReceiver_id(chat_message_data_modal.getSender_id());
                        }
                        dataModel.setAdded_date(getCurrentDate());
                        dataModel.setCreateDate(getCurrentDate());
                        data.add(dataModel);
                    } else if (Attached_video_path.length() > 0) {
                        MessagesChatMsgsDataModel dataModel = new MessagesChatMsgsDataModel();
                        dataModel.setMsg_TExt(Msg_Text.getText().toString());
                        dataModel.setImageMsg(false);
                        dataModel.setVideoMsg(true);
                        dataModel.setTimeItem(false);
                        dataModel.setReceiver(false);
                        dataModel.setAdded_date(getCurrentDate());
                        dataModel.setCreateDate(getCurrentDate());
                        dataModel.setVideo_path("");
                        dataModel.setVideo_thumbni("");
                        dataModel.setUploadinStart(true);
                        if (chat_message_data_modal.getSender_id() == user.getUser_id()) {
                            dataModel.setReceiver_id(chat_message_data_modal.getReceiver_id());
                        } else {
                            dataModel.setReceiver_id(chat_message_data_modal.getSender_id());
                        }
                        dataModel.setLoacal_image_drawable(null);
                        dataModel.setLocal_video_thumbnil(Attached_Video_thumbnil);
                        dataModel.setLocal_video_path(Attached_video_path);
                        data.add(dataModel);
                    }
                    recyler_adapter.notifyItemInserted(data.size());
                    chat_recyler_view.scrollToPosition(recyler_adapter.getItemCount() - 1);
                    linearLayoutManager.scrollToPosition(recyler_adapter.getItemCount() - 1);
                    Attachment.setVisibility(View.GONE);
                    Attached_image = null;
                    Attached_video_path = "";
                    Attached_image_path = "";
                    Attached_Video_thumbnil = null;
                    Attatchment_Image.setBackground(null);
                    Msg_Text.setText("");
                }
            }
        });

        Msg_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessagingChatViewActivity.this, HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", true);
                startActivityForResult(intent, 1200);
            }
        });


        SetData();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MessagingChatViewActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject json_data = (JSONObject) args[0];
                    try {
                        if (json_data.getInt("user_id") == user.getUser_id()) {
                            MessagesChatMsgsDataModel dataModel = new MessagesChatMsgsDataModel();
                            if (json_data.getString("file_type").equalsIgnoreCase("image")) {
                                dataModel.setImageMsg(true);
                                dataModel.setVideoMsg(false);
                                dataModel.setImage_Path(json_data.optString("file"));
                                dataModel.setVideo_path("");
                                dataModel.setVideo_thumbni("");
                            } else if (json_data.getString("file_type").equalsIgnoreCase("video")) {
                                dataModel.setImageMsg(false);
                                dataModel.setVideoMsg(true);
                                dataModel.setImage_Path("");
                                dataModel.setVideo_path(json_data.optString("file"));
                                dataModel.setVideo_thumbni(json_data.optString("file_poster"));
                            } else {
                                dataModel.setVideoMsg(false);
                                dataModel.setImageMsg(false);
                            }
                            if (user.getUser_id() == json_data.getInt("other_id")) {
                                dataModel.setReceiver(false);
                            } else {
                                dataModel.setReceiver(true);
                            }
                            dataModel.setMsg_TExt(json_data.getString("text"));
                            dataModel.setAdded_date(getCurrentDate());
                            dataModel.setCreateDate(getCurrentDate());
                            data.add(dataModel);
                            recyler_adapter.notifyItemInserted(data.size());
                            chat_recyler_view.scrollToPosition(recyler_adapter.getItemCount() - 1);
                            linearLayoutManager.scrollToPosition(recyler_adapter.getItemCount() - 1);
                            refreshLayout.setRefreshing(true);
                            callRefresh();
                        }
                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };


    public void SetData() {

        if (chat_message_data_modal.getSender_image_path() != null && chat_message_data_modal.getReceiver_image_path() != null) {
            setDataUser();
        } else {
            user_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chat_message_data_modal.getReceiver_id() != Splash.user.getUser_id())
                        GoToProfile(v.getContext(), chat_message_data_modal.getReceiver_id());
                    else {
                        GoToProfile(v.getContext(), chat_message_data_modal.getSender_id());
                    }
                }
            });
            User_Name.setText(chat_message_data_modal.getReceiver_first_name());
            if (chat_message_data_modal.getReceiver_avatar() != null && chat_message_data_modal.getReceiver_avatar().length() > 4) {
                Glide.with(this)
                        .load(images_baseurl + chat_message_data_modal.getReceiver_image_path())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.ic_user_profile).error(R.drawable.noimage)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                User_img.setImageDrawable(resource);
                                return false;
                            }
                        })
                        .into(User_img);
            }
            if (chat_message_data_modal.getSpecial_icon_rec() != null && chat_message_data_modal.getSpecial_icon_rec().length() > 4) {
                profile_img_topi.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(images_baseurl + chat_message_data_modal.getSpecial_icon_rec())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.topi_ic).error(R.drawable.noimage)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                profile_img_topi.setImageDrawable(resource);
                                return false;
                            }
                        })
                        .into(profile_img_topi);
            } else {
                profile_img_topi.setVisibility(View.GONE);
            }
        }
        refreshLayout.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        try {
            if (user.getUser_id() == chat_message_data_modal.getSender_id()) {
                jsonObject.put("user_id", chat_message_data_modal.getReceiver_id());
            } else {
                jsonObject.put("user_id", chat_message_data_modal.getSender_id());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new VollyAPICall(MessagingChatViewActivity.this, false, get_detail_chat, jsonObject, user.getSession_key(), Request.Method.POST, MessagingChatViewActivity.this, APIActions.ApiActions.get_detail_chat);
    }

    private void setDataUser() {
        String icon_path = "";
        String icon_path_sp = "";
        String Name = "";
        if (user.getUser_id() == chat_message_data_modal.getReceiver_id()) {
            if (chat_message_data_modal.getSender_image_path().length() > 8) {
                if ((chat_message_data_modal.getSender_image_path().contains("facebook.com") ||chat_message_data_modal.getSender_image_path().contains("https") ||chat_message_data_modal.getSender_image_path().contains("http") || chat_message_data_modal.getSender_image_path().contains("google.com") || chat_message_data_modal.getSender_image_path().contains("googleusercontent.com")))
                    icon_path = chat_message_data_modal.getSender_image_path();
                else
                    icon_path = images_baseurl + chat_message_data_modal.getSender_image_path();
            } else {
                icon_path = images_baseurl + chat_message_data_modal.getSender_avatar();
            }
            if (chat_message_data_modal.getSpecial_icon_sen().length() > 8) {
                profile_img_topi.setVisibility(View.VISIBLE);
                icon_path_sp = images_baseurl + chat_message_data_modal.getSpecial_icon_sen();
            } else {
                profile_img_topi.setVisibility(View.GONE);
            }
            Name = chat_message_data_modal.getSender_first_name();

        } else if (user.getUser_id() == chat_message_data_modal.getSender_id()) {
            if (chat_message_data_modal.getReceiver_image_path().length() > 8) {
                if ((chat_message_data_modal.getReceiver_image_path().contains("facebook.com") ||chat_message_data_modal.getReceiver_image_path().contains("http") ||chat_message_data_modal.getReceiver_image_path().contains("https") || chat_message_data_modal.getReceiver_image_path().contains("google.com") || chat_message_data_modal.getReceiver_image_path().contains("googleusercontent.com")))
                    icon_path = chat_message_data_modal.getReceiver_image_path();
                else {
                    icon_path = images_baseurl + chat_message_data_modal.getReceiver_image_path();
                }
            } else {
                icon_path = images_baseurl + chat_message_data_modal.getReceiver_avatar();
            }
            if (chat_message_data_modal.getSpecial_icon_rec().length() > 8) {
                profile_img_topi.setVisibility(View.VISIBLE);
                icon_path_sp = images_baseurl + chat_message_data_modal.getSpecial_icon_rec();
            } else {
                profile_img_topi.setVisibility(View.GONE);
            }
            Name = chat_message_data_modal.getReceiver_first_name();
        } else {
            if (chat_message_data_modal.getReceiver_image_path().length() > 8) {
                if ((chat_message_data_modal.getReceiver_image_path().contains("facebook.com") ||chat_message_data_modal.getReceiver_image_path().contains("https") ||chat_message_data_modal.getReceiver_image_path().contains("https") || chat_message_data_modal.getReceiver_image_path().contains("google.com") || chat_message_data_modal.getReceiver_image_path().contains("googleusercontent.com")))
                    icon_path = chat_message_data_modal.getReceiver_image_path();
                else {
                    icon_path = images_baseurl + chat_message_data_modal.getReceiver_image_path();
                }
            } else {
                icon_path = images_baseurl + chat_message_data_modal.getReceiver_avatar();
            }
            if (chat_message_data_modal.getSpecial_icon_rec().length() > 8) {
                profile_img_topi.setVisibility(View.VISIBLE);
                icon_path_sp = images_baseurl + chat_message_data_modal.getSpecial_icon_rec();
            } else {
                profile_img_topi.setVisibility(View.GONE);
            }
//            icon_path = images_baseurl + chat_message_data_modal.getReceiver_image_path();
            Name = chat_message_data_modal.getReceiver_first_name();
        }
        User_Name.setText(Name);
        try {
            Glide.with(this)
                    .load(icon_path)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_user_profile).error(R.drawable.noimage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            User_img.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(User_img);
            Glide.with(this)
                    .load(icon_path_sp)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.topi_ic).error(R.drawable.noimage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            profile_img_topi.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(profile_img_topi);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_message_data_modal.getReceiver_id() != Splash.user.getUser_id())
                    GoToProfile(v.getContext(), chat_message_data_modal.getReceiver_id());
                else {
                    GoToProfile(v.getContext(), chat_message_data_modal.getSender_id());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.home_btn:
                GoToHome(MessagingChatViewActivity.this, true);
                finish();
                break;
            case R.id.slide_back_btn:
                finish();
                break;
            case R.id.slide_home_btn:
                GoToHome(MessagingChatViewActivity.this, true);
                finish();
                break;
            case R.id.group_total_budz:
                Intent i = new Intent(MessagingChatViewActivity.this, CreateGroupSuccessfully.class);
                i.putExtra("isOnlyList", true);
                startActivity(i);
                break;
            case R.id.group_setting:
                Intent setting = new Intent(MessagingChatViewActivity.this, GroupSettingActivity.class);
                startActivity(setting);
                break;

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (data.get(position).isImageMsg()) {
            Intent intent = new Intent(MessagingChatViewActivity.this, MediPreview.class);
            if (data.get(position).getImage_Path().length() > 2) {
                intent.putExtra("path", images_baseurl + data.get(position).getImage_Path());
            } else {
                intent.putExtra("path", data.get(position).getLocal_image_path());
            }
            intent.putExtra("isvideo", false);
            MessagingChatViewActivity.this.startActivity(intent);
        } else if (data.get(position).isVideoMsg()) {
            Intent intent = new Intent(MessagingChatViewActivity.this, MediPreview.class);
            if (data.get(position).getImage_Path().length() > 2) {
                intent.putExtra("path", images_baseurl + data.get(position).getImage_Path());
            } else {
                if (data.get(position).getVideo_path().length() > 2) {
                    intent.putExtra("path", videos_baseurl + data.get(position).getVideo_path());
                } else {
                    intent.putExtra("path", data.get(position).getLocal_video_path());
                }
            }
            intent.putExtra("isvideo", true);
            MessagingChatViewActivity.this.startActivity(intent);
        }
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = calendar.getTime();
        return utcFormat.format(date);
//        return current_year + "-" + current_month + "-" + current_day + " " + current_hour + ":" + current_minutes + " PM";
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent image_data) {
        super.onActivityResult(requestCode, resultCode, image_data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", image_data.getExtras().getString("file_path_arg"));
            if (image_data.getExtras().getBoolean("isVideo")) {
                Attached_video_path = image_data.getExtras().getString("file_path_arg");
                if (image_data.getExtras().getBoolean("camera_video")) {
                    final String filePath = image_data.getExtras().getString("file_path_arg");
                    video_processing = ProgressDialogVideoProcessing.newInstance();
                    video_processing.show(((FragmentActivity) this).getSupportFragmentManager(), "pd");
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            new VideoProcessing().execute(filePath);
                        }
                    }, 200);
                } else {
                    Attachment.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            String filePath = image_data.getExtras().getString("file_path_arg");
                            Bitmap video_thumbnil = getVideoThumbnail(filePath);
                            Drawable drawable = null;
                            if (video_thumbnil != null) {
                                video_thumbnil = Bitmap.createScaledBitmap(video_thumbnil, 300, 300, false);
                                int corner_radious = (video_thumbnil.getWidth() * 6) / 100;
                                Bitmap bitmap = getRoundedCornerBitmap(video_thumbnil, corner_radious);
                                drawable = new BitmapDrawable(getResources(), bitmap);
                                Attatchment_Image.setBackground(drawable);
                                Attachment_video_icon.setVisibility(View.VISIBLE);
                            } else {
                                Drawable d = ContextCompat.getDrawable(MessagingChatViewActivity.this, R.drawable.test_img);
                                Bitmap bitmapOrg = ((BitmapDrawable) d).getBitmap();

                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                int corner_radious = (bitmapOrg.getWidth() * 6) / 100;
                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                drawable = new BitmapDrawable(MessagingChatViewActivity.this.getResources(), bitmap);
                                Attatchment_Image.setBackground(drawable);
                                Attachment_video_icon.setVisibility(View.VISIBLE);
                            }
                        }
                    }, 10);
                }
            } else {
                Attached_image_path = image_data.getExtras().getString("file_path_arg");
                Attachment.setVisibility(View.VISIBLE);
                Bitmap bitmapOrg = BitmapFactory.decodeFile(image_data.getExtras().getString("file_path_arg"));
                bitmapOrg = checkRotation(bitmapOrg, image_data.getExtras().getString("file_path_arg"));
                Attached_image = new BitmapDrawable(getResources(), bitmapOrg);
                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                int corner_radious = (bitmapOrg.getWidth() * 4) / 100;
                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                Attatchment_Image.setBackground(drawable);
                Attachment_video_icon.setVisibility(View.GONE);
//                Attached_image = drawable;
            }
        }
    }

    public static Bitmap getVideoThumbnail(String path) {
        Bitmap bitmap = null;
        FFmpegMediaMetadataRetriever fmmr = new FFmpegMediaMetadataRetriever();
        try {
            fmmr.setDataSource(path);

            final byte[] data = fmmr.getEmbeddedPicture();

            if (data != null) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
            if (bitmap == null) {
                bitmap = fmmr.getFrameAtTime();
            }
        } catch (Exception e) {
            bitmap = null;
        } finally {
            fmmr.release();
        }
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        refreshLayout.setRefreshing(false);
        if (apiActions == APIActions.ApiActions.get_detail_chat) {
            JSONObject object = null;
            try {
                data.clear();
                object = new JSONObject(response);
                JSONArray data_array = object.getJSONArray("successData");
                for (int x = 0; x < data_array.length(); x++) {
                    JSONObject msg_object = data_array.getJSONObject(x);
                    MessagesChatMsgsDataModel dataModel = new MessagesChatMsgsDataModel();
                    if (!Objects.equals(msg_object.optString("file_path"), "null") && !msg_object.optString("file_path").equalsIgnoreCase("null")) {
                        if (msg_object.getString("file_type").equalsIgnoreCase("Image")) {
                            dataModel.setImageMsg(true);
                            dataModel.setVideoMsg(false);
                            dataModel.setImage_Path(msg_object.optString("file_path"));
                            dataModel.setVideo_path("");
                            dataModel.setVideo_thumbni("");
                        } else {
                            dataModel.setImageMsg(false);
                            dataModel.setVideoMsg(true);
                            dataModel.setImage_Path("");
                            dataModel.setVideo_path(msg_object.optString("file_path"));
                            dataModel.setVideo_thumbni(msg_object.optString("poster"));
                        }
                    } else {
                        dataModel.setVideoMsg(false);
                        dataModel.setImageMsg(false);
                    }
                    if (user.getUser_id() == msg_object.getInt("sender_id")) {
                        dataModel.setReceiver(false);
//                        dataModel.setReceiverName(Splash.user.getFirst_name() + " " + Splash.user.getLast_name());
//                        dataModel.setSenderName(msg_object.getJSONObject("receiver").getString("name"));
                    } else {
//                        dataModel.setReceiverName(Splash.user.getFirst_name() + " " + Splash.user.getLast_name());
//                        dataModel.setSenderName(msg_object.getJSONObject("sender").getString("name"));
                        dataModel.setReceiver(true);
                    }
                    String test = msg_object.getString("message");
                    dataModel.setMsg_TExt(test);
                    dataModel.setAdded_date(msg_object.getString("created_at"));
                    dataModel.setCreateDate(msg_object.getString("created_at"));
                    data.add(dataModel);
                    recyler_adapter.notifyItemChanged(x);
                    if (chat_message_data_modal.getSender_image_path() == null && chat_message_data_modal.getReceiver_image_path() == null) {
                        chat_message_data_modal.setReceiver_id(msg_object.getJSONObject("receiver").getInt("id"));
                        chat_message_data_modal.setSender_id(msg_object.getJSONObject("sender").getInt("id"));
                        chat_message_data_modal.setSender_avatar(msg_object.getJSONObject("receiver").getString("avatar"));
                        chat_message_data_modal.setSender_id(msg_object.getJSONObject("receiver").getInt("id"));
                        chat_message_data_modal.setSender_image_path(msg_object.getJSONObject("receiver").getString("image_path"));
                        chat_message_data_modal.setReceiver_avatar(msg_object.getJSONObject("sender").getString("avatar"));
                        chat_message_data_modal.setReceiver_id(msg_object.getJSONObject("sender").getInt("id"));
                        chat_message_data_modal.setReceiver_image_path(msg_object.getJSONObject("sender").getString("image_path"));
                        chat_message_data_modal.setReceiver_first_name(msg_object.getJSONObject("sender").getString("first_name"));
                        chat_message_data_modal.setSender_first_name(msg_object.getJSONObject("receiver").getString("first_name"));
                        chat_message_data_modal.setSpecial_icon_rec(msg_object.getJSONObject("sender").getString("special_icon"));
                        chat_message_data_modal.setSpecial_icon_sen(msg_object.getJSONObject("receiver").getString("special_icon"));

                        setDataUser();
                    }
                    if (user.getUser_id() != msg_object.getJSONObject("sender").getInt("id")) {
                        chat_message_data_modal.setSender_id(msg_object.getJSONObject("sender").getInt("id"));
                        chat_message_data_modal.setSender_first_name(msg_object.getJSONObject("sender").getString("first_name"));
                        chat_message_data_modal.setSpecial_icon_sen(msg_object.getJSONObject("sender").getString("special_icon"));
                        chat_message_data_modal.setSender_avatar(msg_object.getJSONObject("sender").getString("avatar"));
                        chat_message_data_modal.setSender_image_path(msg_object.getJSONObject("sender").getString("image_path"));
                        setDataUser();
                    } else if (user.getUser_id() != msg_object.getJSONObject("receiver").getInt("id")) {
                        chat_message_data_modal.setReceiver_id(msg_object.getJSONObject("receiver").getInt("id"));
                        chat_message_data_modal.setReceiver_image_path(msg_object.getJSONObject("receiver").getString("image_path"));
                        chat_message_data_modal.setReceiver_first_name(msg_object.getJSONObject("receiver").getString("first_name"));
                        chat_message_data_modal.setSpecial_icon_rec(msg_object.getJSONObject("receiver").getString("special_icon"));
                        chat_message_data_modal.setReceiver_avatar(msg_object.getJSONObject("receiver").getString("avatar"));
                        setDataUser();

                    }
                }
                recyler_adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (apiActions == send_message) {
            Log.d("response", response);
            JSONObject object = new JSONObject();

            if (isUrlSend) {
                isUrlSend = false;
                try {
                    JSONObject responseObject = new JSONObject(response).getJSONObject("successData");
                    if (chat_message_data_modal.getSender_id() == user.getUser_id()) {
                        object.put("user_id", chat_message_data_modal.getReceiver_id());
                    } else {
                        object.put("user_id", chat_message_data_modal.getSender_id());
                    }
                    object.put("other_id", user.getUser_id());
                    object.put("other_name", user.getFirst_name());
                    object.put("photo", "");
                    object.put("text", responseObject.getString("message"));
                    object.put("file", "");
                    object.put("file_type", "");
                    object.put("file_poster", "");
                    object.put("images_base", "");
                    object.put("video_base", "");
                    object.put("site_url", responseObject.getString("site_url"));
                    object.put("site_content", responseObject.getString("site_content"));
                    object.put("site_extracted_url", responseObject.getString("site_extracted_url"));
                    object.put("site_title", responseObject.getString("site_title"));
                    object.put("site_image", responseObject.getString("site_image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                boolean isSocketConnected = mSocket.connected();
                if (isSocketConnected) {
                    Log.d("soket", "connectedddd");
                    mSocket.emit("message_get", object);
                } else {

                    Log.d("soket", "not connecteddddddd");
                    mSocket.emit("message_get", object);
                }
            }
            refreshLayout.setRefreshing(true);
            callRefresh();
        } else if (apiActions == send_message_meida) {
            Log.d("response", response);
            JSONObject response_object = null;
            try {
                response_object = new JSONObject(response);
                json_object.put("path", response_object.getString("path"));
                json_object.put("poster", response_object.getString("poster"));
                json_object.put("file_type", response_object.getString("file_type"));
                json_object.put("message", Msg_Text.getText().toString());
                json_object.put("url", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new VollyAPICall(MessagingChatViewActivity.this, false, URL.send_message, json_object, user.getSession_key(), Request.Method.POST, MessagingChatViewActivity.this, send_message_after_media);
        } else if (apiActions == send_message_after_media) {
            JSONObject object = new JSONObject();
            try {
                object.put("user_id", chat_message_data_modal.getSender_id());
                object.put("other_id", user.getUser_id());
                object.put("other_name", user.getFirst_name());
                object.put("photo", "");
                object.put("text", json_object.getString("message"));
                object.put("file", json_object.getString("path"));
                object.put("file_type", json_object.getString("file_type"));
                object.put("file_poster", json_object.getString("poster"));
                object.put("images_base", images_baseurl);
                object.put("video_base", videos_baseurl);
                object.put("site_url", "");
                object.put("site_content", "");
                object.put("site_extracted_url", "");
                object.put("site_title", "");
                object.put("site_image", "");
                mSocket.emit("message_get", object);

                if (json_object.getString("file_type").equalsIgnoreCase("image")) {
                    MessagesChatMsgsDataModel dataModel = new MessagesChatMsgsDataModel();
                    dataModel.setMsg_TExt(json_object.getString("message"));
                    dataModel.setImageMsg(true);
                    dataModel.setVideoMsg(true);
                    dataModel.setTimeItem(false);
                    dataModel.setReceiver(false);
                    dataModel.setImage_Path(object.getString("file"));
                    dataModel.setAdded_date(getCurrentDate());
                    dataModel.setCreateDate(getCurrentDate());
                    data.add(dataModel);
                } else {
                    MessagesChatMsgsDataModel dataModel = new MessagesChatMsgsDataModel();
                    dataModel.setMsg_TExt(json_object.getString("message"));
                    dataModel.setImageMsg(false);
                    dataModel.setVideoMsg(true);
                    dataModel.setTimeItem(false);
                    dataModel.setReceiver(false);
                    dataModel.setAdded_date(getCurrentDate());
                    dataModel.setCreateDate(getCurrentDate());
                    dataModel.setVideo_path(object.getString("file"));
                    dataModel.setVideo_thumbni(object.getString("file_poster"));
                    data.add(dataModel);
                }
                recyler_adapter.notifyItemInserted(data.size());
                chat_recyler_view.scrollToPosition(recyler_adapter.getItemCount() - 1);
                linearLayoutManager.scrollToPosition(recyler_adapter.getItemCount() - 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == send_msg_video) {

        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        refreshLayout.setRefreshing(false);
    }

    private class VideoProcessing extends AsyncTask<String, Integer, Bitmap> {
        String path = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap video_thumbnil = null;
            try {
                path = params[0];
                video_thumbnil = getVideoThumbnail(path);
                Log.d("img", video_thumbnil.toString());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return video_thumbnil;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (null != bitmap) {
                Attachment.setVisibility(View.VISIBLE);
                bitmap = checkRotationVideo(bitmap, path);
                Bitmap video_thumbnil = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                int corner_radious_plc = (video_thumbnil.getWidth() * 10) / 100;
                Bitmap bitmap_plc = getRoundedCornerBitmap(video_thumbnil, corner_radious_plc);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap_plc);
                Attatchment_Image.setBackground(drawable);
                Attachment_video_icon.setVisibility(View.VISIBLE);
                video_processing.dismiss();
                Attached_Video_thumbnil = drawable;
            }

        }
    }
}
