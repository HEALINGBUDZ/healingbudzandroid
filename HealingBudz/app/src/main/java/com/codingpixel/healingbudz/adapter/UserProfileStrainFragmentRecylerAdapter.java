package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

public class UserProfileStrainFragmentRecylerAdapter extends RecyclerView.Adapter<UserProfileStrainFragmentRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<String> mData = new ArrayList<>();
    private UserProfileStrainFragmentRecylerAdapter.ItemClickListener mClickListener;
    public UserProfileStrainFragmentRecylerAdapter(Context context , ArrayList<String> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context ;
    }

    // inflates the cell layout from xml when needed
    @Override
    public UserProfileStrainFragmentRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_profile_strain_tab_recyler_view_item_layout, parent, false);
        UserProfileStrainFragmentRecylerAdapter.ViewHolder viewHolder = new UserProfileStrainFragmentRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Name.setText(mData.get(position));
    }
    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView Name;
        public ViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.title_main);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    public void setClickListener(UserProfileStrainFragmentRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}