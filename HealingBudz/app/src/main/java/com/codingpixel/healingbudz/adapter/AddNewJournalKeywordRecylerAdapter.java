package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;

import java.util.ArrayList;
import java.util.List;

public class AddNewJournalKeywordRecylerAdapter extends RecyclerView.Adapter<AddNewJournalKeywordRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private AddNewJournalKeywordRecylerAdapter.ItemClickListener mClickListener;
    List<String> mData = new ArrayList<>();
    boolean isDeleteAble;

    public AddNewJournalKeywordRecylerAdapter(Context context, List<String> mData, boolean isDeleteAble) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.isDeleteAble = isDeleteAble;
    }

    // inflates the cell layout from xml when needed
    @Override
    public AddNewJournalKeywordRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.add_new_journal_strain_keyword_recyler_view_item, parent, false);
        AddNewJournalKeywordRecylerAdapter.ViewHolder viewHolder = new AddNewJournalKeywordRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(position < mData.size()){
            holder.text.setText(mData.get(position));
            if (!isDeleteAble) {
                holder.Cross.setVisibility(View.GONE);
            } else {
                holder.Cross.setVisibility(View.VISIBLE);
            }
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        }else {
            return  0 ;
        }

    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text;
        ImageView Cross;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_title);
            Cross = itemView.findViewById(R.id.cross);
            Cross.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onTagsCrossItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(AddNewJournalKeywordRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onTagsCrossItemClick(View view, int position);
    }
}