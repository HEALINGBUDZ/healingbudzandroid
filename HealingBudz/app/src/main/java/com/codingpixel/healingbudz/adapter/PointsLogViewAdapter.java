package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzEditReviewAlertDialog;
import com.codingpixel.healingbudz.DataModel.Points;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.codingpixel.healingbudz.network.model.APIResponseListner;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PointsLogViewAdapter extends RecyclerView.Adapter<PointsLogViewAdapter.ViewHolder> implements APIResponseListner, ReportSendButtonLstner, BudzEditReviewAlertDialog.OnDialogFragmentClickListener {
    private LayoutInflater mInflater;
    Context context;
    List<Points> mData = new ArrayList<>();
    private PointsLogViewAdapter.ItemClickListener mClickListener;

    public PointsLogViewAdapter(Context context, List<Points> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = mData;
    }

    @Override
    public PointsLogViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.points_log_row_layout, parent, false);
        view.setOnTouchListener(null);
        PointsLogViewAdapter.ViewHolder viewHolder = new PointsLogViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PointsLogViewAdapter.ViewHolder holder, final int position) {
        holder.points.setText("+" + mData.get(position).getPoints() + "pts");
        holder.text_content.setText(mData.get(position).getType());
        holder.date.setText(DateConverter.convertDateReward(mData.get(position).getCreatedAt()));

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


        public TextView points, text_content, date;


        public ViewHolder(View view) {
            super(view);
            points = view.findViewById(R.id.points);
            text_content = view.findViewById(R.id.text_content);
            date = view.findViewById(R.id.date);

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
