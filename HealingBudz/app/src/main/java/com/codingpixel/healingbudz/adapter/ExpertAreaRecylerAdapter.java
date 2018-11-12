package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.Registration.AddExpertiseQuestionActivity;
import com.codingpixel.healingbudz.customeUI.SimpleToolTip.SimpleTooltip;

import java.util.ArrayList;
import java.util.List;

public class ExpertAreaRecylerAdapter extends RecyclerView.Adapter<ExpertAreaRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ExpertAreaRecylerAdapter.ItemClickListenerKey mClickListener;
    List<AddExpertiseQuestionActivity.DataModelEntryExpert> mData = new ArrayList<>();
    int question_no;
    boolean fromProfile = false;

    public ExpertAreaRecylerAdapter(Context context, List<AddExpertiseQuestionActivity.DataModelEntryExpert> mData, int question_number) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.question_no = question_number;
        fromProfile = false;
    }

    public ExpertAreaRecylerAdapter(Context context, List<AddExpertiseQuestionActivity.DataModelEntryExpert> mData, int question_number, boolean isProfile) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.question_no = question_number;
        fromProfile = isProfile;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ExpertAreaRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.expert_area_question_added_itemm, parent, false);
        ExpertAreaRecylerAdapter.ViewHolder viewHolder = new ExpertAreaRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (fromProfile) {
            holder.cross.setVisibility(View.GONE);
            holder.text.setSingleLine(false);
            holder.sugested.setVisibility(View.GONE);
        } else {
            holder.text.setSingleLine(false);
            holder.cross.setVisibility(View.VISIBLE);
        }
        holder.text.setText(mData.get(position).getTitle());
        holder.text.setSelected(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.text.setTooltipText(mData.get(position).getTitle());
        }
//        if (mData.get(position).isSuggest()) {
//            holder.sugested.setVisibility(View.VISIBLE);
//        } else {
//            holder.sugested.setVisibility(View.INVISIBLE);
//        }
        holder.sugested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fromProfile)
                    ShowToolTipDialog(v, 0, fromProfile);
            }
        });
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mData.get(position).getTitle();
                mData.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                if (question_no == 1) {
                    mClickListener.onQuestionOneCross(text);
                } else {
                    mClickListener.onQuestionTwoCross(text);
                }
            }
        });
    }

    public void ShowToolTipDialog(final View v, final int position, boolean isProfile) {
        v.setFocusable(false);
        v.setScrollContainer(false);
        int grt = Gravity.START;

        final SimpleTooltip tooltip = new SimpleTooltip.Builder(v.getContext())
                .anchorView(v)
                .focusable(false)
                .arrowDrawable(null)
                .showArrow(false)
                .text("Sample Text")
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(true)
                .modal(true)
                .gravity(grt)
                .animated(true)
                .contentView(R.layout.strain_survay_sugggestion_alert)
                .focusable(false)
                .overlayMatchParent(true)
                .build();

        tooltip.show();
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text;
        ImageView cross, sugested;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.ans_question_one);
            cross = itemView.findViewById(R.id.cross);
            sugested = itemView.findViewById(R.id.sugested);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClickKey(view, getAdapterPosition());
        }
    }

    public void setClickListener(ExpertAreaRecylerAdapter.ItemClickListenerKey itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setmClickListener(ExpertAreaRecylerAdapter.ItemClickListenerKey mClickListener) {
        this.mClickListener = mClickListener;

    }

    public boolean isSuggestItem(String text) {
        for (int i = 0; i < mData.size(); i++) {
            if (text.equalsIgnoreCase(mData.get(i).getTitle())) {
                return mData.get(i).isSuggest();
            }
        }
        return false;
    }

    public boolean isContainItem(String text) {
        for (int i = 0; i < mData.size(); i++) {
            if (text.equalsIgnoreCase(mData.get(i).getTitle())) {
                return true;
            }
        }
        return false;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListenerKey {
        void onItemClickKey(View view, int position);

        void onQuestionOneCross(String text);

        void onQuestionTwoCross(String text);
    }
}