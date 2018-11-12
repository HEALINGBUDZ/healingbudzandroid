package com.codingpixel.healingbudz.activity.home.side_menu.messages;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.MessagesDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.MessagesRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity.chat_message_data_modal;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_Chat;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.search_chat_user;
import static com.codingpixel.healingbudz.network.model.URL.delete_chat;
import static com.codingpixel.healingbudz.network.model.URL.delete_chat_budz;
import static com.codingpixel.healingbudz.network.model.URL.delete_chat_budzs;
import static com.codingpixel.healingbudz.network.model.URL.get_budz_chats;
import static com.codingpixel.healingbudz.network.model.URL.get_budz_chats_user;
import static com.codingpixel.healingbudz.network.model.URL.get_chats;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class MessagesActivity extends AppCompatActivity implements MessagesRecylerAdapter.ItemClickListener, APIResponseListner {
    ImageView Home, Back;
    RecyclerView Messages_recyler_view;
    TextView No_record_found;
    ImageView Search_btn;
    EditText Search_field;
    boolean isBudz = false, isBudzPeople = false;
    boolean isRefres = false;
    MessagesRecylerAdapter recyler_adapter;
    private SwipeRefreshLayout refreshLayout;
    ImageView Add_New_MSg;
    ArrayList<MessagesDataModel> messages_data = new ArrayList<>();
    Button myMessageTab, myBusinessTab, my_business_name;
    View myMessageTabIndicator, myBusinessTabIndicator;
    LinearLayout back_to_business;
    int budzId = -1;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ChangeStatusBarColor(MessagesActivity.this, "#171717");
        myMessageTab = findViewById(R.id.tab_one);
        myBusinessTab = findViewById(R.id.tab_two);
        myMessageTabIndicator = findViewById(R.id.my_messages_indicator);
        my_business_name = findViewById(R.id.my_business_name);
        back_to_business = findViewById(R.id.back_to_business);
        myBusinessTabIndicator = findViewById(R.id.my_business_indicator);
        myBusinessTabIndicator.setVisibility(View.INVISIBLE);
        myMessageTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBudz = false;
                isBudzPeople = false;
                myMessageTabIndicator.setVisibility(View.VISIBLE);
                myBusinessTabIndicator.setVisibility(View.INVISIBLE);
                Add_New_MSg.setVisibility(View.VISIBLE);

                back_to_business.setVisibility(View.GONE);
                refreshLayout.setRefreshing(true);
                new VollyAPICall(MessagesActivity.this, false, get_chats, new JSONObject(), user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_chats);

            }
        });
        myBusinessTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBudz = true;
                isBudzPeople = false;
                myBusinessTabIndicator.setVisibility(View.VISIBLE);
                myMessageTabIndicator.setVisibility(View.INVISIBLE);
                Add_New_MSg.setVisibility(View.INVISIBLE);
                back_to_business.setVisibility(View.GONE);
                refreshLayout.setRefreshing(true);
                new VollyAPICall(MessagesActivity.this, false, get_budz_chats, new JSONObject(), user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_budz_chats);


            }
        });
        my_business_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_to_business.performClick();
            }
        });
        back_to_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBudz = true;
                isBudzPeople = false;
                refreshLayout.setRefreshing(true);
                back_to_business.setVisibility(View.GONE);
                new VollyAPICall(MessagesActivity.this, false, get_budz_chats, new JSONObject(), user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_budz_chats);


            }
        });
        Search_field = findViewById(R.id.search_field);
        Add_New_MSg = findViewById(R.id.add_new_msg);
        Search_btn = findViewById(R.id.search_btn);
        No_record_found = findViewById(R.id.no_record_found);
        Back = findViewById(R.id.back_btn);
        Search_field.clearFocus();
        Search_field.setInputType(InputType.TYPE_NULL);
        HideKeyboard(MessagesActivity.this);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Home = findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(MessagesActivity.this, true);
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Search_field.setInputType(InputType.TYPE_CLASS_TEXT);
                Search_field.requestFocus();
            }
        }, 600);

        Search_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(Search_field.length() > 0)) {
                    Search_field.setText(Search_field.getText().toString());
                }
            }
        });

        Search_field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    HideKeyboard((Activity) textView.getContext());
//                    if (Search_field.getText().toString().length() > 1) {
//                        HideKeyboard(MessagesActivity.this);
//                        refreshLayout.setRefreshing(true);
//                        JSONObject object = new JSONObject();
////                        new VollyAPICall(MessagesActivity.this, false, search_users + "?query=" + Search_field.getText().toString().trim(), object, user.getSession_key(), Request.Method.GET, MessagesActivity.this, search_chat_user);
//                    }
                    return true;
                }
                return false;
            }
        });
        Search_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (!editable.toString().isEmpty() && editable.toString().trim().length() > 0) {
                recyler_adapter.filter(editable.toString());
//                }
            }
        });
        InitRefreshLayout();
        refreshLayout.setRefreshing(true);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(MessagesActivity.this, false, get_chats, jsonObject, user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_chats);

        Search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Search_field.getText().length() > 0) {
                    HideKeyboard(MessagesActivity.this);
                    refreshLayout.setRefreshing(true);
                    JSONObject object = new JSONObject();
//                    new VollyAPICall(MessagesActivity.this, false, search_users + "?query=" + Search_field.getText().toString().trim(), object, user.getSession_key(), Request.Method.GET, MessagesActivity.this, search_chat_user);
                    Search_field.setText("");
                }
            }
        });

        Add_New_MSg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRefres = true;
                GoTo(MessagesActivity.this, NewMessagingChatViewActivity.class);
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void InitRefreshLayout() {
        Messages_recyler_view = findViewById(R.id.messages_recyler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessagesActivity.this);
        Messages_recyler_view.setLayoutManager(linearLayoutManager);
        recyler_adapter = new MessagesRecylerAdapter(this, messages_data, true);
        Messages_recyler_view.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        refreshLayout.setColorSchemeColors(Color.parseColor("#0083cb"));
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Search_field.clearFocus();
                Search_field.setText("");
                JSONObject jsonObject = new JSONObject();

                if (isBudz) {
                    new VollyAPICall(MessagesActivity.this, false, get_budz_chats, jsonObject, user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_budz_chats);

                } else if (isBudzPeople) {
                    new VollyAPICall(MessagesActivity.this, false, get_budz_chats_user + budzId, jsonObject, user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_budz_chats_user);

                } else {
                    new VollyAPICall(MessagesActivity.this, false, get_chats, jsonObject, user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_chats);

                }

            }
        });
//        Messages_recyler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(Messages_recyler_view.getLayoutManager());
//                int lastVisible = layoutManager.findFirstCompletelyVisibleItemPosition();
//                if (lastVisible < 1) {
//                    refreshLayout.setEnabled(true);
//                }
//            }
//        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (!recyler_adapter.getItem(position).isBudz() && !recyler_adapter.getItem(position).isBudzPeople()) {
            isRefres = true;
            chat_message_data_modal = recyler_adapter.getItem(position);
            if (chat_message_data_modal.getReceiver_id() == Splash.user.getUser_id()) {
                Splash.otherName = chat_message_data_modal.getSender_first_name();
            } else {
                Splash.otherName = chat_message_data_modal.getReceiver_first_name();
            }
            GoTo(MessagesActivity.this, MessagingChatViewActivity.class);
        } else if (recyler_adapter.getItem(position).isBudzPeople()) {
            isRefres = true;
            Splash.image_path = URL.images_baseurl + path;
            Splash.NameBusiness = my_business_name.getText().toString();
            MessagingBusinessChatViewActivity.chat_message_data_modal = recyler_adapter.getItem(position);
            if (MessagingBusinessChatViewActivity.chat_message_data_modal.getReceiver_id() == Splash.user.getUser_id()) {
                Splash.otherName = MessagingBusinessChatViewActivity.chat_message_data_modal.getSender_first_name();
            } else {
                Splash.otherName = MessagingBusinessChatViewActivity.chat_message_data_modal.getReceiver_first_name();
            }
            Splash.otherName = my_business_name.getText().toString();
            Intent i = new Intent(MessagesActivity.this, MessagingBusinessChatViewActivity.class);
            i.putExtra("budz_id", budzId);
            i.putExtra("chat_id", recyler_adapter.getItem(position).getId());
            startActivity(i);
        } else {
            path = "";
            my_business_name.setText(recyler_adapter.getItem(position).getBudzName());
            back_to_business.setVisibility(View.VISIBLE);
            isBudzPeople = true;
            isBudz = false;
            refreshLayout.setRefreshing(true);
            budzId = recyler_adapter.getItem(position).getBudz_id();
            new VollyAPICall(MessagesActivity.this, false, get_budz_chats_user + budzId, new JSONObject(), user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_budz_chats_user);
        }
    }

    String path;

    @Override
    protected void onResume() {
        super.onResume();
        HideKeyboard(this);
        if (isRefres) {
            Search_field.clearFocus();
            Search_field.setText("");
            isRefres = false;
            JSONObject jsonObject = new JSONObject();
            if (isBudz) {

                new VollyAPICall(MessagesActivity.this, false, get_budz_chats, jsonObject, user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_budz_chats);

            } else if (isBudzPeople) {

                new VollyAPICall(MessagesActivity.this, false, get_budz_chats_user + budzId, jsonObject, user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_budz_chats_user);
            } else {

                new VollyAPICall(MessagesActivity.this, false, get_chats, jsonObject, user.getSession_key(), Request.Method.GET, MessagesActivity.this, APIActions.ApiActions.get_chats);

            }
        }
    }

    @Override
    public void onDeleteClick(View view, final int position) {
        //delete user chat
        if (messages_data.size() > 0) {
            if (!recyler_adapter.getItem(position).isBudz() && !recyler_adapter.getItem(position).isBudzPeople()) {
                new SweetAlertDialog(MessagesActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this chat?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("chat_id", recyler_adapter.getItem(position).getId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new VollyAPICall(MessagesActivity.this, false, delete_chat, object, user.getSession_key(), Request.Method.POST, MessagesActivity.this, delete_Chat);
//                            messages_data.remove(recyler_adapter.getItem(position));
                                recyler_adapter.deleteItem(position);
                                recyler_adapter.notifyDataSetChanged();
                            }
                        })
                        .setCancelText("Close!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else if (recyler_adapter.getItem(position).isBudzPeople()) {
                new SweetAlertDialog(MessagesActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete business chat with this user?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("chat_id", recyler_adapter.getItem(position).getId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new VollyAPICall(MessagesActivity.this, false, delete_chat_budz, object, user.getSession_key(), Request.Method.POST, MessagesActivity.this, delete_Chat);
//                            messages_data.remove(recyler_adapter.getItem(position));
                                recyler_adapter.deleteItem(position);
                                recyler_adapter.notifyDataSetChanged();
                            }
                        })
                        .setCancelText("Close!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else {
                new SweetAlertDialog(MessagesActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this business chat?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("budz_id", recyler_adapter.getItem(position).getBudz_id());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new VollyAPICall(MessagesActivity.this, false, delete_chat_budzs, object, user.getSession_key(), Request.Method.POST, MessagesActivity.this, delete_Chat);
//                            messages_data.remove(recyler_adapter.getItem(position));
                                recyler_adapter.deleteItem(position);
                                recyler_adapter.notifyDataSetChanged();
                            }
                        })
                        .setCancelText("Close!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        refreshLayout.setRefreshing(false);
        if (apiActions == APIActions.ApiActions.get_chats || apiActions == APIActions.ApiActions.get_budz_chats_user) {
            try {
                messages_data.clear();
                JSONObject object = new JSONObject(response);
                JSONArray data_array = object.getJSONArray("successData");
                if (data_array.length() > 0) {
                    for (int x = 0; x < data_array.length(); x++) {
                        JSONObject msg_object = data_array.getJSONObject(x);
                        MessagesDataModel messagesDataModel = new MessagesDataModel();
                        messagesDataModel.setId(msg_object.getInt("id"));
                        messagesDataModel.setSender_id(msg_object.getInt("sender_id"));
                        messagesDataModel.setReceiver_id(msg_object.getInt("receiver_id"));
                        messagesDataModel.setLast_message_id(msg_object.getInt("last_message_id"));
                        messagesDataModel.setSender_deleted(msg_object.getInt("sender_deleted"));
                        messagesDataModel.setReceiver_deleted(msg_object.getInt("receiver_deleted"));
                        messagesDataModel.setCreated_at(msg_object.getString("created_at"));
                        messagesDataModel.setUpdated_at(msg_object.getString("updated_at"));
                        messagesDataModel.setMessages_count(msg_object.getInt("messages_count"));
                        messagesDataModel.setSender_first_name(msg_object.getJSONObject("sender").getString("first_name"));
                        messagesDataModel.setSender_image_path(msg_object.getJSONObject("sender").getString("image_path"));
                        messagesDataModel.setSender_avatar(msg_object.getJSONObject("sender").getString("avatar"));
                        messagesDataModel.setSender_point(msg_object.getJSONObject("sender").getInt("points"));
                        messagesDataModel.setReceiver_first_name(msg_object.getJSONObject("receiver").getString("first_name"));
                        messagesDataModel.setReceiver_image_path(msg_object.getJSONObject("receiver").getString("image_path"));
                        messagesDataModel.setReceiver_avatar(msg_object.getJSONObject("receiver").getString("avatar"));
                        messagesDataModel.setSpecial_icon_rec(msg_object.getJSONObject("receiver").getString("special_icon"));
                        messagesDataModel.setRecev_point(msg_object.getJSONObject("receiver").getInt("points"));
                        messagesDataModel.setSpecial_icon_sen(msg_object.getJSONObject("sender").getString("special_icon"));
                        messagesDataModel.setSave_count(msg_object.getInt("is_saved_count"));
                        messagesDataModel.setIs_online_count(msg_object.getJSONObject("sender").getInt("is_online_count"));
                        messagesDataModel.setIs_online_count_rec(msg_object.getJSONObject("receiver").getInt("is_online_count"));
                        messagesDataModel.setBudz(false);
                        if (apiActions == APIActions.ApiActions.get_chats) {
                            messagesDataModel.setMain(true);

                        }
                        if (apiActions == APIActions.ApiActions.get_budz_chats_user) {
                            messagesDataModel.setBudzPeople(true);
                            messagesDataModel.setMain(false);

                        } else {
                            messagesDataModel.setBudzPeople(false);
                        }
                        messages_data.add(messagesDataModel);
                    }
                    recyler_adapter = new MessagesRecylerAdapter(this, messages_data, true);
                    Messages_recyler_view.setAdapter(recyler_adapter);
                    recyler_adapter.setClickListener(this);
                    recyler_adapter.notifyDataSetChanged();
                    No_record_found.setVisibility(View.GONE);
                } else {
                    recyler_adapter.clearData();
                    recyler_adapter.notifyDataSetChanged();
                    Log.d("response", response);
                    No_record_found.setText("No Message Found!");
                    No_record_found.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == APIActions.ApiActions.get_budz_chats) {
            try {
                messages_data.clear();
                JSONObject object = new JSONObject(response);
                JSONArray data_array = object.getJSONArray("successData");
                if (data_array.length() > 0) {
                    for (int x = 0; x < data_array.length(); x++) {
                        JSONObject msg_object = data_array.getJSONObject(x);
                        MessagesDataModel messagesDataModel = new MessagesDataModel();
                        messagesDataModel.setId(msg_object.getInt("id"));
                        messagesDataModel.setSender_id(msg_object.getInt("sender_id"));
                        messagesDataModel.setReceiver_id(msg_object.getInt("receiver_id"));
                        messagesDataModel.setLast_message_id(msg_object.getInt("last_message_id"));
                        messagesDataModel.setSender_deleted(msg_object.getInt("sender_deleted"));
                        messagesDataModel.setReceiver_deleted(msg_object.getInt("receiver_deleted"));
                        messagesDataModel.setCreated_at(msg_object.getString("created_at"));
                        messagesDataModel.setUpdated_at(msg_object.getString("updated_at"));
                        messagesDataModel.setMessages_count(msg_object.getInt("messages_chat_count"));
                        messagesDataModel.setMember_count(msg_object.getInt("member_count"));
                        messagesDataModel.setBudz_id(msg_object.getInt("budz_id"));
                        messagesDataModel.setBudzName(msg_object.getJSONObject("budz").getString("title"));
                        messagesDataModel.setBudzType(msg_object.getJSONObject("budz").getInt("business_type_id"));
                        messagesDataModel.setBudzBusinessName(msg_object.getJSONObject("budz").getJSONObject("get_biz_type").getString("title"));
                        messagesDataModel.setBudz(true);
                        messagesDataModel.setBudzPeople(false);
                        messagesDataModel.setSender_first_name(msg_object.getJSONObject("budz").getString("title"));
                        messagesDataModel.setReceiver_first_name(msg_object.getJSONObject("budz").getString("title"));
                        messages_data.add(messagesDataModel);
                    }
                    recyler_adapter = new MessagesRecylerAdapter(this, messages_data, true);
                    Messages_recyler_view.setAdapter(recyler_adapter);
                    recyler_adapter.setClickListener(this);
                    recyler_adapter.notifyDataSetChanged();
                    No_record_found.setVisibility(View.GONE);
                } else {
                    recyler_adapter.clearData();
                    recyler_adapter.notifyDataSetChanged();
                    Log.d("response", response);
                    No_record_found.setText("No Business Message Found!");
                    No_record_found.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == search_chat_user) {
            try {
                JSONObject object = new JSONObject(response);
                JSONArray data_array = object.getJSONArray("successData");
                messages_data.clear();
                if (data_array.length() > 0) {
                    No_record_found.setVisibility(View.GONE);
                    for (int x = 0; x < data_array.length(); x++) {
                        JSONObject msg_object = data_array.getJSONObject(x);
                        MessagesDataModel messagesDataModel = new MessagesDataModel();
                        messagesDataModel.setId(msg_object.getInt("id"));
                        messagesDataModel.setSender_id(msg_object.getInt("id"));
                        messagesDataModel.setReceiver_id(msg_object.getInt("id"));
                        messagesDataModel.setLast_message_id(msg_object.getInt("id"));
                        messagesDataModel.setSender_deleted(0);
                        messagesDataModel.setReceiver_deleted(0);
                        messagesDataModel.setCreated_at(msg_object.getString("created_at"));
                        messagesDataModel.setUpdated_at(msg_object.getString("created_at"));
                        messagesDataModel.setMessages_count(0);
                        messagesDataModel.setSender_first_name(msg_object.getString("first_name"));
                        messagesDataModel.setSender_image_path(msg_object.getString("image_path"));
                        messagesDataModel.setSender_point(msg_object.getInt("points"));
                        messagesDataModel.setRecev_point(msg_object.getInt("points"));
                        messagesDataModel.setSender_avatar(msg_object.getString("avatar"));
                        messagesDataModel.setReceiver_first_name(msg_object.getString("first_name"));
                        messagesDataModel.setReceiver_image_path(msg_object.getString("image_path"));
                        messagesDataModel.setReceiver_avatar(msg_object.getString("avatar"));
                        messagesDataModel.setSpecial_icon_rec(msg_object.getJSONObject("receiver").getString("special_icon"));
                        messagesDataModel.setSpecial_icon_sen(msg_object.getJSONObject("sender").getString("special_icon"));
                        messages_data.add(messagesDataModel);
                    }
                    recyler_adapter = new MessagesRecylerAdapter(this, messages_data, true);
                    Messages_recyler_view.setAdapter(recyler_adapter);
                    recyler_adapter.setClickListener(this);
                } else {
                    No_record_found.setVisibility(View.VISIBLE);
                }
                recyler_adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == delete_Chat) {
            Log.d("response", response);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        refreshLayout.setRefreshing(false);
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getApplicationContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
