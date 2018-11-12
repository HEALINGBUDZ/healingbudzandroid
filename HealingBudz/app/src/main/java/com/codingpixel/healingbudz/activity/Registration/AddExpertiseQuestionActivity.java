package com.codingpixel.healingbudz.activity.Registration;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.Registration.dialogue.IntroHowToAddQuestionDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.EditUserProfileActivity;
import com.codingpixel.healingbudz.adapter.ExpertAreaRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.side_menu.profile.UserProfileActivity.profiledataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.post_Experties;
import static com.codingpixel.healingbudz.network.model.URL.add_expertise;
import static com.codingpixel.healingbudz.network.model.URL.get_expertise;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class AddExpertiseQuestionActivity extends AppCompatActivity implements APIResponseListner, ExpertAreaRecylerAdapter.ItemClickListenerKey, IntroHowToAddQuestionDialog.OnDialogFragmentClickListener {
    Button Add_Expert_Area;
    RelativeLayout Question_One;
    LinearLayout Question_Two;
    Button Remind_me_later;
    Button Complete_profile;
    ArrayAdapter<String> adapter, adapter_two;
    String Question_One_Answer = "";
    String Question_Two_Answer = "";
    String Question_One_Experties = "";
    String Question_Two_Experties = "";
    int question_one_keyword_count = 0;
    int question_two_keyword_count = 0;
    ScrollView scrollView;
    boolean isEdit = false;
    RecyclerView question_one_recyler_view;
    View bottom_view;
    ImageView intro_how_too;
    RecyclerView question_two_recyler_view;
    AutoCompleteTextView Edit_text_question_one;
    AutoCompleteTextView Edit_text_question_two;
    boolean isButtonPressed = false;
    ArrayList<ExpertiesDataModel> QUESTION_ONE_EXPERTIES = new ArrayList<>();
    ArrayList<ExpertiesDataModel> QUESTION_TWO_EXPERTIES = new ArrayList<>();
    ArrayList<ExpertiesDataModel> QUESTION_ONE_EXPERTIES_Suggest = new ArrayList<>();
    ArrayList<ExpertiesDataModel> QUESTION_TWO_EXPERTIES_Suggest = new ArrayList<>();

    TextView Question_One_Text, Question_Two_Text;
    private static final List<String> MedicalKeywords = new ArrayList<String>();
    private static final List<String> q_one_keyword = new ArrayList<String>();
    private static final List<String> StraintKeywords = new ArrayList<String>();
    private static final List<String> q_two_keywords = new ArrayList<String>();

    ArrayList<DataModelEntryExpert> question_one_experties = new ArrayList<>();
    ArrayList<DataModelEntryExpert> question_two_experties = new ArrayList<>();
    ExpertAreaRecylerAdapter question_one_adapter;
    ExpertAreaRecylerAdapter question_two_adapter;
    //    MyOptionsPickerView singlePickerOne;
//    MyOptionsPickerView singlePickerTwo;
    View v_suggest_2;
    View v_suggest_1;

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

    ScrollView scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expertise_question);
//        FullScreen(this);
        StatusBarUtil.setTransparent(this);
        HideKeyboard(AddExpertiseQuestionActivity.this);
        isEdit = getIntent().getExtras().getBoolean("isEdit");
        JSONObject object = new JSONObject();
        scroller = (ScrollView) findViewById(R.id.scroll_view);
        scroller.setFocusableInTouchMode(true);
        scroller.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//        singlePickerOne = new MyOptionsPickerView(AddExpertiseQuestionActivity.this);
//        singlePickerTwo = new MyOptionsPickerView(AddExpertiseQuestionActivity.this);
        new VollyAPICall(AddExpertiseQuestionActivity.this, true, get_expertise, object, user.getSession_key(), Request.Method.GET, AddExpertiseQuestionActivity.this, APIActions.ApiActions.get_Experties);
        Complete_profile = (Button) findViewById(R.id.complete_profile);
        intro_how_too = (ImageView) findViewById(R.id.intro_how_too);
//        bottom_view = (View) findViewById(R.id.bottom_view);
        Remind_me_later = (Button) findViewById(R.id.remind_me_later);
        question_one_recyler_view = (RecyclerView) findViewById(R.id.question_one_recylerview);
        question_one_recyler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        question_two_recyler_view = (RecyclerView) findViewById(R.id.question_two_recylerview);
        question_two_recyler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        question_two_adapter = new ExpertAreaRecylerAdapter(this, question_two_experties, 2);
        question_two_recyler_view.setAdapter(question_two_adapter);
        question_two_adapter.setClickListener(this);
        question_one_adapter = new ExpertAreaRecylerAdapter(this, question_one_experties, 1);
        question_one_recyler_view.setAdapter(question_one_adapter);
        question_one_adapter.setClickListener(this);
        Question_One = findViewById(R.id.question_one_layout);
        Question_Two = findViewById(R.id.question_two_layout);

        Question_One_Text = (TextView) findViewById(R.id.question_one);
        Question_Two_Text = (TextView) findViewById(R.id.question_two);

        Add_Expert_Area = (Button) findViewById(R.id.add_epert_area);
        Add_Expert_Area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Question_One_Experties.length() != 0 || Question_Two_Experties.length() != 0) && (question_one_keyword_count > 0 || question_two_keyword_count > 0)) {
//                    Question_One.setVisibility(View.GONE);
//                    Question_Two.setVisibility(View.GONE);
//                    Complete_profile.setVisibility(View.VISIBLE);
//                    Add_Expert_Area.setVisibility(View.GONE);
//                    Remind_me_later.setVisibility(View.GONE);
                    isButtonPressed = true;
                    Complete_profile.performClick();
                } else {
                    CustomeToast.ShowCustomToast(getApplicationContext(), "Add Minimum One Experience For Each!", Gravity.TOP);
                }
            }
        });

        Complete_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (question_one_keyword_count > 0 || question_two_keyword_count > 0) {
                    if (QUESTION_ONE_EXPERTIES.size() > 0 || QUESTION_ONE_EXPERTIES_Suggest.size() > 0) {
                        Question_One_Experties = "";
                        Question_One_Answer = "";
                        for (int x = 0; x < question_one_experties.size(); x++) {
                            if (Question_One_Experties.equalsIgnoreCase("")) {
                                Question_One_Experties = Question_One_Experties + getItemID(question_one_experties.get(x).getTitle(), QUESTION_ONE_EXPERTIES);
                            } else {
                                Question_One_Experties = Question_One_Experties + "," + getItemID(question_one_experties.get(x).getTitle(), QUESTION_ONE_EXPERTIES);
                            }

                            if (Question_One_Answer.equalsIgnoreCase("")) {
                                Question_One_Answer = Question_One_Answer + question_one_experties.get(x).getTitle();
                            } else {
                                Question_One_Answer = Question_One_Answer + "\n" + question_one_experties.get(x).getTitle();
                            }
                        }
                    }
                    if (QUESTION_TWO_EXPERTIES.size() > 0 || QUESTION_TWO_EXPERTIES_Suggest.size() > 0) {
                        Question_Two_Experties = "";
                        Question_Two_Answer = "";
                        for (int x = 0; x < question_two_experties.size(); x++) {
                            if (Question_Two_Experties.equalsIgnoreCase("")) {
                                Question_Two_Experties = Question_Two_Experties + getItemID(question_two_experties.get(x).getTitle(), QUESTION_TWO_EXPERTIES);
                            } else {
                                Question_Two_Experties = Question_Two_Experties + "," + getItemID(question_two_experties.get(x).getTitle(), QUESTION_TWO_EXPERTIES);
                            }

                            if (Question_Two_Answer.equalsIgnoreCase("")) {
                                Question_Two_Answer = Question_Two_Answer + question_two_experties.get(x).getTitle();
                            } else {
                                Question_Two_Answer = Question_Two_Answer + "\n" + question_two_experties.get(x).getTitle();
                            }
                        }
                    }
                    try {
                        JSONObject object = new JSONObject();
                        object.put("question_one_exprties", Question_One_Experties);
                        object.put("question_two_exprties", Question_Two_Experties);
                        EditUserProfileActivity.list2 = question_one_experties;
                        EditUserProfileActivity.list = question_two_experties;

                        new VollyAPICall(AddExpertiseQuestionActivity.this, true, add_expertise, object, user.getSession_key(), Request.Method.POST, AddExpertiseQuestionActivity.this, post_Experties);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    CustomeToast.ShowCustomToast(getApplicationContext(), "Add minimum One Experience for each to continue!", Gravity.TOP);
//                    Question_One.setVisibility(View.VISIBLE);
//                    Question_Two.setVisibility(View.VISIBLE);
                    Complete_profile.setVisibility(View.GONE);
//                    Add_Expert_Area.setVisibility(View.VISIBLE);
//                    Remind_me_later.setVisibility(View.VISIBLE);
                }
            }
        });


        v_suggest_1 = findViewById(R.id.no_suggest_layout_1);

        v_suggest_2 = findViewById(R.id.no_suggest_layout_2);
        Edit_text_question_one = findViewById(R.id.edit_text_question_one);
        Edit_text_question_one.setThreshold(1);
//        Edit_text_question_one.setFocusable(false);
        Edit_text_question_two = findViewById(R.id.edit_text_question_two);
//        Edit_text_question_two.setDropDownAnchor(R.id.question_two_recylerview);
        Edit_text_question_two.setThreshold(1);
        Edit_text_question_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Edit_text_question_one.isPopupShowing()) {
                            v_suggest_1.setVisibility(View.GONE);
                        } else if (editable.toString().isEmpty()) {
                            v_suggest_1.setVisibility(View.GONE);
                        } else {
                            v_suggest_1.setVisibility(View.GONE);
                        }
                    }
                }, 20);
            }
        });
        Edit_text_question_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Edit_text_question_two.isPopupShowing()) {
                            v_suggest_2.setVisibility(View.GONE);
                        } else if (editable.toString().isEmpty()) {
                            v_suggest_2.setVisibility(View.GONE);
                        } else {
                            v_suggest_2.setVisibility(View.GONE);
                        }
                    }
                }, 20);
            }
        });
        LinearLayout listen_1 = v_suggest_1.findViewById(R.id.suggest_add);
        listen_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkifcontainFirst()) {
                    CustomeToast.ShowCustomToast(view.getContext(), "This Suggestion Already Added!", Gravity.TOP);
                } else {
                    try {
                        new VollyAPICall(view.getContext()
                                , true
                                , URL.add_expertise_suggestion
                                , new JSONObject().put("suggestion", Edit_text_question_one.getText().toString().trim()).put("exp_question_id", 1)
                                , user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                            @Override
                            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                Log.d("SavedEr1: ", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    ExpertiesDataModel suggested = new Gson().fromJson(jsonObject.getJSONObject("successData").toString(), ExpertiesDataModel.class);
                                    suggested.setTitle(Edit_text_question_one.getText().toString().trim());
                                    suggested.setId(Integer.parseInt(suggested.getExp_id()));
                                    QUESTION_ONE_EXPERTIES_Suggest.add(suggested);
                                    question_one_keyword_count++;
                                    question_one_experties.add(new DataModelEntryExpert(Edit_text_question_one.getText().toString().trim(), true));
                                    if (Question_One_Experties.equalsIgnoreCase("")) {
                                        Question_One_Experties = Question_One_Experties + suggested.getExp_id();
                                    } else {
                                        Question_One_Experties = Question_One_Experties + "," + suggested.getExp_id();
                                    }
                                    if (Question_One_Answer.equalsIgnoreCase("")) {
                                        Question_One_Answer = Question_One_Answer + Edit_text_question_one.getText().toString().trim() + "\n";
                                    } else {
                                        Question_One_Answer = Question_One_Answer + "\n" + Edit_text_question_one.getText().toString().trim();
                                    }

                                    if (question_one_keyword_count >= 5) {
                                        Edit_text_question_one.setText("");
                                        Question_One.setVisibility(View.GONE);
                                        HideKeyboard(AddExpertiseQuestionActivity.this);
                                        if (question_two_keyword_count >= 5) {
//                        Complete_profile.setVisibility(View.VISIBLE);
                                            Complete_profile.setVisibility(View.GONE);
                                            Add_Expert_Area.setVisibility(View.VISIBLE);
                                            Remind_me_later.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Edit_text_question_one.setText("");
                                    }
                                    question_one_adapter.notifyDataSetChanged();
                                    RefineExpties();
                                    Edit_text_question_one.setText("");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    CustomeToast.ShowCustomToast(AddExpertiseQuestionActivity.this, object.getString("errorMessage"), Gravity.TOP);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, APIActions.ApiActions.get_suggestion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        LinearLayout listen_2 = v_suggest_2.findViewById(R.id.suggest_add);
        listen_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkifcontainSecond()) {
                    CustomeToast.ShowCustomToast(view.getContext(), "This Suggestion Already Added!", Gravity.TOP);
                } else {
                    try {
                        new VollyAPICall(view.getContext()
                                , true
                                , URL.add_expertise_suggestion
                                , new JSONObject().put("suggestion", Edit_text_question_two.getText().toString().trim()).put("exp_question_id", 2)
                                , user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                            @Override
                            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    ExpertiesDataModel suggested = new Gson().fromJson(jsonObject.getJSONObject("successData").toString(), ExpertiesDataModel.class);
                                    suggested.setTitle(Edit_text_question_two.getText().toString().trim());
                                    suggested.setId(Integer.parseInt(suggested.getExp_id()));
                                    QUESTION_TWO_EXPERTIES_Suggest.add(suggested);
                                    question_two_keyword_count++;
                                    question_two_experties.add(new DataModelEntryExpert(Edit_text_question_two.getText().toString().trim(), true));
                                    if (Question_Two_Answer.equalsIgnoreCase("")) {
                                        Question_Two_Answer = Question_Two_Answer + Edit_text_question_two.getText().toString().trim() + "\n";
                                    } else {
                                        Question_Two_Answer = Question_Two_Answer + "\n" + Edit_text_question_two.getText().toString().trim();
                                    }
                                    if (Question_Two_Experties.equalsIgnoreCase("")) {
                                        Question_Two_Experties = Question_Two_Experties + suggested.getExp_id();
                                    } else {
                                        Question_Two_Experties = Question_Two_Experties + "," + suggested.getExp_id();
                                    }

                                    if (question_two_keyword_count >= 5) {
                                        if (question_one_keyword_count >= 5) {
//                        Complete_profile.setVisibility(View.VISIBLE);
                                            Complete_profile.setVisibility(View.GONE);
//                        Add_Expert_Area.setVisibility(View.GONE);
                                            Add_Expert_Area.setVisibility(View.VISIBLE);
                                            Remind_me_later.setVisibility(View.GONE);
                                        }
                                        Edit_text_question_two.setText("");
                                        Question_Two.setVisibility(View.GONE);
                                        HideKeyboard(AddExpertiseQuestionActivity.this);

                                    } else {
                                        Edit_text_question_two.setText("");
                                    }
                                    question_two_adapter.notifyDataSetChanged();
                                    RefineExpties();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    CustomeToast.ShowCustomToast(AddExpertiseQuestionActivity.this, object.getString("errorMessage"), Gravity.TOP);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, APIActions.ApiActions.get_suggestion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Edit_text_question_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                question_one_keyword_count++;
                Log.d("selcet", adapterView.getSelectedItemPosition() + "");
                question_one_experties.add(new DataModelEntryExpert(adapterView.getItemAtPosition(i).toString()));
                if (Question_One_Experties.equalsIgnoreCase("")) {
                    Question_One_Experties = Question_One_Experties + getItemID(adapterView.getItemAtPosition(i).toString(), QUESTION_ONE_EXPERTIES, QUESTION_ONE_EXPERTIES_Suggest);
                } else {
                    Question_One_Experties = Question_One_Experties + "," + getItemID(adapterView.getItemAtPosition(i).toString(), QUESTION_ONE_EXPERTIES, QUESTION_ONE_EXPERTIES_Suggest);
                }
                if (Question_One_Answer.equalsIgnoreCase("")) {
                    Question_One_Answer = Question_One_Answer + adapterView.getItemAtPosition(i) + "\n";
                } else {
                    Question_One_Answer = Question_One_Answer + "\n" + adapterView.getItemAtPosition(i);
                }

                if (question_one_keyword_count >= 5) {
                    Edit_text_question_one.setText("");
                    Question_One.setVisibility(View.GONE);
                    HideKeyboard(AddExpertiseQuestionActivity.this);
                    if (question_two_keyword_count >= 5) {
//                        Complete_profile.setVisibility(View.VISIBLE);
                        Complete_profile.setVisibility(View.GONE);
                        Add_Expert_Area.setVisibility(View.VISIBLE);
                        Remind_me_later.setVisibility(View.GONE);
                    }
                } else {
                    Edit_text_question_one.setText("");
                }
                question_one_adapter.notifyDataSetChanged();
                RefineExpties();

            }
        });
        Edit_text_question_two.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    HideKeyboard(AddExpertiseQuestionActivity.this);
                    return true;
                }
                return false;
            }
        });
        Edit_text_question_two.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        Edit_text_question_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                question_two_keyword_count++;
                question_two_experties.add(new DataModelEntryExpert(adapterView.getItemAtPosition(i).toString()));
                if (Question_Two_Answer.equalsIgnoreCase("")) {
                    Question_Two_Answer = Question_Two_Answer + adapterView.getItemAtPosition(i) + "\n";
                } else {
                    Question_Two_Answer = Question_Two_Answer + "\n" + adapterView.getItemAtPosition(i);
                }
                if (Question_Two_Experties.equalsIgnoreCase("")) {
                    Question_Two_Experties = Question_Two_Experties + getItemID(adapterView.getItemAtPosition(i).toString(), QUESTION_TWO_EXPERTIES, QUESTION_TWO_EXPERTIES_Suggest);
                } else {
                    Question_Two_Experties = Question_Two_Experties + "," + getItemID(adapterView.getItemAtPosition(i).toString(), QUESTION_TWO_EXPERTIES, QUESTION_TWO_EXPERTIES_Suggest);
                }

                if (question_two_keyword_count >= 5) {
                    if (question_one_keyword_count >= 5) {
//                        Complete_profile.setVisibility(View.VISIBLE);
                        Complete_profile.setVisibility(View.GONE);
//                        Add_Expert_Area.setVisibility(View.GONE);
                        Add_Expert_Area.setVisibility(View.VISIBLE);
                        Remind_me_later.setVisibility(View.GONE);
                    }
                    Edit_text_question_two.setText("");
                    Question_Two.setVisibility(View.GONE);
                    HideKeyboard(AddExpertiseQuestionActivity.this);

                } else {
                    Edit_text_question_two.setText("");
                }
                question_two_adapter.notifyDataSetChanged();
                RefineExpties();
            }
        });

        Remind_me_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEdit) {
                    GoToHome(AddExpertiseQuestionActivity.this, false);
                }
                finish();
            }
        });

        if (isEdit) {
            List<ExpertiesDataModel> dataModelList = (List<ExpertiesDataModel>) getIntent().getExtras().getSerializable("expertArray");
            question_one_keyword_count = getIntent().getExtras().getInt("medical_expertiex_count");
            question_two_keyword_count = getIntent().getExtras().getInt("strain_expertiex_count");
            Question_One_Answer = getIntent().getExtras().getString("medical_expertiex");
            Question_One_Experties = getIntent().getExtras().getString("medical_expertiex_ids");
            Question_Two_Answer = getIntent().getExtras().getString("strain_expertiex");
            Question_Two_Experties = getIntent().getExtras().getString("strain_expertiex_ids");
            if (question_one_keyword_count > 0 && question_two_keyword_count > 0) {
//                String[] one_experties = Question_One_Answer.split("\n");
//                for (int x = 0; x < one_experties.length; x++) {
//                    question_one_experties.add(new DataModelEntryExpert(one_experties[x]));
//                }
                for (int i = 0; i < dataModelList.size(); i++) {
                    if (dataModelList.get(i).getMedical_id() != null) {
                        if (dataModelList.get(i).isApproved == 0 && dataModelList.get(i).getMedical().isApproved.equalsIgnoreCase("0")) {
                            question_one_experties.add(new DataModelEntryExpert(dataModelList.get(i).getMedical().getTitle(), true));
                        } else {
                            question_one_experties.add(new DataModelEntryExpert(dataModelList.get(i).getMedical().getTitle(), false));
                        }

                    } else {
                        if (dataModelList.get(i).isApproved == 0 && dataModelList.get(i).getStrain().isApproved.equalsIgnoreCase("0")) {
                            question_two_experties.add(new DataModelEntryExpert(dataModelList.get(i).getStrain().getTitle(), true));
                        } else {
                            question_two_experties.add(new DataModelEntryExpert(dataModelList.get(i).getStrain().getTitle(), false));
                        }
                    }
                }
                question_one_adapter.notifyDataSetChanged();
//                String[] two_experties = Question_Two_Answer.split("\n");
//                for (int x = 0; x < two_experties.length; x++) {
//                    question_two_experties.add(new DataModelEntryExpert(two_experties[x]));
//                }
                question_two_adapter.notifyDataSetChanged();
            }
            if (question_one_keyword_count == 5) {
                Question_One.setVisibility(View.GONE);
            }

            if (question_two_keyword_count == 5) {
                Question_Two.setVisibility(View.GONE);
            }

            if (question_two_keyword_count == 5 && question_one_keyword_count == 5) {
                Complete_profile.setVisibility(View.GONE);
//                Complete_profile.setVisibility(View.VISIBLE);
//                Add_Expert_Area.setVisibility(View.GONE);
                Add_Expert_Area.setVisibility(View.VISIBLE);
                Remind_me_later.setVisibility(View.GONE);
            }

        }
        intro_how_too.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO for dialogue
//                IntroHowToAddQuestionDialog introHowToAddQuestionDialog = IntroHowToAddQuestionDialog.newInstance(AddExpertiseQuestionActivity.this);
//                introHowToAddQuestionDialog.show(getSupportFragmentManager(), "pd");
            }
        });
//        Edit_text_question_two.setDropDownAnchor(R.id.question_two_recylerview);
//        Question setDropDownAnchor
    }

    private boolean checkifcontainFirst() {
        for (int i = 0; i < question_one_experties.size(); i++) {
            if (question_one_experties.get(i).getTitle().trim().equalsIgnoreCase(Edit_text_question_one.getText().toString().trim())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkifcontainSecond() {
        for (int i = 0; i < question_two_experties.size(); i++) {
            if (question_two_experties.get(i).getTitle().trim().equalsIgnoreCase(Edit_text_question_two.getText().toString().trim())) {
                return true;
            }
        }
        return false;
    }

    public void RefineExpties() {
        ArrayList<String> data = new ArrayList<>();
        for (int x = 0; x < q_one_keyword.size(); x++) {
            if (!Question_One_Answer.contains(q_one_keyword.get(x))) {
                data.add(q_one_keyword.get(x));
            }
        }
        MedicalKeywords.clear();
        MedicalKeywords.addAll(data);
        ArrayList<String> data_1 = new ArrayList<>();
        for (int x = 0; x < q_two_keywords.size(); x++) {
            if (!Question_Two_Answer.contains(q_two_keywords.get(x))) {
                data_1.add(q_two_keywords.get(x));
            }
        }

        StraintKeywords.clear();
        StraintKeywords.addAll(data_1);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, MedicalKeywords);
        Edit_text_question_one.setAdapter(adapter);
        Edit_text_question_one.setThreshold(1);
//        singlePickerOne.setPicker((ArrayList) MedicalKeywords);
//        singlePickerOne.setTitle("");
//        singlePickerOne.setCyclic(false);
//        singlePickerOne.setSelectOptions(0);
//        singlePickerOne.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3) {
//
//                try {
////                Log.d("selcet", adapterView.getSelectedItemPosition() + "");
//                    question_one_experties.add(MedicalKeywords.get(options1));
//                    question_one_keyword_count++;
//                    if (Question_One_Experties.equalsIgnoreCase("")) {
//                        Question_One_Experties = Question_One_Experties + getItemID(MedicalKeywords.get(options1), QUESTION_ONE_EXPERTIES);
//                    } else {
//                        Question_One_Experties = Question_One_Experties + "," + getItemID(MedicalKeywords.get(options1), QUESTION_ONE_EXPERTIES);
//                    }
//                    if (Question_One_Answer.equalsIgnoreCase("")) {
//                        Question_One_Answer = Question_One_Answer + MedicalKeywords.get(options1) + "\n";
//                    } else {
//                        Question_One_Answer = Question_One_Answer + "\n" + MedicalKeywords.get(options1);
//                    }
//
//                    if (question_one_keyword_count >= 5) {
//                        Edit_text_question_one.setText("");
//                        Question_One.setVisibility(View.GONE);
//                        HideKeyboard(AddExpertiseQuestionActivity.this);
//                        if (question_two_keyword_count >= 5) {
////                        Complete_profile.setVisibility(View.VISIBLE);
//                            Complete_profile.setVisibility(View.GONE);
//                            Add_Expert_Area.setVisibility(View.VISIBLE);
//                            Remind_me_later.setVisibility(View.GONE);
//                        }
//                    } else {
//                        Edit_text_question_one.setText("");
//                    }
//                    question_one_adapter.notifyDataSetChanged();
//                    RefineExpties();
//                } catch (IndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


        adapter_two = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, StraintKeywords);
        Edit_text_question_two.setAdapter(adapter_two);
        Edit_text_question_two.setThreshold(1);
//        singlePickerTwo.setPicker((ArrayList) StraintKeywords);
//        singlePickerTwo.setTitle("");
//        singlePickerTwo.setCyclic(false);
//        singlePickerTwo.setSelectOptions(0);
//
//        singlePickerTwo.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3) {
//                try {
//                    question_two_experties.add(StraintKeywords.get(options1));
//                    question_two_keyword_count++;
//
//                    if (Question_Two_Answer.equalsIgnoreCase("")) {
//                        Question_Two_Answer = Question_Two_Answer + StraintKeywords.get(options1) + "\n";
//                    } else {
//                        Question_Two_Answer = Question_Two_Answer + "\n" + StraintKeywords.get(options1);
//                    }
//                    if (Question_Two_Experties.equalsIgnoreCase("")) {
//                        Question_Two_Experties = Question_Two_Experties + getItemID(StraintKeywords.get(options1), QUESTION_TWO_EXPERTIES);
//                    } else {
//                        Question_Two_Experties = Question_Two_Experties + "," + getItemID(StraintKeywords.get(options1), QUESTION_TWO_EXPERTIES);
//                    }
//
//                    if (question_two_keyword_count >= 5) {
//                        if (question_one_keyword_count >= 5) {
////                        Complete_profile.setVisibility(View.VISIBLE);
//                            Complete_profile.setVisibility(View.GONE);
////                        Add_Expert_Area.setVisibility(View.GONE);
//                            Add_Expert_Area.setVisibility(View.VISIBLE);
//                            Remind_me_later.setVisibility(View.GONE);
//                        }
//                        Edit_text_question_two.setText("");
//                        Question_Two.setVisibility(View.GONE);
//                        HideKeyboard(AddExpertiseQuestionActivity.this);
//
//                    } else {
//                        Edit_text_question_two.setText("");
//                    }
//                    question_two_adapter.notifyDataSetChanged();
//                    RefineExpties();
//                } catch (IndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.get_Experties) {
            try {
                JSONObject object = new JSONObject(response);
                JSONObject jsonArray = object.getJSONObject("successData");
                q_one_keyword.clear();
                q_two_keywords.clear();
                JSONArray strains = jsonArray.getJSONArray("strains");
                JSONArray medicals = jsonArray.getJSONArray("medicals");
                for (int i = 0; i < strains.length(); i++) {
                    JSONObject jsonObject1 = strains.getJSONObject(i);
                    q_two_keywords.add(jsonObject1.getString("title"));
                    ExpertiesDataModel dataModel = new ExpertiesDataModel();
                    dataModel.setId(jsonObject1.getInt("id"));
                    dataModel.setTitle(jsonObject1.getString("title"));
                    dataModel.setCreated_at(jsonObject1.getString("created_at"));
                    dataModel.setUpdated_at(jsonObject1.getString("updated_at"));
                    dataModel.setIsApproved(Integer.valueOf(jsonObject1.getString("approved")));
                    QUESTION_TWO_EXPERTIES.add(dataModel);
                }
                for (int i = 0; i < medicals.length(); i++) {
                    JSONObject jsonObject1 = medicals.getJSONObject(i);
                    q_one_keyword.add(jsonObject1.getString("m_condition"));
                    ExpertiesDataModel dataModel = new ExpertiesDataModel();
                    dataModel.setId(jsonObject1.getInt("id"));
                    dataModel.setTitle(jsonObject1.getString("m_condition"));
                    dataModel.setCreated_at(jsonObject1.getString("created_at"));
                    dataModel.setUpdated_at(jsonObject1.getString("updated_at"));
                    dataModel.setIsApproved(Integer.valueOf(jsonObject1.getString("is_approved")));
                    QUESTION_ONE_EXPERTIES.add(dataModel);
                }
                //                for (int x = 0; x < jsonArray.length(); x++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(x);
//                    if (x == 0) {
                Question_One_Text.setText("Which conditions or ailments have you treated with cannabis?");
//                    }
//                    if (x == 1) {
                Question_Two_Text.setText("What marijuana strains do you have experience with?");
//                    }
//                    JSONArray experties_array = jsonObject.getJSONArray("get_experties");
//                    for (int y = 0; y < experties_array.length(); y++) {
//                        JSONObject jsonObject1 = experties_array.getJSONObject(y);
////                        if (jsonObject1.getString("is_approved").equalsIgnoreCase("1")) {
//                        if (x == 0) {
//
//                            q_one_keyword.add(jsonObject1.getString("title"));
//                            ExpertiesDataModel dataModel = new ExpertiesDataModel();
//                            dataModel.setId(jsonObject1.getInt("id"));
//                            dataModel.setTitle(jsonObject1.getString("title"));
//                            dataModel.setCreated_at(jsonObject1.getString("created_at"));
//                            dataModel.setUpdated_at(jsonObject1.getString("updated_at"));
//                            dataModel.setIsApproved(Integer.valueOf(jsonObject1.getString("is_approved")));
//                            QUESTION_ONE_EXPERTIES.add(dataModel);
//                        }
//                        if (x == 1) {
//                            ExpertiesDataModel dataModel = new ExpertiesDataModel();
//                            dataModel.setId(jsonObject1.getInt("id"));
//                            dataModel.setTitle(jsonObject1.getString("title"));
//                            dataModel.setCreated_at(jsonObject1.getString("created_at"));
//                            dataModel.setUpdated_at(jsonObject1.getString("updated_at"));
//
//                            dataModel.setIsApproved(Integer.valueOf(jsonObject1.getString("is_approved")));
//                            QUESTION_TWO_EXPERTIES.add(dataModel);
//                            q_two_keywords.add(jsonObject1.getString("title"));
//                        }
////                        }
//                    }
//                }
                RefineExpties();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == post_Experties) {
            if (isEdit) {
                profiledataModel.setMedical_expertiex(Question_One_Answer);
                profiledataModel.setMedical_expertiex_count(question_one_keyword_count);
                profiledataModel.setMedical_expertiex_ids(Question_One_Experties);

                profiledataModel.setStrain_Experties_ids(Question_Two_Experties);
                profiledataModel.setStrain_Experties(Question_Two_Answer);
                profiledataModel.setStrain_Experties_count(question_two_keyword_count);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new MessageEvent(false));
                    }
                }, 100);
                if (isButtonPressed)
                    finish();
            } else {
                GoTo(AddExpertiseQuestionActivity.this, FinalStepProfileComplete.class);
                finish();
            }

        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getApplicationContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getItemID(String text, ArrayList<ExpertiesDataModel> dataModels) {
        int id = 1;
        for (int x = 0; x < dataModels.size(); x++) {
            if (text.equalsIgnoreCase(dataModels.get(x).getTitle())) {
                id = dataModels.get(x).getId();
                break;
            }
        }
        return id;
    }

    public int getItemID(String text, ArrayList<ExpertiesDataModel> dataModels, ArrayList<ExpertiesDataModel> dataModelsSuggest) {
        int id = 1;
        for (int x = 0; x < dataModels.size(); x++) {
            if (text.equalsIgnoreCase(dataModels.get(x).getTitle())) {
                id = dataModels.get(x).getId();
                return id;
            }
        }
        for (int x = 0; x < dataModelsSuggest.size(); x++) {
            if (text.equalsIgnoreCase(dataModelsSuggest.get(x).getTitle())) {
                id = dataModelsSuggest.get(x).getId();
                return id;
            }
        }
        return id;
    }

    @Override
    public void onItemClickKey(View view, int position) {

    }

    @Override
    public void onQuestionOneCross(String text) {

        Question_One_Answer = "";
        int pos = 0;
        for (int x = 0; x < question_one_experties.size(); x++) {
            if (Question_One_Answer.equalsIgnoreCase("")) {
                Question_One_Answer = Question_One_Answer + question_one_experties.get(x).getTitle();
            } else {
                Question_One_Answer = Question_One_Answer + "\n" + question_one_experties.get(x).getTitle();
            }
            if (question_one_experties.get(x).getTitle().equalsIgnoreCase(text)) {
                pos = x;
            }
        }
//        question_one_experties.remove(pos);
        question_one_keyword_count--;


        if (question_one_keyword_count < 5) {
            Question_One.setVisibility(View.VISIBLE);
        }

        if (question_two_keyword_count < 5) {
            Question_Two.setVisibility(View.VISIBLE);
        }

        Complete_profile.setVisibility(View.GONE);
        Add_Expert_Area.setVisibility(View.VISIBLE);
        Remind_me_later.setVisibility(View.VISIBLE);
        RefineExpties();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isButtonPressed = false;
//                Complete_profile.performClick();
//            }
//        }, 100);
    }

    @Override
    public void onQuestionTwoCross(String text) {
        Question_Two_Answer = "";
        int pos = 0;
        for (int x = 0; x < question_two_experties.size(); x++) {
            if (Question_Two_Answer.equalsIgnoreCase("")) {
                Question_Two_Answer = Question_Two_Answer + question_two_experties.get(x).getTitle();
            } else {
                Question_Two_Answer = Question_Two_Answer + "\n" + question_two_experties.get(x).getTitle();
            }
            pos = x;
        }
//        question_two_experties.remove(pos);
        question_two_keyword_count--;
        if (question_one_keyword_count < 5) {
            Question_One.setVisibility(View.VISIBLE);
        }

        if (question_two_keyword_count < 5) {
            Question_Two.setVisibility(View.VISIBLE);
        }

        Complete_profile.setVisibility(View.GONE);
        Add_Expert_Area.setVisibility(View.VISIBLE);
        Remind_me_later.setVisibility(View.VISIBLE);
        RefineExpties();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isButtonPressed = false;
//                Complete_profile.performClick();
//            }
//        }, 100);
    }

    @Override
    public void onCrossClick(IntroHowToAddQuestionDialog dialog) {

    }

    public static class DataModelEntryExpert {
        String title;
        boolean isSuggest;
        int positionItem;

        public int getPositionItem() {
            return positionItem;
        }

        public void setPositionItem(int positionItem) {
            this.positionItem = positionItem;
        }

        public DataModelEntryExpert(String title) {
            this.title = title;
        }

        public DataModelEntryExpert(String title, boolean isSuggest) {
            this.title = title;
            this.isSuggest = isSuggest;
        }

        public DataModelEntryExpert(String title, boolean isSuggest, int positionItem) {
            this.title = title;
            this.isSuggest = isSuggest;
            this.positionItem = positionItem;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isSuggest() {
            return isSuggest;
        }

        public void setSuggest(boolean suggest) {
            isSuggest = suggest;
        }
    }

    public class Experty implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("exp_question_id")
        @Expose
        private Integer expQuestionId;
        @SerializedName("is_approved")
        @Expose
        public String isApproved;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getExpQuestionId() {
            return expQuestionId;
        }

        public void setExpQuestionId(Integer expQuestionId) {
            this.expQuestionId = expQuestionId;
        }

        public String getIsApproved() {
            return isApproved;
        }

        public void setIsApproved(String isApproved) {
            this.isApproved = isApproved;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

    public class ExpertyMed implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("m_condition")
        @Expose
        private String title;
        @SerializedName("exp_question_id")
        @Expose
        private Integer expQuestionId;
        @SerializedName("is_approved")
        @Expose
        public String isApproved;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getExpQuestionId() {
            return expQuestionId;
        }

        public void setExpQuestionId(Integer expQuestionId) {
            this.expQuestionId = expQuestionId;
        }

        public String getIsApproved() {
            return isApproved;
        }

        public void setIsApproved(String isApproved) {
            this.isApproved = isApproved;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

    public class ExpertiesDataModel implements Serializable {
        @SerializedName("id")
        @Expose
        int id;
        String title;
        @SerializedName("exp_id")
        @Expose
        String exp_id;
        @SerializedName("strain_id")
        @Expose
        String strain_id;
        @SerializedName("medical_use_id")
        @Expose
        String medical_id;
        @SerializedName("exp_question_id")
        @Expose
        String exp_question_id;
        @SerializedName("created_at")
        @Expose
        String created_at;
        @SerializedName("updated_at")
        @Expose
        String updated_at;
        @SerializedName("experty")
        @Expose
        private Experty experty;
        @SerializedName("strain")
        @Expose
        private Experty strain;
        @SerializedName("medical")
        @Expose
        private ExpertyMed medical;

        public Experty getStrain() {
            return strain;
        }

        public String getStrain_id() {
            return strain_id;
        }

        public void setStrain_id(String strain_id) {
            this.strain_id = strain_id;
        }

        public String getMedical_id() {
            return medical_id;
        }

        public void setMedical_id(String medical_id) {
            this.medical_id = medical_id;
        }

        public void setStrain(Experty strain) {
            this.strain = strain;
        }

        public ExpertyMed getMedical() {
            return medical;
        }

        public void setMedical(ExpertyMed medical) {
            this.medical = medical;
        }

        public Experty getExperty() {
            return experty;
        }

        public void setExperty(Experty experty) {
            this.experty = experty;
        }

        public String getExp_id() {
            return exp_id;
        }

        public void setExp_id(String exp_id) {
            this.exp_id = exp_id;
        }

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

        public String getExp_question_id() {
            return exp_question_id;
        }

        public void setExp_question_id(String exp_question_id) {
            this.exp_question_id = exp_question_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        @SerializedName("user_id")
        @Expose
        private Integer userId;


        @SerializedName("is_approved")
        @Expose
        public Integer isApproved;


        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }


        public Integer getIsApproved() {
            return isApproved;
        }

        public void setIsApproved(Integer isApproved) {
            this.isApproved = isApproved;
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


