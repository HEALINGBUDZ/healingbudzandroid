package com.codingpixel.healingbudz.activity.home.side_menu.my_groups;

import android.os.Bundle;
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
import android.widget.ScrollView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.AddNewJournalKeywordRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.side_menu.my_groups.MyGroupsActivity.isRefreshable;
import static com.codingpixel.healingbudz.activity.home.side_menu.my_groups.MyGroupsActivity.my_groups_activity_DataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_tags;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class EditGroup extends AppCompatActivity implements APIResponseListner, AddNewJournalKeywordRecylerAdapter.ItemClickListener {
    RecyclerView add_keywords_recyler_view;
    AutoCompleteTextView Search_Strains;
    ImageView Back, Home;
    Button Create_Group;
    ScrollView scrollView;
    EditText Group_name, Group_discription;
    AddNewJournalKeywordRecylerAdapter addNewJournalKeywordRecylerAdapter;
    private static final List<String> data = new ArrayList<String>();
    private static final List<String> data_ids = new ArrayList<String>();
    private static final List<String> keywords = new ArrayList<String>();
    private static final List<String> main_keywords = new ArrayList<String>();
    ArrayList<Keywords> keywords_data = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String Tags_id = "";
    boolean isTagsUpdate = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        data.clear();
        data_ids.clear();
        keywords.clear();
        main_keywords.clear();
        ChangeStatusBarColor(EditGroup.this, "#b96d17");
        HideKeyboard(EditGroup.this);
        Search_Strains = (AutoCompleteTextView) findViewById(R.id.search_Strain);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, keywords);
        Search_Strains.setAdapter(adapter);
        addNewJournalKeywordRecylerAdapter = new AddNewJournalKeywordRecylerAdapter(EditGroup.this, data, true);
        add_keywords_recyler_view = (RecyclerView) findViewById(R.id.strain_recyler_view);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(EditGroup.this);
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
                addNewJournalKeywordRecylerAdapter.notifyItemInserted(data.size() - 1);
                isTagsUpdate = true ;
                if (Tags_id.equalsIgnoreCase("")) {
                    Tags_id = Tags_id + getItemID(adapterView.getItemAtPosition(i).toString(), keywords_data);
                } else {
                    Tags_id = Tags_id + "," + getItemID(adapterView.getItemAtPosition(i).toString(), keywords_data);
                }
                RefineExpties();
            }
        });

        Group_name = (EditText) findViewById(R.id.group_name);
        Group_name.setText(my_groups_activity_DataModel.getName());
        Group_discription = (EditText) findViewById(R.id.group_discription);
        Group_discription.setText(my_groups_activity_DataModel.getDescription());
        scrollView = (ScrollView) findViewById(R.id.main_scroll);

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
                GoToHome(EditGroup.this, true);
                finish();
            }
        });
        Create_Group = (Button) findViewById(R.id.create_new_group);
        Create_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTagsUpdate || !Group_name.getText().toString().equalsIgnoreCase(my_groups_activity_DataModel.getTitle()) || !Group_discription.getText().toString().equalsIgnoreCase(my_groups_activity_DataModel.getDescription())) {
                    JSONObject jsonObject = new JSONObject();
                    my_groups_activity_DataModel.setTitle(Group_name.getText().toString());
                    Tags_id = "";
                    for(int x= 0 ; x <data.size() ; x++ ) {
                        if (Tags_id.equalsIgnoreCase("")) {
                            Tags_id = Tags_id + getItemID(data.get(x), keywords_data);
                        } else {
                            Tags_id = Tags_id + "," + getItemID(data.get(x), keywords_data);
                        }
                    }
                    try {
                        jsonObject.put("title", Group_name.getText().toString());
                        jsonObject.put("group_id", my_groups_activity_DataModel.getId());
                        jsonObject.put("description", Group_discription.getText().toString());
                        jsonObject.put("tags" , Tags_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new VollyAPICall(EditGroup.this, true, URL.edit_group, jsonObject, user.getSession_key(), Request.Method.POST, EditGroup.this, APIActions.ApiActions.edit_group);
                } else {
                    CustomeToast.ShowCustomToast(EditGroup.this, "Required Fields are missing", Gravity.TOP);
                }
            }
        });

        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(this, false, URL.get_tags, jsonObject, user.getSession_key(), Request.Method.GET, this, get_tags);

        keywords.clear();
        main_keywords.clear();
        if(my_groups_activity_DataModel.getGroup_tags().length() > 0){
        for (int x = 0; x < my_groups_activity_DataModel.getGroup_tags().split(",").length; x++) {
            data.add(my_groups_activity_DataModel.getGroup_tags().split(",")[x]);
            Keywords k = new Keywords();
            k.setTitle(my_groups_activity_DataModel.getGroup_tags().split(",")[x]);
            k.setId(Integer.parseInt(my_groups_activity_DataModel.getGroup_tags_ids().split(",")[x].replace(" ", "")));
            k.setIs_approved(0);
            main_keywords.add(my_groups_activity_DataModel.getGroup_tags().split(",")[x]);
            keywords_data.add(k);
            if (Tags_id.equalsIgnoreCase("")) {
                Tags_id = Tags_id + my_groups_activity_DataModel.getGroup_tags_ids().split(",")[x];
            } else {
                Tags_id = Tags_id + "," + my_groups_activity_DataModel.getGroup_tags_ids().split(",")[x];
            }
        }
        }
        addNewJournalKeywordRecylerAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        Group_name.setText(my_groups_activity_DataModel.getName());
        Group_discription.setText(my_groups_activity_DataModel.getDescription());
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

                if (!isAlreaderAdded) {
                    data.add(keywords_data.get(x).getTitle());
                }
            } else {
                if (id != Integer.parseInt(Tags_id)) {
                    data.add(keywords_data.get(x).getTitle());
                }
            }
        }
        keywords.clear();
        keywords.addAll(data);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, keywords);
        Search_Strains.setAdapter(adapter);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == get_tags) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray tags_array = jsonObject.getJSONArray("successData");
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
        } else {
            CustomeToast.ShowCustomToast(EditGroup.this, "Updated Successfully!", Gravity.TOP);

            try {
                JSONObject jsonObject = new JSONObject(response);
                my_groups_activity_DataModel.setTitle(jsonObject.getJSONObject("successData").getString("title"));
                my_groups_activity_DataModel.setDescription(jsonObject.getJSONObject("successData").getString("description"));
                isRefreshable = true;
                finish();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefreshable) {
            Group_name.setText(my_groups_activity_DataModel.getName());
        }
    }

    @Override
    public void onTagsCrossItemClick(View view, int position) {
        data.remove(position);
        addNewJournalKeywordRecylerAdapter.notifyItemRemoved(position);
        isTagsUpdate = true ;
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
