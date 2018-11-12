package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.JournalTagsDataModal;
import com.codingpixel.healingbudz.R;

import java.util.List;

public class AddNewJournalSelectTagRecylerAdapter extends RecyclerView.Adapter<AddNewJournalSelectTagRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private AddNewJournalSelectTagRecylerAdapter.ItemClickListener mClickListener;
    List<JournalTagsDataModal> mData;

    public AddNewJournalSelectTagRecylerAdapter(Context context, List<JournalTagsDataModal> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    // inflates the cell layout from xml when needed
    @Override
    public AddNewJournalSelectTagRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.select_tag_addnewjournal_recyler_view_item, parent, false);
        AddNewJournalSelectTagRecylerAdapter.ViewHolder viewHolder = new AddNewJournalSelectTagRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.text.setText(mData.get(position).getTitle());
//        MakeKeywordClickableText(holder.text.getContext(), mData.get(position).getTitle(), holder.text);
        if (mData.get(position).isTagClicked()) {
            holder.Tags_BG.setBackgroundResource(R.drawable.tag_clicked_bg);
        } else {
            holder.Tags_BG.setBackgroundResource(R.drawable.tag_select_bg);
        }
    }
    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text;
        LinearLayout Tags_BG;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_title);
            Tags_BG = itemView.findViewById(R.id.tags_bg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onTagsItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(AddNewJournalSelectTagRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onTagsItemClick(View view, int position);
    }
}