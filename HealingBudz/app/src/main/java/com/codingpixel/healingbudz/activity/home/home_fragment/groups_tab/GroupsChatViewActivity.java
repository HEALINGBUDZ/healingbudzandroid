package com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab;

import android.annotation.SuppressLint;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.GroupsChatMsgsDataModel;
import com.codingpixel.healingbudz.DataModel.GroupsDataModel;
import com.codingpixel.healingbudz.GestureListner.OnSwipeTouchListner;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.GroupChatViewRecylerAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomEditText;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialogVideoProcessing;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotationVideo;
import static com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupSettingActivity.isGroupClose;
import static com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity.getCurrentDate;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_groups;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_messgaes;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.join_group;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.send_message;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.sharedBaseUrl;
import static com.codingpixel.healingbudz.network.model.URL.videos_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class GroupsChatViewActivity extends AppCompatActivity implements View.OnClickListener, GroupChatViewRecylerAdapter.ItemClickListener, APIResponseListner {
    ImageView Back, Home, Slider_Back, Slider_Home;
    ImageView Group_Icon;
    ImageView list_indicator, Header_Bg;
    boolean[] is_list_open = {false};
    RelativeLayout Group_Budz;
    RelativeLayout Group_Setting, enter_msg;
    private SlideUp slide = null;
    RecyclerView groups_recyler_view;
    SwipeRefreshLayout refreshLayout;
    ImageView Send_Msg_Button;
    CustomEditText Msg_Text;
    TextView Group_Tags, Group_Owner, Group_Total_Budz;
    ProgressDialogVideoProcessing video_processing;
    TextView Groups_Title, Group_detail_name, Group_Discription;
    public static GroupsDataModel groupsDataModel;
    LinearLayoutManager linearLayoutManager;
    GroupChatViewRecylerAdapter recyler_adapter;
    ImageView Msg_attachment;
    RelativeLayout Main_Menu;
    int dummy_msg_reply_number = 0;
    RelativeLayout Attachment;
    ImageView Attatchment_Image, Attachment_video_icon, Attachment_cross;
    String Attached_video_path = "";
    String Attached_image_path = "";
    Drawable Attached_image = null;
    Drawable Attached_Video_thumbnil = null;
    Button Join_Group;
    ImageView Share_button;
    String group_id = "";
    boolean isGroupDataLoadAble = false;
    ArrayList<GroupsChatMsgsDataModel> data = new ArrayList<>();
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://139.162.37.73:3000/");
        } catch (URISyntaxException e) {
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_chat_view);
        mSocket.connect();
        mSocket.on("group_message_send", onNewMessage);
        ChangeStatusBarColor(GroupsChatViewActivity.this, "#171717");
        HideKeyboard(GroupsChatViewActivity.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            group_id = extras.getInt("goup_id") + "";
            if (group_id.equalsIgnoreCase("0")) {
                isGroupDataLoadAble = false;
                group_id = "";
            } else {
                isGroupDataLoadAble = true;
            }
        } else {
            isGroupDataLoadAble = false;
            group_id = "";
        }
        Main_Menu = (RelativeLayout) findViewById(R.id.main_menu);
        Share_button = (ImageView) findViewById(R.id.share_button);
        Join_Group = (Button) findViewById(R.id.invite_a_bud);
        Group_Tags = (TextView) findViewById(R.id.group_tags);
        Group_Total_Budz = (TextView) findViewById(R.id.group_budz);
        Group_Owner = (TextView) findViewById(R.id.group_owner_name);
        Group_Discription = (TextView) findViewById(R.id.group_discription);
        Group_detail_name = (TextView) findViewById(R.id.group_detail_name);
        Header_Bg = (ImageView) findViewById(R.id.header_bg);
        Msg_Text = (CustomEditText) findViewById(R.id.msg_text);
        enter_msg = (RelativeLayout) findViewById(R.id.enter_msg);
        Msg_Text.setKeyImeChangeListener(new CustomEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                Msg_Text.clearFocus();
                enter_msg.setBackgroundColor(Color.parseColor("#86877c"));
            }
        });
        Msg_Text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            //            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    enter_msg.setBackgroundColor(Color.parseColor("#FFFFFF"));

                } else {
                    enter_msg.setBackgroundColor(Color.parseColor("#86877c"));
                }
            }
        });
        Msg_attachment = (ImageView) findViewById(R.id.attach_ment);
        Attatchment_Image = (ImageView) findViewById(R.id.attachment_img);
        Attachment = (RelativeLayout) findViewById(R.id.attachment);
        Attachment_video_icon = (ImageView) findViewById(R.id.attachment_video_icon);
        Attachment_cross = (ImageView) findViewById(R.id.attachment_cross);
        Send_Msg_Button = (ImageView) findViewById(R.id.send_buuton);
        Back = (ImageView) findViewById(R.id.back_btn);
        Home = (ImageView) findViewById(R.id.home_btn);
        Slider_Back = (ImageView) findViewById(R.id.slide_back_btn);
        Slider_Home = (ImageView) findViewById(R.id.slide_home_btn);
        Group_Budz = (RelativeLayout) findViewById(R.id.group_total_budz);
        Group_Budz.setOnClickListener(this);
        Group_Setting = (RelativeLayout) findViewById(R.id.group_setting);
        Group_Setting.setOnClickListener(this);
        InitHeaderSlideView();
        Back.setOnClickListener(this);
        Home.setOnClickListener(this);
        Slider_Back.setOnClickListener(this);
        Slider_Home.setOnClickListener(this);
//        data = get_groups_chat();
        Group_Icon = (ImageView) findViewById(R.id.group_icon);
        Groups_Title = (TextView) findViewById(R.id.group_title);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_groups_icon);
        Bitmap bitmapOrg = ((BitmapDrawable) drawable).getBitmap();
        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
        Drawable d = new BitmapDrawable(this.getResources(), bitmap);
        Group_Icon.setImageDrawable(d);
        groups_recyler_view = (RecyclerView) findViewById(R.id.group_recyler_view);
        linearLayoutManager = new LinearLayoutManager(GroupsChatViewActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        groups_recyler_view.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemAnimator animator = groups_recyler_view.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyler_adapter = new GroupChatViewRecylerAdapter(this, data, mSocket);
        groups_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(GroupsChatViewActivity.this);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#86887c"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (groupsDataModel != null) {
                    refreshLayout.setRefreshing(true);
                    JSONObject jsonObject = new JSONObject();
                    new VollyAPICall(GroupsChatViewActivity.this, false, URL.get_group_messgaes + "/" + groupsDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, GroupsChatViewActivity.this, get_messgaes);
                    Log.d("wait", "load");
                }
            }
        });
        groups_recyler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(groups_recyler_view.getLayoutManager());
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
                    if (Msg_Text.getText().length() > 0) {
                        JSONObject json_object = new JSONObject();
                        try {
                            json_object.put("text", Msg_Text.getText().toString());
                            json_object.put("group_id", groupsDataModel.getId());
                            json_object.put("image", "");
                            json_object.put("video", "");
                            new VollyAPICall(GroupsChatViewActivity.this, false, URL.add_group_message, json_object, user.getSession_key(), Request.Method.POST, GroupsChatViewActivity.this, send_message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        GroupsChatMsgsDataModel groupsChatMsgsDataModel = new GroupsChatMsgsDataModel();
                        groupsChatMsgsDataModel.setId(groupsChatMsgsDataModel.getId());
                        groupsChatMsgsDataModel.setGroup_id(groupsDataModel.getId());
                        groupsChatMsgsDataModel.setUser_id(groupsDataModel.getUser_id());
                        groupsChatMsgsDataModel.setMsg_TExt(Msg_Text.getText().toString());
                        groupsChatMsgsDataModel.setFile_path("");
                        groupsChatMsgsDataModel.setPoster("");
                        groupsChatMsgsDataModel.setCreated_at(getCurrentDate());
                        groupsChatMsgsDataModel.setUpdated_at(getCurrentDate());
                        groupsChatMsgsDataModel.setMsgLikes(0);
                        groupsChatMsgsDataModel.setCurrentUserLiked(false);
                        groupsChatMsgsDataModel.setTimeItem(false);
                        groupsChatMsgsDataModel.setNewMemberItem(false);
                        groupsChatMsgsDataModel.setImageMsg(false);
                        groupsChatMsgsDataModel.setVideoMsg(false);
                        groupsChatMsgsDataModel.setPoster("");
                        groupsChatMsgsDataModel.setMember_Name(user.getFirst_name());
                        groupsChatMsgsDataModel.setUser_image_path(user.getImage_path());
                        groupsChatMsgsDataModel.setUser_avatar(user.getAvatar());
                        groupsChatMsgsDataModel.setUploadinStart(false);
                        groupsChatMsgsDataModel.setLoacal_image_drawable(Attached_image);
                        groupsChatMsgsDataModel.setLocal_video_thumbnil(Attached_Video_thumbnil);
                        groupsChatMsgsDataModel.setLocal_image_path(Attached_image_path);
                        groupsChatMsgsDataModel.setLocal_video_path(Attached_video_path);
                        data.add(groupsChatMsgsDataModel);
                        recyler_adapter.notifyItemInserted(data.size());
                        groups_recyler_view.scrollToPosition(recyler_adapter.getItemCount() - 1);
                        linearLayoutManager.scrollToPosition(recyler_adapter.getItemCount() - 1);
                        JSONObject object = new JSONObject();
                        try {
                            object.put("group_id", groupsDataModel.getId());
                            object.put("other_id", Splash.user.getUser_id());
                            object.put("other_name", user.getFirst_name());
                            object.put("photo", user.getImage_path());
                            object.put("text", Msg_Text.getText().toString());
                            object.put("file", "");
                            object.put("type", "");
                            object.put("file_poster", "");
                            object.put("images_base", images_baseurl);
                            object.put("video_base", videos_baseurl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mSocket.emit("group_message_get", object);
                        Msg_Text.setText("");

                    }
                } else {
                    if (Attached_image != null) {
                        GroupsChatMsgsDataModel groupsChatMsgsDataModel = new GroupsChatMsgsDataModel();
                        groupsChatMsgsDataModel.setId(groupsChatMsgsDataModel.getId());
                        groupsChatMsgsDataModel.setGroup_id(groupsDataModel.getId());
                        groupsChatMsgsDataModel.setUser_id(groupsDataModel.getUser_id());
                        groupsChatMsgsDataModel.setMsg_TExt(Msg_Text.getText().toString());
                        groupsChatMsgsDataModel.setFile_path("");
                        groupsChatMsgsDataModel.setPoster("");
                        groupsChatMsgsDataModel.setCreated_at(getCurrentDate());
                        groupsChatMsgsDataModel.setUpdated_at(getCurrentDate());
                        groupsChatMsgsDataModel.setMsgLikes(0);
                        groupsChatMsgsDataModel.setCurrentUserLiked(false);
                        groupsChatMsgsDataModel.setTimeItem(false);
                        groupsChatMsgsDataModel.setNewMemberItem(false);
                        groupsChatMsgsDataModel.setImageMsg(true);
                        groupsChatMsgsDataModel.setVideoMsg(false);
                        groupsChatMsgsDataModel.setPoster("");
                        groupsChatMsgsDataModel.setMember_Name(user.getFirst_name());
                        groupsChatMsgsDataModel.setUser_image_path(user.getImage_path());
                        groupsChatMsgsDataModel.setUser_avatar(user.getAvatar());
                        groupsChatMsgsDataModel.setUploadinStart(true);
                        groupsChatMsgsDataModel.setLoacal_image_drawable(Attached_image);
                        groupsChatMsgsDataModel.setLocal_video_thumbnil(Attached_Video_thumbnil);
                        groupsChatMsgsDataModel.setLocal_image_path(Attached_image_path);
                        groupsChatMsgsDataModel.setLocal_video_path(Attached_video_path);
                        data.add(groupsChatMsgsDataModel);
                    } else if (Attached_video_path.length() > 0) {
                        GroupsChatMsgsDataModel groupsChatMsgsDataModel = new GroupsChatMsgsDataModel();
                        groupsChatMsgsDataModel.setId(groupsChatMsgsDataModel.getId());
                        groupsChatMsgsDataModel.setGroup_id(groupsDataModel.getId());
                        groupsChatMsgsDataModel.setUser_id(groupsDataModel.getUser_id());
                        groupsChatMsgsDataModel.setMsg_TExt(Msg_Text.getText().toString());
                        groupsChatMsgsDataModel.setFile_path("");
                        groupsChatMsgsDataModel.setPoster("");
                        groupsChatMsgsDataModel.setCreated_at(getCurrentDate());
                        groupsChatMsgsDataModel.setUpdated_at(getCurrentDate());
                        groupsChatMsgsDataModel.setMsgLikes(0);
                        groupsChatMsgsDataModel.setCurrentUserLiked(false);
                        groupsChatMsgsDataModel.setTimeItem(false);
                        groupsChatMsgsDataModel.setNewMemberItem(false);
                        groupsChatMsgsDataModel.setImageMsg(false);
                        groupsChatMsgsDataModel.setVideoMsg(true);
                        groupsChatMsgsDataModel.setPoster("");
                        groupsChatMsgsDataModel.setMember_Name(user.getFirst_name());
                        groupsChatMsgsDataModel.setUser_image_path(user.getImage_path());
                        groupsChatMsgsDataModel.setUser_avatar(user.getAvatar());
                        groupsChatMsgsDataModel.setUploadinStart(true);
                        groupsChatMsgsDataModel.setLoacal_image_drawable(Attached_image);
                        groupsChatMsgsDataModel.setLocal_video_thumbnil(Attached_Video_thumbnil);
                        groupsChatMsgsDataModel.setLocal_image_path(Attached_image_path);
                        groupsChatMsgsDataModel.setLocal_video_path(Attached_video_path);
                        data.add(groupsChatMsgsDataModel);
                    }
                    recyler_adapter.notifyItemInserted(data.size());
                    groups_recyler_view.scrollToPosition(recyler_adapter.getItemCount() - 1);
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
                Intent intent = new Intent(GroupsChatViewActivity.this, HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", true);
                startActivityForResult(intent, 1200);
            }
        });

        if (isGroupDataLoadAble) {
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(GroupsChatViewActivity.this, true, URL.get_group + "/" + group_id, jsonObject, user.getSession_key(), Request.Method.GET, GroupsChatViewActivity.this, get_groups);
        } else {
            SetData();
        }
        Main_Menu.setOnTouchListener(new OnSwipeTouchListner(GroupsChatViewActivity.this) {
            public void onSwipeTop() {
                Log.d("bottom to ,", "Top");
            }

            public void onSwipeRight() {
                Log.d("left to ,", "Right");
            }

            public void onSwipeLeft() {
                Log.d("Right to ,", "Left");
            }

            public void onSwipeBottom() {
                Log.d("Top to ,", "Bottom");
                HideKeyboard(GroupsChatViewActivity.this);
                is_list_open[0] = true;
                list_indicator.setVisibility(View.GONE);
                slide.show();

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGroupDataLoadAble) {
        } else {
            SetData();
        }
        if (isGroupClose) {
            isGroupClose = false;
            finish();
        }
    }

    public void SetData() {
        Drawable d = this.getResources().getDrawable(R.drawable.ic_groups_icon);
        Bitmap bitmapOrg = ((BitmapDrawable) d).getBitmap();
        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        Group_Icon.setImageDrawable(drawable);
        Glide.with(this)
                .load(images_baseurl + groupsDataModel.getImage())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_user_profile)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("ready", model);
                        Drawable d = resource;
                        Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
                        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                        Header_Bg.setBackground(new BitmapDrawable(getResources(), bitmapOrg));
                        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
                        Group_Icon.setImageDrawable(drawable);
                        return false;
                    }
                }).into(400, 400);
        Groups_Title.setText(groupsDataModel.getName());
        Group_detail_name.setText(groupsDataModel.getName());
        Group_Discription.setText(groupsDataModel.getDescription());
        Group_Tags.setText(groupsDataModel.getGroup_tags());
        Group_Owner.setText(groupsDataModel.getGroup_owner());
        Group_Total_Budz.setText(groupsDataModel.getGet_members_count() + " BUDZ");

        if (groupsDataModel.isCurrentUserAdmin()) {
            Join_Group.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
            Join_Group.setClickable(false);
            Join_Group.setEnabled(false);
        } else {
            if (groupsDataModel.getIs_following_count() == 0) {
                Join_Group.setVisibility(View.VISIBLE);
            } else {
                Join_Group.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
                Join_Group.setClickable(false);
                Join_Group.setEnabled(false);
            }
        }
        Join_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put("group_id", groupsDataModel.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new VollyAPICall(GroupsChatViewActivity.this, false, URL.join_group, object, user.getSession_key(), Request.Method.POST, GroupsChatViewActivity.this, join_group);
                Join_Group.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
                Join_Group.setClickable(false);
                Join_Group.setEnabled(false);
            }
        });
        refreshLayout.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(GroupsChatViewActivity.this, false, URL.get_group_messgaes + "/" + groupsDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, GroupsChatViewActivity.this, get_messgaes);

        Share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
//                    object.put("msg", "Plz check the Healing Budz here : http://139.162.37.73/healingbudz/groups");
                    object.put("msg", "Check out Healing Budz for your smartphone: " + sharedBaseUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareHBContent(GroupsChatViewActivity.this, object);
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
                GoToHome(GroupsChatViewActivity.this, true);
                finish();
                break;
            case R.id.slide_back_btn:
                finish();
                break;
            case R.id.slide_home_btn:
                GoToHome(GroupsChatViewActivity.this, true);
                finish();
                break;
            case R.id.group_total_budz:
                CreateGroupSuccessfully.groupsDataModel = groupsDataModel;
                Intent i = new Intent(GroupsChatViewActivity.this, CreateGroupSuccessfully.class);
                i.putExtra("isOnlyList", true);
                startActivity(i);
                break;
            case R.id.group_setting:
                Intent setting = new Intent(GroupsChatViewActivity.this, GroupSettingActivity.class);
                startActivity(setting);
                break;
        }
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            GroupsChatViewActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject json_data = (JSONObject) args[0];
                    try {
                        if (json_data.getInt("other_id") != user.getUser_id()) {
                            GroupsChatMsgsDataModel groupsChatMsgsDataModel = new GroupsChatMsgsDataModel();
                            groupsChatMsgsDataModel.setId(groupsChatMsgsDataModel.getId());
                            groupsChatMsgsDataModel.setGroup_id(groupsDataModel.getId());
                            groupsChatMsgsDataModel.setUser_id(json_data.getInt("other_id"));
                            groupsChatMsgsDataModel.setMsg_TExt(json_data.getString("text"));
                            groupsChatMsgsDataModel.setFile_path(json_data.getString("file"));
                            groupsChatMsgsDataModel.setPoster(json_data.getString("file_poster"));
                            groupsChatMsgsDataModel.setCreated_at(getCurrentDate());
                            groupsChatMsgsDataModel.setUpdated_at(getCurrentDate());
                            groupsChatMsgsDataModel.setMsgLikes(0);
                            groupsChatMsgsDataModel.setCurrentUserLiked(false);
                            groupsChatMsgsDataModel.setTimeItem(false);
                            groupsChatMsgsDataModel.setNewMemberItem(false);
                            if (json_data.getString("file_type").equalsIgnoreCase("image")) {
                                groupsChatMsgsDataModel.setImageMsg(true);
                                groupsChatMsgsDataModel.setVideoMsg(false);
                            } else if (json_data.getString("file_type").equalsIgnoreCase("video")) {
                                groupsChatMsgsDataModel.setImageMsg(false);
                                groupsChatMsgsDataModel.setVideoMsg(true);
                            } else {
                                groupsChatMsgsDataModel.setImageMsg(false);
                                groupsChatMsgsDataModel.setVideoMsg(false);
                            }
                            groupsChatMsgsDataModel.setMember_Name(json_data.getString("other_name"));
                            groupsChatMsgsDataModel.setUser_image_path(json_data.getString("photo"));
                            groupsChatMsgsDataModel.setUser_avatar("");
                            groupsChatMsgsDataModel.setUploadinStart(false);
                            groupsChatMsgsDataModel.setLoacal_image_drawable(Attached_image);
                            groupsChatMsgsDataModel.setLocal_video_thumbnil(Attached_Video_thumbnil);
                            groupsChatMsgsDataModel.setLocal_image_path(Attached_image_path);
                            groupsChatMsgsDataModel.setLocal_video_path(Attached_video_path);
                            data.add(groupsChatMsgsDataModel);
                            recyler_adapter.notifyItemInserted(data.size());
                            groups_recyler_view.scrollToPosition(recyler_adapter.getItemCount() - 1);
                            linearLayoutManager.scrollToPosition(recyler_adapter.getItemCount() - 1);
                        }
                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };


    @Override
    public void onItemClick(View view, int position) {
        if (data.get(position).isImageMsg()) {
            Intent intent = new Intent(GroupsChatViewActivity.this, MediPreview.class);
            intent.putExtra("path", images_baseurl + data.get(position).getFile_path());
            intent.putExtra("isvideo", false);
            GroupsChatViewActivity.this.startActivity(intent);
        } else if (data.get(position).isVideoMsg()) {
            Intent intent = new Intent(GroupsChatViewActivity.this, MediPreview.class);
            if (data.get(position).getFile_path() != null && data.get(position).getFile_path().length() > 2) {
                intent.putExtra("path", videos_baseurl + data.get(position).getFile_path());
            } else {
                intent.putExtra("path", data.get(position).getLocal_video_path());
            }
            intent.putExtra("isvideo", true);
            GroupsChatViewActivity.this.startActivity(intent);
        }
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
                                Drawable d = ContextCompat.getDrawable(GroupsChatViewActivity.this, R.drawable.test_img);
                                Bitmap bitmapOrg = ((BitmapDrawable) d).getBitmap();
                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                int corner_radious = (bitmapOrg.getWidth() * 6) / 100;
                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                drawable = new BitmapDrawable(GroupsChatViewActivity.this.getResources(), bitmap);
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

    public void InitHeaderSlideView() {
        final RelativeLayout Close_Layout;
        final RelativeLayout slide_header_list;
        Close_Layout = (RelativeLayout) findViewById(R.id.indicator_layout_closs);
        slide_header_list = (RelativeLayout) findViewById(R.id.slide_header_list);
        list_indicator = (ImageView) findViewById(R.id.indicator);
        list_indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(GroupsChatViewActivity.this);
                is_list_open[0] = true;
                list_indicator.setVisibility(View.GONE);
                slide.show();
            }
        });

        Close_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide.hide();
            }
        });
        slide = new SlideUpBuilder(slide_header_list)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                            list_indicator.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        refreshLayout.setRefreshing(false);
        if (apiActions == get_messgaes) {
            try {
                JSONObject object = new JSONObject(response);
                JSONArray jsonArray = object.getJSONArray("successData");
                if (jsonArray.length() > 0) {
                    data.clear();
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject grp_msg_object = jsonArray.getJSONObject(x);
                        GroupsChatMsgsDataModel groupsChatMsgsDataModel = new GroupsChatMsgsDataModel();
                        groupsChatMsgsDataModel.setGroup_id(groupsChatMsgsDataModel.getId());
                        groupsChatMsgsDataModel.setId(grp_msg_object.getInt("id"));
                        groupsChatMsgsDataModel.setUser_id(grp_msg_object.getInt("user_id"));
                        if (!grp_msg_object.optString("text").equalsIgnoreCase("null") && grp_msg_object.getString("text") != null) {
                            groupsChatMsgsDataModel.setMsg_TExt(grp_msg_object.getString("text"));
                        } else {
                            groupsChatMsgsDataModel.setMsg_TExt("");
                        }
                        groupsChatMsgsDataModel.setFile_path(grp_msg_object.getString("file_path"));
                        groupsChatMsgsDataModel.setPoster(grp_msg_object.getString("poster"));
                        groupsChatMsgsDataModel.setCreated_at(grp_msg_object.getString("created_at"));
                        groupsChatMsgsDataModel.setUpdated_at(grp_msg_object.getString("updated_at"));
                        groupsChatMsgsDataModel.setMsgLikes(grp_msg_object.getInt("likes_count"));

                        if (grp_msg_object.getInt("is_liked_count") == 0) {
                            groupsChatMsgsDataModel.setCurrentUserLiked(false);
                        } else {
                            groupsChatMsgsDataModel.setCurrentUserLiked(true);
                        }
                        groupsChatMsgsDataModel.setTimeItem(false);

                        if (grp_msg_object.getString("type").equalsIgnoreCase("image")) {
                            groupsChatMsgsDataModel.setImageMsg(true);
                            groupsChatMsgsDataModel.setVideoMsg(false);
                            groupsChatMsgsDataModel.setNewMemberItem(false);
                        } else if (grp_msg_object.getString("type").equalsIgnoreCase("video")) {
                            groupsChatMsgsDataModel.setImageMsg(false);
                            groupsChatMsgsDataModel.setVideoMsg(true);
                            groupsChatMsgsDataModel.setNewMemberItem(false);
                        } else if (grp_msg_object.getString("type").equalsIgnoreCase("joined") || grp_msg_object.getString("type").equalsIgnoreCase("removed") || grp_msg_object.getString("type").equalsIgnoreCase("leaved")) {
                            groupsChatMsgsDataModel.setImageMsg(false);
                            groupsChatMsgsDataModel.setVideoMsg(false);
                            groupsChatMsgsDataModel.setNewMemberItem(true);
                        } else {
                            groupsChatMsgsDataModel.setNewMemberItem(false);
                            groupsChatMsgsDataModel.setImageMsg(false);
                            groupsChatMsgsDataModel.setVideoMsg(false);
                        }
                        groupsChatMsgsDataModel.setPoster(grp_msg_object.getString("poster"));
                        groupsChatMsgsDataModel.setMember_Name(grp_msg_object.getJSONObject("user").getString("first_name"));
                        groupsChatMsgsDataModel.setUser_image_path(grp_msg_object.getJSONObject("user").getString("image_path"));
                        groupsChatMsgsDataModel.setUser_avatar(grp_msg_object.getJSONObject("user").getString("avatar"));

                        data.add(groupsChatMsgsDataModel);
                    }
                    recyler_adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == send_message) {
            Log.d("response", response);
        } else if (apiActions == join_group) {

        } else if (apiActions == get_groups) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject groups_object = jsonObject.getJSONObject("successData");
                GroupsDataModel groupsDataModel = new GroupsDataModel();
                groupsDataModel.setId(groups_object.getInt("id"));
                groupsDataModel.setUser_id(groups_object.getInt("user_id"));
                groupsDataModel.setImage(groups_object.getString("image"));
                groupsDataModel.setName(groups_object.getString("title"));
                groupsDataModel.setDescription(groups_object.getString("description"));
                groupsDataModel.setIs_private(groups_object.getInt("is_private"));
                groupsDataModel.setCreated_at(groups_object.getString("created_at"));
                groupsDataModel.setUpdated_at(groups_object.getString("updated_at"));
                groupsDataModel.setIs_admin_count(groups_object.getInt("is_admin_count"));
                if (groups_object.getInt("is_admin_count") == 1) {
                    groupsDataModel.setCurrentUserAdmin(true);
                } else {
                    groupsDataModel.setCurrentUserAdmin(false);
                }
                groupsDataModel.setGet_members_count(groups_object.getInt("get_members_count"));
                String tags = "";
                JSONArray tags_array = groups_object.getJSONArray("tags");
                for (int y = 0; y < tags_array.length(); y++) {
                    JSONObject tags_object = tags_array.getJSONObject(y);
                    if (y == 0) {
                        tags = tags + tags_object.getJSONObject("get_tag").getString("title");
                    } else {
                        tags = tags + ", " + tags_object.getJSONObject("get_tag").getString("title");
                    }
                }
                groupsDataModel.setGroup_tags(tags);
                ArrayList<GroupsDataModel.GroupMembers> groupMembers = new ArrayList<>();
                JSONArray group_members_array = groups_object.getJSONArray("group_followers");
                for (int z = 0; z < group_members_array.length(); z++) {
                    JSONObject followers_object = group_members_array.getJSONObject(z);
                    GroupsDataModel.GroupMembers members = new GroupsDataModel.GroupMembers();
                    if (followers_object.getInt("is_admin") == 1) {
                        members.setAdmin(true);
                        groupsDataModel.setGroup_owner(followers_object.getJSONObject("user").getString("first_name"));
                    } else {
                        members.setAdmin(false);
                    }
                    members.setGroup_id(followers_object.getInt("group_id"));
                    members.setUser_id(followers_object.getInt("user_id"));
                    members.setName(followers_object.getJSONObject("user").getString("first_name"));
                    members.setImage_path(followers_object.getJSONObject("user").getString("image_path"));
                    groupMembers.add(members);
                }
                groupsDataModel.setIs_following_count(groups_object.getInt("is_following_count"));
                groupsDataModel.setGroupMembers(groupMembers);
                this.groupsDataModel = groupsDataModel;
                SetData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        refreshLayout.setRefreshing(false);
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getApplicationContext(), object.getString("errorMessage"), Gravity.CENTER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
