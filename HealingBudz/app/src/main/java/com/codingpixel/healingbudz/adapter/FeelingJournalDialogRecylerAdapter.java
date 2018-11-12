package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codingpixel.healingbudz.R;

public class FeelingJournalDialogRecylerAdapter extends RecyclerView.Adapter<FeelingJournalDialogRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    public int[] resource;
    private FeelingJournalDialogRecylerAdapter.ItemClickListener mClickListener;
    public FeelingJournalDialogRecylerAdapter(Context context , int[] res) {
        this.mInflater = LayoutInflater.from(context);
        this.resource = res;
    }

    // inflates the cell layout from xml when needed
    @Override
    public FeelingJournalDialogRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.emoji_recyler_view_item, parent, false);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 6;
        view.setLayoutParams(lp);
        FeelingJournalDialogRecylerAdapter.ViewHolder viewHolder = new FeelingJournalDialogRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.res.setImageResource(resource[position]);
    }
    // total number of cells
    @Override
    public int getItemCount() {
        return resource.length;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
         ImageView res;
        public ViewHolder(View itemView) {
            super(itemView);
            res = itemView.findViewById(R.id.res);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    public void setClickListener(FeelingJournalDialogRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}