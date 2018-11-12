package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.BudzMapDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;


public class BudzMapMenuRecylerAdapter extends RecyclerView.Adapter<BudzMapMenuRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<BudzMapDataModel> mData = new ArrayList<>();
    Context context;
    private BudzMapMenuRecylerAdapter.ItemClickListener mClickListener;
    public BudzMapMenuRecylerAdapter(Context context ) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    // inflates the cell layout from xml when needed
    @Override
    public BudzMapMenuRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_menu_recyler_item, parent, false);
        BudzMapMenuRecylerAdapter.ViewHolder viewHolder = new BudzMapMenuRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }
    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(BudzMapMenuRecylerAdapter.ViewHolder holder, final int position) {
        if(position > 0){
            holder.Strain_Layout.setVisibility(View.GONE);
        }

        if(position == 1){
            holder.Header_title.setText("Entrees");
        }else if(position == 2){
            holder.Header_title.setText("Samplings");
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return 3;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RecyclerView recyclerView;
        TextView Header_title;
        LinearLayout Strain_Layout;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.item_product_recyler_ciew);
            Header_title = itemView.findViewById(R.id.header_title);
            Strain_Layout = itemView.findViewById(R.id.straint_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(BudzMapMenuRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}