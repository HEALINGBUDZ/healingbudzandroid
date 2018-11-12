package com.codingpixel.healingbudz.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.Product;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzEditReviewAlertDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.MyRewardzActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.dialog.BudzAddressFillDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.dialog.BudzRewardAskDialog;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.dialog.BudzThanksDialog;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.MyRewardzActivity.points;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.purchase_product;

public class HBPointsViewAdapter extends RecyclerView.Adapter<HBPointsViewAdapter.ViewHolder> implements APIResponseListner, ReportSendButtonLstner, BudzEditReviewAlertDialog.OnDialogFragmentClickListener, BudzRewardAskDialog.OnDialogFragmentClickListener, BudzThanksDialog.OnDialogFragmentClickListener, BudzAddressFillDialog.OnDialogFragmentClickListener {
    private LayoutInflater mInflater;
    Context context;
    List<Product> mData = new ArrayList<>();
    private HBPointsViewAdapter.ItemClickListener mClickListener;
    BudzThanksDialog budzThanksDialog;

    public HBPointsViewAdapter(Context context, List<Product> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = mData;
    }

    @Override
    public HBPointsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.hb_point_row_layout, parent, false);
        view.setOnTouchListener(null);
        HBPointsViewAdapter.ViewHolder viewHolder = new HBPointsViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final HBPointsViewAdapter.ViewHolder holder, final int position) {
        Glide.with(context)
                .load(URL.images_baseurl + mData.get(position).getAttachment())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Log.d("ready", model);
//
//                        holder.image_point.setImageDrawable(resource);
//                        return false;
//                    }
//                })
                .into(holder.image_point);
        holder.text_point.setText(mData.get(position).getPoints() + " points");
        holder.title.setText(mData.get(position).getName());
        if (mData.get(position).getPoints() > points) {
            holder.claimReward.setBackground(context.getDrawable(R.drawable.reward_btn_enough));
            holder.claimReward.setText("Get Enough Points");
            holder.claimReward.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.claimReward.setBackground(context.getDrawable(R.drawable.reward_btn));
            holder.claimReward.setText("Redeem Reward");
            holder.claimReward.setTextColor(Color.parseColor("#FFFFFF"));
            holder.claimReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BudzRewardAskDialog budzRewardAskDialog = BudzRewardAskDialog.newInstance(HBPointsViewAdapter.this, mData.get(position).getName(), mData.get(position).getPoints() + " pts", position);
                    budzRewardAskDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "pd");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setClickListener(ItemClickListener strainCommentFullView) {
        this.mClickListener = strainCommentFullView;
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        notifyDataSetChanged();
        if (apiActions == purchase_product) {
            budzThanksDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "pd");
            ((MyRewardzActivity) context).setTopPointUser(points + "");
        }

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(context, object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnSnedClicked(JSONObject data, int position) {

    }

    @Override
    public void onSubmitReview(BudzEditReviewAlertDialog dialog, JSONObject jsonObject) {
        dialog.dismiss();

    }

    @Override
    public void onCross(BudzEditReviewAlertDialog dialog) {

    }

    @Override
    public void onClaimRewardClick(BudzRewardAskDialog dialog, int itemPosition) {
        dialog.dismiss();
        BudzAddressFillDialog budzRewardAskDialog = BudzAddressFillDialog.newInstance(HBPointsViewAdapter.this, mData.get(itemPosition).getName(), mData.get(itemPosition).getPoints() + " pts", itemPosition);
        budzRewardAskDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "pd");


    }

    @Override
    public void onSubmitAddress(BudzAddressFillDialog dialog, JSONObject jsonObject, int itemPosition) {
        dialog.dismiss();
        try {
            jsonObject.put("product_id", mData.get(itemPosition).getId());
            jsonObject.put("product_points", mData.get(itemPosition).getPoints());
            new VollyAPICall(context
                    , true, URL.purchase_product
                    , jsonObject
                    , user.getSession_key()
                    , Request.Method.POST
                    , HBPointsViewAdapter.this
                    , purchase_product);
            points = points - mData.get(itemPosition).getPoints();
            budzThanksDialog = BudzThanksDialog.newInstance(HBPointsViewAdapter.this, mData.get(itemPosition).getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCross(BudzAddressFillDialog dialog) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements APIResponseListner {

        public RelativeLayout first_review_layout;

        TextView text_point, title;
        Button claimReward;
        ImageView image_point;

        public ViewHolder(View view) {
            super(view);
            claimReward = view.findViewById(R.id.claim_reward);
            image_point = view.findViewById(R.id.image_point);
            text_point = view.findViewById(R.id.text_point);
            title = view.findViewById(R.id.title);


        }

        @Override
        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
            Log.d("onRequestSuccess: ", response);
            notifyDataSetChanged();
        }

        @Override
        public void onRequestError(String response, APIActions.ApiActions apiActions) {
            Log.d("onRequestError: ", response);
            notifyDataSetChanged();
        }
    }

    public void SetImageBackground(final ImageView imageView, String Path) {

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);

//        void onDeleteClick(View view, int position);
    }
}
