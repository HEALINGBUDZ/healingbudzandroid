package com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.GroupsDataModel;
import com.codingpixel.healingbudz.DataModel.GroupsInviteNewBudDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.GroupBudzListRecylerViewAdapter;
import com.codingpixel.healingbudz.adapter.GroupSectionInvitedListRecylerAdapter;
import com.codingpixel.healingbudz.adapter.GroupsInvitBudAutocompleteListAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.SEND_SMS;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_users;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.invite_members_for_group;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.StaticObjects.emailPattern;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class CreateGroupSuccessfully extends AppCompatActivity implements APIResponseListner, GroupsInvitBudAutocompleteListAdapter.ItemClickListener {
    ImageView Back, Home, Group_buds_List_Back, Group_buds_List_Hom;
    Button Invite_a_bud;
    Button Invite_bud_a;
    boolean isAdmin = false;
    TextView Group_Name, Congrats_Group_Name, Heading_no_off_budz;
    LinearLayout Main_Layout, Group_bud_list_layout;
    LinearLayout Invite_another_bud;
    ImageView Cross_invite_dialog, cross_invited_list_dialog;
    RelativeLayout Invite_bud_dialog;
    RelativeLayout Invited_bud_list_dialog;
    AutoCompleteTextView NameOREmail;
    AutoCompleteTextView EmailSearch;
    EditText Invite_phone_number;
    Button Invited_List_Dialog_Invite_Button;
    RecyclerView Invited_bud_list;
    RecyclerView Group_buds_list;
    ImageView private_publice_icon;
    GroupBudzListRecylerViewAdapter Group_buds_recyler_adapter;
    GroupSectionInvitedListRecylerAdapter recyler_adapter;
    boolean isBudzListOnly;
    private static final int REQUEST_SMS = 0;
    String email = "", phone_number = "";
    ArrayList<GroupsInviteNewBudDataModel> all_members = new ArrayList<>();
    ArrayList<GroupsInviteNewBudDataModel> get_invited_bud = new ArrayList<>();
    public static GroupsDataModel groupsDataModel;
    Button Invite_Complete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_groups_successfully);
        ChangeStatusBarColor(CreateGroupSuccessfully.this, "#1c1c1c");
        HideKeyboard(CreateGroupSuccessfully.this);
        isBudzListOnly = getIntent().getExtras().getBoolean("isOnlyList");
        Invite_bud_a = (Button) findViewById(R.id.invite_bud_a);
        if (groupsDataModel.isCurrentUserAdmin()) {
            Invite_bud_a.setVisibility(View.VISIBLE);
        } else {
            Invite_bud_a.setVisibility(View.GONE);
        }
        isAdmin = groupsDataModel.isCurrentUserAdmin();
        Cross_invite_dialog = (ImageView) findViewById(R.id.cross_btn);
        cross_invited_list_dialog = (ImageView) findViewById(R.id.bud_list_cross_btn);
        Congrats_Group_Name = (TextView) findViewById(R.id.congrates_group_name);
        Heading_no_off_budz = (TextView) findViewById(R.id.heading_no_off_budz);
        Invite_Complete = (Button) findViewById(R.id.complete_invite_bud);
        private_publice_icon = (ImageView) findViewById(R.id.private_publice_icon);
        Group_Name = (TextView) findViewById(R.id.group_name);
        Group_Name.setText(groupsDataModel.getName());
        Congrats_Group_Name.setText(groupsDataModel.getName());
        if (groupsDataModel.getIs_private() == 0) {
            private_publice_icon.setImageResource(R.drawable.ic_public_group);
        } else {
            private_publice_icon.setImageResource(R.drawable.ic_private_group);
        }
        Group_bud_list_layout = (LinearLayout) findViewById(R.id.group_bud_list_layout);
        Main_Layout = (LinearLayout) findViewById(R.id.main_layout);
        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        GetAllMembers();
        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(CreateGroupSuccessfully.this, true);
                finish();
            }
        });
        Invite_bud_dialog = (RelativeLayout) findViewById(R.id.invit_new_bud_dialog);
        Invited_bud_list_dialog = (RelativeLayout) findViewById(R.id.invited_bud_list_dialog);
        Invited_bud_list_dialog.setOnClickListener(null);
        Invited_bud_list_dialog.setClickable(false);
        Invited_bud_list_dialog.setOnTouchListener(null);
        Invite_a_bud = (Button) findViewById(R.id.invite_a_bud);
        Invite_a_bud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity)view.getContext());
                Invite_a_bud.setClickable(true);
                Invite_bud_dialog.setVisibility(View.VISIBLE);
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                Invite_bud_dialog.startAnimation(startAnimation);
            }
        });
        Invite_bud_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity)view.getContext());
                Invite_a_bud.setClickable(false);
                Invite_bud_dialog.setVisibility(View.VISIBLE);
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                Invite_bud_dialog.startAnimation(startAnimation);
            }
        });

        Cross_invite_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity)view.getContext());
                Invite_a_bud.setClickable(true);
                Invite_bud_dialog.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                Invite_bud_dialog.startAnimation(startAnimation);
            }
        });

        cross_invited_list_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity)view.getContext());
                Invite_a_bud.setClickable(true);
                Invited_bud_list_dialog.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                Invited_bud_list_dialog.startAnimation(startAnimation);
            }
        });

        NameOREmail = (AutoCompleteTextView) findViewById(R.id.name_or_email);
        EmailSearch = (AutoCompleteTextView) findViewById(R.id.email_search);
        Invite_phone_number = (EditText) findViewById(R.id.invite_phone_number);


        Invited_bud_list = (RecyclerView) findViewById(R.id.invited_bud_list_recyler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CreateGroupSuccessfully.this);
        Invited_bud_list.setLayoutManager(layoutManager);
        recyler_adapter = new GroupSectionInvitedListRecylerAdapter(CreateGroupSuccessfully.this, get_invited_bud);
        Invited_bud_list.setAdapter(recyler_adapter);

        Invited_List_Dialog_Invite_Button = (Button) findViewById(R.id.invited_list_dialog_invite_button);
        Invited_List_Dialog_Invite_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity)view.getContext());
                // move forward
                if (get_invited_bud.size() > 0) {
                    Invite_a_bud.setClickable(true);
                    Invited_bud_list_dialog.setVisibility(View.GONE);
                    Invite_bud_dialog.setVisibility(View.GONE);
                    Main_Layout.setVisibility(View.GONE);
                    Group_bud_list_layout.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject();
                    String invited_members_id = "";
                    ArrayList<GroupsDataModel.GroupMembers> list = new ArrayList<>();
                    list.addAll(groupsDataModel.getGroupMembers());
                    for (int x = 0; x < get_invited_bud.size(); x++) {
                        if (invited_members_id.length() == 0) {
                            invited_members_id = invited_members_id + get_invited_bud.get(x).getId();
                        } else {
                            invited_members_id = invited_members_id + "," + get_invited_bud.get(x).getId();
                        }

                        GroupsDataModel.GroupMembers members = new GroupsDataModel.GroupMembers();
                        members.setAdmin(false);
                        members.setGroup_id(groupsDataModel.getId());
                        members.setUser_id(get_invited_bud.get(x).getId());
                        members.setName(get_invited_bud.get(x).getName());
                        members.setImage_path(get_invited_bud.get(x).getImage_path());
                        list.add(members);
                    }
                    if (phone_number.length() > 5) {
                        if (isValidMobile(phone_number)) {
                            // invite via phone number
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone_number));
                            intent.putExtra("sms_body", "Check out Healing Budz for your smartphone. Download it today from " + URL.sharedBaseUrl);
                            startActivity(intent);
                        } else {
                            CustomeToast.ShowCustomToast(CreateGroupSuccessfully.this, "invalid Phone!", Gravity.TOP);
                        }
                    }
                    try {
                        jsonObject.put("user_ids", invited_members_id);
                        jsonObject.put("group_id", groupsDataModel.getId());
                        if (email.length() != 0) {
                            jsonObject.put("email", email);
                        }
                        get_invited_bud.clear();
                        new VollyAPICall(CreateGroupSuccessfully.this, true, URL.invite_members, jsonObject, user.getSession_key(), Request.Method.POST, CreateGroupSuccessfully.this, invite_members_for_group);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Invite_a_bud.setClickable(true);
                    if (phone_number.length() > 5) {
                        if (isValidMobile(phone_number)) {
                            // invite via phone number
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone_number));
                            intent.putExtra("sms_body", "Check out Healing Budz for your smartphone. Download it today from " + URL.sharedBaseUrl);
                            startActivity(intent);
                        } else {
                            CustomeToast.ShowCustomToast(CreateGroupSuccessfully.this, "invalid Phone!", Gravity.TOP);
                        }
                    }

                    try {
                        JSONObject jsonObject = new JSONObject();
                        if (email.length() > 0) {
                            jsonObject.put("user_ids", "");
                            jsonObject.put("group_id", groupsDataModel.getId());
                            jsonObject.put("email", email);
                            get_invited_bud.clear();
                            new VollyAPICall(CreateGroupSuccessfully.this, true, URL.invite_members, jsonObject, user.getSession_key(), Request.Method.POST, CreateGroupSuccessfully.this, invite_members_for_group);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Invite_another_bud = (LinearLayout) findViewById(R.id.invite_another_bud);
        Invite_another_bud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity)view.getContext());
                Invite_a_bud.setClickable(true);
                Invited_bud_list_dialog.setVisibility(View.GONE);
                Invite_bud_dialog.setVisibility(View.VISIBLE);

            }
        });

        Group_buds_list = (RecyclerView) findViewById(R.id.group_buds_list);
        RecyclerView.LayoutManager Group_buds_layoutManager = new LinearLayoutManager(CreateGroupSuccessfully.this);
        Group_buds_list.setLayoutManager(Group_buds_layoutManager);
        Group_buds_recyler_adapter = new GroupBudzListRecylerViewAdapter(CreateGroupSuccessfully.this, groupsDataModel.getGroupMembers(), isAdmin);
        Group_buds_list.setAdapter(Group_buds_recyler_adapter);
        if (groupsDataModel.getGroupMembers() != null) {
            Heading_no_off_budz.setText(groupsDataModel.getGet_members_count() + " BUDZ");
        }
        Group_buds_List_Back = (ImageView) findViewById(R.id.group_buds_back_btn);
        Group_buds_List_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Group_buds_List_Hom = (ImageView) findViewById(R.id.group_buds_home_btn);
        Group_buds_List_Hom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(CreateGroupSuccessfully.this, true);
                finish();
            }
        });
        if (isBudzListOnly) {
            Invite_a_bud.setClickable(false);
            Invited_bud_list_dialog.setVisibility(View.GONE);
            Invite_bud_dialog.setVisibility(View.GONE);
            Main_Layout.setVisibility(View.GONE);
            Group_bud_list_layout.setVisibility(View.VISIBLE);
        }

        Invite_Complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity)view.getContext());
                if (get_invited_bud.size() > 0) {
                    if (EmailSearch.getText().toString().matches(emailPattern)) {
                        email = EmailSearch.getText().toString();
                    } else {
                        email = "";
                    }

                    if (Invite_phone_number.length() > 8) {
                        phone_number = Invite_phone_number.getText().toString();
                    } else {
                        phone_number = "";
                    }
                    NameOREmail.setText("");
                    EmailSearch.setText("");
                    Invite_phone_number.setText("");
                    HideKeyboard(CreateGroupSuccessfully.this);
                    Invite_bud_dialog.setVisibility(View.GONE);
                    Invited_bud_list_dialog.setVisibility(View.VISIBLE);
                } else {
                    phone_number = Invite_phone_number.getText().toString();
                    if (phone_number.length() > 5) {
                        if (isValidMobile(phone_number)) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone_number));
                            intent.putExtra("sms_body", "Check out Healing Budz for your smartphone. Download it today from " + URL.sharedBaseUrl);
                            startActivity(intent);
                        } else {
                            CustomeToast.ShowCustomToast(CreateGroupSuccessfully.this, "invalid Phone!", Gravity.TOP);
                        }
                    }
                    try {
                        if (EmailSearch.length() > 0) {
                            if (EmailSearch.getText().toString().matches(emailPattern)) {
                                email = EmailSearch.getText().toString();
                                JSONObject jsonObject = new JSONObject();
                                if (email.length() > 0) {
//                                    jsonObject.put("user_ids", "");
                                    jsonObject.put("group_id", groupsDataModel.getId());
                                    jsonObject.put("email", email);
                                    get_invited_bud.clear();
                                    new VollyAPICall(CreateGroupSuccessfully.this, true, URL.invite_members, jsonObject, user.getSession_key(), Request.Method.POST, CreateGroupSuccessfully.this, invite_members_for_group);
                                }
                            } else {
                                email = "";
                                CustomeToast.ShowCustomToast(CreateGroupSuccessfully.this, "invalid email address!", Gravity.TOP);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    NameOREmail.setText("");
                    EmailSearch.setText("");
                    Invite_phone_number.setText("");
                    HideKeyboard(CreateGroupSuccessfully.this);
                    Invite_bud_dialog.setVisibility(View.GONE);

                }
            }
        });
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(CreateGroupSuccessfully.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onInviteButtonClick(View view, int position, GroupsInviteNewBudDataModel groupsInviteNewBudDataModel) {
        get_invited_bud.add(groupsInviteNewBudDataModel);
        recyler_adapter.notifyItemInserted(get_invited_bud.size() - 1);
    }

    public void GetAllMembers() {
        JSONObject object = new JSONObject();
        new VollyAPICall(CreateGroupSuccessfully.this, false, URL.get_users, object, user.getSession_key(), Request.Method.GET, CreateGroupSuccessfully.this, get_users);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMySMS(phone_number);

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(SEND_SMS)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{SEND_SMS},
                                                        REQUEST_SMS);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void sendMySMS(String phone) {
        SmsManager sms = SmsManager.getDefault();
        // if message length is too long messages are divided
        List<String> messages = sms.divideMessage("Check out Healing Budz for your smartphone. Download it today from " + URL.sharedBaseUrl);
        for (String msg : messages) {

            PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
            sms.sendTextMessage(phone, null, msg, sentIntent, deliveredIntent);

        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("reponse", response);
        if (apiActions == get_users) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("successData");
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject members_object = jsonArray.getJSONObject(x);
                    GroupsInviteNewBudDataModel model = new GroupsInviteNewBudDataModel();
                    model.setId(members_object.getInt("id"));
                    model.setName(members_object.getString("first_name"));
                    model.setEmail(members_object.getString("email"));
                    model.setImage_path(members_object.getString("image_path"));
                    model.setPoints(members_object.getInt("points"));
                    model.setInvited(false);
                    if (model.getId() != user.getUser_id()) {
                        all_members.add(model);
                        GroupsInvitBudAutocompleteListAdapter groupsInvitBudAutocompleteListAdapter = new GroupsInvitBudAutocompleteListAdapter(CreateGroupSuccessfully.this, R.layout.invite_groups_bud_autocomplete_list_item, all_members, CreateGroupSuccessfully.this, 0);
                        NameOREmail.setAdapter(groupsInvitBudAutocompleteListAdapter);
                        NameOREmail.setThreshold(1);
                    }

//                    GroupsInvitBudAutocompleteListAdapter  groupsInvitBudAutocompleteListAdapter1 = new GroupsInvitBudAutocompleteListAdapter(CreateGroupSuccessfully.this, R.layout.invite_groups_bud_autocomplete_list_item, all_members , CreateGroupSuccessfully.this , 1);
//                    EmailSearch.setAdapter(groupsInvitBudAutocompleteListAdapter1);
//                    EmailSearch.setThreshold(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == invite_members_for_group) {
            Log.d("reponse", response);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("reponse", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(CreateGroupSuccessfully.this, object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
