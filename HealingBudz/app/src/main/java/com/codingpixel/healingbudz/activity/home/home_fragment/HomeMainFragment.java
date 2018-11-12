package com.codingpixel.healingbudz.activity.home.home_fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.AskQuestionFragment;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.HomeFragmentSearchRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.fragment_back_button_listner.BackButtonClickListner;
import com.codingpixel.healingbudz.interfaces.BackInterface;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.HomeActivity.showSubFragmentListner_object;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.network.model.URL.get_all_questions;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class HomeMainFragment extends Fragment implements HomeFragmentSearchRecylerAdapter.ItemClickListener, BackButtonClickListner, APIResponseListner {
    RecyclerView recyclerView;
    EditText search_field;
    LinearLayout searched_content;
    RelativeLayout home_parent_layout;
    HomeFragmentSearchRecylerAdapter recyler_adapter;
    ArrayList<HomeQAfragmentDataModel> main_data = new ArrayList<>();
    ArrayList<HomeQAfragmentDataModel> updated_data = new ArrayList<>();
    private BackInterface callBack;
    Button Ask_bud;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(getContext(), false, get_all_questions, jsonObject, user.getSession_key(), Request.Method.GET, HomeMainFragment.this, APIActions.ApiActions.get_my_question);
        home_parent_layout = view.findViewById(R.id.home_parent_layout);
//        InitFragmentBackbtnListner(getActivity() , this);
        searched_content = view.findViewById(R.id.search_content);
        search_field = view.findViewById(R.id.sarch_edit_field);
        recyclerView = view.findViewById(R.id.budz_question_recylreview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyler_adapter = new HomeFragmentSearchRecylerAdapter(getContext(), main_data);
        recyler_adapter.setClickListener(this);
        recyclerView.setAdapter(recyler_adapter);

        search_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyler_adapter.NotifyDataChange(matched_data(charSequence.toString()));
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        search_field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (searched_content.getVisibility() == View.VISIBLE) {
                        searched_content.setVisibility(View.GONE);
                        HideKeyboard((Activity) v.getContext());
                    }
                }
                return false;
            }
        });
        home_parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searched_content.getVisibility() == View.VISIBLE) {
                    searched_content.setVisibility(View.GONE);
                    HideKeyboard((Activity) view.getContext());
                    callBack.callBack();
                }

            }
        });


        search_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searched_content.setVisibility(View.VISIBLE);
            }
        });

        Ask_bud = view.findViewById(R.id.ask_budd);
        Ask_bud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AskQuestionFragment askQuestionFragment = new AskQuestionFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.attatch_fragment_right_to_left);
                transaction.add(R.id.fragment_id_full_screen, askQuestionFragment, "1");
                transaction.commitAllowingStateLoss();
            }
        });
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {
        HideKeyboard(getActivity());
        if (updated_data.get(position).getId() != -1) {
//        search_field.setText(updated_data.get(position).getQuestion());
//        searched_content.setVisibility(View.GONE);
            showSubFragmentListner_object.ShowQuestions(updated_data.get(position), false);
        }
    }

    public ArrayList<HomeQAfragmentDataModel> matched_data(String query) {
        ArrayList<HomeQAfragmentDataModel> data = new ArrayList<>();
        for (int x = 0; x < main_data.size(); x++) {
            if (main_data.get(x).getQuestion().toLowerCase().contains(query.toLowerCase())) {
                data.add(main_data.get(x));
            }
        }
        if (data.size() == 0) {
            data.add(new HomeQAfragmentDataModel(-1));
        }
        updated_data = data;
        return data;
    }

    @Override
    public void onBackButtonClick() {

    }

    public void SetBackListener(BackInterface homeActivity) {
        this.callBack = homeActivity;
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            JSONArray question_array = object.getJSONArray("successData");
            if (question_array.length() == 0) {
                main_data.clear();
                recyler_adapter.notifyDataSetChanged();
            } else {
                main_data.clear();
                recyler_adapter.notifyDataSetChanged();
                for (int x = 0; x < question_array.length(); x++) {
                    JSONObject quesiton_object = question_array.getJSONObject(x);
                    HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                    JSONObject user_object = quesiton_object.getJSONObject("get_user");
                    model.setUser_name(user_object.getString("first_name"));
                    model.setUser_name_dscription("asks...");
                    String user_photo = user_object.optString("image_path");
                    if (!user_photo.equalsIgnoreCase("null") && user_photo.length() > 6) {
                        model.setUser_photo(user_photo);
                        model.setAvatar(user_object.optString("avatar"));
                    } else {
                        model.setUser_photo(user_object.optString("avatar"));
                        model.setAvatar(user_object.optString("avatar"));
                    }
                    model.setUser_location(user_object.optString("location"));
                    model.setQuestion(quesiton_object.getString("question"));
                    model.setId(quesiton_object.getInt("id"));
                    model.setUser_id(quesiton_object.getInt("user_id"));
                    model.setCreated_at(quesiton_object.getString("created_at"));
                    model.setUpdated_at(quesiton_object.getString("updated_at"));
                    model.setQuestion_description(quesiton_object.getString("description"));
                    model.setAnswerCount(quesiton_object.getInt("get_answers_count"));
                    model.setGet_user_flag_count(quesiton_object.getInt("get_user_flag_count"));
                    model.setGet_user_likes_count(quesiton_object.getInt("get_user_likes_count"));
                    main_data.add(model);
                    recyler_adapter.notifyItemChanged(x);
                }
                recyler_adapter.notifyDataSetChanged();
                updated_data = main_data;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {

    }
}
