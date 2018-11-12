package com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.tabs;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.Product_Redeem;
import com.codingpixel.healingbudz.DataModel.Reward;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.HomeActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.HomeGlobalSearchActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.dialog.StrainEditReviewAlertDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.help_support.HelpAndSupportActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.MyRewardzActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.folllowkeys.FollowKeywordActivity;
import com.codingpixel.healingbudz.adapter.FreePointsRedeemViewAdapter;
import com.codingpixel.healingbudz.adapter.FreePointsViewAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.static_function.IntentFunction;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_my_rewards;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import com.codingpixel.healingbudz.customeUI.CustomAutoCompleteTextView;

public class FreePointsTabFragment extends Fragment implements APIResponseListner, ReportSendButtonLstner, StrainEditReviewAlertDialog.OnDialogFragmentClickListener, FreePointsViewAdapter.ItemClickListener, FreePointsRedeemViewAdapter.ItemClickListener {


//

    private static View view;
    private RecyclerView recycler_view_free_points;
    private RecyclerView recycler_view_free_points_second;
    RelativeLayout redeem_point_layout, get_point_layout;
    List<Reward> rewardList = new ArrayList<>();
    List<Product_Redeem> productList = new ArrayList<>();
    FreePointsViewAdapter pointsLogViewAdapter;
    FreePointsRedeemViewAdapter pointsLogViewAdapterRedeem;
    TextView free_points_text, not_found, points_content;
    boolean isUpdate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.free_points_tab_layout, container, false);
        HideKeyboard(getActivity());
        InitView(view);
        this.view = view;
        new VollyAPICall(view.getContext()
                , true
                , URL.get_my_rewards
                , new JSONObject()
                , user.getSession_key()
                , Request.Method.GET
                , FreePointsTabFragment.this
                , get_my_rewards);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isUpdate) {
            isUpdate = false;
            new VollyAPICall(view.getContext()
                    , true
                    , URL.get_my_rewards
                    , new JSONObject()
                    , user.getSession_key()
                    , Request.Method.GET
                    , FreePointsTabFragment.this
                    , get_my_rewards);
        }
    }

    private void InitView(View view) {
        points_content = view.findViewById(R.id.points_content);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recycler_view_free_points = (RecyclerView) view.findViewById(R.id.recycler_view_free_points);
        recycler_view_free_points.setLayoutManager(layoutManager1);
        recycler_view_free_points_second = (RecyclerView) view.findViewById(R.id.recycler_view_free_points_second);
        redeem_point_layout = (RelativeLayout) view.findViewById(R.id.redeem_point_layout);
        get_point_layout = (RelativeLayout) view.findViewById(R.id.get_point_layout);
        redeem_point_layout.setVisibility(View.GONE);
        get_point_layout.setVisibility(View.GONE);
        recycler_view_free_points_second.setLayoutManager(layoutManager2);
        pointsLogViewAdapter = new FreePointsViewAdapter(getActivity(), rewardList);

        recycler_view_free_points.setAdapter(pointsLogViewAdapter);
        pointsLogViewAdapterRedeem = new FreePointsRedeemViewAdapter(getActivity(), productList);
        recycler_view_free_points_second.setAdapter(pointsLogViewAdapterRedeem);
        pointsLogViewAdapter.setClickListener(this);
        pointsLogViewAdapterRedeem.setClickListener(this);
        free_points_text = view.findViewById(R.id.free_points_text);
        not_found = view.findViewById(R.id.not_found);
        free_points_text.setText(Html.fromHtml("GET " + "<font color=#82BB2B>" + "350 FREE" + "</font>" + " REWARD POINTS"));
//        free_points_text.setTextC

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == get_my_rewards) {
            try {
                JSONObject object = new JSONObject(response).getJSONObject("successData");
                int totalPoints = object.getInt("total_points");
                ((MyRewardzActivity) view.getContext()).setTopPointUser("" + totalPoints);
                MyRewardzActivity.points = totalPoints;
                points_content.setText("" + totalPoints);
                JSONArray rewardArray = object.getJSONArray("rewards");
                JSONArray productArray = object.getJSONArray("products");
                rewardList.clear();
                rewardList.addAll(Arrays.asList(new Gson().fromJson(rewardArray.toString(), Reward[].class)));
                productList.clear();
                productList.addAll(Arrays.asList(new Gson().fromJson(productArray.toString(), Product_Redeem[].class)));
                pointsLogViewAdapter.notifyDataSetChanged();
                int count = 0;
                for (int i = 0; i < rewardList.size(); i++) {
                    if (rewardList.get(i).getUserRewardsCount() == 1) {
                        count++;
                    }
                }

                pointsLogViewAdapterRedeem.notifyDataSetChanged();
                if (count == (rewardList.size() - 1)) {
                    get_point_layout.setVisibility(View.VISIBLE);
                } else {
                    get_point_layout.setVisibility(View.VISIBLE);
                }
                redeem_point_layout.setVisibility(View.VISIBLE);

                if (productList.size() > 0) {
                    not_found.setVisibility(View.GONE);
                } else {
                    not_found.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void OnSnedClicked(JSONObject data, int position) {

    }

    @Override
    public void onSubmitReview(StrainEditReviewAlertDialog dialog, JSONObject jsonObject) {


    }

    @Override
    public void onCross(StrainEditReviewAlertDialog dialog) {

    }


    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case 0:
                //Ask A Question
                if (HomeActivity.getKeywordDialogItemClickListner != null) {
                    ((AppCompatActivity) view.getContext()).finish();
                    HomeActivity.getKeywordDialogItemClickListner.ShowQAHomeScreenAskQuestion();
                }
                break;
            case 1:
                // Goto Profile
                IntentFunction.GoToProfile(view.getContext(), user.getUser_id());
                break;
            case 2:
                // Follow a Bud
                Intent global_bud = new Intent(view.getContext(), HomeGlobalSearchActivity.class);
                global_bud.putExtra("follow_bud", true);
                startActivity(global_bud);
                break;
            case 3:
                // Follow a KeyWord
                isUpdate = true;
                GoTo(view.getContext(), FollowKeywordActivity.class);
                break;
            case 4:

                // Invite a Friend
                Intent activity_mysaves = new Intent(view.getContext(), HelpAndSupportActivity.class);
                startActivity(activity_mysaves);
                break;
            case 5:
                // Share Question
                if (HomeActivity.getKeywordDialogItemClickListner != null) {
                    ((AppCompatActivity) view.getContext()).finish();
                    HomeActivity.getKeywordDialogItemClickListner.ShowQAHomeScreen();
                }
                break;
            case 6:
                // Strain Survey
                if (HomeActivity.getKeywordDialogItemClickListner != null) {
                    ((AppCompatActivity) view.getContext()).finish();
                    HomeActivity.getKeywordDialogItemClickListner.ShowStrainTab();
                }
                break;

        }

    }

    @Override
    public void onItemClickProduct(View view, int position) {

    }

    @Override
    public void onViewProductClick(View view, int position) {
        ((MyRewardzActivity) getActivity()).GotoHbStore();
    }
}
