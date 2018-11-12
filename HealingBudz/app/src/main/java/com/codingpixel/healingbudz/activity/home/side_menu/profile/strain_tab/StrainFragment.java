package com.codingpixel.healingbudz.activity.home.side_menu.profile.strain_tab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.adapter.UserProfileStrainFragmentRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_user_profile_strains;

@SuppressLint("ValidFragment")
public class StrainFragment extends Fragment implements APIResponseListner, UserProfileStrainFragmentRecylerAdapter.ItemClickListener {
    RecyclerView recyclerView;
    LinearLayout Main_content, Refresh;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    int USER_ID;
    UserProfileStrainFragmentRecylerAdapter recyler_adapter;
    TextView not_found;

    @SuppressLint("ValidFragment")
    public StrainFragment(int user_id) {
        this.USER_ID = user_id;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_profile_strain_tab_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyler_View);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        Main_content = view.findViewById(R.id.main_contetn);
        not_found = view.findViewById(R.id.not_found);
        Refresh = view.findViewById(R.id.refresh);
        recyler_adapter = new UserProfileStrainFragmentRecylerAdapter(getContext(), data);
        recyclerView.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        recyclerView.setNestedScrollingEnabled(false);
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(getContext(), false, URL.get_user_profile_strains + "/" + USER_ID, jsonObject, user.getSession_key(), Request.Method.GET, StrainFragment.this, get_user_profile_strains);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh.setVisibility(View.GONE);
        Main_content.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("successData");
            data.clear();
            for (int x = 0; x < jsonArray.length(); x++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                data.add(jsonObject1.getJSONObject("get_strain").optString("title"));
                ids.add(jsonObject1.optInt("strain_id"));
            }
            recyler_adapter.notifyDataSetChanged();
            if (data.size() == 0) {
                not_found.setVisibility(View.VISIBLE);
                Main_content.setVisibility(View.GONE);
            } else {
                Main_content.setVisibility(View.VISIBLE);
                not_found.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh.setVisibility(View.GONE);
        Main_content.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent strain_intetn = new Intent(getContext(), StrainDetailsActivity.class);
        strain_intetn.putExtra("strain_id", ids.get(position));
        startActivity(strain_intetn);
    }
}
