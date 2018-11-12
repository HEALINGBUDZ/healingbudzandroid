package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapSpecialProducts;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.BudzMapSpecialProductRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_profile;

public class SpecialInfoTabFragment extends Fragment implements APIResponseListner {
    RecyclerView special_product_recyler_view;
    LinearLayout Refresh;
    TextView No_Record, title;
    RelativeLayout add_product_open;
    BudzMapSpecialProductRecylerAdapter recyler_adapter;
    ArrayList<BudzMapSpecialProducts> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.special_fragment_tab_fragment_layout, container, false);
        special_product_recyler_view = view.findViewById(R.id.special_product_recyler_view);
        add_product_open = view.findViewById(R.id.add_product_open);
        RecyclerView.LayoutManager layoutManager_home_saerch = new LinearLayoutManager(getContext());
        special_product_recyler_view.setLayoutManager(layoutManager_home_saerch);
        recyler_adapter = new BudzMapSpecialProductRecylerAdapter(getContext(), data);
        special_product_recyler_view.setAdapter(recyler_adapter);
        special_product_recyler_view.setNestedScrollingEnabled(false);

        Refresh = view.findViewById(R.id.refresh);
        No_Record = view.findViewById(R.id.no_record);
        Refresh.setVisibility(View.VISIBLE);
        title = view.findViewById(R.id.title);
        JSONObject object = new JSONObject();
        new VollyAPICall(view.getContext(), false, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), object, user.getSession_key(), Request.Method.GET, SpecialInfoTabFragment.this, get_budz_profile);
        add_product_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof BudzMapDetailsActivity) {
                    ((BudzMapDetailsActivity) getActivity()).onCreateSpecial();
                }
            }
        });
        if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id()) {
            add_product_open.setVisibility(View.VISIBLE);
        } else {
            add_product_open.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        JSONObject jsonObject = null;
        data.clear();
        try {
            jsonObject = new JSONObject(response);
            if (!jsonObject.getJSONObject("successData").isNull("special")) {
                JSONArray specials = jsonObject.getJSONObject("successData").getJSONArray("special");
                for (int x = 0; x < specials.length(); x++) {
                    JSONObject sp = specials.getJSONObject(x);
                    BudzMapSpecialProducts specialProducts = new BudzMapSpecialProducts();
                    specialProducts.setId(sp.getInt("id"));
                    specialProducts.setUser_id(sp.getInt("user_id"));
//                    specialProducts.setSub_user_id(sp.getInt("sub_user_id"));
                    specialProducts.setTitle(sp.getString("title"));
                    specialProducts.setMessage(sp.getString("description"));
                    specialProducts.setValidity_date(sp.getString("date"));
//                    specialProducts.setImage(sp.getString("image"));
//                    specialProducts.setLat(sp.getDouble("lat"));
//                    specialProducts.setLng(sp.getDouble("lng"));
//                    specialProducts.setZip_code(sp.getString("zip_code"));
//                    specialProducts.setPublic_location(sp.getString("public_location"));
                    specialProducts.setCreated_at(sp.getString("created_at"));
                    specialProducts.setUpdated_at(sp.getString("updated_at"));
                    if (!sp.isNull("user_like_count")) {
                        if (sp.getJSONArray("user_like_count").length() > 0) {
                            specialProducts.setSaved(true);
                        } else {
                            specialProducts.setSaved(false);
                        }
                    } else {
                        specialProducts.setSaved(false);
                    }
                    data.add(specialProducts);
                }
                if (data.size() > 0) {
                    title.setVisibility(View.VISIBLE);
                    recyler_adapter.notifyDataSetChanged();
                } else {
                    title.setVisibility(View.GONE);
                    No_Record.setVisibility(View.VISIBLE);
                }

            } else {
                title.setVisibility(View.GONE);
                No_Record.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }
}
