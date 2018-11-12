package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.HomeDrawerDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Utility;

import java.util.ArrayList;

public class HomeDrawerRecylerAdapter extends RecyclerView.Adapter<HomeDrawerRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<HomeDrawerDataModel> mData = new ArrayList<>();
    private HomeDrawerRecylerAdapter.ItemClickListener mClickListener;

    public HomeDrawerRecylerAdapter(Context context, ArrayList<HomeDrawerDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public HomeDrawerRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.home_drawer_recyler_item, parent, false);
        int value = 10;
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            Log.d("heaight", parent.getMeasuredHeight() / value + "");
            lp.height = parent.getMeasuredHeight() / value;
            int height = parent.getMeasuredHeight() / value;
            if (height > 50) {
                view.setLayoutParams(lp);
            }
            HomeDrawerRecylerAdapter.ViewHolder viewHolder = new HomeDrawerRecylerAdapter.ViewHolder(view);
            return viewHolder;
        } else {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            Log.d("heaight", parent.getMeasuredHeight() / value + "");
            int height = parent.getMeasuredHeight() / value;
            if (height == 0) {
                lp.height = 158;
            } else {
                lp.height = height;
            }
            view.setLayoutParams(lp);
            HomeDrawerRecylerAdapter.ViewHolder viewHolder = new HomeDrawerRecylerAdapter.ViewHolder(view);
            return viewHolder;
        }
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(HomeDrawerRecylerAdapter.ViewHolder holder, final int position) {
        holder.Title.setText(mData.get(position).getTitle());
        if (mData.get(position).getTitle().equalsIgnoreCase("My Buzz")) {
            holder.image_resourse.setPadding((int) Utility.convertDpToPixel(1.0F, holder.image_resourse.getContext()),
                    (int) Utility.convertDpToPixel(1.0F, holder.image_resourse.getContext()),
                    (int) Utility.convertDpToPixel(1.0F, holder.image_resourse.getContext()),
                    (int) Utility.convertDpToPixel(1.0F, holder.image_resourse.getContext()));
        } else {
            holder.image_resourse.setPadding((int) Utility.convertDpToPixel(3.0F, holder.image_resourse.getContext()),
                    (int) Utility.convertDpToPixel(3.0F, holder.image_resourse.getContext()),
                    (int) Utility.convertDpToPixel(3.0F, holder.image_resourse.getContext()),
                    (int) Utility.convertDpToPixel(3.0F, holder.image_resourse.getContext()));
        }
        if (mData.get(position).getImage_rsourse() == 0) {
            holder.image_resourse.setVisibility(View.GONE);
            holder.text_image.setVisibility(View.VISIBLE);
            holder.text_image.setText(mData.get(position).getText_image());
        } else {
            holder.image_resourse.setVisibility(View.VISIBLE);
            holder.text_image.setVisibility(View.GONE);
            holder.image_resourse.setImageResource(mData.get(position).getImage_rsourse());
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        TextView Title, text_image;
        ImageView image_resourse;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.drawer_title);
            text_image = itemView.findViewById(R.id.text_img);
            image_resourse = itemView.findViewById(R.id.img_resourse);
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

    // allows clicks events to be caught
    public void setClickListener(HomeDrawerRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}