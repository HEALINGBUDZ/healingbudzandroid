package com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.GroupsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.AddNewJournalKeywordRecylerAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.UploadFileWithProgress;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.side_menu.my_groups.MyGroupsActivity.isRefreshable;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class AddNewGroup extends AppCompatActivity implements APIResponseListner, AddNewJournalKeywordRecylerAdapter.ItemClickListener {
    RecyclerView add_keywords_recyler_view;
    AutoCompleteTextView Search_Strains;
    ImageView Back, Home,new_group_image;
    Button Create_Group;
    ScrollView scrollView;
    String group_cover_path = "";
    RelativeLayout Group_Cover;
    LinearLayout Add_Group_image;
    Button btn_private, btn_public;
    EditText Group_name, Group_discription;
    boolean isPublic = false, isPrivate = true;
    AddNewJournalKeywordRecylerAdapter addNewJournalKeywordRecylerAdapter;
    private static final List<String> data = new ArrayList<String>();
    private static final List<String> keywords = new ArrayList<String>();
    private static final List<String> main_keywords = new ArrayList<String>();
    ArrayList<Keywords> keywords_data = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String Tags_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group);
        data.clear();
        ChangeStatusBarColor(AddNewGroup.this, "#b96d17");
        HideKeyboard(AddNewGroup.this);
        new_group_image= (ImageView) findViewById(R.id.new_group_image);
        Search_Strains = (AutoCompleteTextView) findViewById(R.id.search_Strain);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, keywords);
        Search_Strains.setAdapter(adapter);
        addNewJournalKeywordRecylerAdapter = new AddNewJournalKeywordRecylerAdapter(AddNewGroup.this, data, true);
        add_keywords_recyler_view = (RecyclerView) findViewById(R.id.strain_recyler_view);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(AddNewGroup.this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        add_keywords_recyler_view.setLayoutManager(layoutManager);
        add_keywords_recyler_view.setAdapter(addNewJournalKeywordRecylerAdapter);
        addNewJournalKeywordRecylerAdapter.setClickListener(this);

        Search_Strains.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Search_Strains.setText("");
                data.add(adapterView.getItemAtPosition(i).toString());
                addNewJournalKeywordRecylerAdapter.notifyDataSetChanged();
                if (Tags_id.equalsIgnoreCase("")) {
                    Tags_id = Tags_id + getItemID(adapterView.getItemAtPosition(i).toString(), keywords_data);
                } else {
                    Tags_id = Tags_id + "," + getItemID(adapterView.getItemAtPosition(i).toString(), keywords_data);
                }
                RefineExpties();
            }
        });

        Group_Cover = (RelativeLayout) findViewById(R.id.group_cover);
        Add_Group_image = (LinearLayout) findViewById(R.id.add_group_image);
        Group_name = (EditText) findViewById(R.id.group_name);
        Group_discription = (EditText) findViewById(R.id.group_discription);
        scrollView = (ScrollView) findViewById(R.id.main_scroll);

        btn_private = (Button) findViewById(R.id.private_btn);
        btn_public = (Button) findViewById(R.id.public_btn);

        btn_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPrivate) {
                    isPublic = false;
                    isPrivate = true;
                    btn_private.setBackgroundResource(R.drawable.toggle_btn_private);
                    btn_public.setBackground(null);
                }
            }
        });

        btn_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPublic) {
                    isPrivate = false;
                    isPublic = true;
                    btn_public.setBackgroundResource(R.drawable.toggle_btn_public);
                    btn_private.setBackground(null);
                }
            }
        });


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
                GoToHome(AddNewGroup.this, true);
                finish();
            }
        });
        Create_Group = (Button) findViewById(R.id.create_new_group);
        Create_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Group_name.length() > 0 && Group_discription.length() > 0 && group_cover_path.length() > 0) {
                    JSONArray parameter_value = new JSONArray();
                    parameter_value.put(Group_name.getText().toString());
                    if (isPrivate) {
                        parameter_value.put("1");
                    } else {
                        parameter_value.put("0");
                    }
                    Tags_id = "";
                    for(int x= 0 ; x <data.size() ; x++ ){
                        if (Tags_id.equalsIgnoreCase("")) {
                            Tags_id = Tags_id + getItemID(data.get(x), keywords_data);
                        } else {
                            Tags_id = Tags_id + "," + getItemID(data.get(x), keywords_data);
                        }
                    }
                    parameter_value.put(Tags_id);
                    parameter_value.put(Group_discription.getText().toString());
                    JSONArray parameter_key = new JSONArray();
                    parameter_key.put("title");
                    parameter_key.put("is_private");
                    parameter_key.put("tags");
                    parameter_key.put("description");
                    new UploadFileWithProgress(AddNewGroup.this, true, URL.add_group, group_cover_path, "image", parameter_value, parameter_key, null, AddNewGroup.this, APIActions.ApiActions.add_group).execute();
                } else {
                    CustomeToast.ShowCustomToast(AddNewGroup.this, "Group Name ,Cover Photo and Description are Required!", Gravity.TOP);
                }
            }
        });

        Add_Group_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewGroup.this, HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", false);
                startActivityForResult(intent, 1200);
            }
        });
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(this, false, URL.get_tags, jsonObject, user.getSession_key(), Request.Method.GET, this, APIActions.ApiActions.get_tags);
    }

    @Override
    public void onTagsCrossItemClick(View view, int position) {
        data.remove(position);
        addNewJournalKeywordRecylerAdapter.notifyItemRemoved(position);
    }

    public void RefineExpties() {
        ArrayList<String> data = new ArrayList<>();
        for (int x = 0; x < keywords_data.size(); x++) {
            int id = keywords_data.get(x).getId();
            if (Tags_id.contains(",")) {
                boolean isAlreaderAdded = false;
                String[] added_ids = Tags_id.split(",");
                for (String added_id : added_ids) {
                    if (id != Integer.parseInt(added_id)) {
                        isAlreaderAdded = false;
                    } else {
                        isAlreaderAdded = true;
                        break;
                    }
                }
                if (!isAlreaderAdded && x < keywords_data.size()) {
                    data.add(keywords_data.get(x).getTitle());
                }
            } else {
                if (id != Integer.parseInt(Tags_id) && x < keywords_data.size()) {
                    data.add(keywords_data.get(x).getTitle());
                }
            }
        }
        keywords.clear();
        keywords.addAll(data);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, keywords);
        Search_Strains.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent image_data) {
        super.onActivityResult(requestCode, resultCode, image_data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", image_data.getExtras().getString("file_path_arg"));
            group_cover_path = image_data.getExtras().getString("file_path_arg");
            Bitmap bitmapOrg = BitmapFactory.decodeFile(image_data.getExtras().getString("file_path_arg"));
            bitmapOrg = checkRotation(bitmapOrg, image_data.getExtras().getString("file_path_arg"));
//            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
//            int corner_radious = (bitmapOrg.getWidth() * 4) / 100;
//            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
            Drawable drawable = new BitmapDrawable(getResources(), bitmapOrg);
            new_group_image.setImageDrawable(drawable);
        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.add_group) {
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
                groupsDataModel.setGet_members_count(groups_object.getInt("get_members_count"));
                groupsDataModel.setIs_admin_count(groups_object.getInt("is_admin_count"));
                if (groups_object.getInt("is_admin_count") == 1) {
                    groupsDataModel.setCurrentUserAdmin(true);
                } else {
                    groupsDataModel.setCurrentUserAdmin(false);
                }
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
                groupsDataModel.setGroupMembers(groupMembers);
                CreateGroupSuccessfully.groupsDataModel = groupsDataModel;
                Intent i = new Intent(AddNewGroup.this, CreateGroupSuccessfully.class);
                i.putExtra("isOnlyList", false);
                startActivity(i);
                data.clear();
                finish();
            } catch (JSONException e) {
                Log.d("rex", e.getMessage());
                e.printStackTrace();
            }
            isRefreshable = true ;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray tags_array = jsonObject.getJSONArray("successData");
                keywords.clear();
                main_keywords.clear();
                for (int x = 0; x < tags_array.length(); x++) {
                    JSONObject object = tags_array.getJSONObject(x);
                    keywords.add(object.getString("title"));
                    main_keywords.add(object.getString("title"));
                    Keywords keywords = new Keywords();
                    keywords.setId(object.getInt("id"));
                    keywords.setTitle(object.getString("title"));
                    keywords.setIs_approved(object.getInt("is_approved"));
                    keywords.setCreated_at(object.getString("created_at"));
                    keywords_data.add(keywords);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }


    public int getItemID(String text, ArrayList<Keywords> dataModels) {
        int id = 1;
        for (int x = 0; x < dataModels.size(); x++) {
            if (text.equalsIgnoreCase(dataModels.get(x).getTitle())) {
                id = dataModels.get(x).getId();
                break;
            }
        }
        return id;
    }

    public class Keywords {
        int id;
        String title;
        int is_approved;
        String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIs_approved() {
            return is_approved;
        }

        public void setIs_approved(int is_approved) {
            this.is_approved = is_approved;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
