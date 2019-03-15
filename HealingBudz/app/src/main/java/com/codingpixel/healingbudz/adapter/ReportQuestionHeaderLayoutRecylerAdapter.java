package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import java.util.ArrayList;

public class ReportQuestionHeaderLayoutRecylerAdapter extends RecyclerView.Adapter<ReportQuestionHeaderLayoutRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<ReportQuestionListDataModel> mData = new ArrayList<>();
    private ReportQuestionHeaderLayoutRecylerAdapter.ItemClickListener mClickListener;
    Context context;
    String chechReport = "";
    String color = "";

    public ReportQuestionHeaderLayoutRecylerAdapter(Context context, ArrayList<ReportQuestionListDataModel> data, String nameReport, String color) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.color = color;
        chechReport = nameReport;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ReportQuestionHeaderLayoutRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.report_question_recyler_item_layout, parent, false);
//        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
//        lp.height = parent.getMeasuredHeight() / 6;
//        view.setLayoutParams(lp);
        ReportQuestionHeaderLayoutRecylerAdapter.ViewHolder viewHolder = new ReportQuestionHeaderLayoutRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ReportQuestionHeaderLayoutRecylerAdapter.ViewHolder holder, final int position) {
        holder.Title.setText(mData.get(position).getTitle());
        holder.image_resourse.setVisibility(View.VISIBLE);
        holder.Text_image.setVisibility(View.GONE);
        if (mData.get(position).isSelected()) {
            holder.image_resourse.setImageResource(R.drawable.ic_double_circle);
            holder.indicator.setVisibility(View.VISIBLE);
            if (chechReport.equalsIgnoreCase("strain")) {
//                holder.Main_content.setBackgroundColor(Color.parseColor("#f4c42f"));
                if (this.color.length() > 0) {
                    holder.indicator.setBackgroundColor(Color.parseColor(color));
                } else {
                    holder.indicator.setBackgroundColor(Color.parseColor("#f4c42f"));
                }

            } else if (chechReport.equalsIgnoreCase("budz")) {
//                holder.Main_content.setBackgroundColor(Color.parseColor("#932a88"));
                holder.indicator.setBackgroundColor(Color.parseColor("#932a88"));
            } else if (chechReport.equalsIgnoreCase("gallery")) {
//                holder.Main_content.setBackgroundColor(Color.parseColor("#f4c42f"));
                if (this.color.length() > 0) {
                    holder.indicator.setBackgroundColor(Color.parseColor(color));
                } else {
                    holder.indicator.setBackgroundColor(Color.parseColor("#f4c42f"));
                }

            } else {

//                holder.indicator.setBackgroundColor(Color.parseColor("#403f3f"));
            }
            holder.Main_content.setBackgroundColor(Color.parseColor("#403f3f"));

        } else {
            holder.image_resourse.setImageResource(R.drawable.ic_white_circle);
            holder.indicator.setVisibility(View.GONE);
            holder.Main_content.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        TextView Title, Text_image;
        ImageView image_resourse;
        View indicator;
        LinearLayout Main_content;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.q_a_header_list_title);
            image_resourse = itemView.findViewById(R.id.img_rscr);
            Text_image = itemView.findViewById(R.id.text_img);
            indicator = itemView.findViewById(R.id.indicator);
            Main_content = itemView.findViewById(R.id.main_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            for (int x = 0; x < mData.size(); x++) {
                if (x == getAdapterPosition()) {
                    mData.get(x).setSelected(true);
                    SharedPrefrences.setInt("REPORT_QUESTION_LIST_SELECTED_ITEM_INDEX", x, context);
                } else {
                    mData.get(x).setSelected(false);
                }
            }
            notifyDataSetChanged();
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

    // allows clicks events to be caught
    public void setClickListener(ReportQuestionHeaderLayoutRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}