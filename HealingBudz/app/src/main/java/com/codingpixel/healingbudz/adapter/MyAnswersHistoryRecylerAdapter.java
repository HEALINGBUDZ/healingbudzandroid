package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;

public class MyAnswersHistoryRecylerAdapter extends RecyclerView.Adapter<MyAnswersHistoryRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<QuestionAnswersDataModel> mData;
    private MyAnswersHistoryRecylerAdapter.ItemClickListener mClickListener;

    public MyAnswersHistoryRecylerAdapter(Context context, ArrayList<QuestionAnswersDataModel> main_data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = main_data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public MyAnswersHistoryRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.myanswers_recyler_veiew_item_history_layout, parent, false);
        MyAnswersHistoryRecylerAdapter.ViewHolder viewHolder = new MyAnswersHistoryRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date mDate = sdf.parse(mData.get(position).getUpdated_at());
//            long timeInMilliseconds = mDate.getTime();
//            String agoo = getTimeAgo(timeInMilliseconds);
//            holder.TimeAgo.setText(agoo);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        holder.TimeAgo.setText(DateConverter.getPrettyTime(mData.get(position).getCreated_at()));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.Question.setText( Html.fromHtml("Q: " +mData.get(position).getQuestion(), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            holder.Question.setText( Html.fromHtml("Q: " +mData.get(position).getQuestion()));
//        }
        MakeKeywordClickableText(holder.Question.getContext(), mData.get(position).getAnswer(), holder.Question);
        MakeKeywordClickableText(holder.question_first.getContext(), mData.get(position).getAnswer(), holder.question_first);
        if (position == 0) {
            holder.time_newa.setVisibility(View.VISIBLE);
            holder.question_first.setVisibility(View.VISIBLE);
            holder.Question.setVisibility(View.GONE);
            holder.TimeAgo.setText(DateConverter.getPrettyTime(mData.get(position).getCreated_at()));
            holder.TimeAgo.setTextColor(Color.parseColor("#0082ca"));
        } else {
            holder.time_newa.setVisibility(View.GONE);
            holder.TimeAgo.setText(DateConverter.getPrettyTime(mData.get(position).getCreated_at()));
            holder.TimeAgo.setTextColor(Color.parseColor("#FFFFFF"));
            holder.question_first.setVisibility(View.GONE);
            holder.Question.setVisibility(View.VISIBLE);
        }
        holder.Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Question.isClickable() && holder.Question.isEnabled()) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });

//        holder.Question.setText("Q: " + mData.get(position).getQuestion());
        holder.Edit.setVisibility(View.VISIBLE);
//        if (mData.get(position).getAnswer_like_count() > 0 || mData.get(position).getFlag_by_user_count() > 0) {
//            holder.Edit.setVisibility(View.INVISIBLE);
//        } else {
//            holder.Edit.setVisibility(View.VISIBLE);
//        }

        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemEditClick(view, position);
            }
        });

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemDeleteClick(view, position);
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView TimeAgo, Question, question_first;
        ImageView Edit, Delete;
        LinearLayout time_newa;

        public ViewHolder(View itemView) {
            super(itemView);
            TimeAgo = itemView.findViewById(R.id.time_new);
            time_newa = itemView.findViewById(R.id.time_newa);
            question_first = itemView.findViewById(R.id.question_first);
            Question = itemView.findViewById(R.id.question);
            Edit = itemView.findViewById(R.id.edit_btn);
            Delete = itemView.findViewById(R.id.delete_btn);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(MyAnswersHistoryRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onItemDeleteClick(View view, int position);

        void onItemEditClick(View view, int position);
    }
}