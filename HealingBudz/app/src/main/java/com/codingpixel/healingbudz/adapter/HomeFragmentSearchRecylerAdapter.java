package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;

public class HomeFragmentSearchRecylerAdapter extends RecyclerView.Adapter<HomeFragmentSearchRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<HomeQAfragmentDataModel> mData = new ArrayList<>();
    private HomeFragmentSearchRecylerAdapter.ItemClickListener mClickListener;

    public HomeFragmentSearchRecylerAdapter(Context context, ArrayList<HomeQAfragmentDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public HomeFragmentSearchRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.home_fragment_search_question_recyler_item, parent, false);
        HomeFragmentSearchRecylerAdapter.ViewHolder viewHolder = new HomeFragmentSearchRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final HomeFragmentSearchRecylerAdapter.ViewHolder holder, final int position) {

        if (mData.get(position).getId() == -1) {
            holder.start_q.setText("Sorry No Record Found");
            holder.main_lin.setVisibility(View.GONE);
            holder.start_q.setVisibility(View.VISIBLE);
        } else {
            holder.main_lin.setVisibility(View.VISIBLE);
            holder.start_q.setVisibility(View.GONE);
            holder.answer_count.setText(mData.get(position).getAnswerCount() + "");
//        holder.Question.setText(mData.get(position).getQuestion());
            MakeKeywordClickableText(holder.Question.getContext(), mData.get(position).getQuestion(), holder.Question);
            holder.Question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.Question.isClickable() && holder.Question.isEnabled()) {
                        if (mClickListener != null)
                            mClickListener.onItemClick(view, holder.getAdapterPosition());
                    }
                }
            });
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        TextView Question, answer_count, start_q;
        LinearLayout main_lin;

        public ViewHolder(View itemView) {
            super(itemView);
            start_q = itemView.findViewById(R.id.start_q);
            main_lin = itemView.findViewById(R.id.main_lin);
            Question = itemView.findViewById(R.id.question);
            answer_count = itemView.findViewById(R.id.answer_count);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    public void NotifyDataChange(ArrayList<HomeQAfragmentDataModel> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(HomeFragmentSearchRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}