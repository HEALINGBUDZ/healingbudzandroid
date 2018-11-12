package com.codingpixel.healingbudz.activity.home.side_menu.my_groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.CreateGroupSuccessfully;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.home.side_menu.my_groups.MyGroupsActivity.my_groups_activity_DataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.remove_group;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class CloseGroupActivity extends AppCompatActivity implements APIResponseListner {
    ImageView Home, Back;
    TextView Group_name;
    ImageView private_publice_icon;
    EditText Remove_member, EditName, EditDiscription, EditTags;
    Button Close_group;
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
        setContentView(R.layout.activity_close_group);
        ChangeStatusBarColor(CloseGroupActivity.this, "#171717");
        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(CloseGroupActivity.this, true);
                finish();
            }
        });
        Group_name = (TextView) findViewById(R.id.group_name);
        Group_name.setText(my_groups_activity_DataModel.getName());
        private_publice_icon = (ImageView) findViewById(R.id.private_publice_icon);
        if (my_groups_activity_DataModel.getIs_private() == 0) {
            private_publice_icon.setImageResource(R.drawable.ic_public_group);
        } else {
            private_publice_icon.setImageResource(R.drawable.ic_private_group);
        }

        Remove_member = (EditText) findViewById(R.id.remove_member);
        Remove_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateGroupSuccessfully.groupsDataModel = my_groups_activity_DataModel;
                Intent i = new Intent(CloseGroupActivity.this, CreateGroupSuccessfully.class);
                i.putExtra("isOnlyList", true);
                startActivity(i);
            }
        });

        Close_group = (Button) findViewById(R.id.close_group);
        Close_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(CloseGroupActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to close this group?")
                        .setConfirmText("Yes, close it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                JSONObject jsonObject = new JSONObject();
                                new VollyAPICall(CloseGroupActivity.this, true, URL.remove_group + "/" + my_groups_activity_DataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, CloseGroupActivity.this, remove_group);
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
//                JSONObject jsonObject = new JSONObject();
//                new VollyAPICall(CloseGroupActivity.this, true, URL.remove_group + "/" + my_groups_activity_DataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, CloseGroupActivity.this, remove_group);

            }
        });

        EditName = (EditText) findViewById(R.id.edit_name);
        EditDiscription = (EditText) findViewById(R.id.edit_discription);
        EditTags = (EditText) findViewById(R.id.edit_tags);

        EditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_groups_activity_DataModel = my_groups_activity_DataModel;
                Intent i = new Intent(CloseGroupActivity.this, EditGroup.class);
                startActivity(i);
                finish();
            }
        });

        EditDiscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_groups_activity_DataModel = my_groups_activity_DataModel;
                Intent i = new Intent(CloseGroupActivity.this, EditGroup.class);
                startActivity(i);
                finish();
            }
        });

        EditTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_groups_activity_DataModel = my_groups_activity_DataModel;
                Intent i = new Intent(CloseGroupActivity.this, EditGroup.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Group_name.setText(my_groups_activity_DataModel.getName());
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        MyGroupsActivity.isRefreshable = true;
        CustomeToast.ShowCustomToast(CloseGroupActivity.this, "Group Closed Successfully!", Gravity.TOP);
        finish();
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        CustomeToast.ShowCustomToast(CloseGroupActivity.this, "Error From Server!", Gravity.TOP);

    }
}
