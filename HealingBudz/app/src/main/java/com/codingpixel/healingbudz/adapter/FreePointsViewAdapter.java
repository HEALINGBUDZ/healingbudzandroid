package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzEditReviewAlertDialog;
import com.codingpixel.healingbudz.DataModel.Reward;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.network.model.APIResponseListner;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FreePointsViewAdapter extends RecyclerView.Adapter<FreePointsViewAdapter.ViewHolder> implements APIResponseListner, ReportSendButtonLstner, BudzEditReviewAlertDialog.OnDialogFragmentClickListener {
    private LayoutInflater mInflater;
    Context context;
    List<Reward> mData = new ArrayList<>();
    private FreePointsViewAdapter.ItemClickListener mClickListener;

    public FreePointsViewAdapter(Context context, List<Reward> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = mData;
    }

    @Override
    public FreePointsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.free_points_row_layout, parent, false);
        view.setOnTouchListener(null);
        FreePointsViewAdapter.ViewHolder viewHolder = new FreePointsViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FreePointsViewAdapter.ViewHolder holder, final int position) {
        if (mData.get(position).getUserRewardsCount() == 1) {
            holder.done.setVisibility(View.VISIBLE);
            holder.pending.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //
                }
            });
            holder.first_review_layout.setBackgroundColor(Color.parseColor("#82BB2B"));
        } else {
            holder.first_review_layout.setBackgroundColor(Color.parseColor("#5C5D5D"));
            holder.done.setVisibility(View.GONE);
            holder.pending.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) mClickListener.onItemClick(v, position);
                }
            });
        }
        holder.name_reward.setText(mData.get(position).getTitle());
        holder.text_point.setText("+" + mData.get(position).getPoints());


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
        mClickListener.onItemClick(null, 0);
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
        TextView name_reward, text_point;

        public ViewHolder(View view) {
            super(view);
            first_review_layout = view.findViewById(R.id.first_review_layout);
            name_reward = view.findViewById(R.id.name_reward);
            text_point = view.findViewById(R.id.text_point);
            done = view.findViewById(R.id.done);
            pending = view.findViewById(R.id.pending);


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
