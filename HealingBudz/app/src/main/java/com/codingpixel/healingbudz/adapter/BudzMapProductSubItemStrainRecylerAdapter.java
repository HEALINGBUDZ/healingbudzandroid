package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.Pricing;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;
import java.util.List;


public class BudzMapProductSubItemStrainRecylerAdapter extends RecyclerView.Adapter<BudzMapProductSubItemStrainRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    List<Pricing> mData = new ArrayList<>();
    private BudzMapProductSubItemStrainRecylerAdapter.ItemClickListener mClickListener;
    boolean isEmpty;

    public BudzMapProductSubItemStrainRecylerAdapter(Context context, List<Pricing> count) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = count;
        isEmpty = false;
    }


    // inflates the cell layout from xml when needed
    @Override
    public BudzMapProductSubItemStrainRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_product_sub_recyler_item, parent, false);
        BudzMapProductSubItemStrainRecylerAdapter.ViewHolder viewHolder = new BudzMapProductSubItemStrainRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(BudzMapProductSubItemStrainRecylerAdapter.ViewHolder holder, final int position) {
        if (!isEmpty) {

            if (mData.get(position).getWeight() == null) {
                holder.weight.setText("0");
            } else {
                holder.weight.setText(mData.get(position).getWeight() + "");
            }


//            holder.price.setText(Html.fromHtml("<html><body><font size=0.2 >$</font> World </body><html>"+mData.get(position).getPrice() + ""));
            if (mData.get(position).getPrice() == 0) {
                holder.price.setText("$0.00");
            } else {
                holder.price.setText("$" + mData.get(position).getPrice());
            }

        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView weight, price;

        public ViewHolder(View itemView) {
            super(itemView);
            weight = itemView.findViewById(R.id.weight);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(BudzMapProductSubItemStrainRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}