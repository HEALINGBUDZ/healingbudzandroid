package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codingpixel.healingbudz.R;

public class MyJournalTagJournalRecylerAdapter extends RecyclerView.Adapter<MyJournalTagJournalRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private MyJournalTagJournalRecylerAdapter.ItemClickListener mClickListener;
    public MyJournalTagJournalRecylerAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    // inflates the cell layout from xml when needed
    @Override
    public MyJournalTagJournalRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.journal_details_recyler_item_expandable_item, parent, false);
        MyJournalTagJournalRecylerAdapter.ViewHolder viewHolder = new MyJournalTagJournalRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(MyJournalTagJournalRecylerAdapter.ViewHolder holder, final int position) {
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return 1;
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(MyJournalTagJournalRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}