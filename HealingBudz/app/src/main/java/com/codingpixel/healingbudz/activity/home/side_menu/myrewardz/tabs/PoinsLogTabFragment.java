package com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.Points;
import com.codingpixel.healingbudz.DataModel.UserStrainDetailsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.MyRewardzActivity;
import com.codingpixel.healingbudz.adapter.PointsLogViewAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ViewAllDetailsAddedByUserButtonListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_user_point_log;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class PoinsLogTabFragment extends Fragment implements View.OnClickListener, ViewAllDetailsAddedByUserButtonListner, APIResponseListner, PointsLogViewAdapter.ItemClickListener {


    private RecyclerView recycler_view_free_points;
    private LinearLayout refresh;
    TextView not_found, points_content;
    PointsLogViewAdapter pointsLogViewAdapter;
    private List<Points> mData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.points_log_tab_layout, container, false);
        HideKeyboard(getActivity());
        InitView(view);
        new VollyAPICall(view.getContext()
                , false
                , URL.get_user_point_log
                , new JSONObject()
                , user.getSession_key()
                , Request.Method.GET
                , PoinsLogTabFragment.this
                , get_user_point_log);
        return view;
    }


    private void InitView(View view) {


        pointsLogViewAdapter = new PointsLogViewAdapter(getActivity(), mData);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recycler_view_free_points = (RecyclerView) view.findViewById(R.id.recycler_view_points_log);
        points_content = view.findViewById(R.id.points_content);
        points_content.setText("" + MyRewardzActivity.points);
        not_found = view.findViewById(R.id.not_found);
        recycler_view_free_points.setLayoutManager(layoutManager1);
        pointsLogViewAdapter.setClickListener(this);
        recycler_view_free_points.setAdapter(pointsLogViewAdapter);
        refresh = view.findViewById(R.id.refresh);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                refresh.setVisibility(View.GONE);
//                recycler_view_free_points.setAdapter(pointsLogViewAdapter);
//                pointsLogViewAdapter.notifyDataSetChanged();
//
//            }
//        }, 1000);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_more_ifon_strain:

                break;
        }
    }


    @Override
    public void viewAllEditsButtonClick(int position, UserStrainDetailsDataModel userStrainDetailsDataModel) {


    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        refresh.setVisibility(View.GONE);
        if (apiActions == get_user_point_log) {
            try {
                JSONObject object = new JSONObject(response).getJSONObject("successData");
                JSONArray array = object.getJSONArray("points");
                mData.clear();
                mData.addAll(Arrays.asList(new Gson().fromJson(array.toString(), Points[].class)));
                if (mData.size() > 0) {
                    not_found.setVisibility(View.GONE);
                } else {
                    not_found.setVisibility(View.VISIBLE);
                }
                pointsLogViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        refresh.setVisibility(View.GONE);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}

