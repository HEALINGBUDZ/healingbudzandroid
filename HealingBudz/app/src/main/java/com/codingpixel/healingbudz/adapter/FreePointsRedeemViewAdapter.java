package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.Product_Redeem;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzEditReviewAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class FreePointsRedeemViewAdapter extends RecyclerView.Adapter<FreePointsRedeemViewAdapter.ViewHolder> implements APIResponseListner, ReportSendButtonLstner, BudzEditReviewAlertDialog.OnDialogFragmentClickListener {
    private LayoutInflater mInflater;
    Context context;
    List<Product_Redeem> mData = new ArrayList<>();
    private FreePointsRedeemViewAdapter.ItemClickListener mClickListener;

    public FreePointsRedeemViewAdapter(Context context, List<Product_Redeem> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = mData;
    }

    @Override
    public FreePointsRedeemViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.free_points_row_redeem_layout, parent, false);
        view.setOnTouchListener(null);
        FreePointsRedeemViewAdapter.ViewHolder viewHolder = new FreePointsRedeemViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FreePointsRedeemViewAdapter.ViewHolder holder, final int position) {
        Glide.with(context)
                .load(URL.images_baseurl + mData.get(position).getGetProduct().getAttachment())
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
//                        holder.image.setImageDrawable(resource);
//                        return false;
//                    }
//                })
                .into(holder.image);
        holder.text_point.setText(MessageFormat.format("{0} points", mData.get(position).getGetProduct().getPoints()));
        holder.title.setText(mData.get(position).getGetProduct().getName());
        holder.date.setText(MessageFormat.format("Redeem Date: {0}", DateConverter.convertDateReward(mData.get(position).getGetProduct().getCreatedAt())));
        holder.claim_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onViewProductClick(v, position);
                }
            }
        });
//        Redeem Date: for date
//        if (mData.get(position).getUserRewardsCount() == 1) {
//            holder.done.setVisibility(View.VISIBLE);
//            holder.pending.setVisibility(View.GONE);
//            holder.first_review_layout.setBackgroundColor(Color.parseColor("#82BB2B"));
//        } else {
//            holder.first_review_layout.setBackgroundColor(Color.parseColor("#5C5D5D"));
//            holder.done.setVisibility(View.GONE);
//            holder.pending.setVisibility(View.VISIBLE);
//        }
//        holder.name_reward.setText(mData.get(position).getTitle());
//        holder.text_point.setText("+" + mData.get(position).getPoints());

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
        mClickListener.onItemClickProduct(null, 0);
        notifyDataSetChanged();

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {

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

    public class ViewHolder extends RecyclerView.ViewHolder implements APIResponseListner {

        public RelativeLayout first_review_layout;

        LinearLayout done, pending;
        ImageView image;
        Button claim_reward;
        TextView title, text_point, date;

        public ViewHolder(View view) {
            super(view);
            claim_reward = view.findViewById(R.id.claim_reward);
            title = view.findViewById(R.id.title);
            text_point = view.findViewById(R.id.text_point);
            date = view.findViewById(R.id.date);
            image = view.findViewById(R.id.image);
//            first_review_layout = view.findViewById(R.id.first_review_layout);
//            name_reward = view.findViewById(R.id.name_reward);
//            text_point = view.findViewById(R.id.text_point);
//            done = view.findViewById(R.id.done);
//            pending = view.findViewById(R.id.pending);


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
        void onItemClickProduct(View view, int position);

        void onViewProductClick(View view, int position);
//        void onDeleteClick(View view, int position);
    }
}
