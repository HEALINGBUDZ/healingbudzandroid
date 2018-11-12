package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;

public class MyQuestionsRecylerAdapter extends RecyclerView.Adapter<MyQuestionsRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<HomeQAfragmentDataModel> mData = new ArrayList<>();
    private MyQuestionsRecylerAdapter.ItemClickListener mClickListener;

    public MyQuestionsRecylerAdapter(Context context, ArrayList<HomeQAfragmentDataModel> main_data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = main_data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public MyQuestionsRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.myquestions_recyler_veiew_item_layout, parent, false);
        MyQuestionsRecylerAdapter.ViewHolder viewHolder = new MyQuestionsRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.Question.setText(Html.fromHtml("Q: " + mData.get(position).getQuestion(), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            holder.Question.setText(Html.fromHtml("Q: " + mData.get(position).getQuestion()));
//        }
        MakeKeywordClickableText(context, "Q: " + mData.get(position).getQuestion(), holder.Question);
        holder.Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.Question.isClickable() && holder.Question.isEnabled()){
                    if (mClickListener != null) mClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });
        holder.Answers_Count.setText(mData.get(position).getAnswerCount() + "");
        holder.TimeAgo.setText(DateConverter.getPrettyTime(mData.get(position).getUpdated_at()));
        if (mData.get(position).getAnswerCount() > 0 || mData.get(position).getGet_user_likes_count() > 0 || mData.get(position).getGet_user_flag_count() > 0) {
            holder.Edit_Question.setVisibility(View.INVISIBLE);
        } else {
            holder.Edit_Question.setVisibility(View.VISIBLE);
        }

        holder.Edit_Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onEditClick(view, position);
            }
        });

        holder.Delete_Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onDeleteClick(view, position);
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
        TextView Question;
        TextView Answers_Count;
        TextView TimeAgo;
        ImageView Edit_Question, Delete_Question;

        public ViewHolder(View itemView) {
            super(itemView);
            Question = itemView.findViewById(R.id.question);
            Answers_Count = itemView.findViewById(R.id.answers_count);
            TimeAgo = itemView.findViewById(R.id.aggo);
            Delete_Question = itemView.findViewById(R.id.delete_my_question);
            Edit_Question = itemView.findViewById(R.id.edit_my_question);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(MyQuestionsRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onEditClick(View view, int position);

        void onDeleteClick(View view, int position);
    }
}