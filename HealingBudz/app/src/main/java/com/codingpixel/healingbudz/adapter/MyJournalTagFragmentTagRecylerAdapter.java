package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingpixel.healingbudz.activity.home.side_menu.my_journal.TagsFragment;
import com.codingpixel.healingbudz.R;

import java.util.List;

public class MyJournalTagFragmentTagRecylerAdapter extends RecyclerView.Adapter<MyJournalTagFragmentTagRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private MyJournalTagFragmentTagRecylerAdapter.ItemClickListener mClickListener;
    List<TagsFragment.TagsData> mData;
    public MyJournalTagFragmentTagRecylerAdapter(Context context ,  List<TagsFragment.TagsData> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    // inflates the cell layout from xml when needed
    @Override
    public MyJournalTagFragmentTagRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.myjournal_tagfagment_tag_recyler_view_item, parent, false);
        MyJournalTagFragmentTagRecylerAdapter.ViewHolder viewHolder = new MyJournalTagFragmentTagRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.text.setText(mData.get(position).getTitle());
        holder.count.setText(mData.get(position).getCount()+"");
    }
    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView text , count;
        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_title);
            count = itemView.findViewById(R.id.cross);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    public void setClickListener(MyJournalTagFragmentTagRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}