package com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.tabs;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.Product;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.FreePointsViewAdapter;
import com.codingpixel.healingbudz.adapter.HBPointsViewAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
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
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_products;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class HBStoreTabFragment extends Fragment implements APIResponseListner, LocationListener, UserLocationListner, FreePointsViewAdapter.ItemClickListener, HBPointsViewAdapter.ItemClickListener {


    private RecyclerView recycler_view_points_log;
    private HBPointsViewAdapter pointsLogViewAdapter;
    private LinearLayout refresh, coming_soon;
    private List<Product> mData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.hb_store_tab_layout, container, false);
        HideKeyboard(getActivity());
        InitView(view);
        new VollyAPICall(view.getContext()
                , false
                , URL.get_products
                , new JSONObject()
                , user.getSession_key()
                , Request.Method.GET
                , HBStoreTabFragment.this
                , get_products);
        return view;
    }

    public void Init(View view) {
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private void InitView(View view) {

        pointsLogViewAdapter = new HBPointsViewAdapter(getActivity(), mData);
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 2);
        recycler_view_points_log = (RecyclerView) view.findViewById(R.id.recycler_view_points_log);
        recycler_view_points_log.setLayoutManager(layoutManager1);
        pointsLogViewAdapter.setClickListener(this);
        recycler_view_points_log.setAdapter(pointsLogViewAdapter);
        refresh = view.findViewById(R.id.refresh);
        coming_soon = view.findViewById(R.id.coming_soon);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                refresh.setVisibility(View.GONE);
//
//                pointsLogViewAdapter.notifyDataSetChanged();
//
//            }
//        }, 1000);


    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        refresh.setVisibility(View.GONE);
        if (apiActions == get_products) {
            try {
                JSONObject object = new JSONObject(response).getJSONObject("successData");
                JSONArray array = object.getJSONArray("products");
                mData.clear();
                mData.addAll(Arrays.asList(new Gson().fromJson(array.toString(), Product[].class)));
                pointsLogViewAdapter.notifyDataSetChanged();
                if (mData.size() == 0) {
                    coming_soon.setVisibility(View.VISIBLE);
                } else {
                    coming_soon.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        refresh.setVisibility(View.GONE);
        coming_soon.setVisibility(View.VISIBLE);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.BOTTOM);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private Location location;

    @Override
    public void onLocationSuccess(Location location) {

    }

    @Override
    public void onLocationFailed(String Error) {

    }


}
