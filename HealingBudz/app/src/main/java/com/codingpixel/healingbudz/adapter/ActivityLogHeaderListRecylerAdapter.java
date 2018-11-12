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

import com.codingpixel.healingbudz.DataModel.JournalHeaderDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import java.util.ArrayList;

public class ActivityLogHeaderListRecylerAdapter extends RecyclerView.Adapter<ActivityLogHeaderListRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<JournalHeaderDataModel> mData = new ArrayList<>();
    private ActivityLogHeaderListRecylerAdapter.ItemClickListener mClickListener;
    Context context;

    public ActivityLogHeaderListRecylerAdapter(Context context, ArrayList<JournalHeaderDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ActivityLogHeaderListRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_log_header_recyler_view_item, parent, false);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 6;
        view.setLayoutParams(lp);
        ActivityLogHeaderListRecylerAdapter.ViewHolder viewHolder = new ActivityLogHeaderListRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ActivityLogHeaderListRecylerAdapter.ViewHolder holder, final int position) {
        holder.Title.setText(mData.get(position).getTitle());
        if (mData.get(position).getImage_resourse() == 0) {
            holder.Text_image.setText(mData.get(position).getText_image());
            holder.image_resourse.setVisibility(View.GONE);
            holder.Text_image.setVisibility(View.VISIBLE);
        } else {
            holder.image_resourse.setImageResource(mData.get(position).getImage_resourse());
            holder.image_resourse.setVisibility(View.VISIBLE);
            holder.Text_image.setVisibility(View.GONE);
        }
        if (mData.get(position).isSelected()) {
            holder.indicator.setVisibility(View.VISIBLE);
            holder.Main_content.setBackgroundColor(Color.parseColor("#403f3f"));
            holder.img_ic_cross.setVisibility(View.VISIBLE);
        } else {
            holder.img_ic_cross.setVisibility(View.GONE);
            holder.indicator.setVisibility(View.GONE);
            holder.Main_content.setBackgroundColor(Color.parseColor("#00000000"));
        }
//        if (mData.get(position).isSelected()) {
//            holder.indicator.setVisibility(View.VISIBLE);
//            holder.Main_content.setBackgroundColor(Color.parseColor("#403F3F"));
//        } else {
//            holder.indicator.setVisibility(View.GONE);
//            holder.Main_content.setBackgroundColor(Color.parseColor("#00000000"));
//        }
        holder.img_ic_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_ic_cross.setVisibility(View.GONE);
                holder.indicator.setVisibility(View.GONE);
                holder.Main_content.setBackgroundColor(Color.parseColor("#00000000"));
                notifyDataSetChanged();
                mClickListener.onCrossListener(v, position);
            }
        });
        if (position == mData.size() - 1) {
            holder.Line.setVisibility(View.GONE);
        } else {
            holder.Line.setVisibility(View.VISIBLE);
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
        ImageView image_resourse, img_ic_cross;
        View indicator;
        View Line;
        LinearLayout Main_content;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.q_a_header_list_title);
            image_resourse = itemView.findViewById(R.id.img_rscr);
            Text_image = itemView.findViewById(R.id.text_img);
            indicator = itemView.findViewById(R.id.indicator);
            img_ic_cross = itemView.findViewById(R.id.img_ic_cross);
            Main_content = itemView.findViewById(R.id.main_content);
            Line = itemView.findViewById(R.id.line);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onSlideListItemClick(view, getAdapterPosition());
            for (int x = 0; x < mData.size(); x++) {
                if (x == getAdapterPosition()) {
                    mData.get(x).setSelected(true);
                    SharedPrefrences.setInt("GROUPS_HOME_HEADER_LIST_SELECTED_ITEM_INDEX", x, context);
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
    public void setClickListener(ActivityLogHeaderListRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onSlideListItemClick(View view, int position);

        void onCrossListener(View view, int position);
    }
}