package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.BudzMapDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;


public class BudzMapMedicalTypeRecylerAdapter extends RecyclerView.Adapter<BudzMapMedicalTypeRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<BudzMapDataModel> mData = new ArrayList<>();
    Context context;
    private BudzMapMedicalTypeRecylerAdapter.ItemClickListener mClickListener;
    public BudzMapMedicalTypeRecylerAdapter(Context context ) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    // inflates the cell layout from xml when needed
    @Override
    public BudzMapMedicalTypeRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_medical_type_recyler_item, parent, false);
        BudzMapMedicalTypeRecylerAdapter.ViewHolder viewHolder = new BudzMapMedicalTypeRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }
    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(BudzMapMedicalTypeRecylerAdapter.ViewHolder holder, final int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        BudzMapProductSubItemRecylerAdapter recyler_adapter = null;

        if(position == 0){
            holder.Header_title.setVisibility(View.VISIBLE);
        }else {
            holder.Header_title.setVisibility(View.GONE);
        }

        if(position == 3){
            holder.Straint_item.setVisibility(View.VISIBLE);
            holder.Doctor_item.setVisibility(View.GONE);
            recyler_adapter = new BudzMapProductSubItemRecylerAdapter(context , 1);
        }else {
            holder.Straint_item.setVisibility(View.GONE);
            holder.Doctor_item.setVisibility(View.VISIBLE);
        }

        holder.recyclerView.setAdapter(recyler_adapter);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return 4;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RecyclerView recyclerView;
        TextView Header_title;
        LinearLayout Doctor_item, Straint_item;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.medical_recyler_ciew);
            Header_title = itemView.findViewById(R.id.header_title);
            Doctor_item  = itemView.findViewById(R.id.doctor_item);
            Straint_item  = itemView.findViewById(R.id.straint_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(BudzMapMedicalTypeRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}