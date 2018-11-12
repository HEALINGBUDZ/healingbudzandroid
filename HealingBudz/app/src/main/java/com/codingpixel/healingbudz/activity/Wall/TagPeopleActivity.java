package com.codingpixel.healingbudz.activity.Wall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.codingpixel.healingbudz.DataModel.User;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Dialogs;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.Utilities.SetUserValuesInSP;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.adapter.WallTagPeopleAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.interfaces.ApiStatusCallBack;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUserResponse;
import com.codingpixel.healingbudz.network.RestClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by M_Muzammil Sharif on 04-Apr-18.
 */
public class TagPeopleActivity extends AppCompatActivity implements Callback<FollowingUserResponse>, RecyclerViewItemClickListener {
    private WallTagPeopleAdapter adapter;
    private WallTagPeopleAdapter filterAdapter;
    private User user = null;
    ImageButton back_btn;
    private TagsSerialized serialized = null;
    private LinkedHashMap<Integer, FollowingUser> dataSet = null;
    private EditText filterText;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_people);

        user = SetUserValuesInSP.getSavedUser(getApplicationContext());
        if (user == null) {
            onSessionExpire();
            return;
        }

        if (getIntent().getExtras() != null) {
            serialized = (TagsSerialized) getIntent().getExtras().getSerializable("tags");
        }

        initView();
    }

    private void callApi() {
        if (!Utility.isNetworkAvailable(getApplicationContext())) {
            Dialogs.showNetworkNotAvailibleDialog(TagPeopleActivity.this);
            return;
        }
        pd = new ProgressDialog();
        pd.show(getSupportFragmentManager(), "pd");
        RestClient.getInstance(getApplicationContext()).getApiService().getFollowingUsers(user.getUser_id(), user.getSession_key()).enqueue(TagPeopleActivity.this);
    }

    private void initView() {
        findViewById(R.id.done_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RecyclerView tagsList = (RecyclerView) findViewById(R.id.activity_tags_people_tags);
        tagsList.setLayoutManager(ChipsLayoutManager.newBuilder(TagPeopleActivity.this).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        adapter = new WallTagPeopleAdapter(null, TagPeopleActivity.this, true);
        filterAdapter = new WallTagPeopleAdapter(null, new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(Object obj, int pos, int type) {
                adapter.addFollowingUser((FollowingUser) obj);
                filterText.setText("");
                applyFilter();
            }

            @Override
            public boolean onItemLongClick(Object obj, int pos, int type) {
                return false;
            }
        }, false);
        RecyclerView filterList = (RecyclerView) findViewById(R.id.activity_tags_filter_list);
        filterList.setLayoutManager(ChipsLayoutManager.newBuilder(TagPeopleActivity.this).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        filterList.setAdapter(filterAdapter);
        tagsList.setAdapter(adapter);

        if (serialized != null) {
            adapter.addFollowingUsers(serialized.tags);
            serialized = null;
        }
        callApi();
        filterText = (EditText) findViewById(R.id.select_tag_edtxt);
        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                applyFilter();
            }
        });

//        /.setLayoutManager(ChipsLayoutManager.newBuilder(TagPeopleActivity.this).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
    }

    @Override
    public void onResponse(Call<FollowingUserResponse> call, final Response<FollowingUserResponse> response) {
        pd.dismiss();
        if (response.isSuccessful() || response.body() != null) {
            Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                @Override
                public void success() {
                    updateFilter(response.body().getSuccessData());
                }

                @Override
                public void sessionExpire() {
                    onSessionExpire();
                }

                @Override
                public void knownError(String errorMsg) {
                    CustomeToast.ShowCustomToast(TagPeopleActivity.this, errorMsg, Gravity.TOP);
                }

                @Override
                public void unKnownError() {
                    showUnknownError();
                }
            });
        } else {
            showUnknownError();
        }
    }

    private void updateFilter(List<FollowingUser> users) {
        dataSet = new LinkedHashMap<>();
        for (FollowingUser user : users) {
            dataSet.put(user.getId(), user);
        }
        applyFilter();
    }

    private void applyFilter() {
        if (dataSet == null || dataSet.isEmpty()) {
            return;
        }
        LinkedHashMap<Integer, FollowingUser> temp = dataSet;
        List<FollowingUser> filtered = new ArrayList<>();
        if (adapter == null || adapter.getFollowingUsers() == null || adapter.getFollowingUsers().isEmpty()) {
            List<FollowingUser> users = new ArrayList<FollowingUser>(temp.values());
            for (int i = 0; i < users.size(); i++) {
                if ((users.get(i).getFirstName() + " " + users.get(i).getLastName()).toLowerCase().contains(filterText.getText().toString().trim().toLowerCase())) {
                    filtered.add(users.get(i));
                }
            }
            filterAdapter.setFollowingUsers(filtered);
            return;
        }
        for (FollowingUser user : adapter.getFollowingUsers()) {
            temp.remove(user.getId());
        }
        List<FollowingUser> users = new ArrayList<FollowingUser>(temp.values());
        for (int i = 0; i < users.size(); i++) {
            if ((users.get(i).getFirstName() + " " + users.get(i).getLastName()).toLowerCase().contains(filterText.getText().toString().trim().toLowerCase())) {
                filtered.add(users.get(i));
            }
        }
        filterAdapter.setFollowingUsers(filtered);
    }

    private void onSessionExpire() {
        Utility.finishWithResult(TagPeopleActivity.this, null, Flags.SESSION_OUT);
    }

    @Override
    public void onFailure(Call<FollowingUserResponse> call, Throwable t) {
        pd.dismiss();
        showUnknownError();
    }


    private void showUnknownError() {

    }

    @Override
    public void onItemClick(Object obj, int pos, int type) {
        if (type < 0) {
            FollowingUser user = (FollowingUser) obj;
            dataSet.put(user.getId(), user);
            applyFilter();
        }
    }

    @Override
    public boolean onItemLongClick(Object obj, int pos, int type) {
        return false;
    }

    public static class TagsSerialized implements Serializable {
        private List<FollowingUser> tags;

        public TagsSerialized(List<FollowingUser> tags) {
            this.tags = tags;
        }

        public List<FollowingUser> getTags() {
            return tags;
        }

        public void setTags(List<FollowingUser> tags) {
            this.tags = tags;
        }
    }

    @Override
    public void onBackPressed() {
        Bundle b = new Bundle();
        if (adapter == null || adapter.getFollowingUsers() == null || adapter.getFollowingUsers().isEmpty()) {
            super.onBackPressed();
            b.putSerializable("tags", new TagsSerialized(new ArrayList<FollowingUser>()));
        } else {
            b.putSerializable("tags", new TagsSerialized(adapter.getFollowingUsers()));
        }
        Utility.finishWithResult(TagPeopleActivity.this, b, Flags.NOTIFY);
    }
}
