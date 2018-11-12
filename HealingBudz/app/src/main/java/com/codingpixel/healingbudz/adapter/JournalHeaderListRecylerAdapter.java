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

public class JournalHeaderListRecylerAdapter extends RecyclerView.Adapter<JournalHeaderListRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<JournalHeaderDataModel> mData = new ArrayList<>();
    private JournalHeaderListRecylerAdapter.ItemClickListener mClickListener;
    Context context;
    public JournalHeaderListRecylerAdapter(Context context , ArrayList<JournalHeaderDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data ;
        this.context = context;
    }
    // inflates the cell layout from xml when needed
    @Override
    public JournalHeaderListRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.journal_header_recyler_view_item, parent, false);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 3;
        view.setLayoutParams(lp);
        JournalHeaderListRecylerAdapter.ViewHolder viewHolder = new JournalHeaderListRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(JournalHeaderListRecylerAdapter.ViewHolder holder, final int position) {
        holder.Title.setText(mData.get(position).getTitle());
        if(mData.get(position).getImage_resourse() ==0){
            holder.Text_image.setText(mData.get(position).getText_image());
            holder.image_resourse.setVisibility(View.GONE);
            holder.Text_image.setVisibility(View.VISIBLE);
        }else {
            holder.image_resourse.setImageResource(mData.get(position).getImage_resourse());
            holder.image_resourse.setVisibility(View.VISIBLE);
            holder.Text_image.setVisibility(View.GONE);
        }

        if(mData.get(position).isSelected()){
            holder.indicator.setVisibility(View.VISIBLE);
            holder.Main_content.setBackgroundColor(Color.parseColor("#403f3f"));
        }else {
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
        TextView Title , Text_image;
        ImageView image_resourse;
        View indicator;
        LinearLayout  Main_content;
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
            if (mClickListener != null) mClickListener.onSlideItemClick(view, getAdapterPosition());
            for(int x= 0 ; x< mData.size() ; x++){
                if(x==  getAdapterPosition()){
                    mData.get(x).setSelected(true);
                    SharedPrefrences.setInt("JOURNAL_HOME_HEADER_LIST_SELECTED_ITEM_INDEX",x, context);
                }else {
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
    public void setClickListener(JournalHeaderListRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onSlideItemClick(View view, int position);
    }
}