package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codingpixel.healingbudz.DataModel.BudzMapDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;


public class MyBudzMapRecylerAdapter extends RecyclerView.Adapter<MyBudzMapRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<BudzMapDataModel> mData = new ArrayList<>();
    private HomeSearchListRecylerAdapter.ItemClickListener mClickListener;
    public MyBudzMapRecylerAdapter(Context context , ArrayList<BudzMapDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    // inflates the cell layout from xml when needed
    @Override
    public MyBudzMapRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_budz_map_recyler_view_item_layout, parent, false);
        MyBudzMapRecylerAdapter.ViewHolder viewHolder = new MyBudzMapRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }
    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(MyBudzMapRecylerAdapter.ViewHolder holder, final int position) {

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return 2;
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onGlobalSearchItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(HomeSearchListRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}